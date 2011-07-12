/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package uk.ac.ebi.metabolomes.descriptor.observation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;

/**
 * Abstract Observation, all observations should extend this class
 * @author johnmay
 */
public abstract class AbstractObservation
        implements Serializable {

    private JobParameters parameters;
    private static final long serialVersionUID = -8388589068796518323L;
    private GeneProduct product;
    private boolean highlight;


    // todo abstract ObservationType getObservationType();
    public abstract String getObservationName();

    public abstract String getObservationDescription();
    private Color background = null;
    private Color highColor = Color.YELLOW;

    public Color getBackground() {
        return highlight ? highColor : background;
    }

    public GeneProduct getProduct() {
        return product;
    }

    public void setProduct( GeneProduct product ) {
        this.product = product;
    }

    public BufferedImage getObservationImage( int width , int height ) {
        BufferedImage buff = new BufferedImage( width , height , BufferedImage.TYPE_4BYTE_ABGR );
        Graphics g = buff.getGraphics();
        g.fillRect( 0 , 0 , width , height );
        return buff;
    }

    public void toggleHighlight() {
        highlight = highlight ? false : true;
    }

    public boolean isHighlighted() {
        return highlight;
    }

    public JobParameters getParameters() {
        return parameters;
    }

    public void setParameters( JobParameters parameters ) {
        this.parameters = parameters;
    }
}
