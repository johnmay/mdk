package uk.ac.ebi.chemet.service.query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.services.LuceneService;
import uk.ac.ebi.service.query.NameService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

/**
 * AbstractQueryService - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AbstractQueryService {

    private Document[] documents;
    private Directory directory;
    private Analyzer analyzer;
    private IndexReader reader;
    private int max = Preferences.userNodeForPackage(AbstractQueryService.class).getInt("default.max.results", 100);
    private float minSimilarity = 0.5f; // for fuzzy queries

    public AbstractQueryService(){

    }

    public AbstractQueryService(LuceneService service) {
        analyzer  = service.getAnalzyer();
        try {
            setDirectory(service.getDirectory());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(AbstractQueryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setMinSimilarity(float minSimilarity) {
        this.minSimilarity = minSimilarity;
    }

    public float getMinSimilarity() {
        return minSimilarity;
    }

    public void setDirectory(Directory directory) throws IOException {
        if(reader != null){
            reader.close();
        }
        this.directory = directory;
        reader = IndexReader.open(directory, true);
        documents = new Document[reader.numDocs()];
    }

    public void setAnalyzer(Analyzer analyzer){
        this.analyzer = analyzer;
    }

    public void setMaxResults(int max) {
        this.max = max;
    }

    public int getMaxResults() {
        return max;
    }

    public byte[] getBinaryValue(ScoreDoc document, String field) throws CorruptIndexException, IOException {
        int index = document.doc;

        if (documents[index] == null) {
            documents[index] = reader.document(index);
        }

        return documents[index].getBinaryValue(field);
    }

    public String getValue(ScoreDoc document, String field) throws CorruptIndexException, IOException {

        int index = document.doc;

        if (documents[index] == null) {
            documents[index] = reader.document(index);
        }

        return documents[index].get(field);

    }

    public String[] getValues(ScoreDoc document, String field) throws CorruptIndexException, IOException {

        int index = document.doc;

        if (documents[index] == null) {
            documents[index] = reader.document(index);
        }

        return documents[index].getValues(field);

    }

    public <T extends Identifier> Collection<T> getIdentifiers(IndexSearcher searcher, Query query, T identifier){

        Collection<T> identifiers = new ArrayList<T>();

        TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
        try {

            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            for (ScoreDoc document : hits) {
                T id = (T) identifier.newInstance();
                id.setAccession(getValue(document, NameService.IDENTIFIER_TERM.field()));
                identifiers.add(id);
            }
        } catch (IOException ex) {

        }

        return identifiers;
    }

    /**
     * Create a new query
     * @param name
     * @param term
     * @param fuzzy
     * @return
     */
    public Query create(String name, Term term, boolean fuzzy){
        Term searchTerm = term.createTerm(name);
        return fuzzy ? new FuzzyQuery(searchTerm, getMinSimilarity()) : new TermQuery(searchTerm);
    }

    /**
     * Convenience method provides the first value of the specified field for the given query.
     * If no documents are found an empty string is returned
     *
     * @param query
     * @param field
     * @return
     *
     */
    public String getFirstValue(IndexSearcher searcher, Query query, String field) {

        TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
        try {

            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            if (hits.length > 1) {
                System.err.println("expected only one result");
            }


            for (ScoreDoc document : hits) {
                return getValue(document, field);
            }
        } catch (IOException ex) {

        }

        return "";

    }

    public Directory getDirectory() {
        return directory;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }
    Pattern escape = Pattern.compile("[\\\\+\\-\\!\\(\\)\\:\\^\\]\\[\\{\\}\\~\\*\\?]");

    public String escape(String query) {
        return escape.matcher(query).replaceAll("\\\\$0");
    }
}
