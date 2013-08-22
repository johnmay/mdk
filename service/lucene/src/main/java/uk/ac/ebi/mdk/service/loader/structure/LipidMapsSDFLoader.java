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
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import uk.ac.ebi.mdk.service.ResourceLoader;
import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.index.structure.LipidMapsStructureIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.location.ZIPRemoteLocation;
import uk.ac.ebi.mdk.service.loader.writer.DefaultStructureIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author John May
 */
public class LipidMapsSDFLoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(LipidMapsSDFLoader.class);

    public LipidMapsSDFLoader(LuceneIndex index) throws IOException {
        super(index);
        addRequiredResource("Lipid Maps SDF",
                            "A lipid maps SDF folder/directory (can be zipped)",
                            ResourceDirectoryLocation.class,
                            new ZIPRemoteLocation("http://www.lipidmaps.org/downloads/LMSDFDownload23Apr12.zip"));
    }

    public LipidMapsSDFLoader() throws IOException {
        this(new LipidMapsStructureIndex());
    }

    @Override
    public void update() throws IOException {

        ResourceDirectoryLocation location = getLocation("Lipid Maps SDF");

        DefaultStructureIndexWriter writer = new DefaultStructureIndexWriter(getIndex());


        while (location.hasNext() && !isCancelled()){

            InputStream in   = location.next();
            String      name = location.getEntryName();

            // only need a single SDF file (could break if the name changes)
            if(!name.endsWith("FinalAll.sdf"))
                continue;

            IteratingSDFReader reader = new IteratingSDFReader(in,
                                                               SilentChemObjectBuilder.getInstance(),
                                                               true);
            int count = 0;
            while (reader.hasNext() && !isCancelled()){
                IAtomContainer molecule = reader.next();
                String identifier = molecule.getProperty(CDKConstants.TITLE).toString();
                writer.write(identifier, molecule);

                // update progress every 150 entries
                if(++count % 150 == 0)
                    fireProgressUpdate(location.progress());
            }

            writer.close();

        }

    }


    public static void main(String[] args) throws IOException {
        ResourceLoader loader = new LipidMapsSDFLoader(new KeywordNIOIndex("LIPID Maps", "/structure/lipid-maps") {
        });

        if(loader.canBackup()) loader.backup();
        if(loader.canUpdate()) loader.update();
    }

}
