package uk.ac.ebi.chemet.service.query.crossreference;

import org.apache.log4j.Logger;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.chemet.service.query.AbstractQueryService;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.CrossReferenceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractCrossreferenceService<I extends Identifier>
        extends AbstractQueryService<I>
        implements CrossReferenceService<I> {

    private static final Logger LOGGER = Logger.getLogger(AbstractCrossreferenceService.class);

    private Map<String, Class> indexMap = new HashMap<String, Class>();


    public AbstractCrossreferenceService(LuceneIndex index) {
        super(index);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<? extends Identifier> getCrossReferences(I identifier) {

        Query query = construct(identifier.getAccession(), IDENTIFIER);

        Collection<Identifier> crossreferences = new ArrayList<Identifier>();

        TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
        try {

            getSearcher().search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            for (ScoreDoc document : hits) {
                Class c = getIdentifierClass(value(document, DATABASE_IDENTIFIER_INDEX.field()));
                String accession = value(document, DATABASE_ACCESSION.field());
                crossreferences.add(IdentifierFactory.getInstance().ofClass(c, accession));
            }
        } catch (IOException ex) {
            LOGGER.error("IO Exception occurred on service: " + ex.getMessage());
        }

        return crossreferences;
    }


    /**
     * @inheritDoc
     */
    @Override
    public <T extends Identifier> Collection<T> getCrossReferences(I identifier, Class<T> filter) {

        // method could be improved by searching for the specified identifier index but this current
        // implementation will preserve identifier inheritance
        Collection<? extends Identifier> crossreferences = getCrossReferences(identifier);
        Collection<T> subset = new ArrayList<T>();

        for (Identifier crossreference : crossreferences) {
            if (filter.isInstance(crossreference)) {
                subset.add((T) crossreference);
            }
        }

        return subset;

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<I> searchCrossReferences(Identifier crossreference) {
        return getIdentifiers(construct(crossreference.getAccession(), DATABASE_ACCESSION));
    }


    public Class getIdentifierClass(String id) {

        // to save space we store the class name's in the index with a 'class-index' link

        if (indexMap.containsKey(id)) {
            return indexMap.get(id);
        }

        Query q = new TermQuery(CLASS_ID.createTerm(id));
        String name = firstValue(q, CLASS_NAME);
        try {
            Class c = Class.forName(name);
            indexMap.put(id, c);
            return c;
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Could not find class for name " + name);
        }

        return null;

    }

}
