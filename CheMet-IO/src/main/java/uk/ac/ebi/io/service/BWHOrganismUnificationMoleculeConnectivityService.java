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
import uk.ac.ebi.chemet.resource.chemical.ChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.io.remote.ChEBICrossRefs.ChEBICrossRefsLuceneFields;
import uk.ac.ebi.io.remote.MoleculeCollectionConnectivity;
import uk.ac.ebi.io.remote.MoleculeCollectionConnectivity.MoleculeCollectionConnectivityLuceneFields;
import uk.ac.ebi.resource.DefaultIdentifierFactory;
import uk.ac.ebi.resource.organism.Taxonomy;
import uk.ac.ebi.deprecated.services.ChemicalConnectivityQueryService;

/**
 *          ChEBINameService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BWHOrganismUnificationMoleculeConnectivityService
        extends MoleculeConnectivityQueryService implements ChemicalConnectivityQueryService<ChemicalIdentifier> {
    
     /**
     * DB : Database only unification.
     * DBTM : Database and text mining unification.
     * TM : Text mining only unification.
     * ME : Molecule enumeration only unification.
     */
    public enum CollectionType {
        DB, DBTM, TM, ME;
    }
    
    public static String getCollectionName(Taxonomy tax, CollectionType type) {
        return tax.getAccession()+type.toString();
    }

    private static final Logger LOGGER = Logger.getLogger(BWHOrganismUnificationMoleculeConnectivityService.class);
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();
    private final Query collectionQuery;

    private BWHOrganismUnificationMoleculeConnectivityService(Taxonomy tax, CollectionType type) {
        super(new MoleculeCollectionConnectivity(getCollectionName(tax, type)));
        collectionQuery = new TermQuery(new Term(MoleculeCollectionConnectivityLuceneFields.CollectionName.toString(), getCollectionName(tax, type)));
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error("Problems loading directory", ex);
        }
    }
    
    @Override
    Query getCollectionQuery() {
        return collectionQuery;
    }

    public static BWHOrganismUnificationMoleculeConnectivityService getInstance(Taxonomy tax, CollectionType type) {
        BWHOrganismUnificationMoleculeConnectivityServiceHolder.setService(tax, type);
        return BWHOrganismUnificationMoleculeConnectivityServiceHolder.INSTANCE;
    }

    /*public Collection<Identifier> getCrossReferences(ChEBIIdentifier identifier) {
        Query queryPubChemComp = new TermQuery(new Term(ChEBICrossRefsLuceneFields.ChebiID.toString(), identifier.getNumericPartOfAccession()));
        return search(queryPubChemComp);
    }*/

    public Collection<ChemicalIdentifier> getEntriesWithConnectivity(String connectivity) {
        Query queryCon = new TermQuery(new Term(MoleculeCollectionConnectivityLuceneFields.Connectivity.toString(), connectivity));
        
        BooleanQuery query = new BooleanQuery();
        query.add(queryCon, BooleanClause.Occur.MUST);
        query.add(collectionQuery, BooleanClause.Occur.MUST);
        
        return reverseSearch(query);
    }

    private ChemicalIdentifier getIdentifier(String value) {
        return (ChemicalIdentifier)FACTORY.ofSynonym(value);
    }

    private static class BWHOrganismUnificationMoleculeConnectivityServiceHolder {
        private static void setService(Taxonomy tax, CollectionType type) {
             INSTANCE = new BWHOrganismUnificationMoleculeConnectivityService(tax,type);
        }
        private static BWHOrganismUnificationMoleculeConnectivityService INSTANCE;
    }

    private Collection<ChemicalIdentifier> reverseSearch(Query query) {
        Collection<ChemicalIdentifier> ids = new HashSet<ChemicalIdentifier>();

        try {
            TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
            searcher.search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                ChemicalIdentifier ident = getIdentifier(getValue(scoreDoc, MoleculeCollectionConnectivityLuceneFields.DB.toString()));
                ident.setAccession(getValue(scoreDoc, MoleculeCollectionConnectivityLuceneFields.Identifier.toString()));
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
                Identifier ident = FACTORY.ofSynonym(getValue(scoreDoc, ChEBICrossRefsLuceneFields.ExtDB.toString()));
                ident.setAccession(getValue(scoreDoc, ChEBICrossRefsLuceneFields.ExtID.toString()));
                ids.add(ident);
            }
        } catch (IOException ex) {
            LOGGER.error("Error occur whilst searching with query " + query,ex);
        }

        return ids;
    }

}
