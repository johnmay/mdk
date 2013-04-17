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

package uk.ac.ebi.mdk.service.loader.multiple;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.io.xml.hmdb.HMDBMetabolite;
import uk.ac.ebi.mdk.io.xml.hmdb.HMDBParser;
import uk.ac.ebi.mdk.io.xml.hmdb.marshal.HMDBDefaultMarshals;
import uk.ac.ebi.mdk.service.index.data.HMDBDataIndex;
import uk.ac.ebi.mdk.service.index.name.HMDBNameIndex;
import uk.ac.ebi.mdk.service.loader.AbstractMultiIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.writer.DefaultDataIndexWriter;
import uk.ac.ebi.mdk.service.loader.writer.DefaultNameIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A resource loader for the HMDB XML files. This loader requires the path to
 * the directory containing the XML files. Currently only names/chemical data is
 * loaded but this could easily be expanded to include cross-references.
 *
 * @see HMDBParser
 */
public class HMDBXMLLoader extends AbstractMultiIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(HMDBXMLLoader.class);

    private DefaultNameIndexWriter nameWriter;
    private DefaultDataIndexWriter chemDataWriter;

    public HMDBXMLLoader() {

        addIndex("hmdb.names", new HMDBNameIndex());
        addIndex("hmdb.chemdata", new HMDBDataIndex()); // charge, formula, etc.
        //    addIndex("hmdb.inchi", null); // not yet supported

        addRequiredResource("HMDB XML Directory",
                            "Directory of HMDB xml files",
                            ResourceDirectoryLocation.class);

    }

    public String getName() {
        return "HMDB XML";
    }

    @Override
    public void update() throws IOException {

        // access location of XML File
        ResourceDirectoryLocation location = getLocation("HMDB XML Directory");


        // open the index writers
        nameWriter = new DefaultNameIndexWriter(getIndex("hmdb.names"));
        chemDataWriter = new DefaultDataIndexWriter(getIndex("hmdb.chemdata"));

        int count = 0;

        while (!isCancelled() && location.hasNext()) {
            InputStream in = null;
            try {
                in = location.next();

                // skip non xml files
                if (!location.getEntryName().endsWith(".xml"))
                    continue;

                HMDBParser parser = new HMDBParser(new InputStreamReader(in),
                                                   HMDBDefaultMarshals.ACCESSION,
                                                   HMDBDefaultMarshals.SECONDARY_ACCESSION,
                                                   HMDBDefaultMarshals.SECOUNDARY_ACCESSION,
                                                   HMDBDefaultMarshals.CHARGE,
                                                   HMDBDefaultMarshals.FORMULA,
                                                   HMDBDefaultMarshals.COMMON_NAME,
                                                   HMDBDefaultMarshals.IUPAC_NAME,
                                                   HMDBDefaultMarshals.SYNONYMS);

                for (HMDBMetabolite entry : parser.metabolites()) {
                    nameWriter.write(entry.getAccession(),
                                     entry.getCommonName(),
                                     entry.getIUPACName(),
                                     entry.getSynonyms());
                    chemDataWriter.write(entry.getAccession(),
                                         entry.getFormalCharge(),
                                         entry.getMolecularFormula());
                }

                // only update every 150 entries
                if(++count % 150 == 0)
                    fireProgressUpdate(location.progress());


            } catch (XMLStreamException e) {
                LOGGER.error("unable to read " + location.getEntryName() + ": " + e);
            } finally {
                in.close();
            }
        }

        fireProgressUpdate(1.0d);

        location.close();
        nameWriter.close();
        chemDataWriter.close();

    }

}
