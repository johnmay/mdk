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
            public JLabel getComponent(JList list, Identifier value, int index) {
                JLabel label = super.getComponent(list, value, index);
                label.setText(value.getShortDescription());
                label.setToolTipText(value.getLongDescription());
                return label;
            }
        });
    }

}
