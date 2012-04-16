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
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.interfaces.services.ChemicalDataQueryService;
import uk.ac.ebi.io.remote.ChEBIChemicalData;

/**
 *          ChEBINameService - 2011.10.26 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @deprecated use the one in chemet-service (ChEBIDataService)
 */
@Deprecated
public class ChEBIChemicalDataService
        extends ChEBIQueryService
        implements ChemicalDataQueryService<ChEBIIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(ChEBIChemicalDataService.class);
    private IndexSearcher searcher;
    private Term idTerm = new Term("Id");
    private IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();

    private ChEBIChemicalDataService() {
        super(new ChEBIChemicalData());
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ChEBIChemicalDataService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ChEBIChemicalDataService getInstance() {
        return ChEBIChemicalDataServiceHolder.INSTANCE;
    }

    private static class ChEBIChemicalDataServiceHolder {

        private static final ChEBIChemicalDataService INSTANCE = new ChEBIChemicalDataService();
    }

    public Collection<IMolecularFormula> getFormulas(ChEBIIdentifier identifier) {
        try {
            Query q = new TermQuery(idTerm.createTerm(identifier.getAccession()));

            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length > 1) {
                LOGGER.info("more then one hit found for an id! this shouldn't happen");
            }
            for (ScoreDoc scoreDoc : hits) {
                Collection<IMolecularFormula> formulas = new ArrayList();
                for (String mf : getValues(scoreDoc, "Formula")) {
                    formulas.add(MolecularFormulaManipulator.getMolecularFormula(mf, builder));
                }
                return formulas;
            }
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
        }
        return new ArrayList();
    }

    public Double getCharge(ChEBIIdentifier identifier) {
        try {
            Query q = new TermQuery(idTerm.createTerm(identifier.getAccession()));

            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length > 1) {
                LOGGER.info("more then one hit found for an id! this shouldn't happen");
            }
            for (ScoreDoc scoreDoc : hits) {
                String chargeText = getValue(scoreDoc, "Charge");
                if (chargeText != null) {
                    return Double.parseDouble(chargeText);
                }
            }
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }
}
