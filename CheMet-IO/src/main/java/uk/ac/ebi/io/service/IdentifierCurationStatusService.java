/**
 * ChEBINameService.java
 *
 * 2011.10.26
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.io.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.io.remote.IdentifierCurationStatus;
import uk.ac.ebi.io.remote.IdentifierCurationStatus.CurationStatus;
import uk.ac.ebi.io.remote.IdentifierCurationStatus.IdentifierPropertiesLuceneFields;
import uk.ac.ebi.resource.DefaultIdentifierFactory;

/**
 *          PubChemCompoundCrossReferenceService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class IdentifierCurationStatusService
        extends IdentifierCurationStatusQueryService {

    private static final Logger LOGGER = Logger.getLogger(IdentifierCurationStatusService.class);
    private IndexSearcher searcher;
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    private IdentifierCurationStatusService() {
        super(new IdentifierCurationStatus());
        setMaxResults(1);
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems loading index searcher", ex);
        }
    }

    public static IdentifierCurationStatusService getInstance() {
        return IdentifierCurationStatusServiceHolder.INSTANCE;
    }

    public String getManualCuration(Identifier identifier, String collection) {
        Query curationTypeQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.CurationType.toString(), CurationStatus.Manually.toString()));
        Query collectionQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.CollectionName.toString(), collection));
        Query extDBQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.ExtDB.toString(), identifier.getShortDescription()));
        Query extIDQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.ExtID.toString(), identifier.getAccession()));
        BooleanQuery query = new BooleanQuery();
        query.add(new BooleanClause(extIDQuery, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(extDBQuery, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(collectionQuery, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(curationTypeQuery, BooleanClause.Occur.MUST));
        return search(query);
    }

    public String getSemiManualCuration(Identifier identifier, String collection) {
        Query curationTypeQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.CurationType.toString(), CurationStatus.SemiManual.toString()));
        Query collectionQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.CollectionName.toString(), collection));
        Query extDBQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.ExtDB.toString(), identifier.getShortDescription()));
        Query extIDQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.ExtID.toString(), identifier.getAccession()));
        BooleanQuery query = new BooleanQuery();
        query.add(new BooleanClause(extIDQuery, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(extDBQuery, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(collectionQuery, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(curationTypeQuery, BooleanClause.Occur.MUST));
        return search(query);
    }

    public String getAutomaticCuration(Identifier identifier, String collection) {
        Query curationTypeQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.CurationType.toString(), CurationStatus.Automatic.toString()));
        Query collectionQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.CollectionName.toString(), collection));
        Query extDBQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.ExtDB.toString(), identifier.getShortDescription()));
        Query extIDQuery = new TermQuery(new Term(IdentifierPropertiesLuceneFields.ExtID.toString(), identifier.getAccession()));
        BooleanQuery query = new BooleanQuery();
        query.add(new BooleanClause(extIDQuery, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(extDBQuery, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(collectionQuery, BooleanClause.Occur.MUST));
        query.add(new BooleanClause(curationTypeQuery, BooleanClause.Occur.MUST));
        return search(query);
    }

    private static class IdentifierCurationStatusServiceHolder {

        private static final IdentifierCurationStatusService INSTANCE = new IdentifierCurationStatusService();
    }

    private String search(Query query) {
        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if(hits.length>0)
                return getValue(hits[0], IdentifierPropertiesLuceneFields.CurationValue.toString());
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }
        return "";
    }
}
