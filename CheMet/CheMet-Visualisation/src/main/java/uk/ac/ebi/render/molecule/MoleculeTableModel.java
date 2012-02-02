/**
 * MoleculeTableModel.java
 *
 * 2011.10.31
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
package uk.ac.ebi.render.molecule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.metabolomes.webservices.util.CandidateEntry;
import uk.ac.ebi.metabolomes.webservices.util.SynonymCandidateEntry;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.visualisation.molecule.access.EntityValueAccessor;


/**
 *          MoleculeTableModel - 2011.10.31 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MoleculeTableModel extends DefaultTableModel {

    private List<Metabolite> metabolites = new ArrayList();

    private Map<Metabolite, CandidateEntry> map = new HashMap();

    private List<EntityValueAccessor> columns = new ArrayList<EntityValueAccessor>();


    public MoleculeTableModel(EntityValueAccessor... accessors) {
        columns = Arrays.asList(accessors);
    }


    public void set(List<Metabolite> metabolites) {

        this.metabolites = metabolites;

        Object[][] data = new Object[metabolites.size()][columns.size()];

        for (int i = 0; i < columns.size(); i++) {
            for (int j = 0; j < metabolites.size(); j++) {
                Metabolite m = metabolites.get(j);
                data[j][i] = columns.get(i).getValue(m);
            }
        }

        setDataVector(data, new String[columns.size()]);
        fireTableStructureChanged();

    }


    @Override
    public String getColumnName(int column) {
        return columns.get(column).getName();
    }


    public CandidateEntry getCandidate(Metabolite m) {
        return map.get(m);
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnClass();
    }


    public Collection<Metabolite> getEntities(int[] index) {
        List<Metabolite> aggregateList = new ArrayList();
        for (int i : index) {
            aggregateList.add(metabolites.get(i));
        }
        return aggregateList;
    }
}
