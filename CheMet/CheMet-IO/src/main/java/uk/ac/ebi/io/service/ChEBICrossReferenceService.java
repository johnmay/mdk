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
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.services.CrossReferenceQueryService;
import uk.ac.ebi.io.remote.ChEBICrossRefs;
import uk.ac.ebi.io.remote.ChEBICrossRefs.ChEBICrossRefsLuceneFields;
import uk.ac.ebi.io.remote.PubChemCompoundCrossRefs;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

/**
 *          ChEBINameService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChEBICrossReferenceService
        extends ChEBIQueryService
        implements CrossReferenceQueryService<ChEBIIdentifier>  {

    private static final Logger LOGGER = Logger.getLogger(ChEBICrossReferenceService.class);
    private IndexSearcher searcher;
    private static final IdentifierFactory FACTORY = IdentifierFactory.getInstance();

    private ChEBICrossReferenceService() {
        super(new ChEBICrossRefs());
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ChEBICrossReferenceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ChEBICrossReferenceService getInstance() {
        return ChEBICrossRefServiceHolder.INSTANCE;
    }

    public Collection<Identifier> getCrossReferences(ChEBIIdentifier identifier) {
        Query queryPubChemComp = new TermQuery(new Term(ChEBICrossRefsLuceneFields.ChebiID.toString(), identifier.getNumericPartOfAccession()));
        return search(queryPubChemComp);
    }

    public Collection<ChEBIIdentifier> getReverseCrossReferences(Identifier identifier) {
        Query queryExtDB = new TermQuery(new Term(ChEBICrossRefsLuceneFields.ExtDB.toString(), identifier.getShortDescription()));
        Query queryExtID = new TermQuery(new Term(ChEBICrossRefsLuceneFields.ExtID.toString(), identifier.getAccession()));
        
        BooleanQuery query = new BooleanQuery();
        query.add(queryExtDB, BooleanClause.Occur.MUST);
        query.add(queryExtID, BooleanClause.Occur.MUST);
        
        return reverseSearch(query);
    }

    private static class ChEBICrossRefServiceHolder {

        private static final ChEBICrossReferenceService INSTANCE = new ChEBICrossReferenceService();
    }

    private Collection<ChEBIIdentifier> reverseSearch(Query query) {
        Collection<ChEBIIdentifier> ids = new HashSet<ChEBIIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                ChEBIIdentifier ident = getIdentifier();
                ident.setAccession(getValue(scoreDoc, ChEBICrossRefsLuceneFields.ChebiID.toString()));
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
                Identifier ident = FACTORY.ofSynonym(getValue(scoreDoc, ChEBICrossRefsLuceneFields.ExtDB.toString()));
                ident.setAccession(getValue(scoreDoc, ChEBICrossRefsLuceneFields.ExtID.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query,ex);
        }

        return ids;
    }

}
