/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package uk.ac.ebi.metabolomes.core.reaction.matrix;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Generic reaction matrix class
 * @author johnmay
 * @param <T> Type of the matrix (Integer,Float,String)
 * @param <M> The type for the compound names (uses equals() method) this allows to have the metabolites stored as InChIs,InChIKeys,IMolecules etc..
 * @param <R> The type of the reactions
 */
public abstract class AbstractReactionMatrix<T , M , R> {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( AbstractReactionMatrix.class );
    // matrix is store as an array list of arraylists atm.
    // todo store as a fixed size array that resizes it's self on the fly
    //private ArrayList<ArrayList<T>> oldMatrix = new ArrayList<ArrayList<T>>();
    public int moleculeCount = 0;
    public int reactionCount = 0;
    // bi-directional hashmaps
    private LinkedHashMap<M , Integer> molecules = new LinkedHashMap<M , Integer>( 10 );
    private LinkedHashMap<R , Integer> reactions = new LinkedHashMap<R , Integer>( 10 );
    private LinkedHashMap<Integer , M> moleculesIHashMap = new LinkedHashMap<Integer , M>( 10 );
    private LinkedHashMap<Integer , R> reactionsIHashMap = new LinkedHashMap<Integer , R>( 10 );
    // fixed-matrix
    private T[][] matrix;
    private int maxMoleculeCapacity;
    private int maxReactionCapacity;
    private static final int defaultMoleculesStartSize = 10;
    private static final int defaultReactionsStartSize = 10;

    /**
     * Instantiates the reaction matrix with default start size 10x10. When the
     * matrix reaches the maximum size the new size is doubled.
     */
    public AbstractReactionMatrix() {
        this( defaultMoleculesStartSize , defaultReactionsStartSize );
    }

    /**
     * Specifies a new stoichiometric matrix with specified initial capacities, this can be used
     * if the final or expected size of the final matrix is known. Specifying the capacity here
     * reduces resize penalty
     * @param n Initial capacity of molecules
     * @param n Initial capacity of reactions
     */
    public AbstractReactionMatrix( int n , int m ) {
        // we store this back to front as there are more reactions then molecules so less resizing this way
        matrix = ( T[][] ) new Object[ m ][ n ];
        // set the max capacities
        maxMoleculeCapacity = m;
        maxReactionCapacity = n;
    }

    public boolean addReaction( R reaction ,
                                M[] newMolecules ,
                                T[] values ) {

        // if the reaction hasn't be added already (based on identifier)
        if ( !reactions.containsKey( reaction ) ) {

            // ensure size of fixed matrix
            {
                if ( reactionCount + 1 >= maxReactionCapacity ) {
                    maxReactionCapacity *= 2;
                    for ( int i = 0; i < matrix.length; i++ ) {
                        matrix[i] = Arrays.copyOf( matrix[i] , maxReactionCapacity );
                    }
                }


            }

            // index
            {
                // add new molecules to hash if there are any new one
                for ( M m : newMolecules ) {
                    if ( !molecules.containsKey( m ) ) {
                        moleculesIHashMap.put( moleculeCount , m );
                        molecules.put( m , moleculeCount++ );
                    }
                }
            }

            // ensure space in the matrix
            if ( moleculeCount + 1 >= maxMoleculeCapacity ) {
                maxMoleculeCapacity *= 2;
                matrix = Arrays.copyOf( matrix , maxMoleculeCapacity );
                // null fill new metabolite rows
                for ( int i = matrix.length / 2; i < matrix.length; i++ ) {
                    matrix[i] = ( T[] ) new Object[ maxReactionCapacity ];
                }
            }
            // add the reaction to the fixed matrix
            for ( int i = 0; i < newMolecules.length; i++ ) {
                matrix[molecules.get( newMolecules[i] )][reactionCount] = values[i];
            }

            // index the reaction and add to the matrix
            reactionsIHashMap.put( reactionCount , reaction );
            reactions.put( reaction , reactionCount++ );

//            oldMatrix.add( new ArrayList<T>( Arrays.asList( reactionRow ) ) );
            return true;
        } else {
            logger.debug( "duplicated reactions for " + reaction.toString() );
            return false;
        }
    }

    public void display( PrintStream stream ) {
        display( stream , ',' );
    }

    public void display( PrintStream stream , char sep ) {
        display( stream , sep , " " , countColumnNulls() , 5 , 5 );
    }

    /**
     * Displays the matrix to the desired PrintStream
     * @param stream
     * @param sep
     * @param empty The value to replace null values with
     * @param ordered Array of integers that the columns show be ordered
     */
    public void display( PrintStream stream ,
                         char sep ,
                         String empty ,
                         Integer[] ordered ,
                         int maxMolNameLength ,
                         int maxReactionNameLength ) {



        if ( ordered == null ) {
            ordered = countColumnNulls();
        }

        stream.printf( "%" + maxMolNameLength + "s" , "" );

        String format = sep + " %" + maxReactionNameLength + "s";
        for ( int i = 0; i < reactionCount; i++ ) {
            stream.printf( format , reactionsIHashMap.get( i ).toString() );
        }
        stream.println();

        Set<Entry<R , Integer>> reactionSet = reactions.entrySet();
        Iterator<Entry<R , Integer>> reactionIterator = reactionSet.iterator();

        String molNameFormat = "%" + maxMolNameLength + "s";
        String valueFormat = sep + " %" + maxReactionNameLength + "s";
        for ( int i = 0; i < moleculeCount; i++ ) {
            stream.printf( molNameFormat , moleculesIHashMap.get( i ) );
            for ( int j = 0; j < reactionCount; j++ ) {
                T value = matrix[i][j];
                stream.printf( valueFormat , ( value == null ? empty : value ).toString() );
            }
            stream.println();
        }
    }

    /**
     * Return a fixed size matrix
     * @return reaction matrix of type <T>[][]
     */
    public T[][] getFixedMatrix() {
        // truncate the reactions (removes null paddings)
        T[][] outputMatrix = Arrays.copyOf( matrix , moleculeCount );
        for ( int i = 0; i < outputMatrix.length; i++ ) {
            outputMatrix[i] = Arrays.copyOf( outputMatrix[i] , reactionCount );
        }
        return outputMatrix;
    }

    /**
     * Accessor to a value at a given reactionIndex, moleculeIndex
     * @param reactionI
     * @param moleculeI
     * @return The value at the requested location
     */
    public T get( int i , int j ) {
        return matrix[i][j];
    }

    /**
     * Access for all values in row i
     * @param i The row to retrieve
     */
    public T[] getRow( int i ) {
        return Arrays.copyOf( matrix[i] , reactionCount );
    }

    /**
     * Access to the values for a single reaction
     */
    public T[] getColumn( int j ) {
        Object[] copy = new Object[ moleculeCount ];
        for ( int i = 0; i < moleculeCount; i++ ) {
            copy[i] = matrix[i][j];
        }
        return ( T[] ) copy;
    }

    /**
     * Counts the number of null objects in each column
     * @return Array of the null count for each column
     */
    public Integer[] countColumnNulls() {
        Integer[] nulls = new Integer[ moleculeCount ];
        for ( int i = 0; i < moleculeCount; i++ ) {
            nulls[i] = 0;
            for ( int j = 0; j < reactionCount; j++ ) {
                nulls[i] += matrix[i][j] == null ? 1 : 0;
            }
        }
        return nulls;
    }

    /**
     * Returns a fixed size array of the molecules
     * @return
     */
    public Set<M> getMolecules() {
        return molecules.keySet();
    }

    /**
     * Returns a fixed size array of the molecules
     * @return
     */
    public Set<R> getReactions() {
        return reactions.keySet();
    }

    public int getMoleculeCount() {
        return moleculeCount;
    }

    public int getReactionCount() {
        return reactionCount;
    }

    /**
     * Returns the molecule for a given position
     * @param i
     * @return
     */
    public M getMolecule( int i ) {
        return moleculesIHashMap.get( i );
    }

    /**
     * Returns the reaction for a given position
     * @param i
     * @return
     */
    public R getReaction( Integer i ) {
        return reactionsIHashMap.get( i );
    }

    /**
     * Access the reactions for a specific molecule
     * @param molecule
     * @return Map of the reaction to the value
     */
    public Map<R , T> getReactions( M molecule ) {
        Integer i = molecules.get( molecule );
        HashMap<R , T> subReactions = new HashMap<R , T>();
        for ( int j = 0; j < reactionCount; j++ ) {
            if ( matrix[i][j] != null ) {
                subReactions.put( getReaction( j ) , matrix[i][j] );
            }
        }
        return subReactions;
    }

    /**
     * Returns a HashSet of molecules that occur more then the provided
     * threshold
     * @param threshold The value above which a molecule is
     *        considered highly connected (0 returns an empty set)
     * @return Set of the high connected Molecules
     */
    public Set<M> getHighlyConnectedMolecules( int threshold ) {

        Integer[] numberOfNulls = countColumnNulls();
        Set<M> highlyConnected = new HashSet<M>();

        // return empty set if less then zero is specified
        if ( threshold == 0 ) {
            return highlyConnected;
        }

        for ( int i = 0; i < numberOfNulls.length; i++ ) {
            Integer integer = numberOfNulls[i];
            if ( ( reactionCount - integer ) > threshold ) {
                highlyConnected.add( getMolecule( i ) );
            }
        }

        return highlyConnected;

    }

    /**
     * Writes the reaction matrix to a text file that CytoScape can
     * read and build a connection file (for cytoscape) with all edge
     * values set as "rc" and a threshold at -0meaning no highly
     * connected molecules are removed
     * @param fw         file writer to write the text file to
     * @throws IOException
     */
    public void toTextFile( FileWriter fw ) throws IOException {
        toTextFile( fw , 0 );
    }

    /**
     * Writes the reaction matrix to a text file that CytoScape can
     * read and build a connection file (for cytoscape) with threshold
     * of highly connected molecule specified and all edge values set as "rc"
     * @param fw         file writer to write the text file to
     * @param connectivityThreshhold the value at which to consider components highly
     *                  connected and exempt from the file (-1 = do not remove)
     * @throws IOException
     */
    public Map<String , M> toTextFile( FileWriter fw ,
                                       int connectivityThreshhold ) throws IOException {
        return toSIF( fw , connectivityThreshhold , new ArrayList() , true );
    }

    /**
     * Writes the reaction matrix to a text file that CytoScape can
     * read and build a connection file (for Cytoscape) with threshold
     * of highly connected molecule specified and edge values specified
     *
     * If split is true the name of the highly connected molecule is prefixed with a ticker integer
     * and added the returned HashMap.
     *
     * @param writer      writer to write the text format too
     * @param connectivityThreshhold  the value at which to consider components highly
     *                   connected and exempt from the file (0 = do not remove)
     * @param edgeValues A list of edge values to use for the reactions
     *                   this allows you to provide a connection type
     *                   i.e. directionality. The toStirng method is used
     *                   to print the value. Providing an empty list invokes
     *                   default behavior
     * @throws IOException
     */
    public Map<String , M> toSIF( Writer writer ,
                                  int connectivityThreshhold ,
                                  List edgeValues ,
                                  boolean split ) throws IOException {

        Map<String , M> highlyConnectedMap = new HashMap<String , M>();

        if ( edgeValues != null
             && edgeValues.size() != getReactionCount()
             && !edgeValues.isEmpty() ) {
            logger.error( "number of edge values does not equal the number reactions" );
            return highlyConnectedMap;
        }


        Set<M> highlyConnected = getHighlyConnectedMolecules( connectivityThreshhold );
        Set<M> workingMolecules = getMolecules();

        for ( M molecule : workingMolecules ) {
            if ( highlyConnected.contains( molecule ) ) {
                if ( split ) {
                    Map<R , T> reactionMap = getReactions( molecule );
                    for ( R r : reactionMap.keySet() ) {
                        Object edge = edgeValues == null || edgeValues.isEmpty() ? matrix[molecules.get( molecule )][reactions.get( r )] : edgeValues.get( reactions.get( r ) );
                        String newName = ticker++ + "-" + molecule.toString();
                        writer.write( r + "\t" + edge.toString() + "\t" + newName + "\n" );
                        highlyConnectedMap.put( newName , molecule );
                    }
                } else {
                    logger.debug( "ignoring highly connected: " + highlyConnected );
                }
            } else {
                Map<R , T> reactionMap = getReactions( molecule );
                for ( R r : reactionMap.keySet() ) {
                    Object edge = edgeValues == null || edgeValues.isEmpty() ? matrix[molecules.get( molecule )][reactions.get( r )] : edgeValues.get( reactions.get( r ) );
                    writer.write( r + "\t" + edge.toString() + "\t" + molecule + "\n" );
                }
            }
        }
        return highlyConnectedMap;
    }

    public boolean containsMolecule( M molecule ) {
        return molecules.containsKey( molecule );
    }
    public static int ticker = 1;
}
