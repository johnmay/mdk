package uk.ac.ebi.mdk.ui.render.list;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.Theme;
import uk.ac.ebi.caf.component.theme.ThemeManager;

import javax.swing.*;

/**
 * Renderer using the normal font/colour for list
 *
 * @author John May
 */
public class DefaultRenderer
        extends JLabel
        implements ListCellRenderer {

    private static final Logger LOGGER = Logger.getLogger(DefaultRenderer.class);

    public DefaultRenderer() {
        Theme theme = ThemeManager.getInstance().getTheme();
        setFont(theme.getBodyFont());
        setOpaque(true); // needs to be opaque for background
    }

    @Override
    public JLabel getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        this.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        this.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        this.setText(value.toString());

        return this;

    }

}
