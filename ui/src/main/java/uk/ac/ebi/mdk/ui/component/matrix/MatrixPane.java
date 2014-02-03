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
import uk.ac.ebi.mdk.domain.matrix.StoichiometricMatrix;
import uk.ac.ebi.mdk.ui.render.table.VerticalTableHeaderCellRenderer;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
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
public class MatrixPane<M, R> extends JScrollPane {

    private static final Logger LOGGER = Logger.getLogger(MatrixPane.class);

    private StoichiometricMatrix<M, R> matrix;

    private JTable table;

    public MatrixPane(final StoichiometricMatrix<M, R> matrix) {
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
        
        final Font defaultFont = ThemeManager.getInstance().getTheme().getBodyFont().deriveFont(8f);
        final Font boldFont    = defaultFont.deriveFont(Font.BOLD, 10f);

        //XXX it should use the matrix is the model
        table = new JTable(new SparseMatrixModel<M, R>(matrix));
        table.setDefaultRenderer(Double.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel component = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Double coef = (Double) value;
                if (coef > 0) {
                    component.setForeground(new Color(0x01A325));
                    component.setFont(boldFont);
                } else if (coef < 0) {
                    component.setForeground(new Color(0xDB000C));
                    component.setFont(boldFont);
                } else { 
                    component.setForeground(Color.LIGHT_GRAY);
                    component.setFont(defaultFont);
                }
                component.setText(String.format("%s%.0f", coef > 0 ? "+" : "", coef));
                return component;
            }
        });
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
    
    private static final class SparseMatrixModel<M, R> implements TableModel {
        
        private final StoichiometricMatrix<M, R> s;
        
        private SparseMatrixModel(StoichiometricMatrix<M, R> s) {
            this.s = s;
        }

        @Override public Object getValueAt(int row, int column) {
            return s.get(row, column);
        }

        @Override public int getRowCount() {
            return s.getMoleculeCount();
        }

        @Override public int getColumnCount() {
            return s.getMoleculeCount();
        }

        @Override public String getColumnName(int columnIndex) {
            return s.getReaction(columnIndex).toString();
        }

        @Override public Class<?> getColumnClass(int columnIndex) {
            return Double.class;
        }

        @Override public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override public void addTableModelListener(TableModelListener l) {

        }

        @Override public void removeTableModelListener(TableModelListener l) {

        }
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
