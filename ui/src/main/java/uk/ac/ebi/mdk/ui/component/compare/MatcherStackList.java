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

package uk.ac.ebi.mdk.ui.component.compare;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.list.MutableJList;
import uk.ac.ebi.mdk.ui.render.list.DefaultRenderer;

import javax.swing.*;

/**
 * Provides editing of entity matcher preference
 *
 * @author John May
 */
public class MatcherStackList extends MutableJList<MatcherDescription> {

    private static final Logger LOGGER = Logger.getLogger(MatcherStackList.class);

    public MatcherStackList() {
        super(MatcherDescription.class);
        setCellRenderer(new DefaultRenderer<MatcherDescription>() {
            @Override
            public JLabel getComponent(JList list, MatcherDescription matcher, int index) {

                setText(matcher.getName());
                setToolTipText(matcher.getDescription());

                return this;

            }
        });
    }

}
