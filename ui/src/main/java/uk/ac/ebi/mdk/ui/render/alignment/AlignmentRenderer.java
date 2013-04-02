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

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

/**
 * @name    LocalAlignmentRenderer
 * @date    2011.07.14
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class AlignmentRenderer
        extends AbstractAlignmentRenderer {

    private static final Logger LOGGER = Logger.getLogger(AlignmentRenderer.class);
    private Line2D traceLine;

    /**
     * Instantiates with specified bounds, color and padding
     * @param bounds
     * @param colorer
     * @param padding
     */
    public AlignmentRenderer(Rectangle bounds, AbstractAlignmentColor colorer,
                             Integer padding) {
        super(bounds, colorer, padding);
    }

    /**
     * Instantiates a renderer with specified bounds and colorer and padding of 10 % the heigh
     * @param bounds
     * @param colorer
     */
    public AlignmentRenderer(Rectangle bounds, AbstractAlignmentColor colorer) {
        this(bounds, colorer, bounds.height / 10);
    }

    /**
     * Renders an alignment and returns a buffered image
     * @param alignment
     * @return
     */
    public BufferedImage render(LocalAlignment alignment, GeneProduct product) {

        // get an empty image from the superclass
        BufferedImage image = newBufferedImage();


        // get the graphics and render to this
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        render(alignment, product, g2);
        g2.dispose();

        return image;

    }

    /**
     * Renders an alignment onto a Graphics2D object with the bounds specified in the object
     * @param alignment
     * @param g2
     */
    public void render(LocalAlignment alignment, GeneProduct product, Graphics2D g2) {
        render(alignment, product, g2, super.outerBounds, super.innerBounds);
    }

    /**
     * Renders an alignment onto a Graphics2D object with
     * specified bounds. This allows multiple draws on a single image.
     * e.g. stacked alignments as in a blast output
     * @param alignment
     * @param g2
     * @param outerBounds, Rectangle innerBounds
     */
    public void render(LocalAlignment alignment, GeneProduct product, Graphics2D g2,
                       Rectangle outerBounds, Rectangle innerBounds) {

        g2.setColor(super.color.getBackgroundColor());
        g2.fill(outerBounds);

        // draw the trace line
        g2.setColor(super.color.getTraceColor());
        g2.draw(getTraceLine(outerBounds, innerBounds));

        // draw the match region
        g2.setColor(super.color.getMatchColor(alignment));
        float sequenceLength = product.getSequences().iterator().next().getLength();
        // normalise length by the total length of the sequence
        int homologyStart = (int) (innerBounds.width * ((float) alignment.getQueryStart() / (float) sequenceLength));
        int homologyEnd = (int) (innerBounds.width * ((float) alignment.getQueryEnd() / (float) sequenceLength));
        float hitBarX = innerBounds.x + homologyStart;
        float hitBarY = innerBounds.y;
        float hitBarHeight = innerBounds.height;
        float hitBarWidth = homologyEnd - homologyStart;

        g2.fill(new Rectangle2D.Float(hitBarX, hitBarY, hitBarWidth, hitBarHeight));

    }

    /**
     * Build the trace line, only needs to be done once for multiple alignments
     * @return
     */
    protected Line2D getTraceLine(Rectangle outerBounds, Rectangle innerBounds) {

        // create a new traceline if the current object is null or the center point is different
        if (traceLine == null || outerBounds.getCenterY() != traceLine.getY1()) {

            Double centreY = outerBounds.getCenterY();
            traceLine = new Line2D.Double(innerBounds.x, centreY, innerBounds.width, centreY);
        }

        return traceLine;
    }
}
