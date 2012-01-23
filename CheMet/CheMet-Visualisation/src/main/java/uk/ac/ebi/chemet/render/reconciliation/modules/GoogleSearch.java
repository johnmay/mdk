/**
 * GoogleSearch.java
 *
 * 2012.01.13
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.chemet.render.reconciliation.modules;

import com.jgoodies.forms.layout.CellConstraints;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.*;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.render.factory.ButtonFactory;
import uk.ac.ebi.chemet.render.factory.FieldFactory;
import uk.ac.ebi.chemet.render.factory.PanelFactory;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.renderers.ReconciliationComponent;


/**
 *
 * GoogleSearch 2012.01.13
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @author $Author$ (this version)
 *
 * Class description
 *
 */
public class GoogleSearch
        implements ReconciliationComponent {

    private static final Logger LOGGER = Logger.getLogger(GoogleSearch.class);

    private final JPanel component;

    private final JTextField field;

    private final JButton search;

    private static final String GOOGLE_SEARCH_FORMAT = "http://www.google.com/search?ie=UTF-8&q=%s";


    public GoogleSearch() {

        component = PanelFactory.createDialogPanel("p, 4dlu, p", "p");
        field = FieldFactory.newField(30);
        search = ButtonFactory.newButton("Search", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                String query = field.getText();
                String address = String.format(GOOGLE_SEARCH_FORMAT, query);

                try {
                    Desktop.getDesktop().browse(new URI(address));
                } catch (Exception ex) {
                    LOGGER.error("Unable to open browser: " + ex.getMessage());
                }
            }
        });

        CellConstraints cc = new CellConstraints();

        component.add(field, cc.xy(1, 1));
        component.add(search, cc.xy(3, 1));

    }


    public String getDescription() {
        return "Google search";
    }


    public JComponent getComponent() {
        return component;
    }


    public void setup(Metabolite metabolite) {
        field.setText(metabolite.getName());
    }


    public boolean canTransferAnnotations() {
        return false;
    }


    public void transferAnnotations() {
        // do nothing
    }
}
