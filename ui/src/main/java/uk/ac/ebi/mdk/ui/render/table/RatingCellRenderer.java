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

import com.explodingpixels.data.Rating;
import com.explodingpixels.macwidgets.RatingComponent;
import uk.ac.ebi.mdk.domain.entity.StarRating;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * RatingCellRenderer - 2011.12.09 <br>
 * Class wraps MacWidgets render for use with our own Rating class
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class RatingCellRenderer extends DefaultTableCellRenderer {

    private static final Map<StarRating, RatingComponent> components = new HashMap<StarRating, RatingComponent>();
    private final        JLabel                           label      = new JLabel();

    static {
        components.put(StarRating.NO_RATING, new RatingComponent(Rating.NO_RATING));
        components.put(StarRating.ONE_STAR, new RatingComponent(Rating.ONE_STAR));
        components.put(StarRating.TWO_STARS, new RatingComponent(Rating.TWO_STARS));
        components.put(StarRating.THREE_STARS, new RatingComponent(Rating.THREE_STARS));
        components.put(StarRating.FOUR_STARS, new RatingComponent(Rating.FOUR_STARS));
        components.put(StarRating.FIVE_STARS, new RatingComponent(Rating.FIVE_STARS));
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

//        // get correct bg/fg color
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        JComponent component = components.get((StarRating) value).getComponent();

        component.setOpaque(isOpaque());
        component.setBackground(new Color(getBackground().getRGB()));
        component.setForeground(new Color(getForeground().getRGB()));
        component.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        return component;
    }


}
