/*
 * Copyright (c) 2013. Pablo Moreno
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
package uk.ac.ebi.mdk.tool.comparator;

import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 * Compare atom container by their heavy atom count.
 * @author  pmoreno
 */
public final class HeavyAtomCountComparator implements Comparator<IAtomContainer> {

    /**
     * @inheritDoc
     */
    @Override
    public int compare(IAtomContainer molA, IAtomContainer molB) {
        IMolecularFormula formA = MolecularFormulaManipulator.getMolecularFormula(molA);
        IMolecularFormula formB = MolecularFormulaManipulator.getMolecularFormula(molB);
        
        List<IElement> heavyElementsA = MolecularFormulaManipulator.getHeavyElements(formA);
        List<IElement> heavyElementsB = MolecularFormulaManipulator.getHeavyElements(formB);
        
        return ((Integer)heavyElementsA.size()).compareTo(heavyElementsB.size());
    }

    /**
     * Convenience comparator conjunction.
     * @param other comparator to invoke if this comparator ties
     * @return a new comparator composing this and <i>next</i>.
     */
    public static Comparator<IAtomContainer> and(Comparator<IAtomContainer> other) {
        return new ComparatorConjunction<IAtomContainer>(new HeavyAtomCountComparator(),
                                                         other);
    }
}
