/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.apps.io;

import org.openscience.cdk.Isotope;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.isomorphism.matchers.IQueryAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.GibbsEnergy;
import uk.ac.ebi.mdk.domain.annotation.MolecularFormula;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.annotation.SystematicName;
import uk.ac.ebi.mdk.domain.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
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
import uk.ac.ebi.mdk.io.text.biocyc.CompoundAttribute;
import uk.ac.ebi.mdk.io.text.biocyc.ReactionAttribute;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uk.ac.ebi.mdk.io.text.biocyc.CompoundAttribute.CHEMICAL_FORMULA;

/**
 * Converts a BioCyc project to MDK domain objects
 *
 * @author John May
 */
public class BioCycConverter {

    private Map<String, Metabolite> metaboliteMap = new HashMap<String, Metabolite>();
    // class map may also hold proteins classes etc.
    private Map<String, Metabolite> classMap      = new HashMap<String, Metabolite>();

    private AutomaticCompartmentResolver resolver = new AutomaticCompartmentResolver();

    private File   root;
    private File   data;
    private String name;
    private String bioCycPrefix;

    private Reconstruction reconstruction;

    /**
     * Create a BioCyc converter for the BioCyC PGDB at 'root'.
     *
     * @param root          location of the PDGB
     * @param name          name of the reconstruction
     * @param bioCycPrefix prefix for the BioCyc identifier (e.g. META = MetaCyc)
     */
    public BioCycConverter(File root, String name, String bioCycPrefix) {

        this.root = root;
        this.name = name;
        this.data = new File(root, "data");
        this.bioCycPrefix = bioCycPrefix;

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
        BioCycDatReader<CompoundAttribute> reader = new BioCycDatReader<CompoundAttribute>(new FileInputStream(compounds),
                                                                                           CompoundAttribute.values());


        while (reader.hasNext()) {
            Metabolite m = dat2Metabolite(reader.next());
            reconstruction.addMetabolite(m);
            String id = m.getAccession();
            metaboliteMap.put(id.substring(id.indexOf(":") + 1), m);
        }

        reader.close();

    }

    public void importClasses() throws IOException {

        File compounds = new File(root, "data/classes.dat");
        BioCycDatReader<CompoundAttribute> reader = new BioCycDatReader<CompoundAttribute>(new FileInputStream(compounds),
                                                                                           CompoundAttribute.values());


        while (reader.hasNext()) {
            Metabolite m = dat2Metabolite(reader.next());
            classMap.put(m.getAccession(), m);
        }

        reader.close();

    }

    public void importMetaboliteStructures() throws IOException {

        if (reconstruction.metabolome().isEmpty())
            importMetabolites();

        Map<String, File> map = getMolFileMap();

        MDLV2000Reader reader = new MDLV2000Reader();

        System.out.print("loading structures...");
        
        for (Metabolite metabolite : reconstruction.metabolome()) {

            String accession = metabolite.getAccession();
            
            if (map.containsKey(accession)) {

                try {
                    reader.setReader(new FileReader(map.remove(accession)));
                    IAtomContainer atomContainer = reader.read(SilentChemObjectBuilder.getInstance().newInstance(IAtomContainer.class));
                    if (atomContainer.getAtomCount() > 0) {
                        if (atomContainer instanceof IQueryAtomContainer) {
                            System.err.println("Skipping chemical structure (query structure): " + accession);
                        } else if (hasNullBond(atomContainer)) {
                            System.err.println("Skipping chemical structure (null bond order): " + accession);
                        } else {
                            metabolite.addAnnotation(new AtomContainerAnnotation(atomContainer));
                        }
                    }
                } catch (CDKException e) {
                    System.err.println(e.getMessage());
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }

            }

        }
        System.out.println("done");

        System.out.println(map.keySet().size() + " structures were not injected");
        if (map.size() < 1000)
            System.out.println(map.keySet());

    }
    
    // filter out problematic structures (null bond order in this case)
    static boolean hasNullBond(IAtomContainer m) {
        for (IBond b : m.bonds())
            if (b.getOrder() == null)
                return true;
        return false;
    }

    public void importReactions() throws IOException {

        if (reconstruction.metabolome().isEmpty()) {
            importMetabolites();
        }
        if (classMap.isEmpty()) {
            importClasses();
        }

        File reactions = new File(root, "data/reactions.dat");
        BioCycDatReader<ReactionAttribute> reader = new BioCycDatReader<ReactionAttribute>(new FileInputStream(reactions),
                                                                                           ReactionAttribute.values());


        while (reader.hasNext()) {
            MetabolicReaction reaction = dat2Reaction(reader.next());
            reconstruction.addReaction(reaction);
        }

        Map<Identifier, Reaction> idToReaction = new HashMap<Identifier, Reaction>();

        System.out.println("Duplicate reaction identifiers:");
        for (Reaction reaction : reconstruction.reactome()) {
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

        // check if we have a mol directory
        File[] files = data.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (!pathname.isDirectory())
                    return false;
                return pathname.listFiles(molFilter).length != 0;
            }
        });

        File molRoot;

        if (files.length == 1) {
            molRoot = files[0];
        }
        else {
            throw new IllegalArgumentException("no directory of molecule '.mol'");
        }

        Map<String, File> map = new HashMap<String, File>();

        for (File mol : molRoot.listFiles(molFilter)) {
            map.put(bioCycPrefix + ":" + mol.getName().substring(0, mol.getName().lastIndexOf(".mol")), mol);
        }

        return map;

    }

    public Metabolite dat2Metabolite(AttributedEntry<CompoundAttribute, String> entry) {

        Metabolite m = DefaultEntityFactory.getInstance().ofClass(Metabolite.class);

        // basic info
        m.setIdentifier(new BioCycChemicalIdentifier(bioCycPrefix, entry.getFirst(CompoundAttribute.UNIQUE_ID)));
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

    public MetabolicReaction dat2Reaction(AttributedEntry<ReactionAttribute, String> entry) {

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

    private MetabolicParticipant getParticipant(AttributedEntry<ReactionAttribute, String> entry, ReactionAttribute attribute, String uid) {

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
        m.setIdentifier(new BioCycChemicalIdentifier(bioCycPrefix, uid));
        m.setAbbreviation("n/a");
        m.setName("unnamed metabolite");

        metaboliteMap.put(uid, m);

        return m;

    }

    private Set<String> unknownResources = new HashSet<String>();

    private Collection<Annotation> getCrossReferences(Collection<String> dblinks) {
        Collection<Annotation> annotations = new ArrayList<Annotation>();
        for (String dblink : dblinks) {
            Annotation annotation = getCrossReference(dblink);
            if (annotation != null)
                annotations.add(annotation);
        }
        return annotations;
    }

    private Annotation getCrossReference(String dblink) {

        Matcher matcher = DB_LINK.matcher(dblink);
        if (matcher.find()) {

            String resource = matcher.group(1).trim();
            String accession = matcher.group(2).trim();

            IdentifierFactory ID_FACTORY = DefaultIdentifierFactory.getInstance();

            if (ID_FACTORY.hasSynonym(resource)) {
                return DefaultAnnotationFactory.getInstance().getCrossReference(
                        ID_FACTORY.ofSynonym(resource, accession));
            }
            else {
                unknownResources.add(resource);
                
            }

        }

        return null;
    }
    
    private void describeErrors() {
        System.err.println("The following resources were unknown (or ambiguous)");
        for (String resource : unknownResources) {
            System.err.println(" - " + resource);
        }
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
        
        converter.describeErrors();

        long start = System.currentTimeMillis();
        ReconstructionIOHelper.write(converter.reconstruction, converter.reconstruction.getContainer());
        long end = System.currentTimeMillis();

        System.out.println("Written reconstruction [" + (end - start) + " ms] to " + converter.reconstruction.getContainer());


    }


}
