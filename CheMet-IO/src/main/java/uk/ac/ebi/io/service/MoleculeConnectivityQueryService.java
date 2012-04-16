/**
 * ChEBIQueryService.java
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
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.chemet.resource.basic.BasicChemicalIdentifier;
import uk.ac.ebi.chemet.resource.chemical.ChemicalIdentifier;
import uk.ac.ebi.interfaces.services.LuceneService;
import uk.ac.ebi.io.remote.MoleculeCollectionConnectivity.MoleculeCollectionConnectivityLuceneFields;

/**
 *          UniProtQueryService - 2011.10.26 <br>
 *          Query service for UniProt identifiers. By default if gives a TrEMBLIdentifier.
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class MoleculeConnectivityQueryService
        extends AbstractQueryService {
    
    
    private static final Logger LOGGER = Logger.getLogger(MoleculeConnectivityQueryService.class);
    
    IndexSearcher searcher;

    public MoleculeConnectivityQueryService(LuceneService service) {
        super(service);
    }

    /**
     * Returns a generic BasicChemicalIdentifier. This method is here for some generic applications, but should be
     * overrided with a more accurate Identifier whenever possible.
     * 
     * @return a BasicChemicalIdentifier.
     */
    public ChemicalIdentifier getIdentifier() {
        return new BasicChemicalIdentifier();
    }

    public String wrapConnectivity(String connectivity) {
        return "\"" + connectivity + "\"";
    }
    
    public String getInChIConnectivity(ChemicalIdentifier identifier) {
        Query queryId = new TermQuery(new Term(MoleculeCollectionConnectivityLuceneFields.Identifier.toString(), handleChEBICase(identifier.getAccession())));
        Query queryDb = new TermQuery(new Term(MoleculeCollectionConnectivityLuceneFields.DB.toString(), handleChEBICase(identifier.getShortDescription())));
        
        BooleanQuery query = new BooleanQuery();
        query.add(queryId,Occur.MUST);
        query.add(queryDb,Occur.MUST);
        if(getCollectionQuery()!=null)
            query.add(getCollectionQuery(),Occur.MUST);
        
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
        if(shortDescription.contains("CHEBI"))
            return shortDescription.replaceFirst("CHEBI", "ChEBI");
        return shortDescription;
    }
    
    String searchGetConnectivity(Query query) {
        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                return getValue(scoreDoc, MoleculeCollectionConnectivityLuceneFields.Connectivity.toString());
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
