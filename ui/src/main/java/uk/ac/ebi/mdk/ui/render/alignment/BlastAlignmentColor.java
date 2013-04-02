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
package uk.ac.ebi.mdk.ui.render.alignment;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

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
