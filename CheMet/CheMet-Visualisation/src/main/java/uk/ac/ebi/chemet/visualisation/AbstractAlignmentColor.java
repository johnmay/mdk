package uk.ac.ebi.chemet.visualisation;

/**
 * AbstractAlignmentColor.java
 *
 * 2011.07.14
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
import java.awt.Color;
import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.metabolomes.descriptor.observation.AbstractObservation;

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
    public final Color matchColor;
    public final Color mismatchColor;
    public final Color traceColor;
    // methods?

    public AbstractAlignmentColor( int imageType , Color backgroundColor ,
                                   Color matchColor , Color mismatchColor ,
                                   Color traceColor ) {
        this.imageType = imageType;
        this.backgroundColor = backgroundColor;
        this.matchColor = matchColor;
        this.mismatchColor = mismatchColor;
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
        return matchColor;
    }

    public Color getMismatchColor( Observation observation ) {
        return mismatchColor;
    }

    public Color getTraceColor() {
        return traceColor;
    }
}
