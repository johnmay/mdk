/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.tool.match;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.IFingerprinter;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Sub-structure matching will indicate two metabolites as matching if the
 * reference is a sub-structure of the query. The matcher can be 'strict'
 * in which case the match will always be false if the reference is not
 * a Markush structure (containing -R). The fingerprinting method is specified
 * by providing an {@see IFingerprinter} instance to the constructor. In most
 * cases the basic CDK fingerprinter will be sufficient but using the
 * 'ExtendedFingerprinter' may yield more specific results.
 *
 * @author John May
 */
public class SubstructureMatcher extends AbstractMatcher<Metabolite, Map<BitSet, IAtomContainer>> {

    private static final Logger LOGGER = Logger.getLogger(SubstructureMatcher.class);

    private IFingerprinter fingerprinter;
    private Isomorphism isomorphism = new Isomorphism(Algorithm.SubStructure, true);
    private Boolean strict;

    public SubstructureMatcher(IFingerprinter fingerprinter, Boolean strict) {
        this.fingerprinter = fingerprinter;
        this.strict = strict;
    }

    /**
     * Indicates that the match is partial 'substructure' and can not be optimised
     * using a hashmap.
     *
     * @return true
     */
    @Override
    public Boolean partialMatch() {
        return Boolean.TRUE;
    }

    /**
     * Matches the query with the reference metric. Each reference vs each query is tested, if any reference is a
     * substructure of the query the metrics are considered matching. To test the substructure the fingerprints
     * are first checked that all the bits in the reference are set in the query and that the query has a larger
     * fingerprint then the reference. If either of these conditions is not met the full sub-structure search does not
     * proceed. Next the reference structure is checked for '-R' groups, if an '-R' group does exist and the matcher
     * has been set to 'strict' (see. constructor) the matcher moves to the next reference structure. Providing the
     * structure didn't contain an 'R' (non-strict) or did (strict) then the Small Molecule Subgraph Detector
     * (see. CDK) is used to test the substructure match. If the reference is a sub-graph of the query the match will
     * return true. If it is not a sub-graph the matcher will continue to the next query structure. Only once all
     * reference structures have been tested against all reference structures and no match has been found will the
     * method return false.
     *
     * @param query     metric for the query calculated via the 'calculateMetric' method
     * @param reference metric for the reference calculated via the 'calculateMetric' method
     *
     * @return whether the reference metabolite is a subgraph of the query
     */
    @Override
    public Boolean matchMetric(Map<BitSet, IAtomContainer> query,
                               Map<BitSet, IAtomContainer> reference) {

        REFERENCE:
        for (Map.Entry<BitSet, IAtomContainer> referenceEntry : reference.entrySet()) {

            BitSet referenceBits = referenceEntry.getKey();

            QUERY:
            for (Map.Entry<BitSet, IAtomContainer> queryEntry : query.entrySet()) {

                BitSet queryBits = queryEntry.getKey();

                if (isSubset(queryBits, referenceBits)) {

                    IAtomContainer queryStructure = queryEntry.getValue();
                    IAtomContainer referenceStructure = referenceEntry.getValue();

                    Boolean markush = isMarkush(referenceStructure);

                    // reference is not a markush structure and matcher is
                    // indicated to only allow markush
                    if (strict && !markush) {
                        // continue to the next iteration
                        continue REFERENCE;
                    }

                    if (markush) {
                        referenceStructure = removeMarkush(referenceStructure);
                    }

                    try {

                        isomorphism.init(referenceStructure, queryStructure, true, true);

                        if (isomorphism.isSubgraph())
                            return Boolean.TRUE;

                        // continues if not sub-graph

                    } catch (CDKException ex) {
                        LOGGER.error("Could not initialise Isomorphism", ex);
                    }


                }

            }
        }

        return Boolean.FALSE;

    }

    /**
     * Calculates the required metric for the entity. This instance will return a map
     * of all chemical structures (in the entity) providing a fingerprint (bitset) and
     * the original molecule. The fingerprint is calculated using the provided IFingerprinter
     * in the constructor. Note. the molecule is cleaned (hydrogens and 'R' groups removed)
     * removed calculating the fingerprint but the original molecule (including R and H) is
     * provided as the value in the map.
     *
     * @inheritDoc
     */
    @Override
    public Map<BitSet, IAtomContainer> calculatedMetric(Metabolite entity) {

        Map<BitSet, IAtomContainer> map = new HashMap<BitSet, IAtomContainer>();

        for (ChemicalStructure annotation : entity.getStructures()) {

            IAtomContainer original = AtomContainerManipulator.removeHydrogens(annotation.getStructure());
            IAtomContainer cleaned = original;

            // check and remove R groups
            if (isMarkush(cleaned)) {
                cleaned = removeMarkush(cleaned);
            }


            try {

                map.put(fingerprinter.getBitFingerprint(cleaned).asBitSet(), original);

            } catch (CDKException ex) {
                LOGGER.debug("Unable to calculate fingerprint for a structure in entity " + entity.getIdentifier(), ex);
            }

        }

        return map;
    }


    private static Boolean isSubset(BitSet query, BitSet reference) {


        if (query.cardinality() < reference.cardinality()) {
            return Boolean.FALSE;
        }

        for (int i = reference.nextSetBit(0); i >= 0; i = reference.nextSetBit(i + 1)) {

            // if we find a bit the reference does not have we don't have a sub-structure
            // and we continue to the next iteration of the 'QUERY' loop.
            if (!query.get(i)) {
                return Boolean.FALSE;
            }

        }

        return Boolean.TRUE;

    }

    private static Boolean isMarkush(IAtomContainer structure) {

        for (IAtom atom : structure.atoms()) {
            if (atom.getSymbol().equals("R")) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;

    }

    private static IAtomContainer removeMarkush(IAtomContainer structure) {

        IChemObjectBuilder builder = structure.getBuilder();
        IAtomContainer shallow = builder.newInstance(IAtomContainer.class, structure);

        for (IAtom atom : shallow.atoms()) {
            if (atom.getSymbol().equals("R")) {
                shallow.removeAtomAndConnectedElectronContainers(atom);
            }
        }

        return shallow;

    }

}
