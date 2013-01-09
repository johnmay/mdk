/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.matrix;

import uk.ac.ebi.mdk.domain.identifier.InChI;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;


/**
 * InChIStoichiometricMatrix.java
 *
 *
 * @author johnmay @date Apr 6, 2011
 */
public class InChIStoichiometricMatrix
        extends StoichiometricMatrixImpl<InChI, ECNumber> {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(
            InChIStoichiometricMatrix.class);


    protected InChIStoichiometricMatrix() {
    }


    protected InChIStoichiometricMatrix(int n, int m) {
        super(n, m);
    }


    @Override
    public Class<? extends ECNumber> getReactionClass() {
        return ECNumber.class;
    }


    @Override
    public Class<? extends InChI> getMoleculeClass() {
        return InChI.class;
    }


    @Override
    public final InChIStoichiometricMatrix init() {
        return (InChIStoichiometricMatrix) super.init();
    }


    public static InChIStoichiometricMatrix create() {
        return new InChIStoichiometricMatrix().init();
    }


    public static InChIStoichiometricMatrix create(int n, int m) {
        return new InChIStoichiometricMatrix(n, m).init();
    }

  

    /**
     * Returns reactions with stochiometric < 0
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *

     *
     * @param metabolite
     * @return
     */
    public ArrayList<ECNumber> getReactionsProducingMolecule(InChI metabolite) {
        ArrayList<ECNumber> productionReactions = new ArrayList<ECNumber>();
        Map<Integer, Double> reactions = getReactions(metabolite);
        for (Entry<Integer, Double> e : reactions.entrySet()) {
            if (e.getValue() > 0.0) {
                productionReactions.add(getReaction(e.getKey()));
            }
        }
        return productionReactions;
    }


    /**
     * Returns reactions with stochiometric < 0
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *

     *
     * @param metabolite
     * @return
     */
    public ArrayList<ECNumber> getReactionsConsumingMolecule(InChI metabolite) {
        ArrayList<ECNumber> productionReactions = new ArrayList<ECNumber>();
        Map<Integer, Double> reactions = getReactions(metabolite);
        for (Entry<Integer, Double> e : reactions.entrySet()) {
            if (e.getValue() < 0.0) {
                productionReactions.add(getReaction(e.getKey()));
            }
        }
        return productionReactions;
    }


    @Override
    public StoichiometricMatrixImpl<InChI, ECNumber> newInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public StoichiometricMatrixImpl<InChI, ECNumber> newInstance(int n, int m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
