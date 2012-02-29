package uk.ac.ebi.chemet.service.loader.crossreference;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.index.crossreference.ChEBICrossReferenceIndex;
import uk.ac.ebi.chemet.service.loader.AbstractChEBILoader;
import uk.ac.ebi.chemet.service.loader.location.RemoteLocation;
import uk.ac.ebi.chemet.service.loader.writer.DefaultCrossReferenceIndexWriter;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.service.exception.MissingLocationException;
import uk.ac.ebi.service.location.ResourceFileLocation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.Map;

/**
 * ChEBICrossReferenceLoader - 29.02.2012 <br/>
 * <p/>
 * Class descriptions.
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
                            "...",
                            ResourceFileLocation.class,
                            new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession.tsv"));

// ekk this file is huge for just getting the pubchem identifiers?
//        addRequiredResource("ChEBI References",
//                            "...",
//                            ResourceFileLocation.class,
//                            new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/database_accession.tsv"));

    }

    @Override
    public void update() throws MissingLocationException, IOException {

        ResourceFileLocation location = getLocation("ChEBI Database Accession");

        CSVReader tsv = new CSVReader(new InputStreamReader(location.open()), '\t', '\0');

        DefaultCrossReferenceIndexWriter writer = new DefaultCrossReferenceIndexWriter(getIndex());

        String[] row = tsv.readNext();
        Map<String,Integer> map = getHeaderMap(row);
        while((row = tsv.readNext()) != null){

            if(isCancelled()) break;

            String identifier  = row[map.get("COMPOUND_ID")];
            String type        = row[map.get("TYPE")];
            String accession   = row[map.get("ACCESSION_NUMBER")];


            try{
                Identifier id = IdentifierFactory.getInstance().ofSynonym(type);
                // could write cross-references for all identifiers but this
                // doubles the index size a
                String chebiid = getPrimaryIdentifier(identifier);
                writer.write(chebiid, id.getIndex().toString(), accession);
            }catch (Exception e){
                LOGGER.warn("No db to synonym");
            }



        }

        writer.close();
        location.close();

    }

    public static void main(String[] args) throws Exception {
        new ChEBICrossReferenceLoader().update();
    }
}
