package uk.ac.ebi.mdk.apps.io;

import org.apache.commons.cli.Option;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.io.IOException;

/**
 * Simple runnable class for converting MnxRef to MDK/Metingear reconstruction.
 * Example usage: 
 * <pre>
 * MnxRefConverterRun -mrhome /databases/MnxRef -o /databases/MnxRef.mr
 * </pre>
 *
 * @author John May
 */
public class MnxRefConverterRun extends CommandLineMain {

    @Override public void setupOptions() {
        add(new Option("mrhome", true, "root directory of MnxRef"));
        add(new Option("o", true, "output reconstruction name"));
    }

    @Override public void process() {
        try {
            Reconstruction reconstruction = MnxRefConverter.convert(getFile("mrhome"));
            ReconstructionIOHelper.write(reconstruction, getFile("o"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new MnxRefConverterRun().process(args);
    }
}
