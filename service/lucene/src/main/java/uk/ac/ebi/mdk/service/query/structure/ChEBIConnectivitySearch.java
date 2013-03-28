/**
 * ChEBIConnectivitySearch.java
 *
 * 2013.03.01
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

package uk.ac.ebi.mdk.service.query.structure;


import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.service.index.other.MoleculeCollectionConnectivityIndex;
import uk.ac.ebi.mdk.service.loader.single.MoleculeCollectionConnectivityLoader;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;

/**
 * @name    ChEBIConnectivitySearch
 * @date    2013.03.01
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class ChEBIConnectivitySearch extends AbstractLuceneService<ChEBIIdentifier> implements ConnectivitySearch<ChEBIIdentifier> {

    private static final Logger LOGGER = Logger.getLogger( ChEBIConnectivitySearch.class );

    private final Query collectionQuery;
    private static final String COLLECTION = "ChEBI";

    public ChEBIConnectivitySearch() {
        super(new MoleculeCollectionConnectivityIndex(COLLECTION));
        collectionQuery = new TermQuery(new Term(MoleculeCollectionConnectivityLoader.MoleculeCollectionConnectivityLuceneFields.CollectionName.toString(), COLLECTION));
    }

    @Override
    public Collection<ChEBIIdentifier> connectivitySearch(String connectivity) {
        Query queryConnectivity = new TermQuery(new Term(MoleculeCollectionConnectivityLoader.MoleculeCollectionConnectivityLuceneFields.Connectivity.toString(), connectivity));

        BooleanQuery query = new BooleanQuery();
        query.add(queryConnectivity, BooleanClause.Occur.MUST);
        query.add(collectionQuery, BooleanClause.Occur.MUST);

        return reverseSearch(query);
    }

    private Collection<ChEBIIdentifier> reverseSearch(Query query) {
        Collection<ChEBIIdentifier> ids = new HashSet<ChEBIIdentifier>();

        try {
            ScoreDoc[] hits = search(query);
            for (ScoreDoc scoreDoc : hits) {
                ChEBIIdentifier ident = getIdentifier();
                ident.setAccession(value(scoreDoc, MoleculeCollectionConnectivityLoader.MoleculeCollectionConnectivityLuceneFields.Identifier.toString()));
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


}
