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

import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.rex.RExTag;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import static uk.ac.ebi.mdk.domain.annotation.rex.RExTag.Type.PRODUCT;
import static uk.ac.ebi.mdk.domain.annotation.rex.RExTag.Type.SUBSTRATE;

/**
 * Render a extract sentence from REx. Tagged values are highlighted.
 *
 * @author John May
 */
public class RExExtractRenderer implements TableCellRenderer {

    private final JTextPane sentence;

    private final Map<RExTag.Type, Style> style = new HashMap<RExTag.Type, Style>();

    public RExExtractRenderer() {
        this.sentence = new JTextPane();
        style.put(SUBSTRATE, sentence.addStyle("substrate", null));
        style.put(PRODUCT, sentence.addStyle("product", null));

        StyleConstants.setBackground(style.get(SUBSTRATE), new Color(255, 200,
                                                                     200));
        StyleConstants.setBackground(style.get(PRODUCT), new Color(200, 255,
                                                                   200));

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        RExExtract extract = (RExExtract) value;
        sentence.setText(extract.sentence());
        StyledDocument doc = sentence.getStyledDocument();
        for (final RExTag tag : extract.tags()) {
            doc.setCharacterAttributes(tag.start(), tag.length(), style.get(
                tag.type()), true);
        }
        return sentence;
    }
}
