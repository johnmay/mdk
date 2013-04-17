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

import uk.ac.ebi.mdk.domain.observation.Observation;

/**
 * @name    AbstractAlignmentColor
 * @date    2011.07.14
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public abstract class AbstractAlignmentColor {

    public final int imageType;
    public final Color backgroundColor;
    public final Color match;
    public final Color equivalent;
    public final Color mismatch;
    public final Color traceColor;
    // methods?

    public AbstractAlignmentColor( int imageType , Color backgroundColor ,
                                   Color matchColor , Color equivalentColor, Color mismatchColor ,
                                   Color traceColor ) {
        this.imageType = imageType;
        this.backgroundColor = backgroundColor;
        this.match = matchColor;
        this.equivalent = equivalentColor;
        this.mismatch = mismatchColor;
        this.traceColor = traceColor;
    }

    /**
     * Returns the image type stored in the alignment color
     * @return Image type e.g. BufferedImage.TYPE_4BYTE_ABGR;
     */
    public int getImageType() {
        return imageType;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getMatchColor( Observation observation ) {
        return match;
    }

    public Color getMismatchColor( Observation observation ) {
        return mismatch;
    }

    public Color getTraceColor() {
        return traceColor;
    }
}
