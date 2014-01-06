package uk.ac.ebi.mdk.apps.tool;

import org.apache.commons.cli.Option;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.periodictable.PeriodicTable;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Reads a reconstruction and counts the frequency of chemical elements.
 *
 * @author John May
 */
public class ChemicalElementFrequency extends CommandLineMain {

    public static void main(String[] args) {
        new ChemicalElementFrequency().process(args);
    }

    @Override public void setupOptions() {
        add(new Option("r", "recon", true, "Reference reconstruction ('.mr' folder)"));
    }

    @Override public void process() {

        Reconstruction reference = null;
        try {
            reference = ReconstructionIOHelper.read(getFile("r"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }

        final int[] counts = new int[118];

        for (Metabolite m : reference.getMetabolome()) {
            for (ChemicalStructure cs : m.getStructures()) {
                IAtomContainer container = cs.getStructure();
                for (IAtom a : container.atoms()) {
                    if (a.getAtomicNumber() == null)
                        a.setAtomicNumber(0);
                    counts[a.getAtomicNumber()]++;
                }
            }
        }

        Integer[] idxs = new Integer[counts.length];
        for (int i = 0; i < counts.length; i++) {
            idxs[i] = i;
        }

        Arrays.sort(idxs, new Comparator<Integer>() {
            @Override public int compare(Integer o1, Integer o2) {
                return counts[o2] - counts[o1];
            }
        });

        for (Integer idx : idxs) {
            System.out.printf("%4s %3s: %d\n", PeriodicTable.getSymbol(idx), idx.toString(), counts[idx]);
        }
    }
}
