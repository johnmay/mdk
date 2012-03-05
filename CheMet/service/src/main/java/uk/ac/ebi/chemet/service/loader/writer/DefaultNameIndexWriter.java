package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.name.*;
import uk.ac.ebi.service.query.QueryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * DefaultNameIndexWriter - 22.02.2012 <br/>
 * <p/>
 * Class writes names to a search-able lucene index
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultNameIndexWriter {

    private static final Logger LOGGER = Logger.getLogger(DefaultNameIndexWriter.class);

    private LuceneIndex index;
    private IndexWriter writer;

    public DefaultNameIndexWriter(LuceneIndex index) throws IOException {
        this.index = index;
        this.writer = new IndexWriter(index.getDirectory(),
                                      new IndexWriterConfig(Version.LUCENE_34, index.getAnalyzer()));
    }

    public void write(String identifier, List<String> names) throws IOException {
        if (!names.isEmpty()) {
            write(identifier,
                  names.get(0),             // preferred
                  "",                       // iupac
                  names.size() > 1          // synonyms (all others)
                          ? names.subList(1, names.size())
                          : new ArrayList<String>());
        }
    }

    /**
     * Write the fields to a document and add it to the index
     * @param identifier
     * @param preferred
     * @param iupac
     * @param synonyms
     * @throws IOException
     */
    public void write(String identifier, String preferred, String iupac, Collection<String> synonyms) throws IOException {

        Document document = new Document();

        document.add(new Field(QueryService.IDENTIFIER.field(), identifier.trim(), Field.Store.YES, Field.Index.ANALYZED));
        if (iupac != null && !iupac.isEmpty()) {
            document.add(new Field(IUPACNameService.IUPAC.field(), iupac.trim(), Field.Store.YES, Field.Index.ANALYZED));
        }
        if (preferred != null && !preferred.isEmpty()) {
            document.add(new Field(PreferredNameService.PREFERRED_NAME.field(), preferred.trim(), Field.Store.YES, Field.Index.ANALYZED));
        }
        if (synonyms != null && synonyms.size() > 0) {
            for (String synonym : synonyms) {

                // skip those which are already in the index
                if(matches(synonym, iupac) || matches(synonym, preferred))
                    continue;

                document.add(new Field(SynonymService.SYNONYM.field(), synonym.trim(), Field.Store.YES, Field.Index.ANALYZED));

            }
        }

        writer.addDocument(document);
    }

    public void write(String identifier, String preferred, String iupac, String brand, String inn, Collection<String> synonyms) throws IOException {

        Document document = new Document();

        document.add(new Field(QueryService.IDENTIFIER.field(), identifier.trim(), Field.Store.YES, Field.Index.ANALYZED));
        if (iupac != null && !iupac.isEmpty()) {
            document.add(new Field(IUPACNameService.IUPAC.field(), iupac.trim(), Field.Store.YES, Field.Index.ANALYZED));
        }
        if (preferred != null && !preferred.isEmpty()) {
            document.add(new Field(PreferredNameService.PREFERRED_NAME.field(), preferred.trim(), Field.Store.YES, Field.Index.ANALYZED));
        }
        if (brand != null && !brand.isEmpty()) {
            document.add(new Field(BrandNameService.BRAND_NAME.field(), brand.trim(), Field.Store.YES, Field.Index.ANALYZED));
        }
        if (inn != null && !inn.isEmpty()) {
            document.add(new Field(InternationalNonproprietaryNameService.INN.field(), inn.trim(), Field.Store.YES, Field.Index.ANALYZED));
        }
        if (synonyms != null && synonyms.size() > 0) {
            for (String synonym : synonyms) {

                // skip those which are already in the index
                if(matches(synonym, iupac) || matches(synonym, preferred))
                    continue;

                document.add(new Field(SynonymService.SYNONYM.field(), synonym.trim(), Field.Store.YES, Field.Index.ANALYZED));

            }
        }

        writer.addDocument(document);
    }
    
    public boolean matches(String name1, String name2){
        if(name1 == null || name2 == null) {
            return false;
        }
        return name1.toLowerCase(Locale.ENGLISH).equals(name2.toLowerCase(Locale.ENGLISH));
    }


    public void close() throws IOException {
        writer.close();
    }

}
