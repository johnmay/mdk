package uk.ac.ebi.mdk.service.loader.multiple;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.data.HMDBDataIndex;
import uk.ac.ebi.mdk.service.index.name.HMDBNameIndex;
import uk.ac.ebi.mdk.service.loader.AbstractMultiIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.writer.DefaultDataIndexWriter;
import uk.ac.ebi.mdk.service.loader.writer.DefaultNameIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HMDBMetabocardsLoader - 26.02.2012 <br/> <p/> Loader parses the information
 * from the Metabocards providing data from the Human Metabolome Database
 * (HMDB). This loader will load a index for Name and Chemical Data
 * (Charge/Formula). Future plans could easily expand this to load Mass and
 * InChI/SMILES indexes.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBMetabocardsLoader extends AbstractMultiIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(HMDBMetabocardsLoader.class);

    private static final Pattern RECORD_START = Pattern.compile("#BEGIN_METABOCARD\\s(HMDB\\d+)");

    private static final Set<String> ACCEPTED_TAGS = new HashSet<String>(Arrays.asList("chemical_formula:", "physiological_charge:",
                                                                                       "iupac:", "name:", "synonyms:"));

    private DefaultNameIndexWriter nameWriter;
    private DefaultDataIndexWriter chemDataWriter;

    public HMDBMetabocardsLoader() {

        addIndex("hmdb.names", new HMDBNameIndex());
        addIndex("hmdb.chemdata", new HMDBDataIndex()); // charge, formula, etc.
        //    addIndex("hmdb.inchi", null); // not yet supported

        addRequiredResource("Metabocards",
                            "HMDB Metabocards file",
                            ResourceFileLocation.class);

    }

    public String getName() {
        return "HMDB Metabocards (legacy)";
    }

    @Override
    public void update() throws IOException {

        ResourceFileLocation location = getLocation("Metabocards");
        BufferedReader scanner = new BufferedReader(new InputStreamReader(location.open()));

        // open the index writers
        nameWriter = new DefaultNameIndexWriter(getIndex("hmdb.names"));
        chemDataWriter = new DefaultDataIndexWriter(getIndex("hmdb.chemdata"));

        Map<String, StringBuffer> map = new HashMap<String, StringBuffer>();

        // current id and tag
        String id = "";
        String tag = "";

        int count = 0;
        String line = "";
        while ((line = scanner.readLine()) != null
                && !isCancelled()) {


            line = line.trim();

            // skip empty line
            if (line.isEmpty()) continue;

            if (isTag(line)) {

                Matcher matcher = RECORD_START.matcher(line);

                if (matcher.matches()) {
                    write(id, map);
                    id = matcher.group(1);

                } else {
                    tag = getTag(line);
                }

                continue;

            }

            // only store certain tags, if a tag isn't accepted the line is skipped
            if (!ACCEPTED_TAGS.contains(tag))
                continue;

            if (!map.containsKey(tag)) {
                map.put(tag, new StringBuffer(50));
            }

            map.get(tag).append(line);

            // update progress every 150 entries
            if ((++count % 150) == 0) {
                fireProgressUpdate(location.progress());
            }


        }


        fireProgressUpdate(1.0d);
        location.close();
        nameWriter.close();
        chemDataWriter.close();

    }

    /**
     * Writes the data stored in the map to the required index
     *
     * @param identifer
     * @param data
     * @throws IOException
     */
    public void write(String identifer, Map<String, StringBuffer> data) throws
                                                                        IOException {

        if (identifer.isEmpty())
            return;

        // store the names
        nameWriter.write(identifer,
                         data.get("name:").toString(),
                         data.get("iupac:").toString(),
                         Arrays.asList(data.get("synonyms:").toString().split(";")));

        chemDataWriter.write(identifer,
                             data.get("physiological_charge:").toString(),
                             data.get("chemical_formula:").toString());

        for (StringBuffer buffer : data.values()) {
            buffer.delete(0, buffer.length());
        }

    }

    public boolean isTag(String line) {
        return line.charAt(0) == '#';
    }

    public String getTag(String line) {
        return line.substring(2);
    }

}
