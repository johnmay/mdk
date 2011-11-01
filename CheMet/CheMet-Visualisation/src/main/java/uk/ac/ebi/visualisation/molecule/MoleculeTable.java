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
package uk.ac.ebi.visualisation.molecule;

import com.explodingpixels.macwidgets.plaf.ITunesTableUI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.Metabolite;
import uk.ac.ebi.mnb.renderers.AnnotationCellRenderer;

/**
 *          MoleculeChooser - 2011.10.31 <br>
 *          A utility table to display molecules
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MoleculeTable extends JTable {

    private static final Logger LOGGER = Logger.getLogger(MoleculeTable.class);

    public MoleculeTable() {
        super(new MoleculeTableModel());
        setUI(new ITunesTableUI());
        setRowHeight(64);
        for(int i = 0 ; i< getColumnCount(); i++){
            getColumnModel().getColumn(i).setWidth(100);
        }
        setDefaultRenderer(List.class, new AnnotationCellRenderer());
    }

    @Override
    public MoleculeTableModel getModel() {
        return (MoleculeTableModel) super.getModel();
    }

    public Collection<Metabolite> getSelectedEntities() {
        return getModel().getEntities(getSelectedRows());
    }

    public static void main(String[] args) {
        MoleculeTable table = new MoleculeTable();
        table.getModel().set(Arrays.asList(new Metabolite("", "", "A metabolite")));
        JFrame frame = new JFrame();
        frame.add(new JScrollPane(table));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

