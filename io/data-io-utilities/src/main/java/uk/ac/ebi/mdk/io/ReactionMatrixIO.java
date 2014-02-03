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
package uk.ac.ebi.mdk.io;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.lang.mutable.MutableInt;
import uk.ac.ebi.mdk.domain.identifier.InChI;
import uk.ac.ebi.mdk.domain.matrix.ReactionMatrix;
import uk.ac.ebi.mdk.domain.matrix.StoichiometricMatrix;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <h3>ReactionMatrixIO.java – MetabolicDevelopmentKit – Jun 28, 2011</h3> A
 * Reader/Writer class for Reaction Matrices. The class uses the OpenCSV library
 * for the reading/writing to streams. <br> <h4>Example:</h4>
 * <pre>
 * {@code
 * File tmp = File.createTempFile( "smatrix" , "tsv" );
 * BasicStoichiometricMatrix sout = new BasicStoichiometricMatrix();
 * sout.addReaction( "A => B" );
 * sout.addReaction( "B => C" );
 * sout.addReaction( "C => A" );
 * ReactionMatrixIO.writeBasicStoichiometricMatrix( sout , new FileWriter( tmp
 * )
 * , '\t' );
 * BasicStoichiometricMatrix sin = ReactionMatrixIO.readBasicStoichiometricMatrix(
 * new FileReader( tmp ) , '\t' );
 * sin.display( System.out );
 * }
 * </pre>
 *
 * @author johnmay
 */
public class ReactionMatrixIO {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ReactionMatrixIO.class);

    private static char separator = '\t';

    private static char quoteCharacter = '\0';

    private static boolean convertDoubles = false;


    /**
     * Invoking method informers writer methods to convert double value to
     * integers
     *
     * @param convert
     */
    public static void setConvertDoubleToInChI(boolean convert) {
        ReactionMatrixIO.convertDoubles = convert;
    }


    /**
     * @param separator
     *
     * @brief Sets the separator character used to delimit fields (default:
     * '\t')
     */
    public static void setSeparator(char separator) {
        ReactionMatrixIO.separator = separator;
    }


    /**
     * @brief Sets the quote character for writing (default: '\0')
     */
    public static void setQuoteCharacter(char quoteCharacter) {
        ReactionMatrixIO.quoteCharacter = quoteCharacter;
    }


    /**
     * Reads a matrix from a file, stream et al.
     *
     * @param reader Reader object to read from
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static StoichiometricMatrix readBasicStoichiometricMatrix(Reader reader, StoichiometricMatrix s) {
        CSVReader csv = new CSVReader(reader, separator, quoteCharacter);
        try {

            // grab the whole matrix
            List<String[]> matrix = csv.readAll();
            // we know the size so can specify this
            s.ensure(matrix.size() - 1,
                     matrix.get(0).length - 1);

            String[] molNames = new String[matrix.size() - 1];
            // get the molecule names
            for (int i = 1; i < matrix.size(); i++) {
                molNames[i - 1] = matrix.get(i)[0];
            }

            String[] reactionNames = new String[matrix.get(0).length - 1];
            // get the molecule names
            for (int j = 1; j < matrix.get(0).length; j++) {
                reactionNames[j - 1] = matrix.get(0)[j];
            }

            // add the reactions
            for (int j = 0; j < reactionNames.length; j++) {

                List<String> molecules = new ArrayList<String>();
                List<Double> coefficients = new ArrayList<Double>();

                for (int i = 0; i < molNames.length; i++) {
                    String value = matrix.get(i + 1)[j + 1];
                    // if the value isn't empty
                    if (value.isEmpty() == false && !value.equals("0.0") && !value.equals("0")) {
                        molecules.add(molNames[i]);
                        coefficients.add(Double.parseDouble(value));
                    }
                }
                s.addReaction(reactionNames[j],
                              molecules.toArray(new String[0]),
                              coefficients.toArray(new Double[0]));
            }


        } catch (IOException ex) {
            logger.error("Unable to read from the CSV: " + reader);
        } finally {
            try {
                csv.close();
            } catch (IOException ex) {
                logger.error("Could not close CSVReader");
            }
        }

        return s;
    }

    @SuppressWarnings("unchecked")
    public static StoichiometricMatrix readCompressedBasicStoichiometricMatrix(InputStream stream, StoichiometricMatrix s) throws IOException {

        DataInputStream in = new DataInputStream(stream);

        int n = in.readInt();
        int m = in.readInt();

        s.ensure(n, m);

        for (int j = 0; j < m; j++) {
            s.setReaction(j, in.readUTF());
            
        }

        for (int i = 0; i < n; i++) {
            s.setMolecule(i, in.readUTF());
        }

        boolean convert = in.readBoolean();
        int size = in.readInt();

        while (--size >= 0) {

            int i = in.readInt();
            int j = in.readInt();
            Object value = convert ? in.readInt() : in.readDouble();
            Double dValue = value instanceof Double ? (Double) value : ((Integer) value).doubleValue();
            s.setValue(i, j, dValue);
        }
        
        try {
            for (int j = 0; j < m; j++) {
                s.setReversible(j, in.readBoolean());
            }
        } catch (IOException e) {
                
        }

        in.close();

        return s;

    }


    //    public static InChIStoichiometricMatrix readInChIStoichiometricMatrix(Reader reader) {
    //        CSVReader csv = new CSVReader(reader, separator, quoteCharacter);
    //        InChIStoichiometricMatrix s = null;
    //        try {
    //
    //            // grab the whole matrix
    //            List<String[]> matrix = csv.readAll();
    //            // we know the size so can specify this
    //            s = InChIStoichiometricMatrix.create(matrix.size() - 1,
    //                                                 matrix.get(0).length - 1);
    //
    //            InChI[] molNames = new InChI[matrix.size() - 1];
    //            // get the molecule names
    //            for (int i = 1; i < matrix.size(); i++) {
    //                molNames[i - 1] = new InChI(matrix.get(i)[0]);
    //            }
    //
    //            ECNumber[] reactionNames = new ECNumber[matrix.get(0).length - 1];
    //            // get the molecule names
    //            for (int j = 1; j < matrix.get(0).length; j++) {
    //                reactionNames[j - 1] = new ECNumber(matrix.get(0)[j]);
    //            }
    //
    //            // add the reactions
    //            for (int j = 0; j < reactionNames.length; j++) {
    //                HashMap<InChI, Double> molValueMap = new HashMap<InChI, Double>();
    //                for (int i = 0; i < molNames.length; i++) {
    //                    String value = matrix.get(i + 1)[j + 1];
    //                    // if the value isn't empty
    //                    if (value.isEmpty() == false) {
    //                        molValueMap.put(molNames[i], Double.parseDouble(value));
    //                    }
    //                }
    //                s.addReaction(reactionNames[j],
    //                              molValueMap.keySet().toArray(new InChI[0]),
    //                              molValueMap.values().toArray(new Double[0]));
    //            }
    //
    //
    //        } catch (IOException ex) {
    //            logger.error("Unable to read from the CSV: " + reader);
    //        } finally {
    //            try {
    //                csv.close();
    //            } catch (IOException ex) {
    //                logger.error("Could not close CSVReader");
    //            }
    //        }
    //
    //        return s;
    //    }


    /**
     * @param s      – Class extending Stoichiometric matrix to write
     * @param writer - Where to write the matrix too
     *
     * @brief Writes a Stoichiometric Matrix (s) using the {@code toString()}
     * method of the molecule object to print the row name. Invoking {
     * <p/>
     * doubles as integers. Note: the writer is not closed on completion
     */
    public static void writeBasicStoichiometricMatrix(StoichiometricMatrix<?, ?> s,
                                                      Writer writer) throws IOException {
        CSVWriter csv = new CSVWriter(new BufferedWriter(writer), separator, quoteCharacter);

        int n = s.getMoleculeCount();
        int m = s.getReactionCount();

        String[] reactionName = new String[s.getReactionCount() + 1];
        for (int j = 0; j < s.getReactionCount(); j++) {
            reactionName[j + 1] = s.getReaction(j).toString();
        }
        csv.writeNext(reactionName);

        for (int i = 0; i < n; i++) {
            String[] copy = new String[m + 1];
            copy[0] = s.getMolecule(i).toString();
            for (int j = 0; j < m; j++) {
                // if the value is null
                copy[j + 1] = convertDoubles
                              ? Integer.toString(s.get(i, j).intValue())
                              : s.get(i, j).toString();
            }
            csv.writeNext(copy);
        }

        csv.close();

    }


    public static void writeCompressedBasicStoichiometricMatrix(StoichiometricMatrix<?, ?> s,
                                                                OutputStream writer) throws IOException {

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(writer, 2048));

        int n = s.getMoleculeCount();
        int m = s.getReactionCount();

        out.writeInt(n);
        out.writeInt(m);

        for (int j = 0; j < m; j++) {
            out.writeUTF(s.getReaction(j).toString());
        }

        for (int i = 0; i < n; i++) {
            out.writeUTF(s.getMolecule(i).toString());
        }

        out.writeBoolean(convertDoubles);
        out.writeInt(s.getNonNullCount());

        for (Map.Entry<ReactionMatrix.IndexKey, Double> e : s.entries()) {
            ReactionMatrix.IndexKey key = e.getKey();
            out.writeInt(key.i());
            out.writeInt(key.j());
            if (convertDoubles)
                out.writeInt(e.getValue().intValue());
            else
                out.writeDouble(e.getValue());
        }

        for (int j = 0; j < m; j++) {
            out.writeBoolean(s.isReversible(j));
        }
        
        out.close();
    }


    /**
     * Writes a sif file (openable with Cytoscape)
     *
     * @param s
     * @param writer
     * @param connectionThreshold
     * @param <R>
     * @param <M>
     *
     * @throws IOException
     */
    public static <R, M> void writeSIF(StoichiometricMatrix<M, R> s,
                                       Writer writer,
                                       Integer connectionThreshold) throws IOException {


        Set<M> highlyConnected = s.getHighlyConnectedMolecules(connectionThreshold);
        Map<M, MutableInt> suffixValue = new HashMap<M, MutableInt>();

        for (M molecule : s.getMolecules()) {

            Map<Integer, Double> reactionMap = s.getReactions(molecule);
            for (Integer i : reactionMap.keySet()) {

                String nodeName = molecule.toString();

                if (highlyConnected.contains(molecule)) {

                    if (!suffixValue.containsKey(molecule)) {
                        suffixValue.put(molecule, new MutableInt(0));
                    }

                    suffixValue.get(molecule).increment();

                    nodeName = nodeName + " " + suffixValue.get(molecule).toString();
                }

                nodeName = nodeName.replaceAll("\\s+", " ");
                nodeName = nodeName.replaceAll("\\s", "_");
                writer.write(s.getReaction(i) + "\t" + "r" + "\t" + nodeName + "\n");

            }

        }

    }

    /**
     * @param s      - A stoichiometric matrix
     * @param writer
     *
     * @brief Writes the InChI additional info, molecule index, inchi, inchikey
     * and auxinfo
     */
    public static void writeInChIAdditionalInfo(StoichiometricMatrix s,
                                                Writer writer) {

        CSVWriter csv = new CSVWriter(new BufferedWriter(writer), separator, quoteCharacter);

        int n = s.getMoleculeCount();

        for (Integer i = 0; i < n; i++) {
            Object obj = s.getMolecule(i);
            if (obj instanceof InChI) {
                InChI inchi = (InChI) obj;
                csv.writeNext(new String[]{i.toString(),
                                           inchi.getInchi(),
                                           inchi.getInchiKey(),
                                           inchi.getAuxInfo()});
            }
            else {
                logger.error("Object is not of type and does not inherit from InChI in matrix array");
            }
        }

    }
}
