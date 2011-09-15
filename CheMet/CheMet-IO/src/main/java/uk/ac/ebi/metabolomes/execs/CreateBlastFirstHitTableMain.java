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
import java.util.Arrays;
import java.util.List;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;
import uk.ac.ebi.metabolomes.core.gene.GeneProductCollection;
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.resource.protein.UniProtIdentifier;
import uk.ac.ebi.metabolomes.io.flatfile.IntEnzXML;
import uk.ac.ebi.metabolomes.io.homology.BlastXML;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;
import uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology.BlastHit;

/**
 *
 * @author johnmay
 */
public class CreateBlastFirstHitTableMain {

    private static IntEnzXML enzyme = null;//
    //IntEnzXML.getLoadedInstance();

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) throws IOException {

        System.out.println( Arrays.asList(args) );

        File file = new File( "/Users/johnmay/EBI/Project/runspace/FunctionalAnnotation/ecocyck12mg1655/blastp/BLOSUM80-UNGAPPED/filtered-enzyme_hits.xml" );
        BlastXML xml = new BlastXML( file );
        GeneProductCollection products = xml.loadProteinHomologyObservations( new GeneProductCollection() , new JobParameters( "flat-file" ) );
        CSVWriter writer = new CSVWriter(
                new FileWriter( new File( "/Users/johnmay/EBI/Project/runspace/FunctionalAnnotation/ecocyck12mg1655/blastp/BLOSUM80-UNGAPPED/filtered-enzyme_hits.tsv" ) ) , '\t' );
        for ( GeneProduct product : products.getAllProducts() ) {
            String[] rowData = getFirstECNumber( product , product.getObservations().getBlastHits() );
            writer.writeNext( rowData );
        }
        writer.close();
    }

    public static ECNumber getECCode( GeneProduct product ) {
        // fine when there is only one EC Code
        String idString = product.getIdentifier().toString();
        int index = idString.indexOf( "ec|" );
        return new ECNumber( idString.substring( index ) );
    }

    public static String[] getFirstECNumber( GeneProduct product , List<BlastHit> hits ) {
        ECNumber ec = getECCode( product );
        for ( BlastHit hit : hits ) {
            UniProtIdentifier upid = hit.getUniProtIdentifier();

            Double coverage = Double.parseDouble( hit.getPositive().toString() ) / Double.parseDouble( product.getSequenceLength().toString() );
            if ( coverage < 1D ) {
                if ( upid != null ) {
                    List<ECNumber> ecCodes = enzyme.getECNumbers( upid );
                    if ( ecCodes.size() > 1 ) {
                        System.err.println( "More then one ec code the SwissProt hit: " + ec + " – " + ecCodes );

                        // if any of the ECs is a match then this is considered a TP and so the actual value is returned.
                        for ( ECNumber predictedEC : ecCodes ) {
                            if ( predictedEC.compare( ec ) == ECNumber.MATCHING_ENTRY ) {
                                System.err.println( "\tfound matching EC so returning for TP" );
                                return new String[]{ product.getIdentifier().toString() , predictedEC.toString() , hit.getBestBitScore().toString() , Double.toString( hit.getBestExpectedValue() ) , coverage.toString() };
                            }
                        }
                        System.err.println( "\tno matching assignment, return first which will count as FP as not to taint the FN results" );
                        return new String[]{ product.getIdentifier().toString() , ecCodes.get( 0 ).toString() , hit.getBestBitScore().toString() , Double.toString( hit.getBestExpectedValue() ) , coverage.toString() };
                    } else if ( ecCodes.size() == 1 ) {
                        return new String[]{ product.getIdentifier().toString() , ecCodes.get( 0 ).toString() , hit.getBestBitScore().toString() , Double.toString( hit.getBestExpectedValue() ) , coverage.toString() };
                    }
                }
            }
        }
        // false negative... could result from having a 100% positive coverage i.e. the same sequence
        return new String[]{ product.getIdentifier().toString() , "N/A" , "0", "0", "0" };
    }
}
