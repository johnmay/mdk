package uk.ac.ebi.mdk.apps.tool;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.cli.Option;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.io.IOException;
import java.util.UUID;

/**
 * List metabolites which only occur in one reaction (i.e. at the edge of the
 * pathway). These metabolites <i>could</i> need <i>exchange</i> reactions.
 *
 * @author John May
 */
public class ListFringeMetabolites extends CommandLineMain {

    public static void main(String[] args) {
        new ListFringeMetabolites().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("i", "input", true, "reconstruction ('.mr' folder)"));
    }

    @Override public void process() {
        try {
            Reconstruction reconstruction = ReconstructionIOHelper
                    .read(getFile("i"));
            BiMap<UUID, Integer> metaboliteMap = HashBiMap.create(reconstruction
                                                                          .metabolome()
                                                                          .size());
            // index metabolites
            int i = 0;
            for (Metabolite m : reconstruction.metabolome())
                metaboliteMap.put(m.uuid(), i++);

            int[] occurences = new int[i];

            for (MetabolicReaction rxn : reconstruction.reactome()) {
                for (MetabolicParticipant p : rxn.getParticipants()) {
                    occurences[metaboliteMap.get(p.getMolecule().uuid())]++;
                }
            }

            for (int j = 0; j < occurences.length; j++) {
                if (occurences[j] == 1) {
                    Metabolite m = reconstruction.entity(metaboliteMap.inverse()
                                                                      .get(j));
                    System.out.println(m.getAccession() + ": " + m.getName());
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
