package uk.ac.ebi.mdk.apps.io;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Isotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.MolecularFormula;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;
import uk.ac.ebi.mdk.io.text.biocyc.AttributedEntry;
import uk.ac.ebi.mdk.io.text.biocyc.BioCycDatReader;
import uk.ac.ebi.mdk.io.text.biocyc.attribute.Attribute;
import uk.ac.ebi.mdk.io.text.biocyc.attribute.CompoundAttribute;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uk.ac.ebi.mdk.io.text.biocyc.attribute.CompoundAttribute.CHEMICAL_FORMULA;
import static uk.ac.ebi.mdk.io.text.biocyc.attribute.CompoundAttribute.DBLINKS;

/**
 * Converts a BioCyc project to MDK domain objects
 *
 * @author John May
 */
public class BioCycConverter {

    private static final Logger LOGGER = Logger.getLogger(BioCycConverter.class);

    private File root;
    private String name;

    private Reconstruction reconstruction;

    public BioCycConverter(File root, String name) {

        this.root = root;
        this.name = name;


        reconstruction = create();

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
        }

        reader.close();

    }


    public static Metabolite dat2Metabolite(AttributedEntry<Attribute, String> entry) {

        Metabolite m = DefaultEntityFactory.getInstance().ofClass(Metabolite.class);

        // basic info
        m.setIdentifier(new BioCycChemicalIdentifier(entry.getFirst(CompoundAttribute.UNIQUE_ID)));
        m.setName(clean(entry.getFirst(CompoundAttribute.COMMON_NAME, "unnamed entity")));
        m.setAbbreviation(clean(entry.getFirst(CompoundAttribute.ABBREV_NAME, m.getName())));

        // resolve annotations
        m.setCharge(getCharge(entry.get(CompoundAttribute.ATOM_CHARGES)).doubleValue());

        if (entry.has(CHEMICAL_FORMULA)) {
            m.addAnnotation(new MolecularFormula(getFormula(entry.get(CHEMICAL_FORMULA))));
        }

        m.addAnnotations(getCrossReferences(entry.get(DBLINKS)));


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

            String resource = matcher.group(1);
            String accession = matcher.group(2);

            IdentifierFactory ID_FACTORY = DefaultIdentifierFactory.getInstance();

            if (ID_FACTORY.hasSynonym(resource)) {
                return DefaultAnnotationFactory.getInstance().getCrossReference(
                        ID_FACTORY.ofSynonym(resource, accession));
            }

        }

        return null;
    }

    private static IMolecularFormula getFormula(Collection<String> formulaParts) {

        IMolecularFormula formula = DefaultChemObjectBuilder.getInstance().newInstance(IMolecularFormula.class);

        for (String formulaPart : formulaParts) {
            String part = formulaPart.substring(1, formulaPart.length() - 1);
            String[] subpart = part.split(" ");
            formula.addIsotope(new Isotope(subpart[0]), Integer.parseInt(subpart[1]));
        }

        return formula;

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
    private static final Pattern DB_LINK = Pattern.compile("\\((.+?)\"(.+?)\".*");
    private static final Pattern REMOVE_TAGS = Pattern.compile("</?(?:i|sub|sup)/?>", Pattern.CASE_INSENSITIVE);
    private static final Pattern ATOM_CHARGE = Pattern.compile("\\(.+?\\s(.+?)\\)");


    // first arg: biocyc root folder
    // second arg name
    public static void main(String[] args) throws IOException {

        BioCycConverter converter = new BioCycConverter(new File(args[0]), args[1]);
        converter.importMetabolites();

        ReconstructionIOHelper.write(converter.reconstruction, converter.reconstruction.getContainer());

        System.out.println("Written reconstruction to " + converter.reconstruction.getContainer());

    }

}
