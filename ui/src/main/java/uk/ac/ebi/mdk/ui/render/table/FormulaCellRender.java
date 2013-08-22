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
package uk.ac.ebi.mdk.ui.render.table;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.TextUtility;
import uk.ac.ebi.mdk.domain.annotation.MolecularFormula;

import javax.swing.*;
import java.util.Collection;
import java.util.Iterator;


/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name FormulaCellRender - 2011.10.06 <br>
 * Class description
 */
public class FormulaCellRender
        extends DefaultRenderer {

    private static final Logger LOGGER = Logger.getLogger(FormulaCellRender.class);

    private StringBuilder sb = new StringBuilder(50);


    public FormulaCellRender() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public JLabel getComponent(JTable table, Object value, int row, int column) {

        // need more space to show sub script
        if (table.getRowHeight(row) != 32) {
            table.setRowHeight(row, 32);
        }

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

        return this;
    }


}
