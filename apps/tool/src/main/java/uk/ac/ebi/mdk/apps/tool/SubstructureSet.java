package uk.ac.ebi.mdk.apps.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.FingerprinterTool;
import org.openscience.cdk.fingerprint.IFingerprinter;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Utility for helping to do subtructure matches. Given a set of structure one
 * can test if the molecule is 'matched' within that set.
 *
 * @author John May
 */
public class SubstructureSet {

    private final IFingerprinter fpr;
    private final Multimap<IAtomContainer, Metabolite> lookup     = HashMultimap.create();
    private final Multimap<BitSet, IAtomContainer>     structures = HashMultimap.create();

    public SubstructureSet(IFingerprinter fpr, Collection<Metabolite> ms) throws CDKException {
        this.fpr = fpr;
        index(ms);
    }

    /**
     * Is the molecule 'm' matched by this set of substructures.
     *
     * @param m molecule
     * @return the molecule is matched
     * @throws CDKException fingerprint exception
     */
    public Collection<Metabolite> matching(IAtomContainer m) {

        try {

            // safer to just do the atom-typing :(
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(m);

            BitSet fp = fpr.getBitFingerprint(m).asBitSet();

            UniversalIsomorphismTester uit = new UniversalIsomorphismTester();

            List<Metabolite> matches = new ArrayList<Metabolite>();
            
            for (BitSet s : structures.keySet()) {
                if (FingerprinterTool.isSubset(fp, s)) {
                    for (IAtomContainer candidate : structures.get(s)) {
                        if (uit.isSubgraph(m, candidate)) {
                            matches.addAll(lookup.get(candidate));  
                        }
                    }
                }
            }

            return matches;
        } catch (CDKException e) {
            System.err.println("CDK had a problem: " + e);
            return Collections.emptyList();
        }
    }

    /**
     * Create fingerprints (using the provided fingerprinter) for the provided
     * molecules. Note pseudo atoms and hydrogens are removed.
     *
     * @param ms molecules
     * @return fingperpints for the molecules
     * @throws CDKException
     */
    private void index(Collection<Metabolite> ms) throws CDKException {
        for (Metabolite m : ms) {
            for (ChemicalStructure cs : m.getStructures()) {
                IAtomContainer container = cs.getStructure();
                container = prepareForFPMatch(container);
                AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(container);
                structures.put(fpr.getBitFingerprint(container).asBitSet(), container);
                lookup.put(container, m);
            }
        }
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
