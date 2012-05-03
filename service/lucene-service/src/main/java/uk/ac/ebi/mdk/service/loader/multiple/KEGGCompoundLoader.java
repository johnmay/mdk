package uk.ac.ebi.mdk.service.loader.multiple;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.data.KEGGCompoundDataIndex;
import uk.ac.ebi.mdk.service.index.name.KEGGCompoundNameIndex;
import uk.ac.ebi.mdk.service.loader.AbstractMultiIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.writer.DefaultDataIndexWriter;
import uk.ac.ebi.mdk.service.loader.writer.DefaultNameIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * KEGGCompoundLoader - 27.02.2012 <br/>
 * <p/>
 * Loads names and formula's from the kegg/compound/compound file into lucene indexes
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundLoader
        extends AbstractMultiIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundLoader.class);

    private static final Pattern COMPOUND_IDENTIFIER = Pattern.compile("(C\\d{5})");

    public KEGGCompoundLoader() {

        addIndex("kegg.names", new KEGGCompoundNameIndex());
        addIndex("kegg.data",  new KEGGCompoundDataIndex());

        addRequiredResource("KEGG Compound",
                            "File with compound information (i.e. 'compound/compound') file",
                            ResourceFileLocation.class);

    }

    @Override
    public void update() throws IOException {

        ResourceFileLocation location = getLocation("KEGG Compound");
        KEGGCompoundParser parser = new KEGGCompoundParser(location.open(),
                                                           KEGGField.FORMULA, KEGGField.NAME, KEGGField.ENTRY, KEGGField.ENZYME);

        DefaultDataIndexWriter data = new DefaultDataIndexWriter(getIndex("kegg.data"));
        DefaultNameIndexWriter name = new DefaultNameIndexWriter(getIndex("kegg.names"));

        long start = System.currentTimeMillis();
        Map<KEGGField, StringBuilder> map;
        while ((map = parser.readNext()) != null) {

            String entry = map.get(KEGGField.ENTRY).toString();
            String[] names = map.get(KEGGField.NAME).toString().split(";");
            String formula = map.containsKey(KEGGField.FORMULA) ? map.get(KEGGField.FORMULA).toString() : null;

            if (entry.contains("Obsolete")) {
                continue;
            }

            // get identifier and write the data
            Matcher matcher = COMPOUND_IDENTIFIER.matcher(entry);
            if (matcher.find()) {

                String identifier = matcher.group(1);

                name.write(identifier, Arrays.asList(names));
                data.write(identifier, "", formula);

            }

        }
        long end = System.currentTimeMillis();

        System.out.println("Load time: " + (end - start) + " ms");

        location.close();

        name.close();
        data.close();

    }

    @Override
    public String getName() {
        return "KEGG Compound";
    }

    // copied from chemet-io, didn't want to have a dependency as IO is quite messy with a lot of
    // old classes and redundant dependencies
    class KEGGCompoundParser {
        private BufferedReader reader;
        private EnumSet<KEGGField> filter = EnumSet.noneOf(KEGGField.class);

        public KEGGCompoundParser(InputStream stream, KEGGField... field) {
            // 12 mb of buffer
            this.reader = new BufferedReader(new InputStreamReader(stream), 1024 * 12);
            for (KEGGField f : field) {
                filter.add(f);
            }
        }

        public Map<KEGGField, StringBuilder> readNext() throws IOException {

            StringBuilder sb = new StringBuilder(1000);

            Map<KEGGField, StringBuilder> map = new EnumMap(KEGGField.class);

            String line;
            KEGGField field = null;
            while ((line = reader.readLine()) != null && !line.equals("///")) {

                String key = line.substring(0, Math.min(line.length(), 12)).trim();
                field = key.isEmpty() ? field : KEGGField.valueOf(key);

                if (filter.contains(field)) {
                    if (!map.containsKey(field)) {
                        map.put(field, new StringBuilder(500));
                    }
                    map.get(field).append(line.substring(12));
                }

            }
            return line == null ? null : map;
        }

    }

    public enum KEGGField {

        ENTRY, NAME,
        REACTION, FORMULA, MASS,
        REMARK, PATHWAY,
        ENZYME, DBLINKS,
        ATOM, BOND,
        COMMENT, BRACKET,
        ORIGINAL, REPEAT,
        SEQUENCE, ORGANISM,
        GENE, REFERENCE,
        // Glycan fields
        COMPOSITION, NODE, EDGE, CLASS, ORTHOLOGY
    }

}
