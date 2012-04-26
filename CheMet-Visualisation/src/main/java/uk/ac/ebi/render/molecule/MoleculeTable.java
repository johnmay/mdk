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
import java.util.Collection;
import javax.swing.JTable;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.chemet.render.table.renderers.AnnotationCellRenderer;
import uk.ac.ebi.chemet.render.table.renderers.ChemicalStructureRenderer;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.visualisation.molecule.access.EntityValueAccessor;


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
        setDefaultRenderer(AtomContainerAnnotation.class, new ChemicalStructureRenderer());
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

}
