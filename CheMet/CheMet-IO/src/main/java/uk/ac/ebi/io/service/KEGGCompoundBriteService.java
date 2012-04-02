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
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.interfaces.services.PropertyQueryService;
import uk.ac.ebi.io.plain.KEGGBriteEntry.KEGGCompBriteCategories;
import uk.ac.ebi.io.remote.KEGGCompoundBrite;
import uk.ac.ebi.io.remote.KEGGCompoundBrite.KEGGCompBriteLuceneFields;
import uk.ac.ebi.resource.IdentifierFactory;

/**
 *          KEGGCompoundBriteService - 2011.10.26 <br>
 *          Provides access to the brite category assignments for KEGG Compounds.
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class KEGGCompoundBriteService
        extends KEGGCompoundQueryService
        implements PropertyQueryService<KEGGCompoundIdentifier>  {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundBriteService.class);
    private IndexSearcher searcher;
    private static final IdentifierFactory FACTORY = IdentifierFactory.getInstance();

    private KEGGCompoundBriteService() {
        super(new KEGGCompoundBrite());
        setMaxResults(100);
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems when loading the lucene searcher", ex);
        }
    }

    public static KEGGCompoundBriteService getInstance() {
        return KEGGCompoundBriteServiceHolder.INSTANCE;
    }

    public Collection<KEGGCompoundIdentifier> fuzzySearchForProperty(String keggBriteCategory) {
        Query entryCat = new FuzzyQuery(new Term(KEGGCompBriteLuceneFields.BriteEntry.toString(),keggBriteCategory));
        Query defCat = new FuzzyQuery(new Term(KEGGCompBriteLuceneFields.BriteDefinition.toString(),keggBriteCategory));
        Query nameCat = new FuzzyQuery(new Term(KEGGCompBriteLuceneFields.BriteName.toString(),keggBriteCategory));
        
        BooleanQuery query = new BooleanQuery();
        query.add(entryCat, BooleanClause.Occur.SHOULD);
        query.add(defCat, BooleanClause.Occur.SHOULD);
        query.add(nameCat, BooleanClause.Occur.SHOULD);
        
        for (KEGGCompBriteCategories cat : KEGGCompBriteCategories.values()) {
            Query nQuery = new FuzzyQuery(new Term(cat.toString(),keggBriteCategory));
            query.add(nQuery,BooleanClause.Occur.SHOULD);
        }
        
        return this.search(query);
    }

    /**
     * Does an exact term search of brite category on the index, returning all the KEGG Compound identifiers that match.
     * It looks for the query string in all the levels of the Brite hierarchy loaded in the index.
     * 
     * @param keggBriteCategory
     * @return collection of KEGGCompoundIdentifier annotated with this category. 
     */
    public Collection<KEGGCompoundIdentifier> searchForProperty(String keggBriteCategory) {
        Query entryCat = new TermQuery(new Term(KEGGCompBriteLuceneFields.BriteEntry.toString(),keggBriteCategory));
        Query defCat = new TermQuery(new Term(KEGGCompBriteLuceneFields.BriteDefinition.toString(),keggBriteCategory));
        Query nameCat = new TermQuery(new Term(KEGGCompBriteLuceneFields.BriteName.toString(),keggBriteCategory));
        
        BooleanQuery query = new BooleanQuery();
        query.add(entryCat, BooleanClause.Occur.SHOULD);
        query.add(defCat, BooleanClause.Occur.SHOULD);
        query.add(nameCat, BooleanClause.Occur.SHOULD);
        
        for (KEGGCompBriteCategories cat : KEGGCompBriteCategories.values()) {
            Query nQuery = new TermQuery(new Term(cat.toString(),keggBriteCategory));
            query.add(nQuery,BooleanClause.Occur.SHOULD);
        }
        
        return this.search(query);
    }
    
    public Boolean compoundHasCategory(String keggCompID, String keggBriteCategory) {
        Query identQuery = new TermQuery(new Term(KEGGCompBriteLuceneFields.KeggCompID.toString(),keggCompID));
        
        BooleanQuery booleanQuery = new BooleanQuery();
        BooleanQuery finalQuery = new BooleanQuery();
        finalQuery.add(identQuery,BooleanClause.Occur.MUST);
        
        for (KEGGCompBriteLuceneFields fields : KEGGCompBriteLuceneFields.values()) {
            if(fields.equals(KEGGCompBriteLuceneFields.KeggCompID))
                continue;
            if(fields.equals(KEGGCompBriteLuceneFields.CompoundName))
                continue;
            Query nQuery = new TermQuery(new Term(fields.toString(),keggBriteCategory));
            booleanQuery.add(nQuery,BooleanClause.Occur.SHOULD);
        }
        
        for (KEGGCompBriteCategories cat : KEGGCompBriteCategories.values()) {
            Query nQuery = new TermQuery(new Term(cat.toString(),keggBriteCategory));
            booleanQuery.add(nQuery,BooleanClause.Occur.SHOULD);
        }
        
        finalQuery.add(booleanQuery,BooleanClause.Occur.MUST);
        return this.search(finalQuery).size() > 0;
    }

    /**
     * Given a KEGGCompoundIdentifier, it retrieves all the brite categories annotated to it in the index. The hierarchy
     * is lost, just a list of categories is retrieved.
     * 
     * @param identifier
     * @return collection of brite categories annotated.
     */
    public Collection<String> getProperties(KEGGCompoundIdentifier identifier) {
        Query compIDQuery = new TermQuery(new Term(KEGGCompBriteLuceneFields.KeggCompID.toString(),identifier.getAccession()));
        return reverseSearch(compIDQuery);
    }

    private static class KEGGCompoundBriteServiceHolder {
        private static final KEGGCompoundBriteService INSTANCE = new KEGGCompoundBriteService();
    }

    private Collection<String> reverseSearch(Query query) {
        Collection<String> ids = new HashSet<String>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                for (KEGGCompBriteLuceneFields luceneField : KEGGCompBriteLuceneFields.values()) {
                    if(luceneField.equals(KEGGCompBriteLuceneFields.KeggCompID))
                        continue;
                    if(luceneField.equals(KEGGCompBriteLuceneFields.CompoundName))
                        continue;
                    String ans = getValue(scoreDoc, luceneField.toString());
                    if(ans!=null)
                        ids.add(ans);
                }
                for (KEGGCompBriteCategories catFields : KEGGCompBriteCategories.values()) {
                    String value = getValue(scoreDoc, catFields.toString());
                    if(value!=null && !value.equalsIgnoreCase("null"))
                        ids.add(value);
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }
    
    private Collection<KEGGCompoundIdentifier> search(Query query) {
        Collection<KEGGCompoundIdentifier> ids = new HashSet<KEGGCompoundIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                KEGGCompoundIdentifier ident = 
                        new KEGGCompoundIdentifier(getValue(scoreDoc, KEGGCompBriteLuceneFields.KeggCompID.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query);
        }

        return ids;
    }

}
