/**
 * DatabaseSearch.java
 *
 * 2012.02.01
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

import com.google.common.collect.Multimap;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.Source;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;
import uk.ac.ebi.caf.component.theme.ThemeManager;
import uk.ac.ebi.core.DefaultEntityFactory;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.renderers.CrossreferenceModule;
import uk.ac.ebi.io.service.ChEBIChemicalDataService;
import uk.ac.ebi.io.service.ChEBINameService;
import uk.ac.ebi.metabolomes.webservices.util.CandidateFactory;
import uk.ac.ebi.metabolomes.webservices.util.SynonymCandidateEntry;
import uk.ac.ebi.reconciliation.ChemicalFingerprintEncoder;
import uk.ac.ebi.render.molecule.MoleculeTable;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.visualisation.molecule.access.AccessionAccessor;
import uk.ac.ebi.visualisation.molecule.access.AnnotationAccess;
import uk.ac.ebi.visualisation.molecule.access.NameAccessor;


/**
 *
 *          DatabaseSearch 2012.02.01
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class DatabaseSearch
        implements CrossreferenceModule {

    private static final Logger LOGGER = Logger.getLogger(DatabaseSearch.class);

    private JComponent component;

    private MoleculeTable table;

    private JTextField field;

    private JCheckBox fuzzy;

    private Timer timer = new Timer(500, new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            System.out.println("fired!");
            updateTable(field.getText());
            timer.stop();
        }
    });


    public DatabaseSearch() {

        component = PanelFactory.createDialogPanel();
        component.setLayout(new BoxLayout(component, BoxLayout.PAGE_AXIS));

        field = FieldFactory.newField(25);
        fuzzy = new JCheckBox("Fuzzy Search");
        table = new MoleculeTable(new NameAccessor(),
                                  new AccessionAccessor(),
                                  new AnnotationAccess(new Source()));
        table.setPreferredSize(new Dimension(300, 484));
        table.setBackground(component.getBackground());
        table.setSelectionBackground(component.getBackground().brighter());
        table.setSelectionForeground(ThemeManager.getInstance().getTheme().getForeground());


        field.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                timer.restart();
            }


            public void removeUpdate(DocumentEvent e) {
                timer.restart();
            }


            public void changedUpdate(DocumentEvent e) {
                timer.restart();
            }
        });
        fuzzy.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                timer.restart();
            }
        });




        // component.add(); // visual inspector

        component.add(getSearchOptions()); // search options


    }


    public String getDescription() {
        return "Database Search";
    }


    public JComponent getComponent() {


        return component;

    }


    public final JComponent getSearchOptions() {

        JComponent options = Box.createHorizontalBox();
        JComponent config = PanelFactory.createDialogPanel("right:p, 4dlu, p", "p, 4dlu, p");

        options.add(config); // search box and database selection

        JScrollPane pane = new JScrollPane(getCandidateTable());
        pane.setBorder(Borders.EMPTY_BORDER);
        table.setPreferredSize(new Dimension(300, 484));

        options.add(pane); // display candidates

        CellConstraints cc = new CellConstraints();

        config.add(LabelFactory.newFormLabel("Query"), cc.xy(1, 1));
        config.add(field, cc.xy(3, 1));
        config.add(fuzzy, cc.xyw(1, 3, 3));

        return options;
    }


    public MoleculeTable getCandidateTable() {

        return table;

    }


    public void updateTable(String name) {

        if (name.isEmpty()) {
            return;
        }

        System.out.println(name);


        CandidateFactory<ChEBIIdentifier> factory = new CandidateFactory(ChEBINameService.getInstance(),
                                                                         new ChemicalFingerprintEncoder());

        Multimap<Integer, SynonymCandidateEntry> map = fuzzy.isSelected()
                                                       ? factory.getFuzzySynonymCandidates(name) : factory.getSynonymCandidates(name);


        List<Metabolite> metabolites = factory.getMetaboliteList(factory.getSortedList(map),
                                                                 new ChEBIIdentifier(),
                                                                 ChEBIChemicalDataService.getInstance(),
                                                                 "ChEBI",
                                                                 DefaultEntityFactory.getInstance());

        getCandidateTable().getModel().set(metabolites);

    }


    public void setup(Metabolite metabolite) {

        field.setText(metabolite.getName());

    }


    public boolean canTransferAnnotations() {
        return true;
    }


    public void transferAnnotations() {
    }
}
