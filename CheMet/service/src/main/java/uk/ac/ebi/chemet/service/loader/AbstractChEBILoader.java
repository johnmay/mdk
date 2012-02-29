package uk.ac.ebi.chemet.service.loader;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.loader.location.ZIPRemoteLocation;
import uk.ac.ebi.service.exception.MissingLocationException;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.location.ResourceFileLocation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AbstractChEBILoader - 28.02.2012 <br/>
 * <p/>
 * Provides methods for resolving ChEBI secondary identifiers to their primary equivalent
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractChEBILoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(AbstractChEBILoader.class);

    private Map<String, String> identifiers = new HashMap<String, String>();

    /**
     * @inheritDoc
     */
    public AbstractChEBILoader(LuceneIndex index) throws IOException {
        super(index);

        addRequiredResource("ChEBI Compounds",
                            "...",
                            ResourceFileLocation.class,
                            new ZIPRemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/compounds.tsv.zip"));

    }

    /**
     * Creates a map from the ChEBI Compounds resources of secondary to primary identifiers.
     *
     * @throws IOException problem reading file
     * @throws MissingLocationException if ChEBI Compounds resource location is missing
     */
    public void createIdentifierMap() throws IOException, MissingLocationException {


        ResourceFileLocation location = getLocation("ChEBI Compounds");
        CSVReader csv = new CSVReader(new InputStreamReader(location.open()), '\t');

        List<String> header = Arrays.asList(csv.readNext());
        int accessionIndex = header.indexOf("CHEBI_ACCESSION");
        int parentIndex = header.indexOf("PARENT_ID");

        Pattern NULL_PATTERN = Pattern.compile("null");
        Pattern ACCESSION_PATTERN = Pattern.compile("(?:C[hH]EBI:)?(\\d+)");

        String[] row = null;
        while ((row = csv.readNext()) != null) {

            String accession = row[accessionIndex];
            String parent = row[parentIndex];

            Matcher accessionMatcher = ACCESSION_PATTERN.matcher(accession);
            Matcher parentMatcher = ACCESSION_PATTERN.matcher(parent);

            if (accessionMatcher.find()) {
                if (parentMatcher.find()) {
                    identifiers.put(accessionMatcher.group(1), "CHEBI:" + parentMatcher.group(1));
                    identifiers.put("CHEBI:" + accessionMatcher.group(1), "CHEBI:" + parentMatcher.group(1));
                } else {
                    identifiers.put(accessionMatcher.group(1), "CHEBI:" + accessionMatcher.group(1));
                }
            }

        }

        location.close();
        csv.close();

    }

    /**
     * Access the primary identifier for the given accession. The accession can be a straight number
     * or prefixed with "CHEBI:". If the map is not build then a new map is build (if you have already
     * opened the 'ChEBI Compounds' resource this method may fail. To avoid this, call {@see createIdentifierMap()}
     * as the first thing in the update() method). If no identifier mapping is found the provided accession
     * is returned.
     *
     * @param accession
     * @return
     * @throws IOException
     * @throws MissingLocationException
     */
    public String getPrimaryIdentifier(String accession) throws IOException, MissingLocationException {

        if (identifiers.isEmpty())
            createIdentifierMap();

        if (identifiers.containsKey(accession)) {
            return identifiers.get(accession);
        }

        return accession;

    }

}
