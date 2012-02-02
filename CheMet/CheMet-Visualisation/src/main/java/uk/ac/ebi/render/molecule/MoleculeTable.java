/**
 * MoleculeChooser.java
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

import com.explodingpixels.macwidgets.plaf.ITunesTableUI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.openscience.cdk.templates.MoleculeFactory;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.annotation.chemical.ChemicalStructure;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.chemet.render.table.renderers.AnnotationCellRenderer;
import uk.ac.ebi.chemet.render.table.renderers.ChemicalStructureRenderer;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.visualisation.molecule.access.ChemicalStructureAccessor;
import uk.ac.ebi.visualisation.molecule.access.EntityValueAccessor;
import uk.ac.ebi.visualisation.molecule.access.NameAccessor;


/**
 *          MoleculeChooser - 2011.10.31 <br>
 *          A utility table to display molecules
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MoleculeTable extends JTable {

    private static final Logger LOGGER = Logger.getLogger(MoleculeTable.class);

    private Color lightGreen = new Color(200, 255, 200);


    public MoleculeTable(EntityValueAccessor... accessors) {
        super(new MoleculeTableModel(accessors));
        
        setUI(new ITunesTableUI());

        setDefaultRenderer(Synonym.class, new AnnotationCellRenderer(false, ","));
        setDefaultRenderer(ChemicalStructure.class, new ChemicalStructureRenderer());
        setDefaultRenderer(Annotation.class, new AnnotationCellRenderer(false, ","));

    }


    @Override
    public Class<?> getColumnClass(int column) {
        return getModel().getColumnClass(column);
    }


    @Override
    public MoleculeTableModel getModel() {
        return (MoleculeTableModel) super.getModel();
    }


    public Collection<Metabolite> getSelectedEntities() {
        return getModel().getEntities(getSelectedRows());
    }


    public static void main(String[] args) {
        final MoleculeTable table = new MoleculeTable(new NameAccessor(),
                                                      new ChemicalStructureAccessor());


        table.setDefaultRenderer(ChemicalStructure.class,
                                 new ChemicalStructureRenderer(CachedMoleculeRenderer.getInstance()));


        Metabolite m = new uk.ac.ebi.core.MetaboliteImplementation("1", "123ta", "1,2,3-Tirazole");
        m.addAnnotation(new ChemicalStructure(MoleculeFactory.make123Triazole()));

        List<Metabolite> mols = new ArrayList();

        for (int i = 0; i < 100; i++) {
            mols.add(m);
        }

        table.getModel().set(mols);


        final JFrame frame = new JFrame();

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.add(new JScrollPane(table));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
