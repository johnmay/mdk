///**
// * NameMatcherMain.java
// *
// * Version $Revision$
// *
// * 2011.07.28
// *
// * This file is part of the CheMet library
// *
// * The CheMet library is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * CheMet is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public License
// * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
// */
//package uk.ac.ebi.chemet.execs;
//
//import au.com.bytecode.opencsv.CSVReader;
//import au.com.bytecode.opencsv.CSVWriter;
//import com.wcohen.ss.SmithWaterman;
//import java.io.Console;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
//import org.apache.commons.cli.Option;
//import org.apache.log4j.Logger;
//import uk.ac.ebi.chebi.webapps.chebiWS.model.DataItem;
//import uk.ac.ebi.metabolomes.execs.CommandLineMain;
//import uk.ac.ebi.metabolomes.http.CactusChemical;
//import uk.ac.ebi.metabolomes.utilities.Util;
//import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;
//
///**
// * @name    NameMatcherMain
// * @date    2011.07.28
// * @version $Rev$ : Last Changed $Date$
// * @author  johnmay
// * @author  $Author$ (this version)
// * @brief   ...class description...
// *
// */
//public class NameMatcherMain
//        extends CommandLineMain {
//
//    private static final Logger LOGGER = Logger.getLogger( NameMatcherMain.class );
//
//    public static void main( String[] args ) {
//        new NameMatcherMain( args ).process();
//    }
//
//    private NameMatcherMain( String[] args ) {
//        super( args );
//    }
//
//    @Override
//    public void setupOptions() {
//
//        // add options here
//        add( new Option( "i" , "input" , true , "input file (tsv)" ) );
//        add( new Option( "c" , "compound-index" , true , "index of compound name" ) );
//        add( new Option( "f" , "formula-index" , true , "index of formula" ) );
//
//
//    }
//
//    @Override
//    public void process() {
//        // add processing here
//        File input = getCmd().hasOption( "i" ) ? new File( getCmd().getOptionValue( "i" ) ) : null;
//
//        ChEBIWebServiceConnection chebi = new ChEBIWebServiceConnection();
//
//        if ( input == null ) {
//            printHelp();
//            System.exit( 1 );
//        }
//
//        try {
//            CSVReader reader = new CSVReader( new FileReader( input ) , '\t' , '\0' );
//            CSVWriter writer = new CSVWriter( new FileWriter( new File( "compound-search.tsv" ) ) , '\t' , '\0' );
//            String[] row;
//            while ( ( row = reader.readNext() ) != null ) {
//                try {
//                    ArrayList<String> mutableRow = new ArrayList<String>( Arrays.asList( row ) );
//                    Set<String> chebiIdentifers = new HashSet<String>();
//                    String compoundName = row[1];
//                    String keggId = row[2];
//
//                    //    if ( !compoundName.equals( "Cytosine" ) ) {
//
//                    System.out.println( compoundName );
//                    Map<String , String> nameMap = chebi.searchByName( compoundName );
//                    Map<String , String> synMap = chebi.searchBySynonym( compoundName );
//                    for ( Map<String , String> map : new Map[]{ nameMap , synMap } ) {
//
//                        if ( chebiIdentifers.isEmpty() ) {
//
//                            ArrayList<Entity> entries =
//                                              chebi.getCompleteEntities( new ArrayList<String>( map.keySet() ) );
//
//                            String lowerCaseString = compoundName.toLowerCase( Locale.ENGLISH );
//
//
//                            for ( Entity entry : entries ) {
//
//                                if ( entry != null ) {
//                                    String asciiName = entry.getChebiAsciiName();
//
//                                    if ( asciiName.toLowerCase( Locale.ENGLISH ).equals( lowerCaseString ) ) {
//                                        chebiIdentifers.add( entry.getChebiId() );
//                                    }
//                                    for ( DataItem synonym : entry.getSynonyms() ) {
//                                        if ( synonym.getData().toLowerCase( Locale.ENGLISH ).equals( lowerCaseString ) ) {
//                                            chebiIdentifers.add( entry.getChebiId() );
//                                        }
//                                    }
//                                } else {
//                                }
//
//                            }
//                        }
//                    }
//                    Console c = System.console();
//
//                    // human input
//                    if ( chebiIdentifers.isEmpty() && keggId.isEmpty() && c != null ) {
//
//                        // col 4 or 5
//                        Pattern alphaNumeric = Pattern.compile( "[^A-z0-9]" );
//                        String compound_stub = alphaNumeric.matcher( compoundName.toLowerCase() ).replaceAll( "" );
//
//
//                        ArrayList<Object[]> chebiIdentigiesFromFormula = new ArrayList<Object[]>();
//
//
//                        for ( Entry<String , String> e : chebi.search( row[5] ).entrySet() ) {
//                            Matcher m = alphaNumeric.matcher( e.getValue().toLowerCase() );
//                            String stub = m.replaceAll( "" );
//                            SmithWaterman sw = new SmithWaterman();
//
//                            chebiIdentigiesFromFormula.add( new Object[]{ e.getKey() ,
//                                                                          e.getValue() ,
//                                                                          sw.score( compound_stub ,
//                                                                                    stub ) } );
//
//
//                        }
//
//
//                        Collections.sort( chebiIdentigiesFromFormula , new Comparator<Object[]>() {
//
//                            public int compare( Object[] o1 , Object[] o2 ) {
//                                Double d1 = ( Double ) o1[2];
//                                Double d2 = ( Double ) o2[2];
//                                return Double.compare( d1 , d2 ) * -1;
//                            }
//                        } );
//
//
//
//                        int indexSize = chebiIdentigiesFromFormula.size() < 5 ? chebiIdentigiesFromFormula.size() :
//                                        5;
//
//
//
//                        if ( indexSize != 0 ) {
//                            System.out.println( compoundName + ":" );
//                            for ( int i = 0; i < indexSize; i++ ) {
//                                Object[] objects = chebiIdentigiesFromFormula.get( i );
//                                System.out.format( "\t[%d] %s %s\n" , i + 1 , objects[0] , objects[1] );
//                            }
//                            System.out.print( "Please choose one [0-5] (Default: 0 – no choice): " );
//
//                            String value = c.readLine();
//                            try {
//                                Integer choice = Integer.parseInt( value );
//                                if ( choice > 0 && choice <= indexSize ) {
//                                    chebiIdentifers.add( chebiIdentigiesFromFormula.get( choice - 1 )[0].toString() );
//                                }
//                            } catch ( NumberFormatException ex ) {
//                                System.out.println( "None chosen" );
//                            }
//
//
//                        }
//
//                    }
//
//
//                    String chebiIdentifiers = Util.join( new ArrayList( chebiIdentifers ) );
//                    String cactusNames = Util.join( CactusChemical.getInstance().getNames( compoundName ) );
//                    mutableRow.add( chebiIdentifiers );
//                    mutableRow.add( cactusNames );
//                    //  System.out.println( "Writing: " + mutableRow );
//                    writer.writeNext( mutableRow.toArray( new String[ 0 ] ) );
//
//                    //}
//
//                } catch ( Exception e ) {
//                    e.printStackTrace();
//                } catch ( java.lang.ExceptionInInitializerError e ) {
//                    System.err.println( "ExceptionInInitializerError: " + e );
//                } catch ( NoClassDefFoundError e ) {
//                    System.err.println( "NoClassDefFoundError: " + e );
//                }
//            }
//
//
//            reader.close();
//            writer.close();
//        } catch ( IOException ex ) {
//            ex.printStackTrace();
//        }
//    }
//}
