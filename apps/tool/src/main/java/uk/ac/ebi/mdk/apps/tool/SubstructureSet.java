package uk.ac.ebi.mdk.apps.tool;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.FingerprinterTool;
import org.openscience.cdk.fingerprint.IFingerprinter;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility for helping to do subtructure matches. Given a set of structure one
 * can test if the molecule is 'matched' within that set.
 *
 * @author John May
 */
public class SubstructureSet {

    private final IFingerprinter              fpr;
    private final Map<BitSet, IAtomContainer> map;

    public SubstructureSet(IFingerprinter fpr, List<IAtomContainer> ms) throws CDKException {
        this.fpr = fpr;
        this.map = index(ms);
    }

    /**
     * Is the molecule 'm' matched by this set of substructures.
     *
     * @param m molecule
     * @return the molecule is matched
     * @throws CDKException fingerprint exception
     */
    public boolean contains(IAtomContainer m) throws CDKException {

        // safer to just do the atom-typing :(
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(m);

        BitSet fp = fpr.getBitFingerprint(m).asBitSet();

        UniversalIsomorphismTester uit = new UniversalIsomorphismTester();

        for (Map.Entry<BitSet, IAtomContainer> e : map.entrySet()) {
            if (FingerprinterTool.isSubset(fp, e.getKey()) && uit.isSubgraph(m, e.getValue())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Create fingerprints (using the provided fingerprinter) for the provided
     * molecules. Note pseudo atoms and hydrogens are removed.
     *
     * @param ms molecules
     * @return fingperpints for the molecules
     * @throws CDKException
     */
    private Map<BitSet, IAtomContainer> index(List<IAtomContainer> ms) throws CDKException {

        Map<BitSet, IAtomContainer> fps = new HashMap<BitSet, IAtomContainer>(ms.size());

        for (IAtomContainer m : ms) {
            m = prepareForFPMatch(m);
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(m);
            if (fps.put(fpr.getBitFingerprint(m).asBitSet(), m) != null)
                System.err.println("replacing existing fingerprint!");
        }

        return fps;

    }

    /**
     * Prepares a molecule for fingerprint matching - removes all pseudo atoms
     * and ALL hydrogens (no exceptions for charge, isotope and bridge). The
     * original molecule is not modified.
     *
     * @param src molecule
     * @return new molecule with no pseudo-atoms
     * @throws RuntimeException clone-not supported
     */
    private static IAtomContainer prepareForFPMatch(IAtomContainer src) {
        try {

            IAtomContainer cpy = src.clone();

            AtomContainerManipulator.removeHydrogens(cpy);

            for (int i = 0; i < src.getAtomCount(); i++) {
                IAtom a = cpy.getAtom(i);
                if (a instanceof IPseudoAtom)
                    cpy.removeAtomAndConnectedElectronContainers(a);
            }

            return cpy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
