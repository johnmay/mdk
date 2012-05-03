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
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.deprecated.services.ChemicalNameQueryService;
import uk.ac.ebi.io.remote.PubChemCompoundNames;
import uk.ac.ebi.io.remote.PubChemCompoundNames.PCCompNamesLuceneFields;
import uk.ac.ebi.io.remote.PubChemCompoundNames.PCCompNameTypes;

/**
 *          PubChemCompoundNameService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class PubChemCompoundNameService
        extends PubChemCompoundQueryService
            implements ChemicalNameQueryService<PubChemCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundNameService.class);
    private IndexSearcher searcher;
    private Term idTerm = new Term(PCCompNamesLuceneFields.PubChemCompID.toString());
    private Term nameTerm = new Term(PCCompNamesLuceneFields.Name.toString());
    private Term typeTerm = new Term(PCCompNamesLuceneFields.Type.toString());

    private PubChemCompoundNameService() {
        super(new PubChemCompoundNames());
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(PubChemCompoundNameService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static PubChemCompoundNameService getInstance() {
        return PubChemCompoundNameServiceHolder.INSTANCE;
    }

    public String getIUPACName(PubChemCompoundIdentifier identifier) {
        try {
            Query queryId = new TermQuery(idTerm.createTerm(identifier.getAccession()));
            Query queryType = new TermQuery(typeTerm.createTerm(PCCompNameTypes.IUPACName.toString()));
            
            BooleanQuery query = new BooleanQuery();
            query.add(queryId, Occur.MUST);
            query.add(queryType, Occur.MUST);
            
            TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if(hits.length>0)
                return getValue(hits[0], PCCompNamesLuceneFields.Name.toString());
        } catch(IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }

    public PubChemCompoundIdentifier getIdentifier() {
        return new PubChemCompoundIdentifier();
    }

    public Collection<PubChemCompoundIdentifier> searchForNameExcludeSynonyms(String name) {
        Query qName = new TermQuery(nameTerm.createTerm(name));
        Query qTypeName = new TermQuery(typeTerm.createTerm(PCCompNameTypes.Name.toString()));
        Query qTypeIUPAC = new TermQuery(typeTerm.createTerm(PCCompNameTypes.IUPACName.toString()));
        
        BooleanQuery query = new BooleanQuery();
        query.add(qName, Occur.MUST);
        BooleanQuery subQueryType = new BooleanQuery();
        subQueryType.add(qTypeName, Occur.SHOULD);
        subQueryType.add(qTypeIUPAC, Occur.SHOULD);
        query.add(subQueryType, Occur.MUST);
        
        return search(query);
    }

    private static class PubChemCompoundNameServiceHolder {

        private static final PubChemCompoundNameService INSTANCE = new PubChemCompoundNameService();
    }

    public Collection<PubChemCompoundIdentifier> fuzzySearchForName(String name) {
        Query q = new FuzzyQuery(nameTerm.createTerm(name));
        return search(q);
    }

    public Collection<PubChemCompoundIdentifier> searchForName(String name) {
        Query q = new TermQuery(nameTerm.createTerm(name));
        return search(q);
    }

    private Collection<PubChemCompoundIdentifier> search(Query query) {
        Collection<PubChemCompoundIdentifier> ids = new HashSet<PubChemCompoundIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                ids.add(new PubChemCompoundIdentifier(getValue(scoreDoc, PCCompNamesLuceneFields.PubChemCompID.toString())));
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }
    
    public String getPreferredName(PubChemCompoundIdentifier identifier) {
        try {
            Query queryId = new TermQuery(idTerm.createTerm(identifier.getAccession()));
            Query queryType = new TermQuery(typeTerm.createTerm(PCCompNameTypes.Name.toString()));
            
            BooleanQuery query = new BooleanQuery();
            query.add(queryId, Occur.MUST);
            query.add(queryType, Occur.MUST);
            
            TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if(hits.length>0)
                return getValue(hits[0], PCCompNamesLuceneFields.Name.toString());
        } catch(IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }
    
    public Collection<String> getSynonyms(PubChemCompoundIdentifier identifier) {
        try {
            Query queryId = new TermQuery(idTerm.createTerm(identifier.getAccession()));
            Query queryType = new TermQuery(typeTerm.createTerm(PCCompNameTypes.Synonym.toString()));
            
            BooleanQuery query = new BooleanQuery();
            query.add(queryId, Occur.MUST);
            query.add(queryType, Occur.MUST);
            
            TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            Collection<String> synonyms = new HashSet<String>(hits.length);
            for (ScoreDoc scoreDoc : hits) {
                synonyms.add(getValue(scoreDoc, PCCompNamesLuceneFields.Name.toString()));
            }
            return synonyms;
        } catch(IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }

    public Collection<String> getNames(PubChemCompoundIdentifier identifier) {
        try {
            Query q = new TermQuery(idTerm.createTerm(identifier.getAccession()));

            TopScoreDocCollector collector = TopScoreDocCollector.create(20, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            //if (hits.length > 1) {
            //    LOGGER.info("more then one hit found for an id! this shouldn't happen");
            //}
            Collection<String> names = new HashSet<String>(hits.length);
            for (ScoreDoc scoreDoc : hits) {
                names.add(getValue(scoreDoc, PCCompNamesLuceneFields.Name.toString()));
                //return Arrays.asList(getValues(scoreDoc, "name"));
            }
            return names;
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }
}
