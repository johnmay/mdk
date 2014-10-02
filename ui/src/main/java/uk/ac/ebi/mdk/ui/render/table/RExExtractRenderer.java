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

import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.rex.RExTag;

import javax.swing.Box;
import javax.swing.JLabel;
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

import static uk.ac.ebi.mdk.domain.annotation.rex.RExTag.Type.ACTION;
import static uk.ac.ebi.mdk.domain.annotation.rex.RExTag.Type.MODIFIER;
import static uk.ac.ebi.mdk.domain.annotation.rex.RExTag.Type.PRODUCT;
import static uk.ac.ebi.mdk.domain.annotation.rex.RExTag.Type.SUBSTRATE;

/**
 * Render a extract sentence from REx. Tagged values are highlighted.
 *
 * @author John May
 */
public class RExExtractRenderer implements TableCellRenderer {

    private final Box panel = Box.createVerticalBox();
    private final JLabel    source;
    private final JTextPane sentence;

    private final Map<RExTag.Type, Style> style = new HashMap<RExTag.Type, Style>();

    public RExExtractRenderer() {
        this.sentence = new JTextPane();
        this.source = LabelFactory.newLabel("");
        this.panel.add(source);
        this.panel.add(sentence);

        source.setAlignmentX(Component.LEFT_ALIGNMENT);
        sentence.setAlignmentX(Component.LEFT_ALIGNMENT);

        sentence.setFont(source.getFont());
        sentence.setForeground(source.getForeground());

        style.put(ACTION, sentence.addStyle("action", null));
        style.put(SUBSTRATE, sentence.addStyle("participant", null));
        style.put(PRODUCT, style.get(SUBSTRATE));
        style.put(MODIFIER, sentence.addStyle("modifier", null));

        StyleConstants.setBackground(style.get(ACTION), new Color(255, 200,
                                                                  200));
        StyleConstants.setBackground(style.get(PRODUCT), new Color(200, 255,
                                                                   200));
        StyleConstants.setBackground(style.get(MODIFIER), new Color(200, 200,
                                                                    255));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        RExExtract extract = (RExExtract) value;
        source.setText(extract.source().getAccession());
        sentence.setText(extract.sentence());
        StyledDocument doc = sentence.getStyledDocument();
        for (final RExTag tag : extract.tags()) {
            doc.setCharacterAttributes(tag.start(), tag.length(), style.get(
                tag.type()), true);
        }

        // pack the row around the label and pane
        int w = table.getColumnModel().getColumn(column).getWidth();
        sentence.setSize(w, Short.MAX_VALUE);
        sentence.setSize(w, sentence.getPreferredSize().height);
        int h = sentence.getHeight() + source.getHeight() + 20;

        if(table.getRowHeight(row) != h){
            table.setRowHeight(row, h);
        }

        return panel;
    }
}
