/**
 * DrawAlignmentsMain.java
 *
 * 2011.07.14
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
package uk.ac.ebi.execs;

import org.apache.log4j.Logger;

/**
 * @name    DrawAlignmentsMain
 * @date    2011.07.14
 * @date    $LastChangedDate$ (this version)
 * @version $Revision$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class DrawAlignmentsMain      {
        //extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger( DrawAlignmentsMain.class );

    public static void main( String[] args ) {
        new DrawAlignmentsMain( args ).process();
    }

    public DrawAlignmentsMain( String[] args ) {
        //super( args );
    }

    public void setupOptions() {
       // add( new Option( "x" , "xml" , true , "BLAST input file (produced by BLASTP -m 7 options)" ) );
    }

    public void process() {

//        File xmlFile = getCmd().hasOption( "x" ) ? new File( getCmd().getOptionValue( "x" ) ) : null;
//
//        if ( xmlFile == null ) {
//
//            LOGGER.error( "No XML file provided" );
//            printHelp();
//        }
//
//        BlastXML blastIO = new BlastXML( xmlFile );
//        GeneProductCollection productCollection =
//                              blastIO.loadProteinHomologyObservations( new GeneProductCollection() ,
//                                                                       new JobParameters() );
//
//        // build the renderer, could put in the factory
//        LocalAlignmentRenderer renderer =
//                               new LocalAlignmentRenderer( new Rectangle() ,
//                                                           new BlastAlignmentColor() ,
//                                                           0 );
//
//
//        for ( GeneProteinProduct geneProteinProduct : productCollection.getProteinProducts() ) {
//
////            BufferedImage img = new BufferedImage( 800 , 5 * geneProteinProduct.getObservations().getBlastHits().size() ,
////                                                   BufferedImage.TYPE_4BYTE_ABGR );
////            Graphics2D g2 = ( Graphics2D ) img.getGraphics();
////            Rectangle outerBounds = new Rectangle( 800 , 5 );
////            Rectangle innerBounds = new Rectangle( 1 , 1 , 798 , 3 );
////
////            for ( LocalAlignment hit : geneProteinProduct.getObservations().getBlastHits() ) {
////                renderer.render( hit , g2 , outerBounds , innerBounds );
////                outerBounds.y += outerBounds.height;
////                innerBounds.y += outerBounds.height;
////            }
////
////            g2.dispose();
////            try {
////                ImageIO.write( img , "png" , new File( "/Users/johnmay/Desktop/test.png" ) );
////                return;
////            } catch ( IOException ex ) {
////                ex.printStackTrace();
////            }
////
//
//            return; // ony do one
//        }



    }
}
