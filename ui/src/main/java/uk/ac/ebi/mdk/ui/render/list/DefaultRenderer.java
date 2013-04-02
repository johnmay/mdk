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
public class DefaultRenderer<O>
        extends JLabel
        implements ListCellRenderer {

    private static final Logger LOGGER = Logger.getLogger(DefaultRenderer.class);

    public DefaultRenderer() {
        Theme theme = ThemeManager.getInstance().getTheme();
        setFont(theme.getBodyFont());
        setOpaque(true); // needs to be opaque for background
    }

    public JLabel getComponent(JList list, O value, int index) {
        setText(value.toString());
        return this;
    }

    @Override
    public final JLabel getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        if (value == null) {
            setText("");
            return this;
        }

        return getComponent(list, (O) value, index);

    }

}
