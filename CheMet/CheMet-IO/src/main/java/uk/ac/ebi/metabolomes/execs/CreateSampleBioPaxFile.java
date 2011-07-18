/**
 * CreateSampleBioPaxFile.java
 *
 * Version $Revision$
 *
 * 2011.07.18
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.execs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.biopax.paxtools.io.jena.JenaIOHandler;
import org.biopax.paxtools.model.BioPAXElement;
import org.biopax.paxtools.model.BioPAXLevel;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.level2.biochemicalReaction;

/**
 * @name    CreateSampleBioPaxFile
 * @date    2011.07.18
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   This class creates a sample (that is subset) output file of selected biochemical reactions from an input
 *          file. The paxtools-jean-io library is utilised for reading/writing the file. The number of 'kept' entires
 *          can be specified by the option '--number' option or left at 100 by default.
 *
 */
public class CreateSampleBioPaxFile
        extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger( CreateSampleBioPaxFile.class );

    public static void main( String[] args ) {
        new CreateSampleBioPaxFile( args ).process();
    }

    private CreateSampleBioPaxFile( String[] args ) {
        super( args );
    }

    @Override
    public void setupOptions() {
        // add options here
        add( new Option( "i" , "input" , true , "A BioPax [.owl] input file" ) );
        add( new Option( "o" , "output" , true , "Path for an OWL output file" ) );
        add( new Option( "n" , "number" , true , "Number of elements to keep" ) );
    }

    @Override
    public void process() {

        try {

            // options
            File input = getCmd().hasOption( "i" ) ? new File( getCmd().getOptionValue( "i" ) ) : null;
            File output = getCmd().hasOption( "o" ) ? new File( getCmd().getOptionValue( "o" ) ) : null;
            Integer n = Integer.parseInt( getCmd().getOptionValue( "n" , "100" ) );

            if ( input == null ) {
                LOGGER.error( "Missing required --input option" );
                printHelp();
            }
            if ( output == null ) {
                output = new File( input.getPath() + ".sample" );
            }

            // load the file and remove all BioPaxElement after the initial number

            JenaIOHandler handler = new JenaIOHandler( BioPAXLevel.L2 );
            Model model = handler.convertFromOWL( new BufferedInputStream( new FileInputStream( input ) , 5000000 ) );

            List<BioPAXElement> elements = new ArrayList<BioPAXElement>( model.getObjects( biochemicalReaction.class ) );


            // keep only n elements
            for ( int i = n; i < elements.size(); i++ ) {
                model.remove( elements.get( i ) );
            }

            handler.convertToOWL( model , new FileOutputStream( output ) );

        } catch ( FileNotFoundException ex ) {

            LOGGER.error( "Error reading form biopax file:" + ex.getMessage() );
        }

    }
}
