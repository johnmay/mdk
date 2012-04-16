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
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.interfaces.services.ChemicalConnectivityQueryService;
import uk.ac.ebi.io.remote.ChEBIMoleculeConnectivity;
import uk.ac.ebi.io.remote.ChEBISecondaryID2PrimaryID.ChEBISecondary2PrimaryLuceneFields;
import uk.ac.ebi.io.remote.MoleculeCollectionConnectivity;
import uk.ac.ebi.io.remote.MoleculeCollectionConnectivity.MoleculeCollectionConnectivityLuceneFields;
import uk.ac.ebi.resource.IdentifierFactory;

/**
 *          ChEBIMoleculeConnectivityService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChEBIMoleculeConnectivityService
        extends MoleculeConnectivityQueryService implements ChemicalConnectivityQueryService<ChEBIIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(ChEBIMoleculeConnectivityService.class);
    private static final IdentifierFactory FACTORY = IdentifierFactory.getInstance();
    private final Query collectionQuery;

    private ChEBIMoleculeConnectivityService() {
        super(new MoleculeCollectionConnectivity(ChEBIMoleculeConnectivity.COLLECTION));
        collectionQuery = new TermQuery(new Term(MoleculeCollectionConnectivityLuceneFields.CollectionName.toString(), ChEBIMoleculeConnectivity.COLLECTION));
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems initializing searcher", ex);
        }
    }

    public static ChEBIMoleculeConnectivityService getInstance() {
        return ChEBIMoleculeConnectivityServiceHolder.INSTANCE;
    }

    public Collection<ChEBIIdentifier> getEntriesWithConnectivity(String connectivity) {
        Query queryConnectivity = new TermQuery(new Term(MoleculeCollectionConnectivityLuceneFields.Connectivity.toString(), connectivity));
        
        BooleanQuery query = new BooleanQuery();
        query.add(queryConnectivity, Occur.MUST);
        query.add(collectionQuery, Occur.MUST);
        
        return reverseSearch(query);
    }

    @Override
    Query getCollectionQuery() {
        return collectionQuery;
    }
    @Override
    public String getInChIConnectivity(ChEBIIdentifier identifier) {
        return super.getInChIConnectivity(identifier);
    }

    private static class ChEBIMoleculeConnectivityServiceHolder {

        private static final ChEBIMoleculeConnectivityService INSTANCE = new ChEBIMoleculeConnectivityService();
    }

    private Collection<ChEBIIdentifier> reverseSearch(Query query) {
        Collection<ChEBIIdentifier> ids = new HashSet<ChEBIIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                ChEBIIdentifier ident = getIdentifier();
                ident.setAccession(getValue(scoreDoc, MoleculeCollectionConnectivityLuceneFields.Identifier.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }
    
    @Override
    public ChEBIIdentifier getIdentifier() {
        return new ChEBIIdentifier();
    }
    
    private ChEBIIdentifier search(Query query) {

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                ChEBIIdentifier ident = new ChEBIIdentifier();
                ident.setAccession(getValue(scoreDoc, ChEBISecondary2PrimaryLuceneFields.ChebiIDPrim.toString()));
                return ident;
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query,ex);
        }

        return null;
    }

}
