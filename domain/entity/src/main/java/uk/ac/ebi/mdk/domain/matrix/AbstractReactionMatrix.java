/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.domain.matrix;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Generic reaction matrix class
 *
 * @param <T> Type of the matrix (Integer,Float,String)
 * @param <M> The type for the compound names (uses equals() method) this allows
 *            to have the metabolites stored as InChIs,InChIKeys,IMolecules
 *            etc..
 * @param <R> The type of the reactions
 * @author johnmay
 */
public abstract class AbstractReactionMatrix<T, M, R> implements ReactionMatrix<T, M, R> {

    public static int check = 0;

    private static final Logger LOGGER = Logger.getLogger(AbstractReactionMatrix.class);

    /** Initial size of the matrix (molecules) */
    public static final int INTIAL_MOLECULE_CAPACITY = 10;

    /** Initial size of the matrix (reactions) */
    public static final int INTIAL_REACTION_CAPACITY = 10;

    /** Map for molecule look-up */
    private LinkedHashMap<M, Integer> moleculeMap;

    /** Multimap of reactions to matrix row index */
    private Multimap<R, Integer> reactionMap;

    /** Reactions define the row indices */
    protected M[] molecules;

    /** Molecules define the column indices */
    private R[] reactions;

    /** Reaction hashcode. */
    private long[] reactionHashcode;

    /** Value storage */
    private Map<IndexKey, T> matrix;

    /** Current molecule capacity */
    protected int moleculeCapacity;

    /** Current reaction capacity */
    protected int reactionCapacity;

    /** Current molecule count */
    public int moleculeCount = 0;

    /** Current reaction count */
    public int reactionCount = 0;

    private int nonNullCount = 0;


    /** Create a reaction matrix with default capacities */
    protected AbstractReactionMatrix() {
        this(INTIAL_MOLECULE_CAPACITY, INTIAL_REACTION_CAPACITY);
    }


    /**
     * Specifies a new stoichiometric matrix with specified initial capacities,
     * this can be used if the final or expected size of the final matrix is
     * known. Specifying the capacity here reduces resize penalty
     *
     * @param m Initial capacity of molecules
     * @param n Initial capacity of reactions
     */
    protected AbstractReactionMatrix(int m, int n) {

        // set the max capacities
        moleculeCapacity = m;
        reactionCapacity = n;

        this.moleculeMap = new LinkedHashMap<M, Integer>(10);
        this.reactionMap = HashMultimap.create(n, 1);

    }


    public AbstractReactionMatrix<T, M, R> init() {
        init(getMoleculeClass(), getReactionClass(), getValueClass());
        return this;
    }


    /**
     * Initializes reflective data structures
     *
     * @param moleculeClass
     * @param reactionClass
     * @param typeClass
     */
    @SuppressWarnings("unchecked")
    public void init(Class<? extends M> moleculeClass,
                     Class<? extends R> reactionClass,
                     Class<? extends T> typeClass) {

        // we store this back to front as there are more reactions then molecules so less resizing this way
        matrix = new HashMap<IndexKey, T>(reactionCapacity * 10);

        this.molecules = (M[]) Array.newInstance(moleculeClass, moleculeCapacity);
        this.reactions = (R[]) Array.newInstance(reactionClass, reactionCapacity);
        this.reactionHashcode = new long[reactionCapacity];

    }


    public abstract Class<? extends M> getMoleculeClass();


    public abstract Class<? extends R> getReactionClass();


    public abstract Class<? extends T> getValueClass();


    public boolean setMolecule(int i, M m) {
        molecules[i] = m;
        moleculeMap.put(m, i);
        if (i >= moleculeCount) {
            moleculeCount = i + 1;
        }
        return true;
    }


    public boolean setReaction(int j, R r) {
        if (j < reactions.length) {
            reactions[j] = r;
            reactionMap.put(r, j);
            if (j >= reactionCount) {
                reactionCount = j + 1;
            }
        }
        return false;
    }


    public boolean setValue(int i, int j, T value) {

        IndexKey key = new IndexKey(i, j);

        T prev = matrix.put(key, value);

        if (prev == null && value != null)
            nonNullCount++;
        if (prev != null && value == null)
            nonNullCount--;

        return false;
    }

    @Override
    public int addReaction(R reaction,
                           M[] newMolecules,
                           T[] values) {
        return addReaction(reaction, newMolecules, values, true);
    }

    /**
     * Add a reaction to the matrix
     *
     * @param reaction
     * @param newMolecules
     * @param values
     * @return the index of the reaction
     */
    @Override
    public int addReaction(R reaction,
                           M[] newMolecules,
                           T[] values,
                           boolean reversible) {

//        if (matrix == null) {  // don't want to check this every time
//            init();
//        }

        // ensure there is enough space
        ensure(moleculeCount + newMolecules.length, reactionCount + 1);

        // add new molecules to hash if there are any new one
        boolean intersect = false;
        for (M m : newMolecules) {
            intersect = !ensure(m) || intersect;
        }


        int hash = 7;
        M[] sorted = Arrays.copyOf(newMolecules, newMolecules.length);
        Arrays.sort(sorted, new Comparator<M>() {
            @Override public int compare(M o1, M o2) {
                return moleculeMap.get(o1).compareTo(moleculeMap.get(o2));
            }
        });

        for (M m : sorted) {
            hash = 31 * hash + moleculeMap.get(m);
        }

        // add the reaction to the fixed matrix
        if (intersect) {
            for (int j = 0; j < reactionCount; j++) {
                if (reactionHashcode[j] == hash) {
                    check++;
                    if (identical(j, newMolecules, values, reversible)) {
                        return j;
                    }
                }
            }
            // no new molecules, check for clash
//            Map<Integer, MutableInt> candidateReactions = new HashMap<Integer, MutableInt>(); // reuse
//            for (int i = 0; i < newMolecules.length; i++) {
//                for (Entry<Integer, T> e : getReactions(newMolecules[i]).entrySet()) {
//                    if (values[i].equals(e.getValue())) {
//                        if (!candidateReactions.containsKey(e.getKey())) {
//                            candidateReactions.put(e.getKey(), new MutableInt());
//                        }
//                        candidateReactions.get(e.getKey()).increment();
//                    }
//                }
//            }
//            if (!candidateReactions.isEmpty()) {
//                for (Entry<Integer, MutableInt> e : candidateReactions.entrySet()) {
//                    if (e.getValue().intValue() == values.length
//                        && getReactionValues(e.getKey()).length == values.length) {
//                        LOGGER.debug("Duplicate reaction: " + reactions[e.getKey()] + " and " + reaction);
//                        return e.getKey();
//                    }
//                }
//            }
        }

        // set the values to the matrix
        for (int i = 0; i < newMolecules.length; i++) {
            int moleculeIndex = moleculeMap.get(newMolecules[i]);
            setValue(moleculeIndex, reactionCount, values[i]);
        }
        // index the reaction
        reactions[reactionCount] = reaction;
        reactionHashcode[reactionCount] = hash;
        reactionMap.put(reaction, reactionCount);
        reactionCount++;

        return reactionCount - 1;

    }

    @SuppressWarnings("unchecked")
    private boolean identical(int j, M[] ms2, T[] vs2, boolean reverisble) {
        M[] ms1 = getReactionMolecules(j);
        T[] vs1 = (T[]) Array.newInstance(vs2[0].getClass(), ms1.length);

        for (int i = 0; i < ms1.length; i++) {
            vs1[i] = _get(moleculeMap.get(ms1[i]), j);
        }

        return identical(j, ms1, vs1, ms2, vs2, reverisble);
    }

    boolean identical(int j, M[] ms1, T[] vs1, M[] ms2, T[] vs2, boolean reverisble) {
        Set<M> ms1Set = Sets.newHashSet(ms1);
        for (M m : ms2) {
            if (!ms1Set.contains(m)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ensures the matrix has the desired capcity to store the specified n
     * (molecules) and m (reactions). If not the the matrix is resized
     * accordingly.
     *
     * @param n number of molecules
     * @param m number of reactions
     */
    @SuppressWarnings("unchecked")
    public void ensure(int n, int m) {

        if (n >= moleculeCapacity) {
            moleculeCapacity = 1 + (Math.max(n, moleculeCapacity) * 2);
            molecules = Arrays.copyOf(molecules, moleculeCapacity);
        }
        if (m >= reactionCapacity) {
            LOGGER.debug("Expanding capacity...");
            reactionCapacity = 1 + Math.max(m, reactionCapacity) * 2;
            reactions = Arrays.copyOf(reactions, reactionCapacity);
            reactionHashcode = Arrays.copyOf(reactionHashcode, reactionCapacity);

            LOGGER.debug("...new capacity:" + moleculeCapacity + "," + reactionCapacity);
        }
    }


    /**
     * Ensures the molecule is present, if the molecule is present then no
     * structural changes are made. If the molecule is not found then the
     * molecule is added to the index and the molecule count increased
     *
     * @param molecule A new molecule
     * @return if the structure was modified
     */
    private boolean ensure(M molecule) {
        if (!moleculeMap.containsKey(molecule)) {
            molecules[moleculeCount] = molecule;
            moleculeMap.put(molecule, moleculeCount);
            moleculeCount++;
            return true;
        }
        return false;
    }


    /** Writes the matrix on System.out PrintStream */
    public void display() {
        display(System.out, ' ');
    }


    /**
     * Writes the matrix on a specified PrintStream
     *
     * @param stream Stream to write to
     */
    public void display(PrintStream stream) {
        display(stream, ' ');
    }


    /**
     * Writes the matrix to a specified PrintStream with the specified
     * separator
     *
     * @param stream
     * @param separator
     */
    public void display(PrintStream stream, char separator) {
        display(stream, separator, " ", 4, 4);
    }


    /**
     * Displays the matrix to the desired PrintStream, Seperator, Value for
     * null, Ordering and Molecule/Reaction trim length
     *
     * @param stream
     * @param seperator
     * @param empty         The value to replace null values with
     * @param molNameLength Trim molecule names to this length
     * @param rxnNameLength Trim reaction names to this length
     */
    public void display(PrintStream stream,
                        char seperator,
                        String empty,
                        int molNameLength,
                        int rxnNameLength) {

        // top-left corner
        stream.printf("%" + molNameLength + "s", "");

        // write reactions
        String format = seperator + " %" + rxnNameLength + "s";
        for (int i = 0; i < reactionCount; i++) {
            stream.printf(format, reactions[i]);
        }
        stream.println();

        String molNameFormat = "%" + molNameLength + "s";
        String valueFormat = seperator + " %" + rxnNameLength + "s";


        for (int i = 0; i < moleculeCount; i++) {

            // write molecule name...
            stream.printf(molNameFormat, molecules[i]);

            // ...and values
            for (int j = 0; j < reactionCount; j++) {
                T value = _get(i, j);
                stream.printf(valueFormat, (value == null
                                            ? empty
                                            : value).toString());
            }
            stream.println();

        }

    }


    /**
     * Returns a fixed size 2D array of the values. The matrix is trimmed to the
     * correct size
     *
     * @return reaction matrix
     */
    @SuppressWarnings("unchecked")
    public T[][] getFixedMatrix() {


        Class c = valueType();

        if (c == null)
            return null;

        // truncate the reactions (removes null paddings)
        T[][] outputMatrix = (T[][]) Array.newInstance(c.getClass(), moleculeCount, reactionCount);
        for (int i = 0; i < outputMatrix.length; i++) {
            for (int j = 0; j < reactionCount; j++) {
                outputMatrix[i][j] = _get(i, j);
            }
        }
        return outputMatrix;
    }


    private Class valueType() {
        if (matrix.isEmpty())
            return null;
        return matrix.values().iterator().next().getClass();
    }


    /**
     * Access to a value at a given index
     *
     * @param i
     * @param j
     * @return The value at the requested location
     */
    public T get(int i, int j) {
        return _get(i, j);
    }

    public T _get(int i, int j) {
        return matrix.get(new IndexKey(i, j));
    }


    /**
     * Assigns the values from the specified 'other' matrix to this one. Note:
     * the values are not deep copied
     *
     * @return reaction indices (of those added)
     */
    public BiMap<Integer, Integer> assign(AbstractReactionMatrix<T, M, R> other) {

        // ensure there is enough space
        this.ensure(getMoleculeCount() + other.getReactionCount(),
                    getReactionCount() + other.getReactionCount());

        BiMap<Integer, Integer> map = HashBiMap.create();

        for (int j = 0; j < other.getReactionCount(); j++) {
            map.put(j, this.addReaction(other.getReaction(j),
                                        other.getReactionMolecules(j),
                                        other.getReactionValues(j)));
        }


        return map;

    }


    public List<T> getValuesForMolecule(int i) {
        return Arrays.asList(getRow(i));
    }


    public int getNonNullCount() {
        return nonNullCount;
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
    @SuppressWarnings("unchecked")
    public T[] getRow(int i) {
        T[] row = (T[]) Array.newInstance(valueType(), reactionCount);
        for (int j = 0; j < reactionCount; j++)
            row[j] = _get(i, j);
        return row;
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


    /** Access to the values for a single reaction */
    @SuppressWarnings("unchecked")
    public T[] getColumn(int j) {
        T[] copy = (T[]) Array.newInstance(valueType(), moleculeCount);
        for (int i = 0; i < moleculeCount; i++) {
            copy[i] = _get(i, j);
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
                nulls[i] += _get(i, j) == null ? 1 : 0;
            }
        }
        return nulls;
    }


    /**
     * Returns a fixed size array of the molecules
     *
     * @return
     */
    @Override
    public M[] getMolecules() {
        return Arrays.copyOf(molecules, moleculeCount);
    }


    /**
     * Returns a list of the reactions
     *
     * @return
     */
    @Override
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
    @Override
    public M getMolecule(Integer i) {
        return molecules[i];
    }


    public Integer getIndex(M molecule) {
        return moleculeMap.get(molecule);
    }


    /**
     * Returns the reaction for a given position
     *
     * @param j
     * @return
     */
    @Override
    public R getReaction(Integer j) {
        return reactions[j];
    }


    @SuppressWarnings("unchecked")
    public T[] getReactionValues(Integer j) {
        List<T> values = new ArrayList<T>();
        for (int i = 0; i < moleculeCount; i++) {
            T value = _get(i, j);
            if (value != null) {
                values.add(value);
            }
        }
        return values.toArray((T[]) Array.newInstance(getValueClass(), values.size()));
    }


    @SuppressWarnings("unchecked")
    public M[] getReactionMolecules(Integer j) {
        List<M> values = new ArrayList<M>();
        for (int i = 0; i < moleculeCount; i++) {
            if (_get(i, j) != null) {
                values.add(molecules[i]);
            }
        }
        return values.toArray((M[]) Array.newInstance(getMoleculeClass(), values.size()));
    }


    public boolean contains(M molecule) {
        return moleculeMap.containsKey(molecule);
    }


    /**
     * Access the reactions for a specific molecule
     *
     * @param molecule
     * @return Map of the reaction to the value
     */
    @Override
    public Map<Integer, T> getReactions(M molecule) {
        Integer i = moleculeMap.get(molecule);
        HashMap<Integer, T> subReactions = new HashMap<Integer, T>();
        for (int j = 0; j < reactionCount; j++) {
            T value = _get(i, j);
            if (value != null) {
                subReactions.put(j, value);
            }
        }
        return subReactions;
    }


    /**
     * Returns a HashSet of molecules that occur more then the provided
     * threshold
     *
     * @param threshold The value above which a molecule is considered highly
     *                  connected (0 returns an empty set)
     * @return Set of the high connected Molecules
     */
    @Override
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
     * @throws java.io.IOException
     */
    public void toTextFile(FileWriter fw) throws IOException {
        toTextFile(fw, 0);
    }


    /**
     * * MOVE ME TO IO ****
     */
    /**
     * Writes the reaction matrix to a text file that CytoScape can read and
     * build a connection file (for cytoscape) with threshold of highly
     * connected molecule specified and all edge values set as "rc"
     *
     * @param fw                     file writer to write the text file to
     * @param connectivityThreshhold the value at which to consider components
     *                               highly connected and exempt from the file
     *                               (-1 = do not remove)
     * @throws java.io.IOException
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
     * @param writer                 writer to write the text format too
     * @param connectivityThreshhold the value at which to consider components
     *                               highly connected and exempt from the file
     *                               (0 = do not remove)
     * @param edgeValues             A list of edge values to use for the
     *                               reactions this allows you to provide a
     *                               connection type i.e. directionality. The
     *                               toStirng method is used to print the value.
     *                               Providing an empty list invokes default
     *                               behavior
     * @throws java.io.IOException
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

    public Set<Map.Entry<IndexKey, T>> entries() {
        return matrix.entrySet();
    }
}
