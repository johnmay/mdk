package uk.ac.ebi.mdk.service.loader.single;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import uk.ac.ebi.mdk.service.index.other.TaxonomyIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.location.RemoteLocation;
import uk.ac.ebi.mdk.service.query.taxonomy.TaxonomyQueryService;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;
import uk.ac.ebi.mdk.service.query.name.NameService;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TaxonomyLoader - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class TaxonomyLoader
        extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(TaxonomyLoader.class);

    public TaxonomyLoader() throws IOException {
        super(new TaxonomyIndex());

        addRequiredResource("UniProt Species List",
                            "",
                            ResourceFileLocation.class,
                            new RemoteLocation("ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/docs/speclist.txt"));

    }

    private static Pattern ENTRY = Pattern.compile("(\\w{5})\\s([EBVA])\\s+(\\d+): N=(.+)");

    public void update() throws IOException {

        ResourceFileLocation species = getLocation("UniProt Species List");
        Scanner scanner = new Scanner(species.open());

        IndexWriter writer = new IndexWriter(getIndex().getDirectory(),
                                             new IndexWriterConfig(Version.LUCENE_34, getIndex().getAnalyzer()));

        while (scanner.hasNext()) {
            String entry = scanner.nextLine();
            Matcher matcher = ENTRY.matcher(entry);

            if(isCancelled()) break;

            if (matcher.matches()) {

                Document document = new Document();

                String code = matcher.group(1);
                String kingdom = matcher.group(2);
                String identifier = matcher.group(3);
                String name = matcher.group(4);

                document.add(new Field(NameService.IDENTIFIER.field(), identifier,
                                       Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
                document.add(new Field(NameService.NAME.field(), name,
                                       Field.Store.YES, Field.Index.ANALYZED));
                document.add(new Field(TaxonomyQueryService.CODE.field(), code,
                                       Field.Store.YES, Field.Index.ANALYZED));
                document.add(new Field(TaxonomyQueryService.KINGDOM.field(), kingdom,
                                       Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));

                writer.addDocument(document);

            }
        }


        species.close();
        writer.close();


    }

    public static void main(String[] args) throws IOException{
        long start = System.currentTimeMillis();
        TaxonomyLoader loader = new TaxonomyLoader();
        System.out.println(loader.canUpdate());
        System.out.println(loader.canBackup() || loader.canRevert());
        System.out.println(loader.canRevert());
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }

}
