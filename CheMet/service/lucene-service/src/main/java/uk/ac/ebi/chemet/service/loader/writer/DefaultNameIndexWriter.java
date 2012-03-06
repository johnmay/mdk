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
public class DefaultNameIndexWriter extends AbstractIndexWriter {

    public DefaultNameIndexWriter(LuceneIndex index) throws IOException {
        super(index);
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

        document.add(create(QueryService.IDENTIFIER, identifier.trim()));
        if (iupac != null && !iupac.isEmpty()) {
            document.add(create(IUPACNameService.IUPAC, iupac.trim()));
        }
        if (preferred != null && !preferred.isEmpty()) {
            document.add(create(PreferredNameService.PREFERRED_NAME, preferred.trim()));
        }
        if (synonyms != null && synonyms.size() > 0) {
            for (String synonym : synonyms) {

                // skip those which are already in the index
                if(matches(synonym, iupac) || matches(synonym, preferred))
                    continue;

                document.add(create(SynonymService.SYNONYM, synonym.trim()));

            }
        }

        add(document);
    }

    public void write(String identifier, String preferred, String iupac, String brand, String inn, Collection<String> synonyms) throws IOException {

        Document document = new Document();

        document.add(create(QueryService.IDENTIFIER, identifier.trim()));
        if (iupac != null && !iupac.isEmpty()) {
            document.add(create(IUPACNameService.IUPAC, iupac.trim()));
        }
        if (preferred != null && !preferred.isEmpty()) {
            document.add(create(PreferredNameService.PREFERRED_NAME, preferred.trim()));
        }
        if (brand != null && !brand.isEmpty()) {
            document.add(create(BrandNameService.BRAND_NAME, brand.trim()));
        }
        if (inn != null && !inn.isEmpty()) {
            document.add(create(InternationalNonproprietaryNameService.INN, inn.trim()));
        }
        if (synonyms != null && synonyms.size() > 0) {
            for (String synonym : synonyms) {

                // skip those which are already in the index
                if(matches(synonym, iupac) || matches(synonym, preferred))
                    continue;

                document.add(create(SynonymService.SYNONYM, synonym.trim()));

            }
        }

        add(document);
    }
    
    public boolean matches(String name1, String name2){
        if(name1 == null || name2 == null) {
            return false;
        }
        return name1.toLowerCase(Locale.ENGLISH).equals(name2.toLowerCase(Locale.ENGLISH));
    }

}
