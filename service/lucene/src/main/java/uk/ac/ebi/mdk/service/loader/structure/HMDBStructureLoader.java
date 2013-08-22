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

package uk.ac.ebi.mdk.service.loader.structure;

import org.apache.log4j.Logger;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import uk.ac.ebi.mdk.service.index.structure.HMDBStructureIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.location.ZIPRemoteLocation;
import uk.ac.ebi.mdk.service.loader.writer.DefaultStructureIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HMDBStructureLoader - 20.02.2012 <br/> Load the Human Metabolome Database
 * chemical structures into a lucene index
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBStructureLoader
        extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(HMDBStructureLoader.class);

    // pattern to match the HMDB Identifier
    private Pattern HMDB_ID = Pattern.compile("(HMDB\\d+)");

    /**
     * Create the structure loader for the {@see HMDBStructureIndex} with a
     * default location set to the SDF file available from http://www.hmdb.ca.
     *
     * @throws IOException thrown from a malformed URL
     */
    public HMDBStructureLoader() throws IOException {
        super(new HMDBStructureIndex());

        addRequiredResource("HMDB SDF",
                            "An SDF file containing the HMDB Id in the title of each Mol entry",
                            ResourceFileLocation.class,
                            new ZIPRemoteLocation("http://www.hmdb.ca/downloads/structures.zip"));
    }


    /**
     * @inheritDoc
     */
    @Override
    public void update() throws IOException {

        ResourceFileLocation location = getLocation("HMDB SDF");

        // reader the sdf
        IteratingSDFReader sdf = new IteratingSDFReader(location.open(), SilentChemObjectBuilder.getInstance());
        DefaultStructureIndexWriter writer = new DefaultStructureIndexWriter(getIndex());
        sdf.setSkip(true);

        int count = 0;

        while (!isCancelled() && sdf.hasNext()) {

            IAtomContainer molecule = sdf.next();
            Object title = molecule.getProperty(CDKConstants.TITLE);
            Object id = molecule.getProperty("HMDB_ID");

            if (id != null) {
                writer.write(id.toString().trim(), molecule);
            } else if (title != null) {

                Matcher matcher = HMDB_ID.matcher(title.toString());
                if (matcher.find()) {

                    // write to the index
                    String identifier = matcher.group(1);
                    writer.write(identifier, molecule);

                }


            }


            if (++count % 200 == 0)
                fireProgressUpdate(location.progress());

        }

        fireProgressUpdate(1.0d);

        location.close();
        writer.close();

    }

}
