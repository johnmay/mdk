/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.execs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.cli.Option;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;
import uk.ac.ebi.metabolomes.core.gene.GeneProductCollection;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;
import uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology.BlastHit;
import uk.ac.ebi.metabolomes.http.uniprot.UniProtIdentifierMapper;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.ECNumber;
import uk.ac.ebi.metabolomes.identifier.IdentifierFactory;
import uk.ac.ebi.metabolomes.identifier.UniProtIdentifier;
import uk.ac.ebi.metabolomes.io.flatfile.IntEnzXML;
import uk.ac.ebi.metabolomes.io.homology.BlastXML;

/**
 *
 * @author johnmay
 */
public class FindBlastClustersMain
        extends CommandLineMain {

    IntEnzXML iexml = IntEnzXML.getLoadedInstance();

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        new FindBlastClustersMain( args ).process();
    }

    public FindBlastClustersMain( String[] args ) {
        super( args );
    }

    @Override
    public void setupOptions() {
        add( new Option( "x" , "xml" , true , "BLAST XML input file" ) );
        add( new Option( "o" , "out" , true , "Output Table" ) );
    }

    @Override
    public void process() {
        File xmlPath = new File( getCmd().getOptionValue( "x" ) );
        if ( xmlPath.exists() ) {
            // load blast results
            BlastXML blastXML = new BlastXML( xmlPath );
            GeneProductCollection productCollection =
                                  blastXML.loadProteinHomologyObservations( new GeneProductCollection() ,
                                                                            new JobParameters( "unknown" ) );
            filter( productCollection );

        }
    }

    public void filter( GeneProductCollection collection ) {

        for ( Double sequenceSensitivity :
              new Double[]{ 0.0 , 0.1 , 0.2 , 0.3 , 0.4 , 0.5 , 0.6 , 0.7 , 0.8 , 0.9 , 1.0 } ) {

            double tp = 0d;
            double fp = 0d;
            double fn = 0d;

            // put the results into bins based on overlapping bit score ranges
            for ( GeneProduct geneProduct : collection.getAllProducts() ) {
                Map<ECNumber , Double[]> bitScoreRangeMap = buildBitScoreRangeMap( geneProduct , sequenceSensitivity );
                // find the distinct groups
                int group = 0;
                List<List<ECNumber>> bins = new ArrayList<List<ECNumber>>();
                for ( Entry<ECNumber , Double[]> entry : bitScoreRangeMap.entrySet() ) {
                    if ( bins.isEmpty() ) {
                        bins.add( new ArrayList<ECNumber>() );
                        bins.get( group ).add( entry.getKey() );
                    } else {
                        ECNumber lastAdded = bins.get( group ).get( bins.get( group ).size() - 1 );
                        // if the lowest score overlaps with the new EC highest score
                        if ( bitScoreRangeMap.get( lastAdded )[1] <= bitScoreRangeMap.get( entry.getKey() )[0] ) {
//                        System.out.println( "Overlapping score: " + lastAdded + " " + Arrays.asList( bitScoreRangeMap.
//                                get( lastAdded ) ) + " : " + entry.getKey() + " " +
//                                            Arrays.asList( bitScoreRangeMap.get( entry.getKey() ) ) );
                            // add to bin
                            bins.get( group ).add( entry.getKey() );
                        } else {
                            // doesn't overlap so create a new group
                            group++;
                            bins.add( new ArrayList<ECNumber>() );
                            bins.get( group ).add( entry.getKey() );
                        }
                    }
                }

                Set<ECNumber> topScoringBin = new HashSet<ECNumber>( bins.isEmpty() ? new ArrayList<ECNumber>() : bins.
                        get(
                        0 ) );
                List<AbstractIdentifier> ids =
                                         IdentifierFactory.getIdentifiers( geneProduct.getIdentifier().toString() );
                Set<ECNumber> ecs = new HashSet<ECNumber>();
                for ( AbstractIdentifier id : ids ) {
                    if ( id instanceof ECNumber ) {
                        ecs.add( ( ECNumber ) id );
                    }
                }

                for ( ECNumber assignedECNumber : ecs ) {
                    if ( topScoringBin.contains( assignedECNumber ) ) {
                        tp++;
                    } else {
                        fn++;
                    }
                }

                for ( ECNumber predicted : topScoringBin ) {
                    if ( ecs.contains( predicted ) ) {
                        // do nothing tp++;
                    } else {
                        fp++;
                    }
                }
                // System.out.print( ecs + "\t" + topScoringBin + "\t" );
                // System.out.println( tp + "\t" + fp + "\t" + fn);
            }

            double ppv = tp / ( tp + fp );
            double tpr = tp / ( tp + fn );
            double fdr = fp / ( fp + tp );

            System.out.printf( "%.1f – PPV: %.2f TPR: %.2f FDR %.2f\n" , sequenceSensitivity , ppv , tpr , fdr );
        }
    }

    public Map<ECNumber , Double[]> buildBitScoreRangeMap( GeneProduct product , Double sequenceIdentitiy ) {

        Map<ECNumber , Double[]> bitScoreRanges = new LinkedHashMap<ECNumber , Double[]>();
        for ( BlastHit homologyHit : product.getObservations().getBlastHits() ) {

            // don't worry about gaps: take the identical residues / avg. lenght of sequences
            double sequenceIdentity = ( double ) homologyHit.getIdentity() / ( ( double ) ( homologyHit.getHitLength() +
                                                                                            product.getSequenceLength() ) /
                                                                               2d );


            if ( sequenceIdentity > sequenceIdentitiy ) {
                UniProtIdentifier upid = homologyHit.getUniProtIdentifier();

                // in older blast formats extra identifers were stored in the hit definition
                if ( upid == null ) {
                    List<AbstractIdentifier> ids = IdentifierFactory.getIdentifiers( homologyHit.getDefinition() );
                    for ( AbstractIdentifier id : ids ) {

                        if ( id instanceof UniProtIdentifier ) {

                            upid = ( UniProtIdentifier ) id;
                        }
                    }
                }
                List<ECNumber> ecs = iexml.getECNumbers( upid );
                Double bitScore = homologyHit.getBitScore();

                for ( ECNumber ec : ecs ) {
                    if ( !bitScoreRanges.containsKey( ec ) ) {
                        bitScoreRanges.put( ec , new Double[]{ bitScore , bitScore } );
                    }
                    bitScoreRanges.get( ec )[1] = bitScore;
                }
                // System.out.println( homologyHit.getUniProtIdentifier() + " " + ecs + " " + bitScore + " " + sequenceIdentity );
            }
        }
        return bitScoreRanges;
    }
}
