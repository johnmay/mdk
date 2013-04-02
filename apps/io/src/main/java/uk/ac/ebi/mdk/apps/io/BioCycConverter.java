package uk.ac.ebi.mdk.apps.io;

import org.apache.log4j.Logger;
import org.openscience.cdk.Isotope;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.*;
import uk.ac.ebi.mdk.domain.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.mdk.domain.entity.*;
import uk.ac.ebi.mdk.domain.entity.reaction.*;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.tool.AutomaticCompartmentResolver;
import uk.ac.ebi.mdk.io.text.attribute.AttributedEntry;
import uk.ac.ebi.mdk.io.text.biocyc.BioCycDatReader;
import uk.ac.ebi.mdk.io.text.attribute.Attribute;
import uk.ac.ebi.mdk.io.text.biocyc.CompoundAttribute;
import uk.ac.ebi.mdk.io.text.biocyc.ReactionAttribute;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uk.ac.ebi.mdk.io.text.biocyc.CompoundAttribute.CHEMICAL_FORMULA;

/**
 * Converts a BioCyc project to MDK domain objects
 *
 * @author John May
 */
public class BioCycConverter {

    private static final Logger LOGGER = Logger.getLogger(BioCycConverter.class);

    private Map<String, Metabolite> metaboliteMap = new HashMap<String, Metabolite>();
    // class map may also hold proteins classes etc.
    private Map<String, Metabolite> classMap      = new HashMap<String, Metabolite>();

    private AutomaticCompartmentResolver resolver = new AutomaticCompartmentResolver();

    private File   root;
    private File   data;
    private String name;
    private String organism;

    private Reconstruction reconstruction;

    public BioCycConverter(File root, String name, String organism) {

        this.root = root;
        this.name = name;
        this.data = new File(root, "data");
        this.organism = organism;

        reconstruction = create();

        resolver.put("cco-out", Organelle.EXTRACELLULAR);
        resolver.put("cco-in", Organelle.CYTOPLASM);
        resolver.put("nill", Organelle.CYTOPLASM);

    }

    private Reconstruction create() {

        EntityFactory ENTITY_FACTORY = DefaultEntityFactory.getInstance();
        Reconstruction reconstruction = ENTITY_FACTORY.ofClass(Reconstruction.class);

        reconstruction.setName(this.name);
        reconstruction.setAbbreviation(this.name);
        reconstruction.setIdentifier(new ReconstructionIdentifier(this.name));

        reconstruction.setTaxonomy(new Taxonomy());

        return reconstruction;

    }

    public void importMetabolites() throws IOException {

        File compounds = new File(root, "data/compounds.dat");
        BioCycDatReader reader = new BioCycDatReader(new FileInputStream(compounds),
                                                     CompoundAttribute.values());


        while (reader.hasNext()) {
            Metabolite m = dat2Metabolite(reader.next());
            reconstruction.addMetabolite(m);
            metaboliteMap.put(m.getAccession(), m);
        }

        reader.close();

    }

    public void importClasses() throws IOException {

        File compounds = new File(root, "data/classes.dat");
        BioCycDatReader reader = new BioCycDatReader(new FileInputStream(compounds),
                                                     CompoundAttribute.values());


        while (reader.hasNext()) {
            Metabolite m = dat2Metabolite(reader.next());
            classMap.put(m.getAccession(), m);
        }

        reader.close();

    }

    public void importMetaboliteStructures() throws IOException {

        if (reconstruction.getMetabolome().isEmpty())
            importMetabolites();

        Map<String, File> map = getMolFileMap();

        MDLV2000Reader reader = new MDLV2000Reader();

        for (Metabolite metabolite : reconstruction.getMetabolome()) {

            String accession = metabolite.getAccession();
            if (map.containsKey(accession)) {

                try {
                    reader.setReader(new FileReader(map.remove(accession)));
                    IAtomContainer atomContainer = reader.read(SilentChemObjectBuilder.getInstance().newInstance(IAtomContainer.class));
                    if (atomContainer.getAtomCount() > 0) {
                        metabolite.addAnnotation(new AtomContainerAnnotation(atomContainer));
                    }
                } catch (CDKException e) {
                    System.err.println(e.getMessage());
                }

            }

        }

        System.out.println(map.keySet().size() + " structures were not injected");

    }

    public void importReactions() throws IOException {

        if (reconstruction.getMetabolome().isEmpty()) {
            importMetabolites();
        }
        if (classMap.isEmpty()) {
            importClasses();
        }

        File compounds = new File(root, "data/reactions.dat");
        BioCycDatReader reader = new BioCycDatReader(new FileInputStream(compounds),
                                                     ReactionAttribute.values());


        while (reader.hasNext()) {
            MetabolicReaction reaction = dat2Reaction(reader.next());
            reconstruction.addReaction(reaction);
        }

        Map<Identifier,Reaction> idToReaction = new HashMap<Identifier, Reaction>();

        System.out.println("Duplicate reaction identifiers:");
        for (Reaction reaction : reconstruction.getReactome()) {
            if (idToReaction.containsKey(reaction.getIdentifier())) {
                System.out.println(reaction.getIdentifier());
            }
            idToReaction.put(reaction.getIdentifier(), reaction);
        }

        reader.close();
    }

    public Map<String, File> getMolFileMap() {

        final FileFilter molFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".mol");
            }
        };

        // check if we have a mod directory
        File[] files = data.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (!pathname.isDirectory())
                    return false;
                return pathname.listFiles(molFilter).length != 0;
            }
        });

        File molRoot = null;

        if (files.length == 1) {
            molRoot = files[0];
        }

        Map<String, File> map = new HashMap<String, File>();

        for (File mol : molRoot.listFiles(molFilter)) {
            map.put(mol.getName().substring(0, mol.getName().lastIndexOf(".mol")), mol);
        }

        return map;

    }

    public Metabolite dat2Metabolite(AttributedEntry<Attribute, String> entry) {

        Metabolite m = DefaultEntityFactory.getInstance().ofClass(Metabolite.class);

        // basic info
        m.setIdentifier(new BioCycChemicalIdentifier(organism, entry.getFirst(CompoundAttribute.UNIQUE_ID)));
        m.setName(clean(entry.getFirst(CompoundAttribute.COMMON_NAME, "unnamed entity")));
        m.setAbbreviation(clean(entry.getFirst(CompoundAttribute.ABBREV_NAME, m.getName())));

        // resolve annotations
        m.setCharge(getCharge(entry.get(CompoundAttribute.ATOM_CHARGES)).doubleValue());

        if (entry.has(CHEMICAL_FORMULA)) {
            IMolecularFormula mf = getFormula(entry.get(CHEMICAL_FORMULA));
            if (mf != null)
                m.addAnnotation(new MolecularFormula(mf));
        }

        m.addAnnotations(getCrossReferences(entry.get(CompoundAttribute.DBLINKS)));


        // synonyms and systematic name
        for (String synonym : entry.get(CompoundAttribute.SYNONYMS)) {
            m.addAnnotation(new Synonym(clean(synonym)));
        }
        for (String synonym : entry.get(CompoundAttribute.SYSTEMATIC_NAME)) {
            m.addAnnotation(new SystematicName(clean(synonym)));
        }

        return m;

    }

    public MetabolicReaction dat2Reaction(AttributedEntry<Attribute, String> entry) {

        MetabolicReaction rxn = DefaultEntityFactory.getInstance().ofClass(MetabolicReaction.class);

        // basic info
        rxn.setIdentifier(new BasicReactionIdentifier(entry.getFirst(ReactionAttribute.UNIQUE_ID)));
        rxn.setName(clean(entry.getFirst(ReactionAttribute.COMMON_NAME, "unnamed entity")));
        rxn.setAbbreviation("n/a");

        if (entry.has(ReactionAttribute.REACTION_DIRECTION)) {

            String direction = entry.getFirst(ReactionAttribute.REACTION_DIRECTION);

            if (DIRECTION_MAP.containsKey(direction)) {
                rxn.setDirection(DIRECTION_MAP.get(direction));
            }

        }

        // synonyms and systematic name
        for (String synonym : entry.get(ReactionAttribute.SYNONYMS)) {
            rxn.addAnnotation(new Synonym(clean(synonym)));
        }
        for (String synonym : entry.get(ReactionAttribute.SYSTEMATIC_NAME)) {
            rxn.addAnnotation(new SystematicName(clean(synonym)));
        }

        rxn.addAnnotations(getCrossReferences(entry.get(ReactionAttribute.DBLINKS)));

        // ec number if it's official or official isn't specified
        if (entry.has(ReactionAttribute.EC_NUMBER)) {
            if (!entry.has(ReactionAttribute.OFFICIAL_EC)
                    || entry.getFirst(ReactionAttribute.OFFICIAL_EC).equals("T")) {
                rxn.addAnnotation(new EnzymeClassification(new ECNumber(entry.getFirst(ReactionAttribute.EC_NUMBER))));
            }
        }

        // gibbs energy
        if (entry.has(ReactionAttribute.DELTAG0)) {
            rxn.addAnnotation(new GibbsEnergy(Double.parseDouble(entry.getFirst(ReactionAttribute.DELTAG0)), 0d));
        }

        // p = participant
        for (String uid : entry.get(ReactionAttribute.LEFT)) {
            rxn.addReactant(getParticipant(entry, ReactionAttribute.LEFT, uid));
        }

        for (String uid : entry.get(ReactionAttribute.RIGHT)) {
            rxn.addProduct(getParticipant(entry, ReactionAttribute.RIGHT, uid));
        }

        return rxn;
    }

    private MetabolicParticipant getParticipant(AttributedEntry<Attribute, String> entry, Attribute attribute, String uid) {

        Metabolite m = getMetabolite(uid);
        Double coef = 1d;
        Compartment compartment = Organelle.CYTOPLASM;

        if (entry.hasNext(attribute, uid, ReactionAttribute.COEFFICIENT)) {

            String coefValue = entry.getNext(attribute, uid).getValue();
            try {
                coef = Double.parseDouble(coefValue);
            } catch (NumberFormatException ex) {
                System.err.println("Coefficient value was not a double: " + coefValue);
            }

            if (entry.hasNext(ReactionAttribute.COEFFICIENT, coefValue, ReactionAttribute.COMPARTMENT)) {
                String compartmentValue = entry.getNext(ReactionAttribute.COEFFICIENT, coefValue).getValue();
                compartment = resolver.getCompartment(compartmentValue);
            }

        }

        if (entry.hasNext(attribute, uid, ReactionAttribute.COMPARTMENT)) {
            String compartmentValue = entry.getNext(attribute, uid).getValue();
            compartment = resolver.getCompartment(compartmentValue);
        }


        return new MetabolicParticipantImplementation(m, coef, compartment);

    }

    private Metabolite getMetabolite(String uid) {

        // check for loaded metabolite
        if (metaboliteMap.containsKey(uid))
            return metaboliteMap.get(uid);

        // could be a class
        if (classMap.containsKey(uid))
            return classMap.get(uid);

        // try removing any pipe (|) bracing
        Matcher matcher = PIPE_BRACE.matcher(uid);
        if (matcher.matches()) {
            return getMetabolite(matcher.group(1));
        }


        System.err.println("Unknown metabolite referenced in reaction. UID - " + uid + " a new entity will be created");

        // create a new one
        Metabolite m = DefaultEntityFactory.getInstance().ofClass(Metabolite.class);
        m.setIdentifier(new BioCycChemicalIdentifier(organism, uid));
        m.setAbbreviation("n/a");
        m.setName("unnamed metabolite");

        metaboliteMap.put(m.getAccession(), m);

        return m;

    }

    private static Collection<Annotation> getCrossReferences(Collection<String> dblinks) {
        Collection<Annotation> annotations = new ArrayList<Annotation>();
        for (String dblink : dblinks) {
            Annotation annotation = getCrossReference(dblink);
            if (annotation != null)
                annotations.add(annotation);
        }
        return annotations;
    }

    private static Annotation getCrossReference(String dblink) {

        Matcher matcher = DB_LINK.matcher(dblink);
        if (matcher.find()) {

            String resource = matcher.group(1).trim();
            String accession = matcher.group(2).trim();

            IdentifierFactory ID_FACTORY = DefaultIdentifierFactory.getInstance();

            if (ID_FACTORY.hasSynonym(resource)) {
                return DefaultAnnotationFactory.getInstance().getCrossReference(
                        ID_FACTORY.ofSynonym(resource, accession));
            } else {
                System.err.println("No synonym found for resource " + resource);
            }

        }

        return null;
    }

    private static IMolecularFormula getFormula(Collection<String> formulaParts) {

        IMolecularFormula formula = SilentChemObjectBuilder.getInstance().newInstance(IMolecularFormula.class);

        for (String formulaPart : formulaParts) {
            String part = formulaPart.substring(1, formulaPart.length() - 1);
            String[] subpart = part.split(" ");
            formula.addIsotope(new Isotope(subpart[0]), Integer.parseInt(subpart[1]));
        }

        return MolecularFormulaManipulator.getString(formula).isEmpty() ? null : formula;

    }

    public static String clean(String name) {

        name = REMOVE_TAGS.matcher(name).replaceAll("");

        // could use a map here but this isn't too slow
        name = name.replaceAll("&alpha;", "α");
        name = name.replaceAll("&beta;", "β");
        name = name.replaceAll("&gamma;", "γ");
        name = name.replaceAll("&kappa;", "κ");
        name = name.replaceAll("&iota;", "ι");
        name = name.replaceAll("&nu;", "ν");
        name = name.replaceAll("&mu;", "μ");
        name = name.replaceAll("&Delta;", "Δ");
        name = name.replaceAll("&delta;", "Δ");
        name = name.replaceAll("&epsilon;", "ε");
        name = name.replaceAll("&omega;", "ω");
        name = name.replaceAll("&psi;", "Ψ");
        name = name.replaceAll("&Psi;", "Ψ");
        name = name.replaceAll("&chi;", "χ");
        name = name.replaceAll("&plusmn;", "±");
        name = name.replaceAll("&pi;", "π");
        name = name.replaceAll("&tau;", "τ");
        name = name.replaceAll("&zeta;", "ζ");

        // arrows
        name = name.replaceAll("&rarr;", "→");
        name = name.replaceAll("&larr;", "←");


        return name;

    }

    private static Integer getCharge(Collection<String> atomCharges) {

        Integer charge = 0;

        for (String atomCharge : atomCharges) {
            Matcher matcher = ATOM_CHARGE.matcher(atomCharge);
            if (matcher.find()) {
                charge += Integer.parseInt(matcher.group(1));
            }
        }

        return charge;

    }

    // patterns
    private static final Pattern DB_LINK     = Pattern.compile("\\((.+?)\"(.+?)\".*");
    private static final Pattern REMOVE_TAGS = Pattern.compile("</?(?:i|sub|sup|em|small)/?>", Pattern.CASE_INSENSITIVE);
    private static final Pattern ATOM_CHARGE = Pattern.compile("\\(.+?\\s(.+?)\\)");
    private static final Pattern PIPE_BRACE  = Pattern.compile("\\|(.+?)\\|");

    private static final Map<String, Direction> DIRECTION_MAP = new HashMap<String, Direction>();

    static {

        DIRECTION_MAP.put("LEFT-TO-RIGHT", Direction.FORWARD);
        DIRECTION_MAP.put("IRREVERSIBLE-LEFT-TO-RIGHT", Direction.FORWARD);
        DIRECTION_MAP.put("PHYSIOL-LEFT-TO-RIGHT", Direction.FORWARD);

        DIRECTION_MAP.put("RIGHT-TO-LEFT", Direction.BACKWARD);
        DIRECTION_MAP.put("PHYSIOL-RIGHT-TO-LEFT", Direction.BACKWARD);
        DIRECTION_MAP.put("IRREVERSIBLE-RIGHT-TO-LEFT", Direction.BACKWARD);

        DIRECTION_MAP.put("REVERSIBLE", Direction.BIDIRECTIONAL);

    }

    // first arg: biocyc root folder
    // second arg name
    public static void main(String[] args) throws IOException {

        // args[2] = organism, e.g. META for metacyc
        BioCycConverter converter = new BioCycConverter(new File(args[0]), args[1], args[2]);
        converter.importMetabolites();
        converter.importClasses();
        converter.importReactions();
        converter.importMetaboliteStructures();

        long start = System.currentTimeMillis();
        ReconstructionIOHelper.write(converter.reconstruction, converter.reconstruction.getContainer());
        long end = System.currentTimeMillis();

        System.out.println("Written reconstruction [" + (end - start) + " ms] to " + converter.reconstruction.getContainer());


    }


}
