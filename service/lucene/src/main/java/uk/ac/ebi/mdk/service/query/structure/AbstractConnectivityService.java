/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.service.query.structure;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.loader.single.MoleculeCollectionConnectivityLoader.MoleculeCollectionConnectivityLuceneFields;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;

/**
 *          
 * @version $Rev: 1915 $ : Last Changed $Date: 2012-04-02 15:17:20 +0100 (Mon, 02 Apr 2012) $
 * @author  pmoreno
 * @author  $Author: johnmay $ (this version)
 */
public abstract class AbstractConnectivityService <I extends Identifier>
        extends AbstractLuceneService<I> implements ConnectivityService<I> {
    
    
    private static final Logger LOGGER = Logger.getLogger(AbstractConnectivityService.class);
    
    IndexSearcher searcher;

    public AbstractConnectivityService(LuceneIndex service) {
        super(service);
    }

    public String wrapConnectivity(String connectivity) {
        return "\"" + connectivity + "\"";
    }
    
    @Override
    public String getConnectivity(I identifier) {
        Query queryId = new TermQuery(new Term(MoleculeCollectionConnectivityLuceneFields.Identifier.toString(), handleChEBICase(identifier.getAccession())));
        Query queryDb = new TermQuery(new Term(MoleculeCollectionConnectivityLuceneFields.DB.toString(), handleChEBICase(identifier.getShortDescription())));
        
        BooleanQuery query = new BooleanQuery();
        query.add(queryId,Occur.MUST);
        query.add(queryDb,Occur.MUST);
        if(getCollectionQuery()!=null) {
            query.add(getCollectionQuery(),Occur.MUST);
        }
        
        return searchGetConnectivity(query);
    }

    /**
     * Very embarrasing method that should be removed promptly. When all the connectivity indexes were built, the default
     * short description for ChEBI was ChEBI, but was changed later to CHEBI, so all the searches for CHEBI would fail
     * in the existing inchis. At some point we should rebuild the connectivity indexes and get rid of these.
     * 
     * TODO rebuild the connectivity indexes and get rid of these 
     * 
     * @param shortDescription
     * @return 
     */
    private String handleChEBICase(String shortDescription) {
        if(shortDescription.contains("CHEBI")) {
            return shortDescription.replaceFirst("CHEBI", "ChEBI");
        }
        return shortDescription;
    }
    
    String searchGetConnectivity(Query query) {
        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                return value(scoreDoc, MoleculeCollectionConnectivityLuceneFields.Connectivity.toString());
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query, ex);
        }

        return null;
    }

    
    /**
     * This method should be overrided when ever possible.
     * @return 
     */
    Query getCollectionQuery() {
        return null;
    }
}
