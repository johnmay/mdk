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
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.interfaces.services.ChemicalNameQueryService;
import uk.ac.ebi.io.remote.ChEBINames;

/**
 *          ChEBINameService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @deprecated use new service in chemet-servicer: ChEBINameService
 */
@Deprecated
public class ChEBINameService
        extends ChEBIQueryService
        implements ChemicalNameQueryService<ChEBIIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(ChEBINameService.class);
    private IndexSearcher searcher;
    private Term idTerm = new Term("id");
    private Term nameTerm = new Term("name");
    private Term typeTerm = new Term(ChEBINames.ChEBINameLuceneFields.type.toString());

    private ChEBINameService() {
        super(new ChEBINames());
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ChEBINameService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ChEBINameService getInstance() {
        return ChEBINameServiceHolder.INSTANCE;
    }

    public String getIUPACName(ChEBIIdentifier identifier) {
        try {
            Query queryId = new TermQuery(idTerm.createTerm("CHEBI:" + identifier.getNumericPartOfAccession()));
            Query queryType = new TermQuery(typeTerm.createTerm("IUPAC NAME"));

            BooleanQuery query = new BooleanQuery();
            query.add(queryId, Occur.MUST);
            query.add(queryType, Occur.MUST);

            TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length > 0) {
                return getValue(hits[0], ChEBINames.ChEBINameLuceneFields.name.toString());
            }
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }

    public Collection<ChEBIIdentifier> searchForNameExcludeSynonyms(String name) {
        Query qName = new TermQuery(nameTerm.createTerm(name));
        Query qTypeName = new TermQuery(typeTerm.createTerm("NAME"));
        Query qTypeIUPAC = new TermQuery(typeTerm.createTerm("IUPAC NAME"));

        BooleanQuery query = new BooleanQuery();
        query.add(qName, Occur.MUST);
        BooleanQuery subQueryType = new BooleanQuery();
        subQueryType.add(qTypeName, Occur.SHOULD);
        subQueryType.add(qTypeIUPAC, Occur.SHOULD);
        query.add(subQueryType, Occur.MUST);
        
        return search(query);
    }

    private static class ChEBINameServiceHolder {

        private static final ChEBINameService INSTANCE = new ChEBINameService();
    }

    public Collection<ChEBIIdentifier> fuzzySearchForName(String name) {
        Query q = new FuzzyQuery(nameTerm.createTerm(name));
        return search(q);
    }

    public Collection<ChEBIIdentifier> searchForName(String name) {
        Query q = new TermQuery(nameTerm.createTerm(name));
        return search(q);
    }

    private Collection<ChEBIIdentifier> search(Query query) {
        Collection<ChEBIIdentifier> ids = new HashSet<ChEBIIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                ids.add(new ChEBIIdentifier(getValue(scoreDoc, "id")));
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }

    public String getPreferredName(ChEBIIdentifier identifier) {
        try {
            Query queryId = new TermQuery(idTerm.createTerm("CHEBI:" + identifier.getNumericPartOfAccession()));
            Query queryType = new TermQuery(typeTerm.createTerm("NAME"));

            BooleanQuery query = new BooleanQuery();
            query.add(queryId, Occur.MUST);
            query.add(queryType, Occur.MUST);

            TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length > 0) {
                return getValue(hits[0], ChEBINames.ChEBINameLuceneFields.name.toString());
            }
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }

    public Collection<String> getSynonyms(ChEBIIdentifier identifier) {
        try {
            Query queryId = new TermQuery(idTerm.createTerm("CHEBI:" + identifier.getNumericPartOfAccession()));
            Query queryType = new TermQuery(typeTerm.createTerm("SYNONYM"));

            BooleanQuery query = new BooleanQuery();
            query.add(queryId, Occur.MUST);
            query.add(queryType, Occur.MUST);

            TopScoreDocCollector collector = TopScoreDocCollector.create(50, true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            Collection<String> synonyms = new HashSet<String>(hits.length);
            for (ScoreDoc scoreDoc : hits) {
                synonyms.add(getValue(scoreDoc, ChEBINames.ChEBINameLuceneFields.name.toString()));
            }
            return synonyms;
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }

    public Collection<String> getNames(ChEBIIdentifier identifier) {
        try {
            Query q = new TermQuery(idTerm.createTerm("CHEBI:" + identifier.getNumericPartOfAccession()));

            TopScoreDocCollector collector = TopScoreDocCollector.create(20, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            //if (hits.length > 1) {
            //    LOGGER.info("more then one hit found for an id! this shouldn't happen");
            //}
            Collection<String> names = new HashSet<String>(hits.length);
            for (ScoreDoc scoreDoc : hits) {
                names.add(getValue(scoreDoc, ChEBINames.ChEBINameLuceneFields.name.toString()));
                //return Arrays.asList(getValues(scoreDoc, "name"));
            }
            return names;
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }
}
