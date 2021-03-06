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
package uk.ac.ebi.mdk.ui.render.table;

import com.jgoodies.forms.factories.Borders;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.Theme;
import uk.ac.ebi.caf.component.theme.ThemeManager;
import uk.ac.ebi.mdk.domain.entity.AbstractAnnotatedEntity;

import javax.swing.*;
import java.awt.*;

/**
 * @name    ListLinkRenderer - 2011.10.06 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ListLinkRenderer extends JLabel implements ListCellRenderer {

    private static final Logger LOGGER = Logger.getLogger(ListLinkRenderer.class);

    public ListLinkRenderer() {
        setFont(ThemeManager.getInstance().getTheme().getLinkFont());
        setBorder(Borders.EMPTY_BORDER);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        AbstractAnnotatedEntity entity = ((AbstractAnnotatedEntity) value);
        String name = entity.getAbbreviation() + " : " + entity.getName();
        setText(name.substring(0, Math.min(40, name.length())));
        setToolTipText(name);
        Theme theme = ThemeManager.getInstance().getTheme();
        setForeground(isSelected ? theme.getEmphasisedForeground() : theme.getForeground());
        return this;
    }
}
