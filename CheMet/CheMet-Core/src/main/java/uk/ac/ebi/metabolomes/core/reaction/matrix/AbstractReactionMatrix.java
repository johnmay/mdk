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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.Map.Entry;
import org.apache.commons.lang.mutable.MutableInt;
import org.apache.log4j.Logger;


/**
 * Generic reaction matrix class
 *
 * @author johnmay
 * @param <T> Type of the matrix (Integer,Float,String)
 * @param <M> The type for the compound names (uses equals() method) this allows
 * to have the metabolites stored as InChIs,InChIKeys,IMolecules etc..
 * @param <R> The type of the reactions
 */
public abstract class AbstractReactionMatrix<T, M, R> {

    private static final Logger LOGGER = Logger.getLogger(AbstractReactionMatrix.class);
    // Intial capacities

    private static final int INTIAL_MOLECULE_CAPACITY = 10;

    private static final int INTIAL_REACTION_CAPACITY = 10;
    // maps for look-up

    private LinkedHashMap<M, Integer> moleculeMap;

    private Multimap<R, Integer> reactionMap;
    // values for molecules (row indicies) and reaction (column indicies)

    private M[] molecules;

    private R[] reactions;
    // fixed-matrix

    private T[][] matrix;
    // current capacities

    private int moleculeCapacity;

    private int reactionCapacity;
    // current size

    public int moleculeCount = 0;

    public int reactionCount = 0;


    /**
     * Instantiates the reaction matrix with default start size 10x10. When the
     * matrix reaches the maximum size the new size is doubled.
     */
    public AbstractReactionMatrix() {
        this(INTIAL_MOLECULE_CAPACITY, INTIAL_REACTION_CAPACITY);
    }


    /**
     * Specifies a new stoichiometric matrix with specified initial capacities,
     * this can be used if the final or expected size of the final matrix is
     * known. Specifying the capacity here reduces resize penalty
     *
     * @param n Initial capacity of molecules
     * @param m Initial capacity of reactions
     */
    public AbstractReactionMatrix(int n, int m) {
        // we store this back to front as there are more reactions then molecules so less resizing this way
        matrix = (T[][]) new Object[n][m];
        // set the max capacities
        moleculeCapacity = n;
        reactionCapacity = m;

        this.molecules = (M[]) new Object[n];
        this.reactions = (R[]) new Object[m];


        this.moleculeMap = new LinkedHashMap<M, Integer>(10);
        this.reactionMap = HashMultimap.create(m, 1);
    }


    /**
     * Add a reaction to the matrix
     *
     * @param reaction
     * @param newMolecules
     * @param values
     * @return
     */
    public boolean addReaction(R reaction,
                               M[] newMolecules,
                               T[] values) {



        // ensure there is enough space
        ensure(moleculeCount + newMolecules.length, reactionCount + 1);

        // add new molecules to hash if there are any new one
        boolean intersect = false;
        for (M m : newMolecules) {
            intersect = !addMolecule(m) || intersect;
        }

        // add the reaction to the fixed matrix
        if (intersect) {
            // no new molecules, check for clash
            Map<R, MutableInt> candidateReactions = new HashMap<R, MutableInt>(); // reuse
            for (int i = 0; i < newMolecules.length; i++) {
                for (Entry<R, T> e : getReactions(newMolecules[i]).entrySet()) {
                    if (values[i].equals(e.getValue())) {
                        if (!candidateReactions.containsKey(e.getKey())) {
                            candidateReactions.put(e.getKey(), new MutableInt());
                        }
                        candidateReactions.get(e.getKey()).increment();
                    }
                }
            }
            if (!candidateReactions.isEmpty()) {
                for (Entry<R, MutableInt> e : candidateReactions.entrySet()) {
                    if (e.getValue().intValue() == values.length) {
                        LOGGER.debug("Duplicate reaction");
                        return false;
                    }
                }
            }
        }

        // set the values to the matrix
        for (int i = 0; i < newMolecules.length; i++) {
            matrix[moleculeMap.get(newMolecules[i])][reactionCount] = values[i];
        }


        // index the reaction
        reactions[reactionCount] = reaction;
        reactionMap.put(reaction, reactionCount);
        reactionCount++;

        return true;

    }


    private void ensure(int n, int m) {

        LOGGER.debug("Ensuring capacity");

        boolean resized = false;
        if (n >= moleculeCapacity) {
            moleculeCapacity = 1 + (Math.max(n, moleculeCapacity) * 2);
            molecules = Arrays.copyOf(molecules, moleculeCapacity);
            matrix = Arrays.copyOf(matrix, moleculeCapacity);
            resized = true;
        }
        if (resized || m >= reactionCapacity) {
            reactionCapacity = 1 + Math.max(m, reactionCapacity) * 2;
            reactions = Arrays.copyOf(reactions, reactionCapacity);
            // null fill new metabolite rows
            for (int i = 0; i < matrix.length; i++) {
                matrix[i] = matrix[i] == null ? (T[]) new Object[reactionCapacity] : Arrays.copyOf(matrix[i], reactionCapacity);
            }
        }
    }


    /**
     *
     * @param molecule
     * @return if a new molecule was added
     */
    private boolean addMolecule(M molecule) {
        if (!moleculeMap.containsKey(molecule)) {
            molecules[moleculeCount] = molecule;
            moleculeMap.put(molecule, moleculeCount);
            moleculeCount++;
            return true;
        }
        return false;
    }


    public void display(PrintStream stream) {
        display(stream, ',');
    }


    public void display(PrintStream stream, char sep) {
        display(stream, sep, " ", countColumnNulls(), 5, 5);
    }


    /**
     * Displays the matrix to the desired PrintStream
     *
     * @param stream
     * @param seperator
     * @param empty The value to replace null values with
     * @param ordered Array of integers that the columns show be ordered
     */
    public void display(PrintStream stream,
                        char seperator,
                        String empty,
                        Integer[] ordered,
                        int molNameLength,
                        int rxnNameLength) {



        if (ordered == null) {
            ordered = countColumnNulls();
        }

        stream.printf("%" + molNameLength + "s", "");

        String format = seperator + " %" + rxnNameLength + "s";
        for (int i = 0; i < reactionCount; i++) {
            stream.printf(format, reactions[i].toString());
        }
        stream.println();

        String molNameFormat = "%" + molNameLength + "s";
        String valueFormat = seperator + " %" + rxnNameLength + "s";
        for (int i = 0; i < moleculeCount; i++) {
            stream.printf(molNameFormat, molecules[i]);
            for (int j = 0; j < reactionCount; j++) {
                T value = matrix[i][j];
                stream.printf(valueFormat, (value == null ? empty : value).toString());
            }
            stream.println();
        }
    }


    /**
     * Return a fixed size matrix.
     *
     * @return reaction matrix of type <T>[][]
     */
    public Object[][] getFixedMatrix() {
        // truncate the reactions (removes null paddings)
        T[][] outputMatrix = Arrays.copyOf(matrix, moleculeCount);
        for (int i = 0; i < outputMatrix.length; i++) {
            outputMatrix[i] = Arrays.copyOf(outputMatrix[i], reactionCount);
        }
        return outputMatrix;
    }


    /**
     * Accessor to a value at a given reactionIndex, moleculeIndex
     *
     * @param i
     * @param j
     * @return The value at the requested location
     */
    public T get(int i, int j) {
        return matrix[i][j];
    }


    public List<T> getValuesForMolecule(int i) {
        return Arrays.asList(getRow(i));
    }


    /**
     * Removes the row at index i
     */
    public void removeRow(int i) {
        System.arraycopy(matrix, i + 1, matrix, i, matrix.length - i - 1);
        moleculeCount--;
        System.arraycopy(molecules, i + 1, molecules, i, molecules.length - i - 1);
        buildMoleculeMap();
    }


    public void removeColumn(int j) {
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], j + 1, matrix[i], j, matrix[i].length - j - 1);
        }
        reactionCount--;
        System.arraycopy(reactions, j + 1, reactions, j, reactions.length - j - 1);
        buildReactionMap();
    }


    /**
     * Rebuilds the molecule map (note this also updates the internal map)
     *
     * @return
     */
    private Map<M, Integer> buildMoleculeMap() {
        moleculeMap.clear();
        for (int i = 0; i < moleculeCount; i++) {
            moleculeMap.put(molecules[i], i);
        }
        return moleculeMap;
    }


    private Multimap<R, Integer> buildReactionMap() {
        reactionMap.clear();
        for (int j = 0; j < reactionCount; j++) {
            reactionMap.put(reactions[j], j);
        }
        return reactionMap;
    }


    /**
     * Access for all stoichiometric values for molecules
     *
     * @param i The row to retrieve
     */
    public T[] getRow(int i) {
        return Arrays.copyOf(matrix[i], reactionCount);
    }


    public T[] getRow(M m) {
        if (!moleculeMap.containsKey(m)) {
            throw new InvalidParameterException("Molecule not found");
        }
        return getRow(this.moleculeMap.get(m));
    }


    public Integer[] getColumnIndicies(R r) {
        return reactionMap.get(r).toArray(new Integer[0]);
    }


    /**
     * Access to the values for a single reaction
     */
    public T[] getColumn(int j) {
        T[] copy = (T[]) new Object[moleculeCount];
        for (int i = 0; i < moleculeCount; i++) {
            copy[i] = matrix[i][j];
        }
        return copy;
    }


    public Map<Integer, T[]> getColumns(R r) {
        Map<Integer, T[]> columns = new HashMap<Integer, T[]>();
        for (Integer j : getColumnIndicies(r)) {
            columns.put(j, getColumn(j));
        }
        return columns;
    }


    /**
     * Counts the number of null objects in each column
     *
     * @return Array of the null count for each column
     */
    public Integer[] countColumnNulls() {
        Integer[] nulls = new Integer[moleculeCount];
        for (int i = 0; i < moleculeCount; i++) {
            nulls[i] = 0;
            for (int j = 0; j < reactionCount; j++) {
                nulls[i] += matrix[i][j] == null ? 1 : 0;
            }
        }
        return nulls;
    }


    /**
     * Returns a fixed size array of the molecules
     *
     * @return
     */
    public M[] getMolecules() {
        return Arrays.copyOf(molecules, moleculeCount);
    }


    /**
     * Returns a list of the reactions
     *
     * @return
     */
    public R[] getReactions() {
        return Arrays.copyOf(reactions, reactionCount);
    }


    public int getMoleculeCount() {
        return moleculeMap.size();
    }


    public int getReactionCount() {
        return reactionCount;
    }


    /**
     * Returns the molecule for a given position
     *
     * @param i
     * @return
     */
    public M getMolecule(Integer i) {
        return molecules[i];
    }


    /**
     * Returns the reaction for a given position
     *
     * @param i
     * @return
     */
    public R getReaction(Integer i) {
        return reactions[i];
    }


    /**
     * Access the reactions for a specific molecule
     *
     * @param molecule
     * @return Map of the reaction to the value
     */
    public Map<R, T> getReactions(M molecule) {
        Integer i = moleculeMap.get(molecule);
        HashMap<R, T> subReactions = new HashMap<R, T>();
        for (int j = 0; j < reactionCount; j++) {
            if (matrix[i][j] != null) {
                subReactions.put(getReaction(j), matrix[i][j]);
            }
        }
        return subReactions;
    }


    /**
     * Returns a HashSet of molecules that occur more then the provided
     * threshold
     *
     * @param threshold The value above which a molecule is considered highly
     * connected (0 returns an empty set)
     * @return Set of the high connected Molecules
     */
    public Set<M> getHighlyConnectedMolecules(int threshold) {

        Integer[] numberOfNulls = countColumnNulls();
        Set<M> highlyConnected = new HashSet<M>();

        // return empty set if less then zero is specified
        if (threshold == 0) {
            return highlyConnected;
        }

        for (int i = 0; i < numberOfNulls.length; i++) {
            Integer integer = numberOfNulls[i];
            if ((reactionCount - integer) > threshold) {
                highlyConnected.add(getMolecule(i));
            }
        }

        return highlyConnected;

    }


    /**
     * Writes the reaction matrix to a text file that CytoScape can read and
     * build a connection file (for cytoscape) with all edge values set as "rc"
     * and a threshold at -0meaning no highly connected molecules are removed
     *
     * @param fw file writer to write the text file to
     * @throws IOException
     */
    public void toTextFile(FileWriter fw) throws IOException {
        toTextFile(fw, 0);
    }


    /**
     * Writes the reaction matrix to a text file that CytoScape can read and
     * build a connection file (for cytoscape) with threshold of highly
     * connected molecule specified and all edge values set as "rc"
     *
     * @param fw file writer to write the text file to
     * @param connectivityThreshhold the value at which to consider components
     * highly connected and exempt from the file (-1 = do not remove)
     * @throws IOException
     */
    public Map<String, M> toTextFile(FileWriter fw,
                                     int connectivityThreshhold) throws IOException {
        return toSIF(fw, connectivityThreshhold, new ArrayList(), true);
    }


    /**
     * Writes the reaction matrix to a text file that CytoScape can read and
     * build a connection file (for Cytoscape) with threshold of highly
     * connected molecule specified and edge values specified
     *
     * If split is true the name of the highly connected molecule is prefixed
     * with a ticker integer and added the returned HashMap.
     *
     * @param writer writer to write the text format too
     * @param connectivityThreshhold the value at which to consider components
     * highly connected and exempt from the file (0 = do not remove)
     * @param edgeValues A list of edge values to use for the reactions this
     * allows you to provide a connection type i.e. directionality. The toStirng
     * method is used to print the value. Providing an empty list invokes
     * default behavior
     * @throws IOException
     */
    public Map<String, M> toSIF(Writer writer,
                                int connectivityThreshhold,
                                List edgeValues,
                                boolean split) throws IOException {

        Map<String, M> highlyConnectedMap = new HashMap<String, M>();

        if (edgeValues != null
            && edgeValues.size() != getReactionCount()
            && !edgeValues.isEmpty()) {
            LOGGER.error("number of edge values does not equal the number reactions");
            return highlyConnectedMap;
        }


        Set<M> highlyConnected = getHighlyConnectedMolecules(connectivityThreshhold);
        M[] workingMolecules = getMolecules();

        throw new UnsupportedOperationException("FIXME");

//        for (M molecule : workingMolecules) {
//            if (highlyConnected.contains(molecule)) {
//                if (split) {
//                    Map<R, T> reactionMap = getReactions(molecule);
//                    for (R r : reactionMap.keySet()) {
//                        Object edge = edgeValues == null || edgeValues.isEmpty() ? matrix[molecules.get(molecule)][reactionsOld.get(r)] : edgeValues.get(reactionsOld.get(r));
//                        String newName = ticker++ + "-" + molecule.toString();
//                        writer.write(r + "\t" + edge.toString() + "\t" + newName + "\n");
//                        highlyConnectedMap.put(newName, molecule);
//                    }
//                } else {
//                    logger.debug("ignoring highly connected: " + highlyConnected);
//                }
//            } else {
//                Map<R, T> reactionMap = getReactions(molecule);
//                for (R r : reactionMap.keySet()) {
//                    Object edge = edgeValues == null || edgeValues.isEmpty() ? matrix[molecules.get(molecule)][reactionsOld.get(r)] : edgeValues.get(reactionsOld.get(r));
//                    writer.write(r + "\t" + edge.toString() + "\t" + molecule + "\n");
//                }
//            }
//        }
//       return highlyConnectedMap;
    }


    public boolean containsMolecule(M molecule) {
        return moleculeMap.containsKey(molecule);
    }

    public static int ticker = 1;


    class ValueComparator implements Comparator {

        Map base;


        public ValueComparator(Map base) {
            this.base = base;
        }


        public int compare(Object a, Object b) {

            if ((Integer) base.get(a) < (Integer) base.get(b)) {
                return 1;
            } else if ((Integer) base.get(a) == (Integer) base.get(b)) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
