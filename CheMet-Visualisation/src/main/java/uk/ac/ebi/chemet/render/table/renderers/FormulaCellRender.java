/**
 * FormulaCellRender.java
 *
 * 2011.10.06
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
package uk.ac.ebi.chemet.render.table.renderers;

import java.awt.Component;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JTable;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.MolecularFormula;
import uk.ac.ebi.caf.utility.TextUtility;


/**
 * @name    FormulaCellRender - 2011.10.06 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class FormulaCellRender
        extends DefaultRenderer {

    private static final Logger LOGGER = Logger.getLogger(FormulaCellRender.class);

    private StringBuilder sb = new StringBuilder(50);


    public FormulaCellRender() {
    }


    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {



        if (value instanceof Collection) {

            Collection<MolecularFormula> formulas = (Collection<MolecularFormula>) value;
            sb.delete(0, sb.length());
            Iterator<MolecularFormula> it = formulas.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toHTML());
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
            this.setText(TextUtility.html(sb.toString()));

        } else {

            MolecularFormula formula = (MolecularFormula) value;
            if (formula.getFormula() != null) {
                this.setText(TextUtility.html(formula.toHTML()));
            } else {
                this.setText(formula.toString());
            }

        }

        this.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        this.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

        return this;
    }
}
