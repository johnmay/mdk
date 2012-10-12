package uk.ac.ebi.mdk.ui.edit.reaction;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.ComboBoxFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.tool.CompartmentResolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DialogCompartmentResolver implements CompartmentResolver {

    private static final Logger LOGGER = Logger.getLogger(DialogCompartmentResolver.class);

    private CompartmentResolver resolver;
    private Window              window;
    private boolean             okayClicked;

    public DialogCompartmentResolver(CompartmentResolver parent,
                                     Window window) {
        this.resolver = parent;
        this.window = window;
    }

    @Override
    public Compartment getCompartment(String compartment) {
        return resolver.isAmbiguous(compartment) ? showDialog(compartment) : resolver.getCompartment(compartment);
    }

    public Compartment showDialog(String compartment) {

        final JDialog dialog = new JDialog(window, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLocationRelativeTo(window);

        List<Compartment> compartmentList = new ArrayList<Compartment>(getCompartments());
        Collections.sort(compartmentList, new Comparator<Compartment>() {
            @Override
            public int compare(Compartment o1, Compartment o2) {
                return o1.getAbbreviation().compareTo(o2.getAbbreviation());
            }
        });
        JComboBox comboBox = ComboBoxFactory.newComboBox(compartmentList);

        final JLabel label = LabelFactory.newLabel("");
        comboBox.setRenderer(new ListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Compartment compartment = (Compartment) value;
                label.setText(compartment.getAbbreviation() + ": " + compartment.getDescription());
                label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                label.setForeground(list.getForeground());
                return label;
            }

        });

        CellConstraints cc = new CellConstraints();
        JPanel panel = PanelFactory.create();
        panel.setLayout(new FormLayout("p, 4dlu, p, 4dlu, p",
                                       "p, 4dlu, p"));
        panel.setBorder(Borders.DLU4_BORDER);
        panel.add(LabelFactory.newLabel("Please select the correct compartment for the given notation:"),
                  cc.xyw(1, 1, 5));
        panel.add(LabelFactory.newFormLabel(compartment),
                  cc.xy(1, 3));
        panel.add(comboBox,
                  cc.xy(3, 3));
        okayClicked = false;
        panel.add(ButtonFactory.newButton(new AbstractAction("Okay") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                okayClicked = true;
            }
        }),
                  cc.xy(5, 3));
        dialog.setContentPane(panel);
        dialog.pack();

        dialog.setVisible(true);

        return okayClicked ? (Compartment) comboBox.getSelectedItem() : null;

    }

    /**
     * Delegates to the parent resolver
     *
     * @param compartment
     *
     * @return
     */
    @Override
    public List<Compartment> getCompartments(String compartment) {
        return resolver.getCompartments(compartment);
    }

    @Override
    public boolean isAmbiguous(String compartment) {
        return resolver.isAmbiguous(compartment);
    }

    @Override
    public Collection<Compartment> getCompartments() {
        return new HashSet<Compartment>(resolver.getCompartments());
    }
}
