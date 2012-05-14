package uk.ac.ebi.mdk.ui.component;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.list.MutableJList;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.ui.render.list.DefaultRenderer;

import javax.swing.*;

/**
 * Provides a list of resources/identifier types
 *
 * @author John May
 */
public class ResourceList extends MutableJList<Identifier> {

    private static final Logger LOGGER = Logger.getLogger(ResourceList.class);

    public ResourceList() {
        super(Identifier.class);
        setCellRenderer(new DefaultRenderer<Identifier>() {
            @Override
            public JLabel getRendererComponent(JList list, Identifier value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = super.getRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(value.getShortDescription());
                label.setToolTipText(value.getLongDescription());
                return label;
            }
        });
    }

}
