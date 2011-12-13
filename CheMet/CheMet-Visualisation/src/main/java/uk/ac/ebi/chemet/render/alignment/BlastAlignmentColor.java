/**
 * BlastAlignmentColor.java
 *
 * 2011.07.15
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
package uk.ac.ebi.chemet.render.alignment;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.observation.sequence.LocalAlignment;

/**
 * @name    BlastAlignmentColor
 * @date    2011.07.15
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class BlastAlignmentColor
        extends AbstractAlignmentColor {

    private static final Logger LOGGER = Logger.getLogger(BlastAlignmentColor.class);

    public BlastAlignmentColor() {
        super(BufferedImage.TYPE_4BYTE_ABGR, Color.WHITE, null, null, null, Color.LIGHT_GRAY);
    }

    @Override
    public Color getMatchColor(Observation observation) {
        if (observation instanceof LocalAlignment) {
            double bitScore = ((LocalAlignment) observation).getBitScore();
            return BlastScoreColorRange.getColorForScore((int) bitScore).getColor();
        }
        return Color.RED;
    }
}
