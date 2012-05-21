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
package uk.ac.ebi.metabolomes.io.flatfile;

import au.com.bytecode.opencsv.CSVReader;
import uk.ac.ebi.mdk.db.DatabaseProperties;

import java.io.*;
import java.util.HashMap;

/**
 * EnzymeAlignmentDensities.java – MetabolicDevelopmentKit – Jun 21, 2011
 * Class reads the enzyme global alignment densities as used in the DETECT
 * (Density Estimation Tool for Enzyme ClassificaTion). Hung SS. 2010
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class EnzymeAlignmentDensities {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( EnzymeAlignmentDensities.class );
    private static final File positiveDensityFile       = DatabaseProperties.getInstance().getFile( "detect.positive" );
    private static final File negativeDensityFile       = DatabaseProperties.getInstance().getFile( "detect.negative" );
    private HashMap<String , String> positiveDensity    = new HashMap<String , String>();
    private HashMap<String , String> negativeDensity    = new HashMap<String , String>();

    private EnzymeAlignmentDensities() {
        try {
            loadDensities();
        } catch ( FileNotFoundException ex ) {
            ex.printStackTrace();
        } catch(IOException ex ) {

        }
    }

    private void loadDensities() throws FileNotFoundException, IOException {
        CSVReader reader = new CSVReader(new BufferedReader(new FileReader( positiveDensityFile ) ) );
        String[] values;
        while((values = reader.readNext()) != null) {
            String ec = values[1];
        }
    }

    public static EnzymeAlignmentDensities getInstance() {
        return EnzymeAlignmentDensitiesHolder.INSTANCE;
    }

    private static class EnzymeAlignmentDensitiesHolder {

        public static EnzymeAlignmentDensities INSTANCE = new EnzymeAlignmentDensities();
    }

    public static void main( String[] args ) {
        EnzymeAlignmentDensities.getInstance();
    }
}
