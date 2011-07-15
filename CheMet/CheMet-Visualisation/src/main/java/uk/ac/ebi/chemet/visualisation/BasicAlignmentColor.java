package uk.ac.ebi.chemet.visualisation;


/**
 * BasicAlignmentColor.java
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

/**
 * @name    BasicAlignmentColor
 * @date    2011.07.14
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Basic implementation of the AbstractAlignmentColor. This class
 *          provides storage of alignment coloring parameters for match and
 *          mismatch colors
 */
public class BasicAlignmentColor
        extends AbstractAlignmentColor {

    private static final Logger LOGGER = Logger.getLogger( BasicAlignmentColor.class );

    /**
     * Basic implementation with a 'black' trace line and 'white' background. The match and mismatch
     * colors remain to be specified. The image type is BufferedImage.TYPE_4BYTE_ABGR
     * @param matchColor Color for a matched alignment
     * @param mismatchColor Color for a mismatch alignment
     */
    public BasicAlignmentColor( Color matchColor ,
                                Color mismatchColor ) {
        super( BufferedImage.TYPE_4BYTE_ABGR , Color.WHITE , matchColor , mismatchColor , Color.BLACK );
    }
}
