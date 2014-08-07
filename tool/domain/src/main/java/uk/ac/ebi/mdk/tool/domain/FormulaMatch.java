/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.tool.domain;

import org.openscience.cdk.config.Elements;
import org.openscience.cdk.graph.GraphUtil;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;

import static org.openscience.cdk.graph.GraphUtil.EdgeToBondMap;

/**
 * @author John May
 */
public class FormulaMatch {
    
    private void matches(IMolecularFormula formula, IAtomContainer cmpd) {
        
        final int[] expectedCounts = new int[128];
        final int[] actualCounts   = new int[128];
        Integer expectedCharge = formula.getCharge();
        Integer actualCharge = 0;
        
        for (IIsotope isotope : formula.isotopes()) {
            expectedCounts[isotope.getAtomicNumber()]++;
        }
        
        for (IAtom atom : cmpd.atoms()) {
            expectedCounts[atom.getAtomicNumber()]++;
            expectedCounts[1] += atom.getImplicitHydrogenCount();
            actualCharge += atom.getFormalCharge();
        }
        
        // condition: different pseudo atom
        if (expectedCounts[0] != actualCounts[0])
            return;
        
        // condition: different heavy atom count
        for (int i = 2; i < expectedCounts.length; i++)
            if (expectedCounts[i] != actualCounts[i])
                return;
        
        boolean chargeMatch = actualCharge.equals(expectedCharge);
        boolean hydMatch = expectedCounts[1] == actualCounts[1];
        
        // condition: charge match and hydrogen match
        if (chargeMatch && hydMatch)
            return;
        
        // condition: charges matched and hydrogen mismatch
        if (chargeMatch && !hydMatch)
            return;
    }
    
    private int[] hydrogenBounds(IAtomContainer cmpd){

        EdgeToBondMap bonds = EdgeToBondMap.withSpaceFor(cmpd);
        int[][]       graph = GraphUtil.toAdjList(cmpd, bonds);
        
        int addableHydrogens = 0, removableHydrogens = 0;
        
        ATOMS:
        for (int v = 0; v < cmpd.getAtomCount(); v++) {
            
            IAtom atm     = cmpd.getAtom(v);
            int   hCount  = atm.getImplicitHydrogenCount();
            int   valence = hCount;
            int   degree  = hCount + graph[v].length;
            int   charge  = atm.getFormalCharge();
            
            for (int w : graph[v]) {
                IBond.Order order = bonds.get(v, w).getOrder();
                if (order == null || order == IBond.Order.UNSET)
                    continue ATOMS;
                valence += order.numeric();
                if (cmpd.getAtom(w).getAtomicNumber() == 1)
                    hCount++;
            }
            
            switch (Elements.ofNumber(atm.getAtomicNumber())) {
                case Hydrogen:
                    if (charge == 1 && degree == 0)
                        removableHydrogens++;
                    break;
                case Carbon:
                    if (charge == +1 || charge == -1)
                        addableHydrogens += 1;
                    break;
                case Nitrogen:
                case Phosphorus:
                    if (charge == -1)
                        addableHydrogens++;
                    else if (charge == 0 && hCount > 0)
                        removableHydrogens += hCount;
                    else if (charge == +1 && hCount > 0)
                        removableHydrogens += hCount;
                    break;
                case Oxygen:
                case Sulfur:
                    if (charge == -1)
                        addableHydrogens++;
                    else if (charge == 0 && hCount > 0)
                        removableHydrogens += hCount;
                    else if (charge == +1 && hCount > 0)
                        removableHydrogens += hCount;
                    break;
            }
        }
        
        return new int[]{addableHydrogens, removableHydrogens};
    }
}
