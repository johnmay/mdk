/**
 * ColorUtilities.java
 *
 * 2011.10.11
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
package uk.ac.ebi.visualisation;

import java.awt.Color;
import org.apache.log4j.Logger;

/**
 * @name    ColorUtilities - 2011.10.11 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ColorUtilities {

    private static final Logger LOGGER = Logger.getLogger(ColorUtilities.class);
    public static Color EMBL_PETROL = new Color(Integer.parseInt("006666", 16));
    public static Color CLEAR = new Color(0, 0, 0, 0);
    public static Color CLEAR_BLACK = new Color(0, 0, 0, 0);
    public static Color CLEAR_WHITE = new Color(255, 255, 255, 0);

    /**
     * Shades a colour by the given amount (0-1). A positive value will lighten the colour whilst a negative value
     * will darken the colour.The colour is transform from RGB to HSB where the amount is added to the brightness and
     * subtracted the saturation
     * @param color The color to shade
     * @param amount The amount to shade by
     * @return
     */
    public static Color shade(final Color color, final float amount) {

        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);


        return Color.getHSBColor(hsb[0],
                                 Math.max(0f, hsb[1] - amount), // decrease saturation
                                 Math.min(1f, hsb[2] + amount)); // increase brightness

    }

    /**
     * Mixes two colour by the provided percentages
     * @param color1
     * @param percentageColor1
     * @param color2
     * @param percentageColor2
     * @return
     */
    public static Color getMixedColor(Color color1, float percentageColor1, Color color2, float percentageColor2) {
        float[] clr1 = color1.getComponents(null);
        float[] clr2 = color2.getComponents(null);
        for (int i = 0; i < clr1.length; i++) {
            clr1[i] = (clr1[i] * percentageColor1) + (clr2[i] * percentageColor2);
        }
        return new Color(clr1[0], clr1[1], clr1[2], clr1[3]);
    }

    /**
     * Returns white or black text depending on the background.
     * See here http://www.codeproject.com/KB/GDI-plus/IdealTextColor.aspx
     * @param background
     * @return
     */
    public static Color getTextColor(Color background) {
        int threshold = 105;
        int delta = (int) ((background.getRed() * 0.299) + (background.getGreen() * 0.587)
                           + (background.getBlue() * 0.114));

        return (255 - delta < threshold) ? Color.BLACK : Color.WHITE;
    }
}
