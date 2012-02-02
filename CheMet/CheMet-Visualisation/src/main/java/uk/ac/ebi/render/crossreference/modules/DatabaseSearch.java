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
package uk.ac.ebi.render.crossreference.modules;

import com.google.common.collect.Multimap;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.Collection;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.Source;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;
import uk.ac.ebi.caf.component.theme.ThemeManager;
import uk.ac.ebi.chemet.render.components.MetaboliteMatchIndication;
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

    private MetaboliteMatchIndication match = new MetaboliteMatchIndication();

    private MoleculeTable table;

    private JTextField field;

    private JCheckBox fuzzy;

    private Metabolite context;

    private Timer timer = new Timer(500, new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            System.out.println("fired!");
            updateTable(field.getText());
            timer.stop();
        }
    });


    public DatabaseSearch() {

        component = PanelFactory.createDialogPanel();
        component.setLayout(new FormLayout("p", "p, 4dlu, p"));

        field = FieldFactory.newField(25);
        fuzzy = new JCheckBox("Fuzzy Search");
        table = new MoleculeTable(new NameAccessor(),
                                  new AccessionAccessor(),
                                  new AnnotationAccess(new Source()));
        table.setPreferredSize(new Dimension(300, 185));
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
        field.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                timer.restart();
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                Collection<Metabolite> entites = table.getSelectedEntities();
                if (entites.isEmpty()) {
                    return;
                }
                Metabolite m = entites.iterator().next();
                match.setSubject(m);
            }
        });



        CellConstraints cc = new CellConstraints();
        component.add(match.getComponent(), cc.xy(1, 1, cc.CENTER, cc.CENTER)); // visual inspector

        component.add(getSearchOptions(), cc.xy(1, 3, cc.CENTER, cc.CENTER)); // search options


    }


    public String getDescription() {
        return "Database Search";
    }


    public JComponent getComponent() {


        return component;

    }


    public final JComponent getSearchOptions() {

        JComponent options = Box.createHorizontalBox();
        JComponent config = PanelFactory.createDialogPanel("p:grow, 4dlu, p:grow", "p, 4dlu, p, 4dlu, p");

        options.add(config); // search box and database selection

        JScrollPane pane = new JScrollPane(getCandidateTable());
        pane.setBorder(Borders.EMPTY_BORDER);
        pane.setPreferredSize(new Dimension(300, 185));
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        options.add(pane); // display candidates

        CellConstraints cc = new CellConstraints();

        config.add(LabelFactory.newFormLabel("Query"), cc.xy(1, 1));
        config.add(field, cc.xy(3, 1));
        config.add(fuzzy, cc.xyw(1, 3, 3));
        config.add(ButtonFactory.newButton(new AbstractAction("Assign") {

            public void actionPerformed(ActionEvent e) {
                transferAnnotations();
            }
        }), cc.xy(1, 5));

        return options;
    }


    public MoleculeTable getCandidateTable() {

        return table;

    }


    public void updateTable(String name) {

        if (name.isEmpty() || !component.isVisible()) {
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

        match.setQuery(metabolite);

        this.context = metabolite;


    }


    public boolean canTransferAnnotations() {
        return true;
    }


    public void transferAnnotations() {

        for (Metabolite metabolte : table.getSelectedEntities()) {

            if (!context.getName().equals(metabolte.getName())) {
                context.addAnnotation(new Synonym(metabolte.getName()));
            }

            context.addAnnotation(new CrossReference(metabolte.getIdentifier()));

        }

    }
}
