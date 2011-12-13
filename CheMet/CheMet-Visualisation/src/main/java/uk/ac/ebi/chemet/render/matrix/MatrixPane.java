/**
 * MatrixTable.java
 *
 * 2011.11.24
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
package uk.ac.ebi.chemet.render.matrix;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.MetabolicReaction;
import uk.ac.ebi.metabolomes.core.reaction.matrix.AbstractReactionMatrix;
import uk.ac.ebi.chemet.render.table.renderers.VerticalTableHeaderCellRenderer;
import uk.ac.ebi.chemet.render.ViewUtilities;

/**
 *          MatrixTable - 2011.11.24 <br>
 *          Displays a matrix with column and row names
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MatrixPane extends JScrollPane {

    private static final Logger LOGGER = Logger.getLogger(MatrixPane.class);
    private AbstractReactionMatrix matrix;
    private JTable table;

    public MatrixPane(final AbstractReactionMatrix matrix) {
        this.matrix = matrix;

        String[] rxns = new String[matrix.getReactionCount()];
        for (int i = 0; i < rxns.length; i++) {
            rxns[i] = ((MetabolicReaction) matrix.getReaction(i)).getAbbreviation();
        }

        table = new JTable(matrix.getFixedMatrix(),
                           rxns);
        setViewportView(table);

        table.setFont(ViewUtilities.DEFAULT_BODY_FONT.deriveFont(10.0f));

        Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            columns.nextElement().setHeaderRenderer(new VerticalTableHeaderCellRenderer());
        }

        ListModel lm = new AbstractListModel() {

            List rheaders = new ArrayList(matrix.getMolecules());

            public int getSize() {
                return rheaders.size();
            }

            public Object getElementAt(int index) {
                return rheaders.get(index);
            }
        };

        JList rh = new JList(lm);
        rh.setFixedCellWidth(120);
        rh.setCellRenderer(new RowHeaderRenderer(table));
        setRowHeaderView(rh);
//        setCorner(JScrollPane.UPPER_RIGHT_CORNER, );

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(15);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(15);
        }

    }

    class RowHeaderRenderer extends JLabel implements ListCellRenderer {

        RowHeaderRenderer(JTable table) {
            setOpaque(true);
            setHorizontalAlignment(RIGHT);
            setFont(ViewUtilities.DEFAULT_BODY_FONT);
        }

        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
}
