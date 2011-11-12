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
import uk.ac.ebi.io.remote.PubChemCompoundCrossRefs;
import uk.ac.ebi.io.remote.PubChemCompoundCrossRefs.PubChemCompoundsCrossRefsLuceneFields;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.resource.chemical.PubChemCompoundIdentifier;

/**
 *          ChEBINameService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class PubChemCompoundCrossReferenceService
        extends PubChemCompoundQueryService
        implements CrossReferenceQueryService<PubChemCompoundIdentifier>  {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundCrossReferenceService.class);
    private IndexSearcher searcher;
    private static final IdentifierFactory FACTORY = IdentifierFactory.getInstance();

    private PubChemCompoundCrossReferenceService() {
        super(new PubChemCompoundCrossRefs());
        setMaxResults(100);
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(PubChemCompoundCrossReferenceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static PubChemCompoundCrossReferenceService getInstance() {
        return PubChemCompoundServiceHolder.INSTANCE;
    }

    public Collection<Identifier> getCrossReferences(PubChemCompoundIdentifier identifier) {
        Query queryPubChemComp = new TermQuery(new Term(PubChemCompoundsCrossRefsLuceneFields.PubChemCompID.toString(), identifier.getAccession()));
        return search(queryPubChemComp);
    }

    public PubChemCompoundIdentifier getIdentifier() {
        return new PubChemCompoundIdentifier();
    }

    public Collection<PubChemCompoundIdentifier> getReverseCrossReferences(Identifier identifier) {
        Query queryExtDB = new TermQuery(new Term(PubChemCompoundsCrossRefsLuceneFields.ExtDB.toString(), identifier.getShortDescription()));
        Query queryExtID = new TermQuery(new Term(PubChemCompoundsCrossRefsLuceneFields.ExtID.toString(), identifier.getAccession()));
        
        BooleanQuery query = new BooleanQuery();
        query.add(queryExtDB, BooleanClause.Occur.MUST);
        query.add(queryExtID, BooleanClause.Occur.MUST);
        
        return reverseSearch(query);
    }

    private static class PubChemCompoundServiceHolder {

        private static final PubChemCompoundCrossReferenceService INSTANCE = new PubChemCompoundCrossReferenceService();
    }

    private Collection<PubChemCompoundIdentifier> reverseSearch(Query query) {
        Collection<PubChemCompoundIdentifier> ids = new HashSet<PubChemCompoundIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                PubChemCompoundIdentifier ident = getIdentifier();
                ident.setAccession(getValue(scoreDoc, PubChemCompoundsCrossRefsLuceneFields.PubChemCompID.toString()));
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
                Identifier ident = FACTORY.ofSynonym(getValue(scoreDoc, PubChemCompoundsCrossRefsLuceneFields.ExtDB.toString()));
                ident.setAccession(getValue(scoreDoc, PubChemCompoundsCrossRefsLuceneFields.ExtID.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }

}
