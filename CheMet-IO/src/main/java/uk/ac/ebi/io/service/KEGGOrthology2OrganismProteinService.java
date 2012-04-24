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
import uk.ac.ebi.chemet.resource.classification.KEGGOrthology;
import uk.ac.ebi.chemet.resource.protein.TrEMBLIdentifier;
import uk.ac.ebi.chemet.resource.protein.UniProtIdentifier;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.io.remote.KEGGOrthology2OrganismProtein;
import uk.ac.ebi.io.remote.KEGGOrthology2OrganismProtein.KEGGOrthologyOrgProtLuceneFields;
import uk.ac.ebi.resource.DefaultIdentifierFactory;
import uk.ac.ebi.resource.organism.Taxonomy;

/**
 *          UniProtCrossReferenceService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class KEGGOrthology2OrganismProteinService
        extends AbstractQueryService  {

    private static final Logger LOGGER = Logger.getLogger(KEGGOrthology2OrganismProteinService.class);
    private IndexSearcher searcher;
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    private KEGGOrthology2OrganismProteinService() {
        super(new KEGGOrthology2OrganismProtein());
        setMaxResults(100);
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems initializing searcher", ex);
        }
    }

    public static KEGGOrthology2OrganismProteinService getInstance() {
        return KEGGOrthology2OrganismProtServiceHolder.INSTANCE;
    }

    public Collection<Identifier> getKEGGKOFamilies(UniProtIdentifier identifier) {
        Query queryUniprotID = new TermQuery(new Term(KEGGOrthologyOrgProtLuceneFields.UniprotAcc.toString(), identifier.getAccession()));
        return search(queryUniprotID);
    }

    public UniProtIdentifier getIdentifier() {
        return new TrEMBLIdentifier();
    }

    public Collection<UniProtIdentifier> getUniProtIdentifiersForProteinFamilyOrganism(KEGGOrthology familyIdentifier, Taxonomy taxIdentifier) {
        Query queryFamily = new TermQuery(new Term(KEGGOrthologyOrgProtLuceneFields.KEGGOrthologyFamily.toString(), familyIdentifier.getAccession()));
        Query queryTax = new TermQuery(new Term(KEGGOrthologyOrgProtLuceneFields.TaxID.toString(), taxIdentifier.getAccession()));
        
        BooleanQuery query = new BooleanQuery();
        query.add(queryFamily, BooleanClause.Occur.MUST);
        query.add(queryTax, BooleanClause.Occur.MUST);
        
        return reverseSearch(query);
    }

    private static class KEGGOrthology2OrganismProtServiceHolder {

        private static final KEGGOrthology2OrganismProteinService INSTANCE = new KEGGOrthology2OrganismProteinService();
    }

    private Collection<UniProtIdentifier> reverseSearch(Query query) {
        Collection<UniProtIdentifier> ids = new HashSet<UniProtIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                UniProtIdentifier ident = getIdentifier();
                ident.setAccession(getValue(scoreDoc, KEGGOrthologyOrgProtLuceneFields.UniprotAcc.toString()));
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
                for (String value : getValues(scoreDoc, KEGGOrthologyOrgProtLuceneFields.KEGGOrthologyFamily.toString())) {
                    KEGGOrthology orthIdent = new KEGGOrthology(value);
                    ids.add(orthIdent);
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }

}
