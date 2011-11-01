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
package uk.ac.ebi.visualisation.molecule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.annotation.chemical.ChemicalStructure;
import uk.ac.ebi.annotation.chemical.MolecularFormula;
import uk.ac.ebi.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.core.Metabolite;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.metabolomes.webservices.util.SynonymCandidateEntry;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;

/**
 *          MoleculeTableModel - 2011.10.31 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MoleculeTableModel extends DefaultTableModel {

    private String[] columns = new String[]{"Name", "Synonyms", "Formula", "Structure", "Charge"};
    List<Metabolite> metabolites = new ArrayList();

    public MoleculeTableModel() {
    }

    public void set(List<Metabolite> metabolites) {

        this.metabolites = metabolites;

        Object[][] data = new Object[metabolites.size()][5];

        for (int i = 0; i < metabolites.size(); i++) {
            Metabolite m = metabolites.get(i);
            data[i][0] = metabolites.get(i).getName();
            data[i][1] = metabolites.get(i).getAnnotationsExtending(Synonym.class);
            data[i][2] = metabolites.get(i).getAnnotationsExtending(MolecularFormula.class);
            data[i][3] = m.hasStructureAssociated() ? m.getFirstChemicalStructure() : null;
            data[i][4] = -42;
        }

        setDataVector(data, columns);
        fireTableStructureChanged();

    }

    public void setCandidates(Collection<SynonymCandidateEntry> candidates) {
        List<Metabolite> tmp = new ArrayList();
        for (SynonymCandidateEntry candidate : candidates) {

            String accession = candidate.getId();
            Metabolite m = new Metabolite("", "", candidate.getDescription());

            if (accession.startsWith("ChEBI") || accession.startsWith("CHEBI")) {
                m.addAnnotation(new ChEBICrossReference(new ChEBIIdentifier(accession)));
            } else if (accession.startsWith("C")) {
                m.addAnnotation(new KEGGCrossReference(new KEGGCompoundIdentifier(accession)));
            } else {
                throw new UnsupportedOperationException("Need to add new identifier!");
            }

            for (String synonym : candidate.getSynonyms()) {
                m.addAnnotation(new Synonym(synonym));
            }


            tmp.add(m);

        }
        set(tmp);
    }

    public Collection<Metabolite> getEntities(int[] index) {
        List<Metabolite> aggregateList = new ArrayList();
        for (int i : index) {
            aggregateList.add(metabolites.get(i));
        }
        return aggregateList;
    }
}
