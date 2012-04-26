/**
 * RatingCellRenderer.java
 *
 * 2011.12.09
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.chemet.render.table.renderers;

import com.explodingpixels.data.Rating;
import com.explodingpixels.macwidgets.ITunesRatingTableCellRenderer;
import java.awt.Component;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JTable;
import uk.ac.ebi.mdk.domain.entity.StarRating;

/**
 *          RatingCellRenderer - 2011.12.09 <br>
 *          Class wraps MacWidgets render for use with our own Rating class
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class RatingCellRenderer extends ITunesRatingTableCellRenderer {

    private static final Map<StarRating, Rating> ratingMap = new EnumMap<StarRating, Rating>(StarRating.class);

    static {
        ratingMap.put(StarRating.ONE_STAR, Rating.ONE_STAR);
        ratingMap.put(StarRating.TWO_STARS, Rating.TWO_STARS);
        ratingMap.put(StarRating.THREE_STARS, Rating.THREE_STARS);
        ratingMap.put(StarRating.FOUR_STARS, Rating.FOUR_STARS);
        ratingMap.put(StarRating.FIVE_STARS, Rating.FIVE_STARS);
        ratingMap.put(StarRating.NO_RATING, Rating.NO_RATING);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (value instanceof StarRating) {
            return super.getTableCellRendererComponent(table, ratingMap.get((StarRating) value), isSelected, hasFocus, row, column);
        }

        return super.getTableCellRendererComponent(table, Rating.NO_RATING, isSelected, hasFocus, row, column);
    }
}
