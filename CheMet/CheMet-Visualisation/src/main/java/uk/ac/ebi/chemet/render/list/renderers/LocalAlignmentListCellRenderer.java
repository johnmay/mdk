/**
 * LocalAlignmentListCellRenderer.java
 *
 * 2011.12.12
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
package uk.ac.ebi.chemet.render.list.renderers;

import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.ColorUtility;
import uk.ac.ebi.chemet.render.alignment.AlignmentRenderer;
import uk.ac.ebi.chemet.render.alignment.BasicAlignmentColor;
import uk.ac.ebi.chemet.render.alignment.BlastConsensusScorer;
import uk.ac.ebi.chemet.render.alignment.ConservationRenderer;
import uk.ac.ebi.interfaces.entities.GeneProduct;
import uk.ac.ebi.observation.sequence.LocalAlignment;
import uk.ac.ebi.chemet.render.ViewUtilities;

/**
 *          LocalAlignmentListCellRenderer - 2011.12.12 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class LocalAlignmentListCellRenderer
        extends ListCellRenderingPool<JLabel, LocalAlignment> {

    private static final Logger LOGGER = Logger.getLogger(LocalAlignmentListCellRenderer.class);
    private static final BasicAlignmentColor color = new BasicAlignmentColor(ColorUtility.EMBL_PETROL,
                                                                             ColorUtility.EMBL_PETROL,
                                                                             Color.lightGray);
    private final static ConservationRenderer COMPLEX_RENDERER = new ConservationRenderer(new Rectangle(0, 0, 750, 10),
                                                                                          color,
                                                                                          new BlastConsensusScorer(),
                                                                                          1);
    private final static AlignmentRenderer BASIC_RENDERER = new AlignmentRenderer(new Rectangle(0, 0, 750, 10),
                                                                                  color,
                                                                                  1);

    public LocalAlignmentListCellRenderer() {
        COMPLEX_RENDERER.setGranularity(0.8f);
    }

    @Override
    public JLabel create() {
        JLabel label = new JLabel();
        label.setOpaque(true);
        return label;
    }

    @Override
    public void expire(JLabel component) {
        // nothing to close
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean setup(JLabel label,
                         LocalAlignment alignment) {

        AlignmentRenderer renderer = alignment.hasSequences() ? COMPLEX_RENDERER : BASIC_RENDERER;
        Icon icon = new ImageIcon(renderer.render(alignment, (GeneProduct) alignment.getEntity()));
        label.setIcon(icon);
        label.setFont(ViewUtilities.DEFAULT_BODY_FONT);
        label.setText(alignment.getSubject());
        label.setToolTipText(ViewUtilities.htmlWrapper(alignment.getHTMLSummary()));

        return true;

    }
}
