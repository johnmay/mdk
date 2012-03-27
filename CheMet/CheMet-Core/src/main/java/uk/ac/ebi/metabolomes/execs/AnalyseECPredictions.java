///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package uk.ac.ebi.metabolomes.execs;
//
//import au.com.bytecode.opencsv.CSVReader;
//import au.com.bytecode.opencsv.CSVWriter;
//import com.sun.net.httpserver.Headers;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.Map.Entry;
//import org.apache.commons.cli.*;
//import uk.ac.ebi.resource.classification.ECNumber;
//
///**
// *
// * @author johnmay
// */
//public class AnalyseECPredictions {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main( String[] args ) throws FileNotFoundException , ParseException , IOException {
//
//        // create Options object
//        Options options = new Options();
//
//        // add t option
//        options.addOption( "f" , "f" , true , "The file to parse the EC numbers from" );
//
//        CommandLineParser parser = new PosixParser();
//        CommandLine cmd = parser.parse( options , args );
//
//        if ( !cmd.hasOption( "f" ) ) {
//            System.out.println( "No option found for --file" );
//            System.exit( 1 );
//        }
//
//        CSVReader reader = new CSVReader( new FileReader( cmd.getOptionValue( "f" ) ) , '\t' );
//
//        LinkedHashMap<String , Integer> headerMap = getHeadersMap( reader );
//
//        System.out.println( "Parsed table headers:" );
//        for ( Entry<String , Integer> e : headerMap.entrySet() ) {
//            System.out.println( "\t" + e.getValue() + " -> " + e.getKey() );
//        }
//
//        // Can parse the ECs in to ECNumber objects later to do more complex comparisons but for now all we
//        // are interested in are the direct and conflicting matches
//        ArrayList<String> obid = new ArrayList<String>();
//        ArrayList<String> imgEc = new ArrayList<String>();
//        ArrayList<String> priamEc = new ArrayList<String>();
//        ArrayList<String> rastEc = new ArrayList<String>();
//        ArrayList<String> blastEc = new ArrayList<String>();
//
//
//        String[] row;
//
//        ArrayList<String[]> rawData = new ArrayList<String[]>();
//
//        while ( ( row = reader.readNext() ) != null ) {
//            rawData.add( row );
//            obid.add( row[headerMap.get( "Gene Object ID" )] );
//            imgEc.add( row[headerMap.get( "IMG EC" )] );
//            priamEc.add( row[headerMap.get( "PRIAM EC" )] );
//            rastEc.add( row[headerMap.get( "RAST EC" )] );
//            blastEc.add( row[headerMap.get( "BLAST LONE EC" )] );
//        }
//        reader.close();
//
//        int total = obid.size();
//        int withNoEC = 0;
//        int withSameEC = 0;
//
//
//        int IMG_INDEX = 0;
//        int PRIAM_INDEX = 1;
//        int BLAST_INDEX = 2;
//        int RAST_INDEX = 3;
//        int DETECT_INDEX = 4;
//
//        String[] headers = new String[]{
//            "IMG" ,
//            "PRIAM" ,
//            "BLAST" ,
//            "RAST"
//        };
//
//        // Want to look for...
//
//        // 1. true positives 1.1.1.1 == 1.1.1.1
//        int[][] TPMatrix = new int[ 4 ][ 4 ];
//
//        // 2. sub positives 1.1.1.1 ~ 1.1.1.1|1.1.1.7
//        int[][] SPMatrix = new int[ 4 ][ 4 ];
//
//        // 3. partial positives 1.1.1.1 ~ 1.1.1.- (e.g. matching sub class or sub sub class)
//        int[][] PPMatrix = new int[ 4 ][ 4 ];
//
//        // 4. false positives 1.1.1.1 != 1.1.1.85
//        int[][] FPMatrix = new int[ 4 ][ 4 ];
//
//
//        // false and true negatives
//        int[][] FNMatrix = new int[ 4 ][ 4 ];
//        int[][] TNMatrix = new int[ 4 ][ 4 ];
//
//
//
//        // all
//
//        // Store the index of the data be analysed so we can print lists of the comparisons
//        ArrayList[][][] indexMapping = new ArrayList[ 6 ][ 4 ][ 4 ];
//        // instantiate all of them
//        for ( int i = 0; i < indexMapping.length; i++ ) {
//            for ( int j = 0; j < indexMapping[i].length; j++ ) {
//                for ( int k = 0; k < indexMapping[i][j].length; k++ ) {
//                    indexMapping[i][j][k] = new ArrayList<Integer>();
//                }
//            }
//        }
//
//        // now create new lists of matches and mistmatches
//        for ( int i = 0; i < obid.size(); i++ ) {
//
//            String[] ecs = new String[]{
//                imgEc.get( i ) ,
//                priamEc.get( i ) ,
//                blastEc.get( i ) ,
//                rastEc.get( i )
//            };
//
//            StringBuilder sb = new StringBuilder();
//            for ( String string : ecs ) {
//                sb.append( string );
//            }
//            if ( sb.toString().equals( "" ) ) {
//                withNoEC++;
//            } else {
//                // decide on TP,SP,PP and FP
//                for ( int j = 0; j < TPMatrix.length; j++ ) {
//                    for ( int k = 0; k < TPMatrix[j].length; k++ ) {
//
//                        int score = score( ecs[j] , ecs[k] );
//
//                        // Add to true positives and false positives matrix
//                        switch ( score ) {
//                            case TRUE_POSITIVE:
//                                TPMatrix[j][k] += 1;
//                                indexMapping[TRUE_POSITIVE_MATRIX][j][k].add( i );
//                                break;
//                            case SUB_POSITIVE:
//                                SPMatrix[j][k] += 1;
//                                indexMapping[SUB_POSITIVE_MATRIX][j][k].add( i );
//                                break;
//                            case PARTIAL_POSITIVE:
//                                indexMapping[PARTIAL_POSITIVE_MATRIX][j][k].add( i );
//                                PPMatrix[j][k] += 1;
//                                break;
//                            case FALSE_POSITIVE:
//                                indexMapping[FALSE_POSITIVE_MATRIX][j][k].add( i );
//                                FPMatrix[j][k] += 1;
//                                break;
//                            case FALSE_NEGATIVE:
//                                indexMapping[FALSE_NEGATIVE_MATRIX][j][k].add( i );
//                                FNMatrix[j][k] += 1;
//                                break;
//                            case TRUE_NEGATIVE:
//                                indexMapping[TRUE_NEGATIVE_MATRIX][j][k].add( i );
//                                TNMatrix[j][k] += 1;
//                                break;
//
//                        }
//                    }
//                }
//            }
//        }
//
//        String path = "/Users/johnmay/EBI/Project/runspace/S.epidermidis/analysis";
//
//        ArrayList<Integer> blastpriam = indexMapping[TRUE_POSITIVE_MATRIX][BLAST_INDEX][PRIAM_INDEX];
//        CSVWriter writer = new CSVWriter( new FileWriter( path + File.separator + "concensus.tsv" ) , '\t' , ( char ) 0 );
//        for ( Integer integer : blastpriam ) {
//            String[] writeRow = new String[]{ obid.get( integer ) ,
//                                              blastEc.get( integer ) };
//            writer.writeNext( writeRow );
//        }
//        writer.close();
//
//        printMatrix( TPMatrix , "TRUE POSITIVE" , headers );
//        printMatrix( SPMatrix , "SUB POSITIVE" , headers );
//        printMatrix( PPMatrix , "PARTIAL POSITIVE" , headers );
//        printMatrix( FPMatrix , "FALSE POSITIVE" , headers );
//        printMatrix( FNMatrix , "FALSE NEGATIVE" , headers );
//        printMatrix( TNMatrix , "TRUE NEGATIVE" , headers );
//
//        ArrayList[][] ids = indexMapping[TRUE_POSITIVE_MATRIX];
//        Integer[][] overlaps = new Integer[][]{
//            {0,},
//            {}
//        };
//        for ( int i = 0; i < ids.length; i++ ) {
//            for ( int j = i; j < ids[i].length; j++ ) {
//                ArrayList idList = ids[i][j];
//            }
//        }
//
//        // print the true positive mappings to a file
//        printList( indexMapping[TRUE_POSITIVE_MATRIX] , "true_positive" , rawData.toArray( new String[ 0 ][ 0 ] ) , headers );
//        printList( indexMapping[FALSE_POSITIVE_MATRIX] , "false_positive" , rawData.toArray( new String[ 0 ][ 0 ] ) , headers );
//        printList( indexMapping[SUB_POSITIVE_MATRIX] , "sub_positive" , rawData.toArray( new String[ 0 ][ 0 ] ) , headers );
//        printList( indexMapping[PARTIAL_POSITIVE_MATRIX] , "partial_positive" , rawData.toArray( new String[ 0 ][ 0 ] ) , headers );
//        printList( indexMapping[TRUE_NEGATIVE_MATRIX] , "true_negative" , rawData.toArray( new String[ 0 ][ 0 ] ) , headers );
//        printList( indexMapping[FALSE_NEGATIVE_MATRIX] , "false_negative" , rawData.toArray( new String[ 0 ][ 0 ] ) , headers );
//
//
//
//        float priamofimg = ( float ) TPMatrix[IMG_INDEX][PRIAM_INDEX] / ( float ) TPMatrix[IMG_INDEX][IMG_INDEX];
//        float rastofimg = ( float ) TPMatrix[IMG_INDEX][RAST_INDEX] / ( float ) TPMatrix[IMG_INDEX][IMG_INDEX];
//        float blastofimg = ( float ) TPMatrix[IMG_INDEX][BLAST_INDEX] / ( float ) TPMatrix[IMG_INDEX][IMG_INDEX];
//
//
//
//        System.out.println( "Dataset stats\n"
//                            + "Total Objects:          " + total + "\n"
//                            + "With no EC assigned:    " + withNoEC + "\n"
//                            + "With some EC assigned:  " + ( total - withNoEC ) + "\n"
//                            + "With same PRIAM/IMG EC: " + priamofimg + "\n"
//                            + "With same IMG/RAST EC:  " + rastofimg + "\n"
//                            + "With same IMG/BLAST EC: " + blastofimg + "\n" );
//
//
//    }
//
//    /**
//     * Get the headers map from the CSVReader
//     * @param reader
//     * @return LinkedHashMap of the column indexes by the header name as key
//     * @throws IOException
//     */
//    public static LinkedHashMap<String , Integer> getHeadersMap( CSVReader reader ) throws IOException {
//        String[] headers = reader.readNext();
//        LinkedHashMap<String , Integer> map = new LinkedHashMap<String , Integer>( headers.length , 1.0f );
//        int index = 0;
//        for ( String h : headers ) {
//            map.put( h , index++ );
//        }
//        return map;
//    }
//
//    private static void printList( ArrayList[][] indexes , String prefix , String[][] rawData , String[] matrixHeaders ) throws IOException {
//
//        String path = "/Users/johnmay/EBI/Project/runspace/S.epidermidis/analysis";
//        for ( int i = 0; i < indexes.length; i++ ) {
//            for ( int j = i; j < indexes[i].length; j++ ) {
//
//                // create a new file
//                File file = new File( path + File.separator + prefix + "-" + matrixHeaders[i] + matrixHeaders[j] + ".tsv" );
//                System.out.println( "created file: " + file );
//                CSVWriter writer = new CSVWriter( new FileWriter( file ) , '\t' );
//                for ( int index : ( Integer[] ) indexes[i][j].toArray( new Integer[ 0 ] ) ) {
//                    writer.writeNext( rawData[index] );
//                }
//                writer.close();
//
//            }
//        }
//    }
//
//    private static int score( String ec1 , String ec2 ) {
//
//
//
//        // true negative if both results show up negative
//        if ( ec1.equals( "" ) && ec2.equals( "" ) ) {
//            return NULL_VALUE;
//        }
//
//        // false positive as result is present when there is none in comparison
//        if ( ec1.equals( "" ) && !ec2.equals( "" ) ) {
//            return FALSE_POSITIVE;
//        }
//
//        // false negative as there is no result when these is an expected one
//        if ( !ec1.equals( "" ) && ec2.equals( "" ) ) {
//            return FALSE_NEGATIVE;
//        }
//
//        // they match! so true positive
//        if ( ec1.equals( ec2 ) ) {
//            return TRUE_POSITIVE;
//        }
//
//
//        // sub and partical positive is the more tricky part
//        {
//
//            ECNumber[] ecArray1 = getECArray( ec1 );
//            ECNumber[] ecArray2 = getECArray( ec2 );
//
//            boolean partialPositive = false;
//            for ( int i = 0; i < ecArray1.length; i++ ) {
//                for ( int j = i; j < ecArray2.length; j++ ) {
//
//                    int comparison = ecArray1[i].compare( ecArray2[j] );
//                    if ( comparison == ECNumber.MATCHING_ENTRY ) {
//                        return SUB_POSITIVE;
//                    } else if ( comparison == ECNumber.MATCHING_SUB_SUBCLASS ) {
//                        partialPositive = true;
//                    } else {
//                        // System.out.println( ec1 + " compared to " + ec2 );
//                        // System.out.println( ecArray1[i] + " =?= " + ecArray2[j] + " result: " + ecArray1[i].compare( ecArray2[j] ) );
//                    }
//
//                }
//            }
//            if ( partialPositive ) {
//                return PARTIAL_POSITIVE;
//            }
//
//            // System.out.println( ec1 + ecNumber1 + "=?=" + ec2 + ecNumber2 );
//
//        }
//
//        if ( !ec1.equals( "" ) || !ec2.equals( "" ) ) {
//            return TRUE_NEGATIVE;
//        }
//
//        System.err.println( "ergu!" + ec1 + ec2 );
//
//        return ERROR_VALUE;
//    }
//
//    public static ECNumber[] getECArray( String str ) {
//        String[] strs = str.split( ";" );
//        ECNumber[] ecs = new ECNumber[ strs.length ];
////        System.out.println( str );
//        for ( int i = 0; i < strs.length; i++ ) {
//            ecs[i] = new ECNumber( strs[i] );
////            System.out.println( "  |- " + ecs[i] );
//        }
//        return ecs;
//    }
//
//    public static void printMatrix( int[][] m , String name , String[] headers ) {
//
//        System.out.println( name + ":" );
//        System.out.printf( "%25s" , "-" );
//        for ( int i = 0; i < headers.length; i++ ) {
//            System.out.printf( " %5s" , headers[i] );
//        }
//        System.out.println( "" );
//        for ( int i = 0; i < m.length; i++ ) {
//            System.out.printf( "%25s" , headers[i] );
//            for ( int j = 0; j < m[i].length; j++ ) {
//                System.out.printf( " %5s" , m[i][j] );
//            }
//            System.out.println( "" );
//        }
//    }
//    private static final int TRUE_POSITIVE = 0;
//    private static final int SUB_POSITIVE = 2;
//    private static final int PARTIAL_POSITIVE = 3;
//    private static final int FALSE_POSITIVE = 1;
//    private static final int FALSE_NEGATIVE = 6;
//    private static final int TRUE_NEGATIVE = 7;
//    private static final int NULL_VALUE = 4;
//    private static final int ERROR_VALUE = 5;
//    //
//    private static final int TRUE_POSITIVE_MATRIX = 0;
//    private static final int FALSE_POSITIVE_MATRIX = 1;
//    private static final int SUB_POSITIVE_MATRIX = 2;
//    private static final int PARTIAL_POSITIVE_MATRIX = 3;
//    private static final int TRUE_NEGATIVE_MATRIX = 4;
//    private static final int FALSE_NEGATIVE_MATRIX = 5;
//}
