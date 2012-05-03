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
import java.util.Collection;
import java.util.HashSet;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.mdk.domain.identifier.TrEMBLIdentifier;
import uk.ac.ebi.mdk.domain.identifier.UniProtIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.deprecated.services.CrossReferenceQueryService;
import uk.ac.ebi.io.remote.UniProtCrossRefs;
import uk.ac.ebi.io.remote.UniProtCrossRefs.UniprotCrossRefsLuceneFields;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;

/**
 *          UniProtCrossReferenceService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class UniProtCrossReferenceService
        extends UniProtQueryService
        implements CrossReferenceQueryService<UniProtIdentifier>  {

    private static final Logger LOGGER = Logger.getLogger(UniProtCrossReferenceService.class);
    private IndexSearcher searcher;
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    private UniProtCrossReferenceService() {
        super(new UniProtCrossRefs());
        setMaxResults(100);
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems initializing searcher", ex);
        }
    }

    public static UniProtCrossReferenceService getInstance() {
        return UniProtCrossReferenceServiceHolder.INSTANCE;
    }

    public Collection<Identifier> getCrossReferences(UniProtIdentifier identifier) {
        Query queryPubChemComp = new TermQuery(new Term(UniprotCrossRefsLuceneFields.UniprotAcc.toString(), identifier.getAccession()));
        return search(queryPubChemComp);
    }

    @Override
    public UniProtIdentifier getIdentifier() {
        return new TrEMBLIdentifier();
    }

    public Collection<UniProtIdentifier> getReverseCrossReferences(Identifier identifier) {
        Query queryExtDB = new TermQuery(new Term(UniprotCrossRefsLuceneFields.ExtDB.toString(), identifier.getShortDescription()));
        Query queryExtID = new TermQuery(new Term(UniprotCrossRefsLuceneFields.ExtID.toString(), identifier.getAccession()));
        
        BooleanQuery query = new BooleanQuery();
        query.add(queryExtDB, BooleanClause.Occur.MUST);
        query.add(queryExtID, BooleanClause.Occur.MUST);
        
        return reverseSearch(query);
    }

    private static class UniProtCrossReferenceServiceHolder {

        private static final UniProtCrossReferenceService INSTANCE = new UniProtCrossReferenceService();
    }

    private Collection<UniProtIdentifier> reverseSearch(Query query) {
        Collection<UniProtIdentifier> ids = new HashSet<UniProtIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                UniProtIdentifier ident = getIdentifier();
                ident.setAccession(getValue(scoreDoc, UniprotCrossRefsLuceneFields.UniprotAcc.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }
    
    private Collection<Identifier> search(Query query) {
        Collection<Identifier> ids = new HashSet<Identifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                Identifier ident = FACTORY.ofSynonym(getValue(scoreDoc, UniprotCrossRefsLuceneFields.ExtDB.toString()));
                ident.setAccession(getValue(scoreDoc, UniprotCrossRefsLuceneFields.ExtID.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }

}
