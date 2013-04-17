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
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.writer.DefaultStructureIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author John May
 */
public class BioCycStructureLoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(BioCycStructureLoader.class);

    private final String org;

    public BioCycStructureLoader(String org, LuceneIndex index) {
        super(index);

        this.org = org;

        addRequiredResource("Mol Folder",
                            "Mol folder containing .mol files and the BioCyc identifier as the name of the file",
                            ResourceDirectoryLocation.class);

    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() throws IOException {

        // get the directory of files
        ResourceDirectoryLocation location = getLocation("mol.folder");
        DefaultStructureIndexWriter writer = new DefaultStructureIndexWriter(getIndex());
        MDLV2000Reader mdlReader = new MDLV2000Reader();

        int count = 0;
        while (location.hasNext() && !isCancelled()) {

            InputStream in = location.next();
            String name = location.getEntryName();

            // skip non-mol files
            if (!name.endsWith(".mol"))
                continue;

            try {
                mdlReader.setReader(in);
                IAtomContainer molecule = mdlReader.read(new AtomContainer());
                writer.write(org + ":" + name.substring(0, name.indexOf(".mol")), molecule);
                mdlReader.close();
            } catch (Exception ex) {
                LOGGER.warn("Could not read entry: " + name + " reason: " + ex.getMessage());
            }

            if(++count % 150 == 0)
                fireProgressUpdate(location.progress());

        }

        writer.close();
        location.close();

    }

}
