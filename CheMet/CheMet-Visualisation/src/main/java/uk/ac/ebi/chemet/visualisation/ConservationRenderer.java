/**
 * ConservationRenderer.java
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
package uk.ac.ebi.chemet.visualisation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.GeneProduct;
import uk.ac.ebi.observation.sequence.LocalAlignment;
import uk.ac.ebi.visualisation.ColorUtilities;

/**
 * @name    ConservationRenderer - 2011.10.11 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ConservationRenderer extends AlignmentRenderer {

    private static final Logger LOGGER = Logger.getLogger(ConservationRenderer.class);
    private ConsensusScorer scorer;
    private Map<Integer, Color> colorMap = new HashMap();
    private float granularity = 0.1f;

    public ConservationRenderer(Rectangle bounds, AbstractAlignmentColor colorer, ConsensusScorer scorer) {
        super(bounds, colorer);
        this.scorer = scorer;
        buildColorMap();

    }

    public ConservationRenderer(Rectangle bounds, AbstractAlignmentColor colorer, ConsensusScorer scorer, Integer padding) {
        super(bounds, colorer, padding);
        this.scorer = scorer;
        buildColorMap();
    }

    private void buildColorMap() {
        Color color = super.color.getMatchColor(null);
        for (int i = 0; i <= 10; i++) {
            colorMap.put(i, ColorUtilities.shade(color, (1 - (i * 0.1f)) / 4));
        }
    }

    /**
     * Sets the granularity of the conservation diagram. 1 = 100px and 100 = 1px.
     * @param granularity 
     */
    public void setGranularity(float granularity) {
        this.granularity = granularity;
    }

    /**
     * Renders a diagram of an alignment colored by conservation. Lighter colors indicate less conservation
     */
    @Override
    public void render(LocalAlignment alignment, GeneProduct product, Graphics2D g2, Rectangle outerBounds, Rectangle innerBounds) {

        g2.setColor(super.color.getBackgroundColor());
        g2.fill(outerBounds);

        // draw the trace line
        g2.setColor(super.color.getTraceColor());
        g2.draw(getTraceLine(outerBounds, innerBounds));

        // draw the match region
        g2.setColor(super.color.getMatchColor(alignment));
        float sequenceLength = product.getSequence().getLength();
        // normalise length by the total length of the sequence
        int homologyStart = (int) (innerBounds.width * ((float) alignment.getQueryStart() / (float) sequenceLength));
        int homologyEnd = (int) (innerBounds.width * ((float) alignment.getQueryEnd() / (float) sequenceLength));
        float hitBarX = innerBounds.x + homologyStart;
        float hitBarY = innerBounds.y;
        float hitBarHeight = innerBounds.height;
        float hitBarWidth = homologyEnd - homologyStart;

        // score
        String score = alignment.getAlignmentSequence();


        // using the granularity we see get the width of the view and the model (alignment)
        double viewWidth = hitBarWidth / (hitBarWidth * granularity);
        double modelWidth = score.length() / (hitBarWidth / viewWidth);

        int increment = (int) Math.ceil(modelWidth);

        for (int i = 0; i < score.length(); i += increment) {
            String region = score.substring(i, Math.min(i + increment, score.length()));
            g2.setColor(getColor(region));
            g2.fill(new Rectangle2D.Double(hitBarX, hitBarY, (increment / modelWidth) * viewWidth, hitBarHeight));
            hitBarX += (increment / modelWidth) * viewWidth;
        }

    }

    /**
     * Returns the color given an alignment concensus
     */
    public Color getColor(String concensus) {

        StringCharacterIterator it = new StringCharacterIterator(concensus);
        float score = 0f;
        for (char c = it.first(); c != it.DONE; c = it.next()) {
            score += scorer.score(c);
        }
        score /= concensus.length();

        return colorMap.get(Math.round(score));

    }
}
