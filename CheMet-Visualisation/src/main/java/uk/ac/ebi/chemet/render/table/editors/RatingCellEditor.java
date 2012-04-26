/**
 * RatingCellEditor.java
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
package uk.ac.ebi.chemet.render.table.editors;

import com.explodingpixels.data.Rating;
import com.explodingpixels.macwidgets.ITunesRatingTableCellEditor;

import java.awt.Component;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JTable;
import uk.ac.ebi.mdk.domain.entity.StarRating;

/**
 *          RatingCellEditor - 2011.12.09 <br>
 *          Class wrap MacWidgets rating cell editor
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class RatingCellEditor extends ITunesRatingTableCellEditor {

    private static final Map<StarRating, Rating> ratingMap = new EnumMap<StarRating, Rating>(StarRating.class);
    private static final Map<Rating, StarRating> ratingInvMap = new EnumMap<Rating, StarRating>(Rating.class);

    static {
        ratingMap.put(StarRating.ONE_STAR, Rating.ONE_STAR);
        ratingMap.put(StarRating.TWO_STARS, Rating.TWO_STARS);
        ratingMap.put(StarRating.THREE_STARS, Rating.THREE_STARS);
        ratingMap.put(StarRating.FOUR_STARS, Rating.FOUR_STARS);
        ratingMap.put(StarRating.FIVE_STARS, Rating.FIVE_STARS);
        ratingMap.put(StarRating.NO_RATING, Rating.NO_RATING);
        ratingInvMap.put(Rating.ONE_STAR, StarRating.ONE_STAR);
        ratingInvMap.put(Rating.TWO_STARS, StarRating.TWO_STARS);
        ratingInvMap.put(Rating.THREE_STARS, StarRating.THREE_STARS);
        ratingInvMap.put(Rating.FOUR_STARS, StarRating.FOUR_STARS);
        ratingInvMap.put(Rating.FIVE_STARS, StarRating.FIVE_STARS);
        ratingInvMap.put(Rating.NO_RATING, StarRating.NO_RATING);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        if (value instanceof StarRating) {
            return super.getTableCellEditorComponent(table, ratingMap.get((StarRating) value), isSelected, row, column);
        }

        return super.getTableCellEditorComponent(table, Rating.NO_RATING, isSelected, row, column);
    }

    @Override
    public StarRating getCellEditorValue() {
        Object rating = super.getCellEditorValue();
        return ratingInvMap.get((Rating) rating);
    }
}
