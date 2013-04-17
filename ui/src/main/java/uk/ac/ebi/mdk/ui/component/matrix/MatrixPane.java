/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.ui.component.matrix;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.ThemeManager;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.matrix.AbstractReactionMatrix;
import uk.ac.ebi.mdk.ui.render.table.VerticalTableHeaderCellRenderer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


/**
 * MatrixTable - 2011.11.24 <br> Displays a matrix with column and row names
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date: 2011-12-13 08:59:08 +0000 (Tue, 13 Dec
 *          2011) $
 */
public class MatrixPane extends JScrollPane {

    private static final Logger LOGGER = Logger.getLogger(MatrixPane.class);

    private AbstractReactionMatrix matrix;

    private JTable table;

    public MatrixPane(final AbstractReactionMatrix matrix) {
        this.matrix = matrix;

        String[] rxns = new String[matrix.getReactionCount()];
        for (int i = 0; i < rxns.length; i++) {
            Object rxn = matrix.getReaction(i);
            if (rxn instanceof MetabolicReaction) {
                rxns[i] = ((MetabolicReaction) rxn).getAbbreviation();
            } else {
                rxns[i] = rxn.toString();
            }

        }

        //XXX it should use the matrix is the model
        table = new JTable(matrix.getFixedMatrix(),
                           rxns);

        setViewportView(table);


        table.setFont(ThemeManager.getInstance().getTheme().getBodyFont().deriveFont(8f));

        Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            columns.nextElement().setHeaderRenderer(new VerticalTableHeaderCellRenderer());
        }

        ListModel lm = new AbstractListModel() {

            List rheaders = Arrays.asList(matrix.getMolecules());


            public int getSize() {
                return rheaders.size();
            }


            public Object getElementAt(int index) {
                return rheaders.get(index);
            }
        };


//        setCorner(JScrollPane.UPPER_RIGHT_CORNER, );

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(20);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(25);
        }

        JList rh = new JList(lm);
        rh.setFixedCellWidth(120);
        rh.setFixedCellHeight(table.getRowHeight());
        rh.setCellRenderer(new RowHeaderRenderer(table));
        setRowHeaderView(rh);

    }


    class RowHeaderRenderer extends JLabel implements ListCellRenderer {

        RowHeaderRenderer(JTable table) {
            setOpaque(true);
            setHorizontalAlignment(RIGHT);
            setFont(ThemeManager.getInstance().getTheme().getBodyFont());
        }


        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
}
