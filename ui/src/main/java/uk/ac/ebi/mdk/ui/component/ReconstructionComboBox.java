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
        for (Reconstruction reconstruction : manager.reconstructions()) {
            model.addElement(reconstruction);
        }
    }

    public JComboBox createComboBox() {

        JComboBox box = ComboBoxFactory.newComboBox();
        box.setModel(model);

        refresh();

        box.setRenderer(new DefaultRenderer<Reconstruction>() {
            @Override
            public JLabel getComponent(JList list, Reconstruction value, int index) {
                setText(value.getAccession());
                setToolTipText(value.getAbbreviation() + ": " + value.getName());
                return this;
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
