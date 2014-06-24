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
package uk.ac.ebi.mdk.ui.edit.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ComboBoxFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.IdentifierLoader;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;


/**
 * CrossreferenceEditor 2012.02.03
 *
 * @author johnmay
 * @author $Author$ (this version) <p/> Class description
 * @version $Rev$ : Last Changed $Date$
 */
public class IdentifierEditor extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(IdentifierEditor.class);

    private JComboBox type;

    private JTextField field;

    private String DEFAULT_TEXT = "enter identifier";

    private static final DefaultIdentifierFactory ID_FACTORY = DefaultIdentifierFactory.getInstance();

    private Set<Class> classes;

    private SuggestType suggestion = new SuggestType();

    private Identifier identifier;

    private final JComponent component;
    private final JButton button;
    private final Color okay  = new Color(0xCBFFC8);
    private final Color empty = new Color(0xCAF1FF);

    public IdentifierEditor() {
        this(new ArrayList<Class<? extends Identifier>>());
    }

    @SuppressWarnings("unchecked")
    public IdentifierEditor(Collection<Class<? extends Identifier>> identifiers) {

        component = this;
        component.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        component.setBorder(BorderFactory.createEmptyBorder());

        classes = new TreeSet<Class>(new Comparator<Class>() {
            @Override
            public int compare(Class o1, Class o2) {
                IdentifierLoader loader = IdentifierLoader.getInstance();
                return loader.getShortDescription(o1).compareTo(loader.getShortDescription(o2));
            }
        });


        for (Identifier factoryID : ID_FACTORY.getSupportedIdentifiers()) {

            if (identifiers.isEmpty()) {
                classes.add(factoryID.getClass());
            }
            else {
                ACCEPT:
                for (Class<? extends Identifier> accepted : identifiers) {
                    if (accepted.isInstance(factoryID)) {
                        classes.add(factoryID.getClass());
                        break ACCEPT;
                    }
                }
            }

        }

        final Font monoSpace = new Font("Courier New", Font.BOLD, 12);
        field = new JTextField();
        field.setFont(monoSpace);
        type = ComboBoxFactory.newComboBox(classes);
        type.setFont(monoSpace);
        field.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        type.setBorder(BorderFactory.createEmptyBorder());
        type.setPreferredSize(new Dimension(150, 27));
        type.setOpaque(false);

        final JLabel label = LabelFactory.newLabel("N/A");
        label.setOpaque(true);
        label.setFont(monoSpace);

        label.setForeground(new Color(0x444444));

        type.setRenderer(new ListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                label.setBackground(component.getBackground());
                if (value != null) {
                    label.setText(ID_FACTORY.ofClass((Class<Identifier>) value).getShortDescription());
                    label.setToolTipText(ID_FACTORY.ofClass((Class<Identifier>) value).getLongDescription());
                } else {
                    label.setText("---");
                    label.setToolTipText("---");
                }
                return label;
            }
        });
        type.setEnabled(false);

        button = new BasicArrowButton(BasicArrowButton.SOUTH);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setOpaque(true);

        // remove border 
        // http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4515838
        type.setUI(new BasicComboBoxUI() {
            @Override protected JButton createArrowButton() {
                return button;
            }

            @Override
            public void paintCurrentValueBackground(
                    Graphics g, Rectangle bounds, boolean hasFocus) {
            }
        });

        // colouring
        component.setBackground(okay);

        // hide the type selection
        if (classes.size() == 1) {
            type.setVisible(false);
        }

        add(type);
        add(Box.createHorizontalStrut(5));
        add(field);
        type.resetKeyboardActions();

        field.setText(DEFAULT_TEXT);

        // remove prompt when user clicks on the textfield
        field.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(DEFAULT_TEXT)) {
                    field.setText("");
                }
            }
        });

        turnOnSuggestion();

    }

    @Override public void setBackground(Color bg) {
        super.setBackground(bg);
        if (type != null) type.setBackground(bg);
        if (field != null) field.setBackground(bg);
        if (button != null) button.setBackground(bg);
    }

    /**
     * Turns on identifier suggestion. On every update of the field the combobox
     * model will now change to reflect the content of the field
     */
    public final void turnOnSuggestion() {
        field.getDocument().removeDocumentListener(suggestion);
        field.getDocument().addDocumentListener(suggestion);
    }


    /**
     * Turns off identifier suggestion
     */
    public final void turnOffSuggestion() {
        field.getDocument().removeDocumentListener(suggestion);
    }


    /**
     * Whether the text field has been filled in
     *
     * @return
     */
    public final boolean isFilled() {
        return !field.getText().isEmpty()
                && !field.getText().trim().equals(DEFAULT_TEXT);
    }


    /**
     * Access an instance of the edited identifier
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Identifier getIdentifier() {

        String accession = field.getText().trim();
        Class<Identifier> c = (Class<Identifier>) type.getSelectedItem();

        // if the idenfier is the same just return the current instance
        if (identifier != null
                && identifier.getAccession().equals(accession)
                && c == identifier.getClass()) {
            return identifier;
        }
        
        if (c == null)
            return null;
        
        identifier = ID_FACTORY.ofClass(c);

        if (!accession.isEmpty())
            identifier.setAccession(accession);

        return identifier;

    }


    /**
     * Set the identifier to display as editable
     *
     * @param identifier
     */
    public void setIdentifier(Identifier identifier) {

        this.identifier = identifier;

        field.setText(identifier.getAccession());
        type.setSelectedItem(identifier.getClass());
        type.setEnabled(isFilled());

    }


    /**
     * Private member the does the suggestions.
     */
    private class SuggestType implements DocumentListener {

        public void changedUpdate(DocumentEvent e) {
            suggest();
        }


        public void insertUpdate(DocumentEvent e) {
            suggest();
        }


        public void removeUpdate(DocumentEvent e) {
            suggest();
        }


        public void suggest() {

            component.setBackground(empty);
            String accession = field.getText().trim();

            if (!accession.isEmpty())
                component.setBackground(okay);

            DefaultComboBoxModel model = (DefaultComboBoxModel) type.getModel();
            model.removeAllElements();
            for (Class<? extends Identifier> c : ID_FACTORY.ofPattern(accession)) {
                model.addElement(c);
            }
            
            type.repaint();
        }
    }
}
