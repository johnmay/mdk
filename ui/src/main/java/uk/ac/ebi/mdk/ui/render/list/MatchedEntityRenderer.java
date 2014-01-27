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

import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.mdk.domain.observation.MatchedEntity;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;

/**
 * LocalAlignmentListCellRenderer - 2011.12.12 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class MatchedEntityRenderer implements ListCellRenderer {

    private final Box    panel;
    private final JLabel entityLabel, reconLabel;

    public MatchedEntityRenderer() {
        panel = Box.createHorizontalBox();
        panel.add((entityLabel = LabelFactory.emptyLabel()));
        panel.add((reconLabel = LabelFactory.emptyLabel()));
    }

    @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        MatchedEntity match = (MatchedEntity) value;

        // todo: item specific rendering (i.e. chemical structures)
        reconLabel.setText(match.entityId().getAccession());
        reconLabel.setText(match.reconId().getAccession());

        return panel;
    }
}
