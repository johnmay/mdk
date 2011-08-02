/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author pmoreno
 */
public class StoichMatrix {

    private HashMap<Long, HashMap<Long, Integer>> matrix;
    private LinkedHashSet<Long> rxns;
    private LinkedHashSet<Long> metabs;

    public StoichMatrix() {
        this.matrix = new HashMap<Long, HashMap<Long, Integer>>();
        this.rxns = new LinkedHashSet<Long>();
        this.metabs = new LinkedHashSet<Long>();
    }

    public void addReaction(Long rxn) {
        if (!matrix.containsKey(rxn)) {
            matrix.put(rxn, new HashMap<Long, Integer>());
            rxns.add(rxn);
        }
    }

    public void addMetabolite(Long rxn, Long metabolite, Integer coef) {
        matrix.get(rxn).put(metabolite, coef);
        if (!metabs.contains(metabolite)) {
            metabs.add(metabolite);
        }
    }

    public Set<Long> getRxns() {
        return rxns;
    }

    public int getNumRxns() {
        return rxns.size();
    }

    public int getNumMetabs() {
        return metabs.size();
    }

    public Set<Long> getMetabs() {
        return metabs;
    }

    public int[][] getAsMatrix() {
        int[][] matrixRep = new int[rxns.size()][metabs.size()];
        int rowMetab = 0;
        int colRxn = 0;
        for (Long rxn : rxns) {
            for (Long metab : metabs) {
                if (matrix.get(rxn).containsKey(metab)) {
                    matrixRep[rowMetab][colRxn] = matrix.get(rxn).get(metab);
                } else {
                    matrixRep[rowMetab][colRxn] = 0;
                }
                rowMetab++;
            }
            colRxn++;
        }
        return matrixRep;
    }

    public int getNumOfDifferentRxns() {
        ArrayList<Long> rxnsTree = new ArrayList<Long>(rxns);
        int rxnsS = rxnsTree.size();
        int numOfDifRxns = this.getNumRxns();
        ArrayList<Long> excluded = new ArrayList<Long>();
        Long rxnOne;
        Long rxnTwo;
        for (int i = 0; i < rxnsS - 1; i++) {
            rxnOne = rxnsTree.get(i);
            if (excluded.contains(rxnOne)) {
                continue;
            }
            for (int j = i + 1; j < rxnsS; j++) {
                rxnTwo = rxnsTree.get(j);
                if (matrix.get(rxnOne).size() == matrix.get(rxnTwo).size()) {
                    boolean misMatch = false;
                    for (Long metabInI : matrix.get(rxnOne).keySet()) {
                        if (!matrix.get(rxnTwo).containsKey(metabInI)) {
                            misMatch = true;
                        } else if (!matrix.get(rxnTwo).get(metabInI).equals(matrix.get(rxnOne).get(metabInI))) {
                            misMatch = true;
                        }
                        if (misMatch) {
                            break;
                        }
                    }
                    if (!misMatch) {
                        excluded.add(rxnTwo);
                        numOfDifRxns--;
                    }
                }

            }
        }

        return numOfDifRxns;

    }

    public Integer getConnectivityOfMetabolite(long metaboliteWID) {
        if (!this.metabs.contains(metaboliteWID)) {
            return null;
        }
        int conections = 0;
        for (Long rxnWID : rxns) {
            if (this.matrix.get(rxnWID).containsKey(metaboliteWID)) {
                conections++;
            }
        }
        return conections;
    }

    public Integer getMaxConnectivityOfReactionSubstrates(long rxnWID) {
        if (!this.rxns.contains(rxnWID)) {
            return null;
        }
        Set<Long> substrates = this.matrix.get(rxnWID).keySet();
        int maxCon = 0;
        for (Long subWID : substrates) {
            // Not only substrates, remove products
            if(this.matrix.get(rxnWID).get(subWID) > 0)
                continue;
            maxCon = Math.max(maxCon, this.getConnectivityOfMetabolite(subWID));
        }
        return maxCon;
    }

    public Integer getMaxConnectivityOfReactionProducts(long rxnWID) {
        if (!this.rxns.contains(rxnWID)) {
            return null;
        }
        Set<Long> products = this.matrix.get(rxnWID).keySet();
        int maxCon = 0;
        for (Long prodWID : products) {
            // Not only substrates, remove products
            if(this.matrix.get(rxnWID).get(prodWID) < 0)
                continue;
            maxCon = Math.max(maxCon, this.getConnectivityOfMetabolite(prodWID));
        }
        return maxCon;
    }

    public Set<Long> getRxnsForMetabolite(Long chemWID) {
        Set<Long> resRxns = new HashSet<Long>();
        for (Long rxnWID : rxns) {
            HashMap<Long,Integer> rxnHM = matrix.get(rxnWID);
            if(rxnHM.containsKey(chemWID))
                resRxns.add(rxnWID);
        }
        return resRxns;
    }
}
