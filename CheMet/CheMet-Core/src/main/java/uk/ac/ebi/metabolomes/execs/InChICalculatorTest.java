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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.Mol2Reader;

/**
 * InChICalculatorTest.java – MetabolicDevelopmentKit – Jun 30, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class InChICalculatorTest {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( InChICalculatorTest.class );

    public static void main( String[] args ) throws FileNotFoundException, CDKException {
        File f = new File( "/Users/johnmay/Desktop/C02232.mol" );
        Mol2Reader reader = new Mol2Reader( new FileInputStream( f ) );
        IMolecule mol = DefaultChemObjectBuilder.getInstance().newInstance( IMolecule.class);
        mol = reader.read(mol);
    }
}
