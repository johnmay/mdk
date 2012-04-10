package uk.ac.ebi.chemet.service.loader.crossreference;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.index.crossreference.ChEBICrossReferenceIndex;
import uk.ac.ebi.chemet.service.loader.AbstractChEBILoader;
import uk.ac.ebi.chemet.service.loader.location.RemoteLocation;
import uk.ac.ebi.chemet.service.loader.writer.DefaultCrossReferenceIndexWriter;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.service.location.ResourceFileLocation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Loader will create a lucene index with cross-references for the ChEBI
 * database. The current implementation only write the the cross-references
 * for ChEBI primary identifiers.
 *
 * Note: PubChem Compound Id's provide a problem as require extensive resolution, the
 *       PubChem SID/CID's are not included in this index as it would require parsing of
 *       a very large file.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBICrossReferenceLoader extends AbstractChEBILoader {

    private static final Logger LOGGER = Logger.getLogger(ChEBICrossReferenceLoader.class);

    public ChEBICrossReferenceLoader() throws IOException {
        super(new ChEBICrossReferenceIndex());

        addRequiredResource("ChEBI Database Accession",
                            "Tab Separated Value (TSV) flat-file containing COMPOUND_ID, TYPE and DATABASE_ACCESSION columns",
                            ResourceFileLocation.class,
                            new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession.tsv"));

        // ekk! this file is huge for just getting the pubchem identifiers.
        // PubChem Identifiers are ignored until we can figure a better way of doing this
        //
        //        addRequiredResource("ChEBI References",
        //                            "...",
        //                            ResourceFileLocation.class,
        //                            new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession.tsv"));

    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() throws IOException {

        // open the database accession file
        ResourceFileLocation location = getLocation("ChEBI Database Accession");
        CSVReader tsv = new CSVReader(new InputStreamReader(location.open()), '\t', '\0');


        // store references in a map first to avoid duplications
        Multimap<String, Identifier> crossreferences = HashMultimap.create();

        String[] row = tsv.readNext();
        Map<String, Integer> map = getHeaderMap(row);
        while ((row = tsv.readNext()) != null) {

            if (isCancelled()) break;

            String identifier = row[map.get("COMPOUND_ID")];
            String type = row[map.get("TYPE")];
            String accession = row[map.get("ACCESSION_NUMBER")];

            // create an instance of the identifier and add it to the map
            try {
                Identifier id = IdentifierFactory.getInstance().ofSynonym(type);
                id.setAccession(accession);
                if(isActive(identifier)){
                    crossreferences.put(getPrimaryIdentifier(identifier), id);
                }
            } catch (Exception e) {
                LOGGER.warn(e.getMessage());
            }
        }

        // put references from the map into the index
        DefaultCrossReferenceIndexWriter writer = new DefaultCrossReferenceIndexWriter(getIndex());
        for (String accession : crossreferences.keySet()) {
            for (Identifier crossreference : crossreferences.get(accession)) {
                writer.write(accession, crossreference);
            }

        }

        writer.close();
        location.close();

    }
}
