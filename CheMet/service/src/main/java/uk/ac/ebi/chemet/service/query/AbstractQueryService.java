package uk.ac.ebi.chemet.service.query;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.QueryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
public abstract class AbstractQueryService<I extends Identifier>
        implements QueryService<I> {

    private static final Logger LOGGER = Logger.getLogger(AbstractQueryService.class);

    private Document[] documents;
    private Directory directory;
    private Analyzer analyzer;
    private IndexReader reader;
    private IndexSearcher searcher;

    private int max = Preferences.userNodeForPackage(AbstractQueryService.class).getInt("default.max.results", 100);
    private float minSimilarity = 0.5f; // for fuzzy queries


    public AbstractQueryService(LuceneIndex index) {
        try {
            analyzer = index.getAnalyzer();
            setDirectory(index.getDirectory());
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Could not create query service");
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setMinSimilarity(float minSimilarity) {
        this.minSimilarity = minSimilarity;
    }

    public float getMinSimilarity() {
        return minSimilarity;
    }

    public void setDirectory(Directory directory) throws IOException {
        if (reader != null) {
            reader.close();
        }
        this.directory = directory;
        reader = IndexReader.open(directory, true);
        documents = new Document[reader.numDocs()];
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override

    public void setMaxResults(int max) {
        this.max = max;
    }

    public int getMaxResults() {
        return max;
    }

    public byte[] getBinaryValue(ScoreDoc document, String field) throws IOException {
        int index = document.doc;

        if (documents[index] == null) {
            documents[index] = reader.document(index);
        }

        return documents[index].getBinaryValue(field);
    }

    public String getValue(ScoreDoc document, String field) throws IOException {

        int index = document.doc;

        if (documents[index] == null) {
            documents[index] = reader.document(index);
        }

        return documents[index].get(field);

    }

    public String[] getValues(ScoreDoc document, String field) throws IOException {

        int index = document.doc;

        if (documents[index] == null) {
            documents[index] = reader.document(index);
        }

        return documents[index].getValues(field);

    }

    public Collection<I> getIdentifiers(Query query) {

        Collection<I> identifiers = new ArrayList<I>();

        TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
        try {

            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            for (ScoreDoc document : hits) {
                I id = getIdentifier();
                id.setAccession(getValue(document, IDENTIFIER.field()));
                identifiers.add(id);
            }
        } catch (IOException ex) {

        }

        return identifiers;
    }

    /**
     * Create a new query
     *
     * @param text
     * @param term
     * @param fuzzy
     *
     * @return
     */
    public Query create(String text, Term term, boolean fuzzy) {
        Term searchTerm = term.createTerm(text);
        return fuzzy ? new FuzzyQuery(searchTerm, getMinSimilarity()) : new TermQuery(searchTerm);
    }

    public Query create(String text, Term term) {
        return create(text, term, false);
    }

    public String getFirstValue(Identifier identifier, Term term){
        return getFirstValue(create(identifier.getAccession(), IDENTIFIER), term.field());
    }
    public String getFirstValue(Query query, Term term){
        return getFirstValue(query, term.field());
    }

    /**
     * Convenience method provides the first value of the specified field for the given query.
     * If no documents are found an empty string is returned
     *
     * @param query
     * @param field
     *
     * @return
     */
    public String getFirstValue(Query query, String field) {

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
            LOGGER.error("IO Exception occurred on service: " + ex.getMessage());
        }

        return "";

    }

    public Collection<String> getValues(Query query, Term term){
        return getValues(query, term.field());
    }

    public Collection<String> getValues(Query query, String field){

        Collection<String> values = new ArrayList<String>();

        TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
        try {

            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            if (hits.length > 1) {
                System.err.println("expected only one result");
            }


            for (ScoreDoc document : hits) {
                values.add(getValue(document, field));
            }
        } catch (IOException ex) {
            LOGGER.error("IO Exception occurred on service: " + ex.getMessage());
        }

        return values;
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
