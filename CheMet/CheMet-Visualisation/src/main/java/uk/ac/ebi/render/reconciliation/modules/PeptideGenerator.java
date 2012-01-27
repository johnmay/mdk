/**
 * PeptideGenerator.java
 *
 * 2012.01.24
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
package uk.ac.ebi.render.reconciliation.modules;

import com.jgoodies.forms.layout.CellConstraints;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;
import uk.ac.ebi.core.tools.PeptideFactory;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.renderers.CrossreferenceModule;


/**
 *
 *          PeptideGenerator 2012.01.24
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class provides a UI to use the {@see PeptideFactory}
 *
 */
public class PeptideGenerator implements CrossreferenceModule {

    private static final Logger LOGGER = Logger.getLogger(PeptideGenerator.class);

    private JPanel component;

    private CellConstraints cc = new CellConstraints();


    public PeptideGenerator() {

        component = PanelFactory.createDialogPanel("p, 4dlu, p, 4dlu, 4dlu, p", "p");

        component.add(LabelFactory.newFormLabel("N-Terminus:"), cc.xy(1, 1));
        component.add(new JComboBox(PeptideFactory.AminoAcid.values()), cc.xy(3, 1));

        component.add(LabelFactory.newFormLabel("C-Terminus:"), cc.xy(5, 1));
        component.add(new JComboBox(PeptideFactory.AminoAcid.values()), cc.xy(7, 1));

    }


    public String getDescription() {
        return "Generate Peptide";
    }


    public JComponent getComponent() {
        return component;
    }


    public void setup(Metabolite metabolite) {
    }


    public boolean canTransferAnnotations() {
        return true;
    }


    public void transferAnnotations() {
        // make the peptide
    }
}
