/**
 * ChEBISecondaryToPrimaryIDQuery.java
 *
 * 2013.02.28
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with CheMet. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.service.query.secondaryresolver;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.service.AbstractService;
import uk.ac.ebi.mdk.service.index.other.ChEBISecondaryToPrimaryIDIndex;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;

/**
 * @name ChEBISecondaryToPrimaryIDQuery
 * @date 2013.02.28
 * @version $Rev$ : Last Changed $Date$
 * @author pmoreno
 * @author $Author$ (this version)
 * @brief ...class description...
 *
 */
public class ChEBISecondaryToPrimaryIDQuery extends AbstractLuceneService<ChEBIIdentifier> implements SecondaryToPrimaryIDResolverService<ChEBIIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(ChEBISecondaryToPrimaryIDQuery.class);
    
    public ChEBISecondaryToPrimaryIDQuery() {
        super(new ChEBISecondaryToPrimaryIDIndex());
    }

    @Override
    public ChEBIIdentifier getPrimaryID(ChEBIIdentifier secondaryIdent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<ChEBIIdentifier> getSecondaryIDsForPrimaryID(ChEBIIdentifier primaryIdentifier) {
        Query query = new TermQuery(new Term(ChEBISecondary2PrimaryLuceneFields.ChebiIDPrim.toString(), primaryIdentifier.getAccession()));

        return reverseSearch(query);
    }

    private Collection<ChEBIIdentifier> reverseSearch(Query query) {
        Collection<ChEBIIdentifier> ids = new HashSet<ChEBIIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            getSearcher().search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                ChEBIIdentifier ident = getIdentifier();
                ident.setAccession(value(scoreDoc, ChEBISecondary2PrimaryLuceneFields.ChebiIDSec.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }

    @Override
    public ChEBIIdentifier getIdentifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum ChEBISecondary2PrimaryLuceneFields {

        ChebiIDSec, ChebiIDPrim;
    }
}
