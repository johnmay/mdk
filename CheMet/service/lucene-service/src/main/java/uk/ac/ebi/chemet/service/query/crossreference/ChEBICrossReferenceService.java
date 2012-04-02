package uk.ac.ebi.chemet.service.query.crossreference;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.chemet.service.index.crossreference.ChEBICrossReferenceIndex;
import uk.ac.ebi.chemet.service.query.AbstractQueryService;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.service.query.CrossReferenceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * ChEBICrossReferenceService - 29.02.2012 <br/>
 * <p/>
 * Provides access to cross-references in ChEBI. Note that the current implementation only
 * works on primary identifiers
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBICrossReferenceService
        extends AbstractQueryService<ChEBIIdentifier>
        implements CrossReferenceService<ChEBIIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(ChEBICrossReferenceService.class);

    private Map<String,Class> indexMap = new HashMap<String,Class>();
    
    public ChEBICrossReferenceService() {
        super(new ChEBICrossReferenceIndex());
    }

    private Class getIdentifierClass(String index){

        // to save space we store the class name's in the index with a 'class-index' link

        if(indexMap.containsKey(index)){
            return indexMap.get(index);
        }

        Query  q    = new TermQuery(new Term("class-index", index));
        String name = firstValue(q, "class");
        try {
            Class c =  Class.forName(name);
            indexMap.put(index, c);
            return c;
        } catch (ClassNotFoundException ex){
            LOGGER.error("Could not find class for name " + name);
        }

        return null;

    }
    
    /**
     * @inheritDoc
     */
    @Override
    public Collection<? extends Identifier> getCrossReferences(ChEBIIdentifier identifier) {

        Query query = construct(identifier.getAccession(), IDENTIFIER);

        Collection<Identifier> crossreferences = new ArrayList<Identifier>();

        TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
        try {

            getSearcher().search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            for (ScoreDoc document : hits) {
                Class  c         = getIdentifierClass(value(document, DATABASE_IDENTIFIER_INDEX.field()));
                String accession = value(document,          DATABASE_ACCESSION.field());
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
    public <T extends Identifier> Collection<T> getCrossReferences(ChEBIIdentifier identifier, Class<T> filter) {

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
    public Collection<ChEBIIdentifier> searchCrossReferences(Identifier crossreference) {

        return getIdentifiers(construct(crossreference.getAccession(), DATABASE_ACCESSION));
        
    }

    /**
     * @inheritDoc
     */
    @Override
    public ChEBIIdentifier getIdentifier() {
        return new ChEBIIdentifier();
    }

}

