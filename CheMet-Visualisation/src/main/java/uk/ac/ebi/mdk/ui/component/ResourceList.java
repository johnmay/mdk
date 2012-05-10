package uk.ac.ebi.mdk.ui.component;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.list.MutableJList;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.HMDBIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
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
        setCellRenderer(new DefaultRenderer() {
            @Override
            public JLabel getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                Identifier identifier = (Identifier) value;

                JLabel label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                label.setText(identifier.getShortDescription());
                label.setToolTipText(identifier.getLongDescription());

                return label;
            }
        });
    }

}
