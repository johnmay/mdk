package uk.ac.ebi.mdk.service.loader.multiple;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.name.LipidMapsNameIndex;
import uk.ac.ebi.mdk.service.loader.AbstractMultiIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.location.ZIPRemoteLocation;
import uk.ac.ebi.mdk.service.loader.writer.DefaultNameIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author John May
 */
public class LipidMapsLoader extends AbstractMultiIndexResourceLoader {


    private static final Logger LOGGER = Logger.getLogger(LipidMapsLoader.class);

    public LipidMapsLoader() throws IOException {

        addIndex("lipid.maps.names", new LipidMapsNameIndex());

        addRequiredResource("Lipid Maps (File)",
                            "Single '.tsv' file from LIPID MAPS. This should be used if you only want to load" +
                                    " a specific class from LIPID MAPS (e.g. Fatty Acids) ",
                            ResourceFileLocation.class);
        addRequiredResource("Lipid Maps (Folder)",
                            "Folder containing multiple '.tsv' entries (can be compressed with Zip). The" +
                                    " file ending '...All.tsv' will be used to load the index ",
                            ResourceDirectoryLocation.class,
                            new ZIPRemoteLocation("http://www.lipidmaps.org/downloads/LMSD_20120412_tsv.zip"));

    }

    @Override
    public void update() throws IOException {
        if (hasLocation("lipid.maps.folder")) {
            updateFromFolder();
        } else if (hasLocation("lipid.maps.file")) {
            ResourceFileLocation location = getLocation("lipid.maps.file");
            update(location.open());
            location.close();
        } else {
            LOGGER.error("No available locations");
        }
    }

    public void update(InputStream in) throws IOException {

        CSVReader tsv = new CSVReader(new InputStreamReader(in), '\t', '\0');
        update(tsv);
        // close not managed by parser

    }

    public void update(CSVReader reader) throws IOException {

        DefaultNameIndexWriter writer = new DefaultNameIndexWriter(getIndex("lipid.maps.names"));

        String[] row;
        reader.readNext(); // skip headers
        while ((row = reader.readNext()) != null && !isCancelled()) {

            // name data
            String identifier = row[0];
            String preferred = row[11];
            String systematic = row[1];
            String synonyms = row[2];

            // chemical data
            String formula = row[6];
            String exactmass = row[5];


            writer.write(identifier, preferred, systematic, "", "", Arrays.asList(synonyms.split(";")));

        }

        writer.close();

    }

    public void updateFromFolder() throws IOException {

        ResourceDirectoryLocation location = getLocation("lipid.maps.folder");

        int count = 0;
        while (location.hasNext()) {

            InputStream in = location.next();
            String name = location.getEntryName();

            if (name.endsWith("All.tsv"))
                update(in);

            // could give better indicator
            fireProgressUpdate(location.progress());

        }
        location.close();


    }

    @Override
    public boolean canUpdate() {
        return hasLocation("lipid.maps.file") || hasLocation("lipid.maps.folder");
    }

    @Override
    public String getName() {
        return "Lipid Maps Name";
    }
}
