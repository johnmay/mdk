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
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import uk.ac.ebi.mdk.service.index.structure.ChEBIStructureIndex;
import uk.ac.ebi.mdk.service.loader.AbstractChEBILoader;
import uk.ac.ebi.mdk.service.loader.location.GZIPRemoteLocation;
import uk.ac.ebi.mdk.service.loader.writer.DefaultStructureIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.util.Map;

/**
 * ChEBIStructureLoader - 20.02.2012 <br/>
 * <p/>
 * This class loads the ChEBI SDF file into a lucene index.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBIStructureLoader
        extends AbstractChEBILoader {

    private static final Logger LOGGER = Logger.getLogger(ChEBIStructureLoader.class);

    /**
     * Default constructor uses the {@see ChEBIStructureIndex} for Analyzer/Directory and File location.
     * This loader sets the default location to the EBI FTP site.
     *
     * @throws IOException thrown if the default location for the chebi sdf is invalid
     */
    public ChEBIStructureLoader() throws IOException {

        super(new ChEBIStructureIndex());

        // tell the loader what we need
        addRequiredResource("ChEBI SDF",
                            "An SDF file containing the ChEBI ID as a property named <ChEBI ID>",
                            ResourceFileLocation.class,
                            new GZIPRemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/SDF/ChEBI_lite.sdf.gz"));

    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() throws IOException {

        // get the SDF ResourceFileLocation and open up an sdf-reader
        ResourceFileLocation location = getLocation("ChEBI SDF");
        IteratingSDFReader sdfReader = new IteratingSDFReader(location.open(), SilentChemObjectBuilder.getInstance());
        sdfReader.setSkip(true);
        DefaultStructureIndexWriter writer = new DefaultStructureIndexWriter(getIndex());

        fireProgressUpdate("loading primary accessions...");
        createMap();
        fireProgressUpdate("done");

        int count = 0;
        while (!isCancelled() && sdfReader.hasNext()) {

            IAtomContainer molecule = (IAtomContainer) sdfReader.next();
            Map properties = molecule.getProperties();

            // if we have a ChEBI identifier, write to the index
            if (properties.containsKey("ChEBI ID")) {
                // re-map to primary id using super class method
                String identifier = properties.get("ChEBI ID").toString();
                if(isActive(identifier)){
                    writer.write(getPrimaryIdentifier(identifier), molecule);
                }
            }

            // update progress
            if(++count % 150 == 0)
                fireProgressUpdate(location.progress());


        }

        // close the sdf reader and the index writer
        sdfReader.close();
        location.close();
        writer.close();

    }

}
