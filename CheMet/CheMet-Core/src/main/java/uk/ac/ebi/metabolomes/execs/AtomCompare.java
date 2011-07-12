/*
 *     This file is part of Metabolic Network Builder
 * 
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.execs;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * Sample class to test the comparison of two atoms
 * @author johnmay
 */
public class AtomCompare {

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) {

        // TODO code application logic here
        AtomContainerComparator comp = new AtomContainerComparator();
        IMolecule benzene = MoleculeFactory.makeBenzene();
        IMolecule triaszole = MoleculeFactory.make123Triazole();
        int selfresult = comp.compare( benzene , benzene );
        System.out.println( "bezene vs. bezene: " + selfresult );
        int diffresult = comp.compare( benzene , triaszole );
        System.out.println( "bezene vs. 123Triazole: " + diffresult );
    }
}
