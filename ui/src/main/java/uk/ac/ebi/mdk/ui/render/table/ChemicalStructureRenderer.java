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

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.ui.render.molecule.MoleculeRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;


/**
 * ChemicalStructureRenderer – 2011.09.29 <br>
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class ChemicalStructureRenderer
        extends DefaultRenderer {

    private MoleculeRenderer renderer;


    public ChemicalStructureRenderer() {
        this(MoleculeRenderer.getInstance());
    }


    public ChemicalStructureRenderer(MoleculeRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JLabel getComponent(JTable table, Object value, int row, int column) {
        Collection collection =
                value instanceof Collection ? (Collection) value : Arrays.asList(value);

        if (table.getColumnModel().getColumn(column).getWidth() != table.getRowHeight(row)) {
            table.setRowHeight(row, table.getColumnModel().getColumn(column).getWidth());
        }

        this.setText(null);

        if (collection.iterator().hasNext()) {
            try {
                Object obj = collection.iterator().next();
                IAtomContainer structure;
                if (obj instanceof IAtomContainer)
                    structure = (IAtomContainer) obj;
                else if (obj instanceof AtomContainerAnnotation)
                    structure = ((AtomContainerAnnotation) obj).getStructure();
                else
                    throw new InternalError("unknown structure type - cannot render: " + obj.getClass());
                this.setIcon(new ImageIcon(
                        renderer.getImage(structure,
                                          new Rectangle(0, 0,
                                                        table.getRowHeight(row) - 10,
                                                        table.getRowHeight(row) - 10),
                                          getBackground())));
            } catch (CDKException ex) {
                System.err.println("Unable to render molecule: " + ex.getMessage());
            }

        } else {
            this.setIcon(null);
        }

        return this;
    }


}
