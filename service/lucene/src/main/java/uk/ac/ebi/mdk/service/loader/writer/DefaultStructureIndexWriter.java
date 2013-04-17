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

package uk.ac.ebi.mdk.service.loader.writer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.fingerprint.IFingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.mdk.service.analyzer.FingerprintSimilarity;
import uk.ac.ebi.mdk.service.index.LuceneIndex;

import java.io.*;
import java.util.BitSet;

import static org.apache.lucene.document.Field.Index.ANALYZED;
import static org.apache.lucene.document.Field.Store.NO;
import static uk.ac.ebi.mdk.service.query.structure.StructureService.*;

/**
 * ${Name}.java - 20.02.2012 <br/> MetaInfo...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultStructureIndexWriter extends AbstractIndexWriter {

    private static final Logger LOGGER = Logger.getLogger(DefaultStructureIndexWriter.class);

    private IFingerprinter fingerprinter;
    private static final int DEFAULT_THRESHOLD = 80; // calculated by looking at atom count distribution in chebi -> 80 > 95% of data set
    private int threshold;
    private MDLV2000Writer mdlWriter = new MDLV2000Writer();

    /**
     * Create a new structure index writer
     * @param index
     * @param fingerprinter fingerprint method
     * @param threshold number of atoms above which a fingerprint should not be calculated
     * @throws IOException
     */
    public DefaultStructureIndexWriter(LuceneIndex index,
                                       IFingerprinter fingerprinter,
                                       int threshold) throws IOException {
        super(index);
        this.fingerprinter = fingerprinter;
        getWriter().getConfig().setSimilarity(new FingerprintSimilarity());
        this.threshold = threshold;
    }

    /**
     * Use basic CDK fingerprinter and default theshold value (50).
     * @param index
     * @throws IOException
     */
    public DefaultStructureIndexWriter(LuceneIndex index) throws IOException {
        this(index, new Fingerprinter(), DEFAULT_THRESHOLD);
    }


    /**
     * Write a CDK molecule and it's identifier to the index
     * @param identifier
     * @param molecule
     * @throws IOException
     */
    public void write(String identifier,
                      IAtomContainer molecule) throws IOException {

        // Serialize to a byte array
        StringWriter sw = new StringWriter();
        try {
            mdlWriter.setWriter(sw);
            mdlWriter.write(molecule);

            // Get the bytes of the serialized object
            write(identifier, sw.toString().getBytes(), getFingerprint(molecule));

        } catch (CDKException e) {
            LOGGER.error("Skipping unwritable structure for " + identifier + " reason " + e.getMessage());
        }


    }

    public BitSet getFingerprint(IAtomContainer molecule){
        try {
            return molecule.getAtomCount() < threshold ? fingerprinter.getFingerprint(molecule) : new BitSet();
        } catch (CDKException ex){
            LOGGER.warn("Fingerprint was not calculated for molecule: " + molecule );
            return new BitSet();
        }
    }

    private void write(String identifier,
                    byte[] molecule,
                    BitSet fp) throws IOException {

        Document document = new Document();

        // store the identifier and AtomContainer values
        document.add(create(IDENTIFIER, identifier));
        document.add(new Field(ATOM_CONTAINER.field(), molecule));

        // index the fingerprint
        for(int i = fp.nextSetBit(0); i != -1; i = fp.nextSetBit(i + 1)){
            document.add(create(FINGERPRINT_BIT, Integer.toString(i), NO, ANALYZED));
        }

        add(document);

    }


}
