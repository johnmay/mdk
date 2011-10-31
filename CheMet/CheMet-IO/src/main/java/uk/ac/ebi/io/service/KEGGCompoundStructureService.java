/**
 * KEGGCompoundStructureService.java
 *
 * 2011.10.28
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
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.interfaces.services.StructureQueryService;
import uk.ac.ebi.io.remote.KEGGCompoundMols;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;

/**
 *          KEGGCompoundStructureService - 2011.10.28 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class KEGGCompoundStructureService extends KEGGCompoundQueryService
        implements StructureQueryService<KEGGCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundStructureService.class);
    private IndexSearcher searcher;

    private KEGGCompoundStructureService() {
        super(new KEGGCompoundMols());
        try {
            searcher = new IndexSearcher(getDirectory(), true);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    public static KEGGCompoundStructureService getInstance() {
        return KEGGCompoundStructureServiceHolder.INSTANCE;
    }

    private static class KEGGCompoundStructureServiceHolder {

        private static final KEGGCompoundStructureService INSTANCE = new KEGGCompoundStructureService();
    }

    public IAtomContainer getStructure(KEGGCompoundIdentifier identifier) {
        try {
            String str = getMol(identifier);

            if (str.isEmpty()) {
                return null;
            }

            MDLV2000Reader reader = new MDLV2000Reader(new StringReader(str));
            IMolecule molecule = new Molecule();
            return (IAtomContainer) reader.read(molecule);

        } catch (CDKException ex) {
            return null;
        }

    }

    public String getMol(KEGGCompoundIdentifier identifier) {
        try {

            Query q = NumericRangeQuery.newIntRange("id", identifier.getValue(), identifier.getValue(), true, true);
            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length > 1) {
                LOGGER.info("more then one hit found for an id! this shouldn't happen");
            }
            Collection<String> names = new HashSet<String>(hits.length);
            for (ScoreDoc scoreDoc : hits) {
                return new String(getBinaryValue(scoreDoc, "mdl"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }

        return "";

    }
}
