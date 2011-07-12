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

import au.com.bytecode.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.cli.Option;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;
import uk.ac.ebi.metabolomes.core.gene.GeneProductCollection;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.ECNumber;
import uk.ac.ebi.metabolomes.identifier.IdentifierFactory;
import uk.ac.ebi.metabolomes.identifier.UniProtIdentifier;
import uk.ac.ebi.metabolomes.io.flatfile.IntEnzXML;
import uk.ac.ebi.metabolomes.io.homology.BlastXML;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;
import uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology.BlastHit;
import uk.ac.ebi.metabolomes.utilities.Util;

/**
 * FilterBlastMain.java
 *
 *
 * @author johnmay
 * @date Jun 1, 2011
 */
public class FilterBlastMain
        extends CommandLineMain {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( FilterBlastMain.class );
    private File xmlFile;
    private Double expectedThreshold;
    private Double sequenceIdentity;
    private IntEnzXML intenz;

    public FilterBlastMain( String[] args ) {
        super( args );
    }

    public static void main( String[] args ) {

        FilterBlastMain filter = new FilterBlastMain( args );
        filter.process();
    }

    @Override
    public final void setupOptions() {
        // set up the options
        super.add( new Option( "x" , "xml" , true , "Blast XML file" ) );
        super.add( new Option( "o" , "output" , true , "Output EC Table" ) );
        super.add( new Option( "e" , "expected-threshold" , true , "Filtered value for blast threshold [default: 1]" ) );
        super.add( new Option( "s" , "sequence-identity-threshold" , true , "Filter by sequence identity. This option removes sequences with less then the specified sequence identity [default: 0]" ) );
    }

    @Override
    public void process() {

        String xmlLocation = super.getCmd().getOptionValue( 'x' );
        xmlFile = new File( xmlLocation );
        String outputLocation = super.getCmd().getOptionValue( 'o' );
        File outputAllTable = new File( outputLocation + "-all.tsv" );
        File outputFirstTable = new File( outputLocation + "-first.tsv" );
        File hitsFolder = new File( outputFirstTable.getParent() , "hits" );

        if ( !xmlFile.exists() ) {
            logger.error( xmlFile + " does not exist" );
            System.exit( 0 );
        }



        intenz = IntEnzXML.getLoadedInstance();


        CSVWriter allTsv = null;
        CSVWriter firstTsv = null;
        try {
            allTsv = new CSVWriter( new FileWriter( outputAllTable ) , '\t' );
            firstTsv = new CSVWriter( new FileWriter( outputFirstTable ) , '\t' );
        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
        BlastXML xml = new BlastXML( xmlFile );
        GeneProductCollection products = xml.loadProteinHomologyObservations( new GeneProductCollection() ,
                                                                              new JobParameters( "flat-file" ) );


        for ( GeneProduct product : products.getAllProducts() ) {


            String idString = product.getIdentifier().toString();
            int start = idString.indexOf( "gnl|biocyc|" );
            int end = idString.indexOf( "|gnl" , start );
            String productName = idString.substring( start + 11 , end );
                        System.out.println( productName );
            CSVWriter hitsTable = null;
            try {
                hitsTable = new CSVWriter( new FileWriter( new File( hitsFolder , productName + "-hits.tsv" ) ) , '\t' );
            } catch ( IOException ex ) {
                ex.printStackTrace();
            }
            hitsTable.writeNext( new String[]{ "SwissProt.Id" ,
                                               "EC.codes" ,
                                               "Expected.Value" ,
                                               "Bit.Score" ,
                                               "Positive" ,
                                               "Identity" ,
                                               "Hit.Length" } );



            List<BlastHit> hits = product.getObservations().getBlastHits();
            HashSet<ECNumber> hitEcs = new HashSet<ECNumber>( 3 );
            String first = null;
            for ( BlastHit blastHit : hits ) {
                UniProtIdentifier id = getUniProtIdentifier( blastHit );
                if ( id != null ) {
                    hitEcs.addAll( intenz.getECNumbers( id ) );
                    if ( first == null
                         && intenz.getECNumbers( id ).isEmpty() == false ) {
                         first = Util.join( intenz.getECNumbers( id ) , ';' , false );
                    }
                    hitsTable.writeNext(
                            new String[]{
                                id.toString() ,
                                Util.join( intenz.getECNumbers( id ) , ';' , false ) ,
                                Double.toString( blastHit.getBestExpectedValue() ) ,
                                Double.toString( blastHit.getBestBitScore() ) ,
                                Double.toString( blastHit.getPositive() ) ,
                                Double.toString( blastHit.getIdentity() ) ,
                                Double.toString( blastHit.getHitLength() )
                            } );
                }
            }

            allTsv.writeNext(
                    new String[]{
                        idString ,
                        Util.join( new ArrayList( hitEcs ) , ';' , false ) } );
            firstTsv.writeNext(
                    new String[]{
                        idString ,
                        first == null || first.isEmpty() ? "NA" : first } );
            try {
                hitsTable.close();
            } catch ( IOException ex ) {
                ex.printStackTrace();
            }
        }
        try {
            allTsv.close();
            firstTsv.close();
        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
    }

    private UniProtIdentifier getUniProtIdentifier( BlastHit hit ) {
        if ( hit.getUniProtIdentifier() != null ) {
            return hit.getUniProtIdentifier();

        }
        List<AbstractIdentifier> ids = IdentifierFactory.getIdentifiers( hit.getDefinition() );
        for ( AbstractIdentifier abstractIdentifier : ids ) {
            if ( abstractIdentifier instanceof UniProtIdentifier ) {
                return ( UniProtIdentifier ) abstractIdentifier;
            }
        }
        return null;
    }
}
