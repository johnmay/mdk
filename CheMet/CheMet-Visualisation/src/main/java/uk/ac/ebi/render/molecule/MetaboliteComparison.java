/**
 * Comparisson.java
 *
 * 2011.11.29
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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.core.Reconstruction;
import uk.ac.ebi.core.tools.ReconstructionComparison;
import uk.ac.ebi.chemet.render.table.renderers.AnnotationCellRenderer;
import uk.ac.ebi.chemet.render.table.renderers.ChemicalStructureRenderer;


/**
 *          Comparisson - 2011.11.29 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetaboliteComparison {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteComparison.class);


    public enum TableData {

        PRESENCE, // x incates precense
        OCCURANCE, // count incates number
        NAME, // metabolite names
        ABBREVIATION, // metabolite abbreviation
        STRUCTURE   // displays the chemical structure
    };

    private ReconstructionComparison comparison;


    public MetaboliteComparison(ReconstructionComparison comparison) {
        this.comparison = comparison;
    }


    /**
     * Generates a comparison table
     * @param data
     * @return
     */
    public JTable getComparisconTable(final TableData type) {

        JTable table = new JTable();

        Reconstruction[] recons = comparison.getReconstructions();

        Map<Integer, Multimap<Reconstruction, Metabolite>> map = new HashMap();

        for (int i = 0; i < recons.length; i++) {
            Reconstruction reconstruction = recons[i];
            for (Entry<Metabolite, Integer> e : comparison.getMoleculeHashMap(reconstruction).entrySet()) {

                Integer key = e.getValue();
                if (!map.containsKey(key)) {
                    Multimap<Reconstruction, Metabolite> sub = ArrayListMultimap.create();
                    map.put(key, sub);
                }
                map.get(key).put(reconstruction, e.getKey());
            }
        }


        List<IAtomContainer> structures = new ArrayList();

        Object[][] data = new Object[map.keySet().size()][recons.length];
        int i = 0;
        for (Entry<Integer, Multimap<Reconstruction, Metabolite>> e : map.entrySet()) {
            Integer key = e.getKey();
            Multimap<Reconstruction, Metabolite> sub = e.getValue();
            for (int j = 0; j < recons.length; j++) {

                Reconstruction recon = recons[j];

                switch (type) {

                    case PRESENCE:
                        data[i][j] = sub.containsKey(recon) ? "x" : "";
                        break;

                    case OCCURANCE:
                        data[i][j] = sub.containsKey(recon) ? sub.get(recon).size() : 0;
                        break;

                    case NAME:

                        List<String> names = new ArrayList();
                        if (sub.containsKey(recon)) {
                            for (Metabolite m : sub.get(recon)) {
                                names.add(m.getName());
                            }
                        }

                        data[i][j] = names;
                        break;

                    case ABBREVIATION:

                        List<String> abbrv = new ArrayList();
                        if (sub.containsKey(recon)) {
                            for (Metabolite m : sub.get(recon)) {
                                abbrv.add(m.getAbbreviation());
                            }
                        }

                        data[i][j] = abbrv;
                        break;

                    case STRUCTURE:

                        List<AtomContainerAnnotation> structure = new ArrayList();
                        if (sub.containsKey(recon)) {
                            for (Metabolite m : sub.get(recon)) {
                                if (m.hasStructureAssociated()) {
                                    structure.add(m.hasStructureAssociated() ? m.getAnnotations(AtomContainerAnnotation.class).iterator().next()
                                                  : null);
                                }
                            }
                        }

                        data[i][j] = structure;
                        break;
                }

            }
            i++;
        }



        DefaultTableModel model = new DefaultTableModel(data, recons) {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (type) {
                    case STRUCTURE:
                        return AtomContainerAnnotation.class;
                    case NAME:
                        return List.class;
                    case ABBREVIATION:
                        return List.class;
                    default:
                        return Object.class;
                }
            }
        };
        table.setModel(model);
        table.setDefaultRenderer(List.class, new AnnotationCellRenderer());
        if (type == TableData.STRUCTURE) {
            table.setDefaultRenderer(AtomContainerAnnotation.class, new ChemicalStructureRenderer());
            table.setRowHeight(64);
        }

        return table;

    }
}
