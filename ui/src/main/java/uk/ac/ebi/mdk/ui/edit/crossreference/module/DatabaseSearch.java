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
package uk.ac.ebi.mdk.ui.edit.crossreference.module;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;
import uk.ac.ebi.caf.component.theme.ThemeManager;
import uk.ac.ebi.mdk.domain.annotation.Source;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.ui.component.MetaboliteMatchIndication;
import uk.ac.ebi.mdk.ui.component.table.MoleculeTable;
import uk.ac.ebi.mdk.ui.component.table.accessor.AccessionAccessor;
import uk.ac.ebi.mdk.ui.component.table.accessor.AnnotationAccess;
import uk.ac.ebi.mdk.ui.component.table.accessor.NameAccessor;
import uk.ac.ebi.mdk.ui.tool.annotation.CrossreferenceModule;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;


/**
 * DatabaseSearch 2012.02.01
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Class description
 * @version $Rev$ : Last Changed $Date$
 */
public class DatabaseSearch
        implements CrossreferenceModule {

    private static final Logger LOGGER = Logger.getLogger(DatabaseSearch.class);

    private JComponent component;

    private MetaboliteMatchIndication match = new MetaboliteMatchIndication();

    private MoleculeTable table;

    private JTextField field;

    private JCheckBox approximate;

    private Metabolite context;

    private Timer timer = new Timer(500, new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            updateTable(field.getText());
            timer.stop();
        }
    });


    public DatabaseSearch() {

        component = PanelFactory.createDialogPanel();
        component.setLayout(new FormLayout("p", "p, 4dlu, p"));

        field = FieldFactory.newField(25);
        approximate = new JCheckBox("Approximate");
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
        approximate.addItemListener(new ItemListener() {

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
        config.add(approximate, cc.xyw(1, 3, 3));
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


//        CandidateFactory<ChEBIIdentifier> factory = new CandidateFactory(LuceneService,
//                                                                         new ChemicalFingerprintEncoder());
//
//        Multimap<Integer, SynonymCandidateEntry> map = approximate.isSelected()
//                                                       ? factory.getFuzzySynonymCandidates(name)
//                                                       : factory.getSynonymCandidates(name);
//
//
//        List<Metabolite> metabolites = factory.getMetaboliteList(factory.getSortedList(map),
//                                                                 new ChEBIIdentifier(),
//                                                                 ChEBIChemicalDataService.getInstance(),
//                                                                 "ChEBI",
//                                                                 DefaultEntityFactory.getInstance());

        throw new UnsupportedOperationException("Fix Me");

        //getCandidateTable().getModel().set(metabolites);


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
