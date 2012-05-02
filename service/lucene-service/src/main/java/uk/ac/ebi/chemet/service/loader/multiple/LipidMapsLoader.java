package uk.ac.ebi.chemet.service.loader.multiple;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.index.name.DefaultNameIndex;
import uk.ac.ebi.chemet.service.loader.AbstractMultiIndexResourceLoader;
import uk.ac.ebi.chemet.service.loader.location.ZIPRemoteLocation;
import uk.ac.ebi.chemet.service.loader.writer.DefaultNameIndexWriter;
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

        addIndex("lipid.maps.names", new DefaultNameIndex("Lipid Maps", "name/lipid-maps"));

        addRequiredResource("Lipid Maps (.tsv)",
                            "Single Lipid Maps TSV file",
                            ResourceFileLocation.class);
        addRequiredResource("Lipid Maps (.zip)",
                            "ZIPped archive with multiple '.tsv' entries",
                            ResourceDirectoryLocation.class,
                            new ZIPRemoteLocation("http://www.lipidmaps.org/downloads/LMSD_20120125_tsv.zip"));

    }

    @Override
    public void update() throws IOException {
        if (hasLocation("lipid.maps.zip")) {
            updateFromZip();
        } else if (hasLocation("lipid.maps.tsv")) {
            ResourceFileLocation location = getLocation("Lipid Maps TSV");
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

        String[] row = reader.readNext();
        while ((row = reader.readNext()) != null) {

            // name data
            String identifier = row[0];
            String preferred  = row[11];
            String systematic = row[1];
            String synonyms   = row[2];

            // chemical data
            String formula    = row[6];
            String exactmass  = row[5];


            writer.write(identifier, preferred, systematic, "", "", Arrays.asList(synonyms.split(";")));

        }

        writer.close();

    }

    public void updateFromZip() throws IOException {

        ResourceDirectoryLocation location = getLocation("lipid.maps.zip");

        while (location.hasNext()) {

            InputStream in = location.next();
            String name = location.getEntryName();

            if (name.endsWith("All.tsv"))
                update(in);

        }
        location.close();


    }

    @Override
    public boolean canUpdate() {
        return hasLocation("lipid.maps.tsv") || hasLocation("lipid.maps.zip");
    }

    @Override
    public String getName() {
        return "Lipid Maps";
    }

    public static void main(String[] args) throws IOException {

        LipidMapsLoader loader = new LipidMapsLoader();
        if (loader.canBackup()) loader.backup();
        if (loader.canUpdate()) {
            loader.update();
        } else {
            LOGGER.error("Unable to update");
        }


    }
}
