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
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.io.remote.UniProtECNumber2OrganismProtein;
import uk.ac.ebi.io.remote.UniProtECNumber2OrganismProtein.UniProtECNumber2OrgProtLuceneFields;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.resource.organism.Taxonomy;
import uk.ac.ebi.resource.protein.UniProtIdentifier;
import uk.ac.ebi.resource.protein.TrEMBLIdentifier;

/**
 *          UniProtCrossReferenceService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class UniProtECNumber2OrganismProteinService
        extends AbstractQueryService  {

    private static final Logger LOGGER = Logger.getLogger(UniProtECNumber2OrganismProteinService.class);
    private IndexSearcher searcher;
    private static final IdentifierFactory FACTORY = IdentifierFactory.getInstance();

    private UniProtECNumber2OrganismProteinService() {
        super(new UniProtECNumber2OrganismProtein());
        setMaxResults(100);
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems initializing searcher", ex);
        }
    }

    public static UniProtECNumber2OrganismProteinService getInstance() {
        return UniProtECNumber2OrganismProtServiceHolder.INSTANCE;
    }

    public Collection<Identifier> getECNumbers(UniProtIdentifier identifier) {
        Query queryUniprotID = new TermQuery(new Term(UniProtECNumber2OrgProtLuceneFields.UniprotAcc.toString(), identifier.getAccession()));
        return search(queryUniprotID);
    }

    public UniProtIdentifier getIdentifier() {
        return new TrEMBLIdentifier();
    }

    public Collection<UniProtIdentifier> getUniProtIdentifiersForECNumberOrganism(ECNumber ecIdentifier, Taxonomy taxIdentifier) {
        Query queryFamily = new TermQuery(new Term(UniProtECNumber2OrgProtLuceneFields.ECNumber.toString(), ecIdentifier.getAccession()));
        Query queryTax = new TermQuery(new Term(UniProtECNumber2OrgProtLuceneFields.TaxID.toString(), taxIdentifier.getAccession()));
        
        BooleanQuery query = new BooleanQuery();
        query.add(queryFamily, BooleanClause.Occur.MUST);
        query.add(queryTax, BooleanClause.Occur.MUST);
        
        return reverseSearch(query);
    }

    private static class UniProtECNumber2OrganismProtServiceHolder {

        private static final UniProtECNumber2OrganismProteinService INSTANCE = new UniProtECNumber2OrganismProteinService();
    }

    private Collection<UniProtIdentifier> reverseSearch(Query query) {
        Collection<UniProtIdentifier> ids = new HashSet<UniProtIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                UniProtIdentifier ident = getIdentifier();
                ident.setAccession(getValue(scoreDoc, UniProtECNumber2OrgProtLuceneFields.UniprotAcc.toString()));
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
                // this should be with values.
                for (String value : getValues(scoreDoc, UniProtECNumber2OrgProtLuceneFields.ECNumber.toString())) {
                    ECNumber ecIdent = new ECNumber();
                    ecIdent.setAccession(value);
                    ids.add(ecIdent);
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }

}
