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

import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.deprecated.services.SecondaryToPrimaryIDResolverService;
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
import uk.ac.ebi.io.remote.PubChemComp2ParentCompound;
import uk.ac.ebi.io.remote.PubChemComp2ParentCompound.PubChemComp2ParentComp;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;

/**
 *          PubChemComp2ParentCompoundResolverService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class PubChemComp2ParentCompoundResolverService
        extends PubChemCompoundQueryService implements SecondaryToPrimaryIDResolverService<PubChemCompoundIdentifier>{

    private static final Logger LOGGER = Logger.getLogger(PubChemComp2ParentCompoundResolverService.class);
    private IndexSearcher searcher;
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    private PubChemComp2ParentCompoundResolverService() {
        super(new PubChemComp2ParentCompound());
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems initializing searcher", ex);
        }
    }

    public static PubChemComp2ParentCompoundResolverService getInstance() {
        return ChEBISecondary2PrimaryIDServiceHolder.INSTANCE;
    }

    /**
     * The method takes an identifier, presumed to be a secondary ChEBI Identifier, looks for a primary id associated in the
     * index and returns it. If the identifier is a primary identifier, the same identifier is returned.
     * @param secondaryIdent
     * @return primaryIdentifier
     */
    @Override
    public PubChemCompoundIdentifier getPrimaryID(PubChemCompoundIdentifier secondaryIdent) {
        Query queryPubChemComp = new TermQuery(new Term(PubChemComp2ParentComp.PubChemCompID.toString(), secondaryIdent.getAccession()));
        PubChemCompoundIdentifier primary =  search(queryPubChemComp);
        if(primary==null)
            return secondaryIdent;
        else
            return primary;
    }

    @Override
    public Collection<PubChemCompoundIdentifier> getSecondaryIDsForPrimaryID(PubChemCompoundIdentifier primaryIdentifier) {
        Query query = new TermQuery(new Term(PubChemComp2ParentComp.ParentPChemCompID.toString(), primaryIdentifier.getAccession()));
        
        return reverseSearch(query);
    }

    private static class ChEBISecondary2PrimaryIDServiceHolder {

        private static final PubChemComp2ParentCompoundResolverService INSTANCE = new PubChemComp2ParentCompoundResolverService();
    }

    private Collection<PubChemCompoundIdentifier> reverseSearch(Query query) {
        Collection<PubChemCompoundIdentifier> ids = new HashSet<PubChemCompoundIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                PubChemCompoundIdentifier ident = getIdentifier();
                ident.setAccession(getValue(scoreDoc, PubChemComp2ParentComp.PubChemCompID.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }
    
    private PubChemCompoundIdentifier search(Query query) {

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                PubChemCompoundIdentifier ident = new PubChemCompoundIdentifier();
                ident.setAccession(getValue(scoreDoc, PubChemComp2ParentComp.ParentPChemCompID.toString()));
                return ident;
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query,ex);
        }

        return null;
    }

}
