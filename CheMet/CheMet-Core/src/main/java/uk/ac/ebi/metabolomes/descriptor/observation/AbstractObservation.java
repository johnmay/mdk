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
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import uk.ac.ebi.metabolomes.core.gene.OldGeneProduct;


/**
 * Abstract Observation, all observations should extend this class
 * @author johnmay
 */
public abstract class AbstractObservation
  implements Externalizable {

    private JobParameters parameters = new JobParameters();
    private OldGeneProduct product;
    private boolean highlight = false;
    private Color background = null;
    private Color highColor = Color.YELLOW;

    // todo abstract ObservationType getObservationType();

    public abstract String getObservationName();


    public abstract String getObservationDescription();


    public Color getBackground() {
        return highlight ? highColor : background;
    }


    public OldGeneProduct getProduct() {
        return product;
    }


    public void setProduct(OldGeneProduct product) {
        this.product = product;
    }


    public BufferedImage getObservationImage(int width, int height) {
        BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = buff.getGraphics();
        g.fillRect(0, 0, width, height);
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


    public void setParameters(JobParameters parameters) {
        this.parameters = parameters;
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        parameters = (JobParameters) in.readObject();
        boolean hasProduct = in.readBoolean();
        if( hasProduct ) {
            product = (OldGeneProduct) in.readObject();
        }
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(parameters);
        out.writeBoolean(product != null);
        if( product != null ) {
            out.writeObject(product);
        }
    }


}

