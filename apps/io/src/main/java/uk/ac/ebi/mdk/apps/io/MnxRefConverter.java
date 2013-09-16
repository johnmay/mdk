package uk.ac.ebi.mdk.apps.io;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.Charge;
import uk.ac.ebi.mdk.domain.annotation.ExactMass;
import uk.ac.ebi.mdk.domain.annotation.InChI;
import uk.ac.ebi.mdk.domain.annotation.MolecularFormula;
import uk.ac.ebi.mdk.domain.annotation.SMILES;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.EnzymeClassification;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.io.text.mnxref.MnxRefCompound;
import uk.ac.ebi.mdk.io.text.mnxref.MnxRefCompoundInput;
import uk.ac.ebi.mdk.io.text.mnxref.MnxRefReaction;
import uk.ac.ebi.mdk.io.text.mnxref.MnxRefReactionInput;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Conversion of MnxRef database to a Metingear reconstruction. Usage example:
 * <blockquote><pre>
 * Reconstruction reconstruction = MnxRefConverter.convert(new
 * File("/datbases/MnxRef/"));
 * </pre></blockquote>
 *
 * @author John May
 */
public final class MnxRefConverter {

    // factories for MDK data model creation
    private final EntityFactory     entities    = DefaultEntityFactory.getInstance();
    private final IdentifierFactory identifiers = DefaultIdentifierFactory.getInstance();

    private final Logger logger = Logger.getLogger(getClass());

    // turn on/off chemical xref and structure loading
    private final boolean loadXref = true, loadStructures = true;

    // keep track of issues such as xrefs we didn't add
    private Set<String> issues = new HashSet<String>();

    private Reconstruction reconstruction;

    /**
     * Convert the 'MnxRef' databases (located at 'root') to a Metingear/MDK
     * Reconstruction.
     *
     * @param root home directory of MnxRef.
     * @return Metingear/MDK reconstruction
     * @throws IOException thrown if something went wrong
     */
    static Reconstruction convert(File root) throws IOException {
        return new MnxRefConverter(root).reconstruction;
    }

    /**
     * Internal - convert from a MnxRef 'root' directory.
     *
     * @param root home of 'MnxRef'
     * @throws IOException something went wrong with the conversion
     */
    private MnxRefConverter(File root) throws IOException {

        File chemProps = checkExists(new File(root, "chem_prop.tsv"));
        File chemXref = checkExists(new File(root, "chem_xref.tsv"));
        File reacProps = checkExists(new File(root, "reac_prop.tsv"));
        File reacXref = checkExists(new File(root, "reac_xref.tsv"));

        reconstruction = entities.newReconstruction();
        reconstruction.setIdentifier(new ReconstructionIdentifier("MnxRef"));
        reconstruction.setTaxonomy(new Taxonomy(0, "unknw", Taxonomy.Kingdom.BACTERIA, "reference db", "reference db"));

        Set<String> participants = listParticipants(reacProps, reacXref);
        logger.info(participants.size() + " participants idenfied");

        Map<String, Metabolite> abbrvToMetabolite = Maps.newHashMapWithExpectedSize(participants.size());

        for (Metabolite m : buildMetabolome(chemProps, chemXref, participants)) {
            abbrvToMetabolite.put(m.getAbbreviation(), m);
            reconstruction.addMetabolite(m);
        }

        if (!participants.isEmpty()) {
            logger.warn(participants.size() + " participants were not found when loading chemicals: " + participants);
        }

        for (MetabolicReaction rxn : buildReactions(reacProps, reacXref, abbrvToMetabolite)) {
            reconstruction.addReaction(rxn);
        }

        logger.info("loaded");
        logger.info(reconstruction.metabolome().size() + " metabolites");
        logger.info(reconstruction.reactome().size() + " reactions");


        if (!issues.isEmpty()) {
            logger.warn("The following issues were found.");
            for (String issue : issues) {
                logger.warn("\t- " + issue);
            }
            issues.clear();
        }
    }


    /**
     * Build the MnxRef reactions. The metabolites should be built first and
     * indexed to the 'metabolome' map by the MNX ID (MDK abbreviation).
     *
     * @param reacProp   reac_prop file
     * @param reacXref   reac_xref file
     * @param metabolome map of MNX id the MDK Metabolite instance
     * @return list of metabolic reactions
     * @throws IOException
     */
    private List<MetabolicReaction> buildReactions(File reacProp, File reacXref, Map<String, Metabolite> metabolome) throws IOException {

        MnxRefReactionInput xrri = new MnxRefReactionInput(reacProp, reacXref);

        List<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();

        for (MnxRefReaction reaction : xrri.entries()) {
            MetabolicReaction rxn = parse(reaction.equation(), metabolome);
            if (rxn == null)
                continue;
            rxn.setIdentifier(BasicReactionIdentifier.nextIdentifier());
            rxn.setAbbreviation(reaction.id());
            rxn.setName(reaction.description());
            if (!reaction.enzymeCode().isEmpty()) {
                for (String ec : reaction.enzymeCode().split(";")) {
                    rxn.addAnnotation(new EnzymeClassification<Observation>(new ECNumber(ec)));
                }
            }
            reactions.add(rxn);
        }

        return reactions;
    }

    /**
     * Create the base 'reaction' for the provided equation. If an issue was
     * found then no reaction is returned.
     *
     * @param equation    equation for the reaction
     * @param metabolites map of the MNX id to the created metabolite object
     * @return reaction
     */
    private MetabolicReaction parse(String equation, Map<String, Metabolite> metabolites) {

        boolean product = false;
        double coef = 1.0;

        MetabolicReaction rxn = entities.reaction();

        for (String part : equation.split("\\s+")) {
            if (part.isEmpty())
                continue;

            char c = part.charAt(0);
            if (c == 'M') {
                Metabolite m = metabolites.get(part);
                if (m == null)
                    return null;
                if (product) {
                    rxn.addProduct(new MetabolicParticipantImplementation(m, coef));
                }
                else {
                    rxn.addReactant(new MetabolicParticipantImplementation(m, coef));
                }
                coef = 1;
            }
            else {
                if (Character.isDigit(c)) {
                    // stoichiometric
                    try {
                        coef = Double.parseDouble(part);
                    } catch (NumberFormatException e) {
                        issues.add("invalid coefficient " + part);
                        return null;
                    }
                }
                else {
                    if (c == '=') {
                        if (product) {
                            issues.add("multi step reaction: " + equation);
                            return null;
                        }
                        product = true;
                    }
                    else if (c != '+') {
                        return null;
                    }
                }
            }
        }
        return rxn;
    }

    /**
     * List the chemical ids which occur in reactions. Only loading those which
     * occur in reactions reduces the number of compounds which we store.
     *
     * @param reacProp properties of the reactions
     * @param reacXref cross-references of the reactions
     * @return list of participants
     * @throws IOException internal error whilst loading the reaction table
     */
    private Set<String> listParticipants(File reacProp, File reacXref) throws IOException {

        MnxRefReactionInput xrri = new MnxRefReactionInput(reacProp, reacXref);
        List<String> metabolites = new ArrayList<String>();

        for (MnxRefReaction reaction : xrri.entries()) {

            String equation = reaction.equation();
            for (String section : equation.split("\\s+")) {

                if (section.isEmpty())
                    continue;

                char c = section.charAt(0);
                if (c == 'M') {
                    metabolites.add(section);
                }
                else {
                    if (Character.isDigit(c)) {
                        // stoichiometric
                    }
                    else {
                        if (c == '=' || c == '+') {
                            // reaction grammar  
                        }
                        else {
                            issues.add("unknown reaction part: " + section);
                        }
                    }
                }
            }

        }

        return Sets.newHashSet(metabolites);
    }

    /**
     * Builds the metabolome of the MnxRxn version - only metabolites which are
     * members of the 'participants' are created.
     *
     * @param chemProps    chem_props file
     * @param chemXref     chem_xref file
     * @param participants which metabolites to create
     * @return metabolites
     * @throws IOException internal error whilst loading the compound file
     */
    private List<Metabolite> buildMetabolome(File chemProps, File chemXref, Set<String> participants) throws IOException {
        MnxRefCompoundInput xrci = new MnxRefCompoundInput(chemProps, chemXref);
        List<Metabolite> metabolites = new ArrayList<Metabolite>();
        for (MnxRefCompound compound : xrci.entries()) {
            if (participants.remove(compound.id()))
                metabolites.add(toMetabolite(compound));
        }
        return metabolites;
    }

    /**
     * Create a MDK Metabolite from a MnxRefCompound.
     *
     * @param cpd the MnxRef compound
     * @return metabolite for use in MDK
     */
    private Metabolite toMetabolite(MnxRefCompound cpd) {
        Metabolite m = entities.metabolite();

        m.setIdentifier(BasicChemicalIdentifier.nextIdentifier());
        m.setAbbreviation(cpd.id());
        m.setName(cpd.name());

        if (!cpd.formula().isEmpty())
            m.addAnnotation(new MolecularFormula(cpd.formula()));
        if (cpd.charge() != 0)
            m.addAnnotation(new Charge((double) cpd.charge()));
        if (cpd.mass() != 0)
            m.addAnnotation(new ExactMass((float) cpd.mass()));

        if (loadStructures && !cpd.inchi().isEmpty())
            m.addAnnotation(new InChI(cpd.inchi()));
        if (loadStructures && !cpd.smiles().isEmpty()) {
            try {
                String smi = cpd.smiles().replaceAll("\\[(?:ACP|PK|ION-CHAN|Flavopro|EGF|CALSEQUE|AGONIST|ACTIN|Raf|PKC|PLCbeta|TARGET|Cys|Gly|Glu|PROFILIN|PHOSPHOL|Hb|IP3R|R)\\]",
                                                     "[*]");

                if (!smi.equals(cpd.smiles())) {
                    issues.add("fixed invalid SMILES " + smi + " in " + cpd.id() + " original source " + cpd.source().split(":")[0]);
                }

                if (smi.contains("~")) {
                    issues.add("skipping SMARTS defintion in " + cpd.id() + " original source " + cpd.source().split(":")[0]);
                }
                else {
                    m.addAnnotation(new SMILES(smi));
                }
            } catch (Exception e) {
                System.out.println(cpd.smiles() + " is invalid");
            }
        }

        if (!loadXref)
            return m;

        for (MnxRefCompound.Xref xref : cpd.xrefs()) {
            String[] idParts = xref.id().split(":");
            Identifier id = identifiers.ofName(idParts[0], idParts[1]);
            if (id == IdentifierFactory.EMPTY_IDENTIFIER) {
                // special case
                if (idParts[0].equals("kegg")) {
                    switch (idParts[1].charAt(0)) {
                        case 'G':
                            m.addAnnotation(CrossReference.create(identifiers.ofName("KEGG Glycan",
                                                                                     idParts[1])));
                            break;
                        case 'C':
                            m.addAnnotation(CrossReference.create(identifiers.ofName("KEGG Compound",
                                                                                     idParts[1])));
                            break;
                        case 'D':
                            m.addAnnotation(CrossReference.create(identifiers.ofName("KEGG Drug",
                                                                                     idParts[1])));
                            break;
                        default:
                            issues.add("unknown kegg identifier prefix " + idParts[1].charAt(0) + "#####");
                    }
                }
                else {
                    issues.add("unknown resource " + idParts[0]);
                }
            }
            else {
                m.addAnnotation(CrossReference.create(id));
            }
        }

        return m;
    }


    /**
     * Simple utility to check a file exists and fail otherwise.
     *
     * @param f a file
     * @return the input file
     * @throws IOException thrown if the file 'f' did not exist
     */
    private static File checkExists(File f) throws IOException {
        if (!f.exists()) {
            throw new IOException("expected file " + f + " did not exists");
        }
        return f;
    }
}
