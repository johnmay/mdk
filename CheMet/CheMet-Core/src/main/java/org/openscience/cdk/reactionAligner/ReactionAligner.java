/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openscience.cdk.reactionAligner;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.apache.log4j.Logger;
import org.openscience.cdk.Mapping;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.fingerprint.FingerprinterTool;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMapping;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.tools.manipulator.ReactionManipulator;


/**
 * This class aims at aligning two CDK Reactions, so that their most similar
 * reactants and products can then be compared. The class will always produce
 * an output, so it is meant for alignment of reactions that "make sense".
 *
 * @author pmoreno
 */
@Deprecated
public class ReactionAligner {

    private Logger logger;


    /**
     * This method returns a list of finger prints in the order
     * that metabolites are in the reaction, according to the
     * reaction manipulator class.
     * @param rxn
     * @return
     */
    List<BitSet> calculateFingerPrintsForRxn(IMoleculeSet rxnMols) {
        ArrayList<BitSet> rxnFPs = new ArrayList<BitSet>();
        Fingerprinter fingerprinter = new Fingerprinter();
        for (IAtomContainer mol : rxnMols.molecules()) {
            BitSet fingerPrint = null;
            try {
                fingerPrint = fingerprinter.getFingerprint(mol);
            } catch (CDKException ex) {
                logger.error("Probably fingerprinter timed out on ring search", ex);
            }
            rxnFPs.add(fingerPrint);
        }

        return rxnFPs;
    }


    /**
     * Given reactions A and B, delivers a mapping between them based on the fingerprints.
     * The mapping is delivered as a
     * @param rxnA
     * @param rxnB
     * @return
     */
    public List<IMapping> getMappingBetweenReactions(IReaction rxnA, IReaction rxnB) {

        List<IMapping> mappingRels = new ArrayList<IMapping>();
        IMoleculeSet molsRxnA = ReactionManipulator.getAllMolecules(rxnA);
        IMoleculeSet molsRxnB = ReactionManipulator.getAllMolecules(rxnB);
        List<BitSet> rxnAFPs = this.calculateFingerPrintsForRxn(molsRxnA);
        List<BitSet> rxnBFPs = this.calculateFingerPrintsForRxn(molsRxnB);

        for (int i = 0; i < rxnAFPs.size(); i++) {
            if (rxnAFPs.get(i) == null) {
                continue;
            }
            int minDiff = 100000;
            List<Integer> candidates = new ArrayList<Integer>();
            for (int j = 0; j < rxnBFPs.size(); j++) {
                if (rxnBFPs.get(j) == null) {
                    continue;
                }
                int differences = FingerprinterTool.listDifferences(rxnAFPs.get(i), rxnBFPs.get(j)).size();
                if (differences < minDiff) {
                    candidates.clear();
                    candidates.add(j);
                    minDiff = differences;
                } else if (differences == minDiff) {
                    candidates.add(j);
                }

            }
            if (candidates.size() == 1) {
                mappingRels.add(new Mapping(molsRxnA.getMolecule(i), molsRxnB.getMolecule(candidates.get(0))));
            }
        }
        return mappingRels;
    }
}
