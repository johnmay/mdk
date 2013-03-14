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
package old;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.ui.render.table.AnnotationCellRenderer;
import uk.ac.ebi.mdk.ui.render.table.ChemicalStructureRenderer;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.tool.domain.ReconstructionComparison;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.util.Map.Entry;


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
     * @param type
     * @return
     */
    public JTable getComparisconTable(final TableData type) {

        JTable table = new JTable();

        Reconstruction[] recons = comparison.getReconstructions();

        Map<Long, Multimap<Reconstruction, Metabolite>> map = new HashMap<Long, Multimap<Reconstruction, Metabolite>>();

        for (int i = 0; i < recons.length; i++) {
            Reconstruction reconstruction = recons[i];
            for (Entry<Metabolite, Long> e : comparison.getMoleculeHashMap(reconstruction).entrySet()) {

                Long key = e.getValue();
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
        for (Entry<Long, Multimap<Reconstruction, Metabolite>> e : map.entrySet()) {
            Long key = e.getKey();
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
                                if (m.hasStructure()) {
                                    Collection<AtomContainerAnnotation> collection = m.getAnnotationsExtending(AtomContainerAnnotation.class);
                                    if (!collection.isEmpty()) {
                                        structure.add(collection.iterator().next());
                                    }
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
