package uk.ac.ebi.mdk.apps.tool;

import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import org.apache.commons.cli.Option;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.HybridizationFingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.Set;

/**
 * Utility which takes a reconstruction and a txt file of names and produces
 * fingerprints for those compounds. The current fingerprint method is the
 * hybridisation fingerprinter from the CDK.
 * 
 * @author John May
 */
public class FingerprintNamedMetabolites extends CommandLineMain {

    public static void main(String[] args) {
        new FingerprintNamedMetabolites().process(args);
    }

    @Override public void setupOptions() {
        add(new Option("i", "input", true, "metabolic reconstruction (.mr)"));
        add(new Option("n", "names", true, "names of metabolites to fingerprint"));
    }

    @Override public void process() {

        Reconstruction reconstruction = load(getFile("i"));
        Set<String> names = nameSet(getFile("n"));

        HybridizationFingerprinter fpr = new HybridizationFingerprinter();

        try {
            for (String name : names) {
                for (Metabolite m : reconstruction.metabolome().ofName(name)) {
                    for (ChemicalStructure cs : m.getStructures()) {
                        IAtomContainer container = cs.getStructure();
                        configure(container);
                        BitSet s = fpr.getBitFingerprint(container).asBitSet();
                        System.out.println("\"" + name + "\"," + toBinaryString(s, 1024));
                    }
                }
            }
        } catch (CDKException e) {
            System.err.println(e.getMessage());
        }

    }

    String toBinaryString(BitSet s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (i > 0)
                sb.append(",");
            sb.append(s.get(i) ? 1 : 0);
        }
        return sb.toString();
    }

    private void configure(IAtomContainer m) {
        try {
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(m);
        } catch (CDKException e) {
            System.err.println(e.getMessage());
        }
    }

    static Set<String> nameSet(File f) {
        try {
            return Sets.newHashSet(CharStreams.readLines(new FileReader(f)));
        } catch (IOException e) {
            throw new InternalError(e.getMessage());
        }
    }

    static Reconstruction load(File f) {
        try {
            return ReconstructionIOHelper.read(f);
        } catch (ClassNotFoundException e) {
            throw new InternalError(e.getMessage());
        } catch (IOException e) {
            throw new InternalError(e.getMessage());
        }
    }
}
