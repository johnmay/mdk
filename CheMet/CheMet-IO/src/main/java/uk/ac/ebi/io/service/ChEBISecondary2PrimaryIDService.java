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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.io.remote.ChEBISecondaryID2PrimaryID;
import uk.ac.ebi.io.remote.ChEBISecondaryID2PrimaryID.ChEBISecondary2PrimaryLuceneFields;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

/**
 *          ChEBINameService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChEBISecondary2PrimaryIDService
        extends ChEBIQueryService{

    private static final Logger LOGGER = Logger.getLogger(ChEBISecondary2PrimaryIDService.class);
    private IndexSearcher searcher;
    private static final IdentifierFactory FACTORY = IdentifierFactory.getInstance();

    private ChEBISecondary2PrimaryIDService() {
        super(new ChEBISecondaryID2PrimaryID());
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems initializing searcher", ex);
        }
    }

    public static ChEBISecondary2PrimaryIDService getInstance() {
        return ChEBISecondary2PrimaryIDServiceHolder.INSTANCE;
    }

    /**
     * The method takes an identifier, presumed to be a secondary ChEBI Identifier, looks for a primary id associated in the
     * index and returns it. If the identifier is a primary identifier, the same identifier is returned.
     * @param secondaryIdent
     * @return primaryIdentifier
     */
    public ChEBIIdentifier getPrimaryChEBIID(ChEBIIdentifier secondaryIdent) {
        Query queryPubChemComp = new TermQuery(new Term(ChEBISecondary2PrimaryLuceneFields.ChebiIDSec.toString(), secondaryIdent.getNumericPartOfAccession()));
        ChEBIIdentifier primary =  search(queryPubChemComp);
        if(primary==null)
            return secondaryIdent;
        else
            return primary;
    }

    public Collection<ChEBIIdentifier> getSecondaryIDsForPrimaryID(ChEBIIdentifier primaryIdentifier) {
        Query query = new TermQuery(new Term(ChEBISecondary2PrimaryLuceneFields.ChebiIDPrim.toString(), primaryIdentifier.getAccession()));
        
        return reverseSearch(query);
    }

    private static class ChEBISecondary2PrimaryIDServiceHolder {

        private static final ChEBISecondary2PrimaryIDService INSTANCE = new ChEBISecondary2PrimaryIDService();
    }

    private Collection<ChEBIIdentifier> reverseSearch(Query query) {
        Collection<ChEBIIdentifier> ids = new HashSet<ChEBIIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                ChEBIIdentifier ident = getIdentifier();
                ident.setAccession(getValue(scoreDoc, ChEBISecondary2PrimaryLuceneFields.ChebiIDSec.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
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
