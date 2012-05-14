package uk.ac.ebi.mdk.ui.component;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ComboBoxFactory;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.DefaultReconstructionManager;
import uk.ac.ebi.mdk.domain.entity.collection.ReconstructionManager;
import uk.ac.ebi.mdk.ui.render.list.DefaultRenderer;

import javax.swing.*;

/**
 * @author John May
 */
public class ReconstructionComboBox {

    private static final Logger LOGGER = Logger.getLogger(ReconstructionComboBox.class);

    private JComboBox comboBox;
    private DefaultComboBoxModel model = new DefaultComboBoxModel();


    public ReconstructionComboBox() {
    }

    /**
     * Updates the combo-box model to such currently managed (open) reconstructions
     */
    public void refresh() {
        model.removeAllElements();
        ReconstructionManager manager = DefaultReconstructionManager.getInstance();
        for (Reconstruction reconstruction : manager.getProjects()) {
            model.addElement(reconstruction);
        }
    }

    public JComboBox createComboBox() {

        JComboBox box = ComboBoxFactory.newComboBox();
        box.setModel(model);

        refresh();

        box.setRenderer(new DefaultRenderer<Reconstruction>() {
            @Override
            public JLabel getRendererComponent(JList list, Reconstruction value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = super.getRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(value.getAccession());
                label.setToolTipText(value.getAbbreviation() + ": " + value.getName());
                return label;
            }
        });

        return box;

    }


    /**
     * Might need to call refresh prior to setSelected
     *
     * @param reconstruction
     */
    public void setSelected(Reconstruction reconstruction) {
        model.setSelectedItem(reconstruction);
    }

    public Reconstruction getSelected() {
        return (Reconstruction) model.getSelectedItem();
    }

    public JComponent getComponent() {
        if (comboBox == null)
            comboBox = createComboBox();
        return comboBox;
    }

}
