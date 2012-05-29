package uk.ac.ebi.mdk.apps.tool;

import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.tool.MappedEntityAligner;
import uk.ac.ebi.mdk.tool.domain.MolecularHashFactory;
import uk.ac.ebi.mdk.tool.domain.hash.*;
import uk.ac.ebi.mdk.tool.match.EntityAligner;
import uk.ac.ebi.mdk.tool.match.MetaboliteHashCodeMatcher;
import uk.ac.ebi.mdk.tool.match.NameMatcher;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Tool finds cases where molecules match on structure but not on name
 *
 * @author John May
 */
public class FindMismatches extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(FindMismatches.class);

    public static void main(String[] args) {
        new FindMismatches().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("q", "query", true, "Query reconstruction"));
        add(new Option("r", "reference", true, "Reference reconstruction"));
        add(new Option("s", "synonyms", false, "Include synonyms?"));
    }

    @Override
    public void process() {


        Reconstruction query = getReconstruction(getFile("q"));
        Reconstruction reference = getReconstruction(getFile("r"));

        EntityAligner<Metabolite> aligner = new MappedEntityAligner<Metabolite>(reference.getMetabolome(),
                                                                                true,
                                                                                true);

        MetaboliteHashCodeMatcher hashCodeMatcher = new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                                                                  BondOrderSumSeed.class,
                                                                                  ConnectedAtomSeed.class,
                                                                                  ChargeSeed.class,
                                                                                  StereoSeed.class);

        MolecularHashFactory.getInstance().setDepth(1);
        aligner.push(hashCodeMatcher);
//        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
//                                                   BondOrderSumSeed.class,
//                                                   ConnectedAtomSeed.class,
//                                                   StereoSeed.class));
//        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
//                                                   BondOrderSumSeed.class,
//                                                   ConnectedAtomSeed.class,
//                                                   ChargeSeed.class));
//        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
//                                                   BondOrderSumSeed.class,
//                                                   ConnectedAtomSeed.class));

        NameMatcher matcher = new NameMatcher<Metabolite>(true, has("s"));

        int matched = 0;

        for (Metabolite metabolite : query.getMetabolome()) {

            List<Metabolite> matches = aligner.getMatches(metabolite);

            matched += matches.isEmpty() ? 0 : 1;

            System.out.println(metabolite.getName() + ": ");
            for (Metabolite match : matches) {
                boolean nameMatch = matcher.matches(metabolite, match);
                System.out.println("\t" + match.getIdentifier() + " [" + nameMatch + "]");
                if (!nameMatch) {

                    for (ChemicalStructure structure : metabolite.getStructures()) {
                        System.out.println("\t\t" + hashCodeMatcher.getHash(structure.getStructure()));
                        System.out.println("\t\t" + hashCodeMatcher.getHash(match.getStructures().iterator().next().getStructure()));
                    }

                }
            }
        }

        System.out.println(matched);

    }


    public Reconstruction getReconstruction(File file) {
        try {
            return ReconstructionIOHelper.read(file);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new InvalidParameterException("Reconstruction " + file + " was not read!");
    }
}
