package uk.ac.ebi.mdk.apps.tool;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.cli.Option;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.tool.domain.TransportReactionUtil;

import java.io.IOException;

/**
 * List the transport reactions in a model.
 *
 * @author John May
 */
public class ListTransporters extends CommandLineMain {

    public static void main(String[] args) {
        new ListTransporters().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("i", "input", true, "reconstruction ('.mr' folder)"));
    }

    @Override public void process() {
        try {
            Reconstruction reconstruction = ReconstructionIOHelper
                    .read(getFile("i"));
            for(MetabolicReaction r : reconstruction.reactome()){
                if(TransportReactionUtil.isTransport(r)){
                    System.out.println(r.getAccession() + "\t" + r);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
