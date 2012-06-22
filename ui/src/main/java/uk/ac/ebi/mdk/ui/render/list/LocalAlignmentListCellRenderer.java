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
package uk.ac.ebi.mdk.ui.render.list;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.ColorUtility;
import uk.ac.ebi.caf.utility.TextUtility;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;
import uk.ac.ebi.mdk.ui.render.alignment.AlignmentRenderer;
import uk.ac.ebi.mdk.ui.render.alignment.BasicAlignmentColor;
import uk.ac.ebi.mdk.ui.render.alignment.BlastConsensusScorer;
import uk.ac.ebi.mdk.ui.render.alignment.ConservationRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * LocalAlignmentListCellRenderer - 2011.12.12 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class LocalAlignmentListCellRenderer
        extends DefaultRenderer
        implements ListCellRenderer {

    private static final Logger              LOGGER = Logger.getLogger(LocalAlignmentListCellRenderer.class);
    private static final BasicAlignmentColor color  = new BasicAlignmentColor(ColorUtility.EMBL_PETROL,
                                                                              ColorUtility.EMBL_PETROL,
                                                                              Color.lightGray);
    private GeneProduct entity;
    private final static ConservationRenderer COMPLEX_RENDERER = new ConservationRenderer(new Rectangle(0, 0, 750, 10),
                                                                                          color,
                                                                                          new BlastConsensusScorer(),
                                                                                          1);
    private final static AlignmentRenderer    BASIC_RENDERER   = new AlignmentRenderer(new Rectangle(0, 0, 750, 10),
                                                                                       color,
                                                                                       1);

    public LocalAlignmentListCellRenderer() {
        COMPLEX_RENDERER.setGranularity(0.8f);
    }

    @Override
    public JLabel getComponent(JList list, Object value, int index) {

        LocalAlignment alignment = (LocalAlignment) value;
        AlignmentRenderer renderer = alignment.hasSequences() ? COMPLEX_RENDERER : BASIC_RENDERER;
        Icon icon = new ImageIcon(renderer.render(alignment, (GeneProduct) alignment.getEntity()));

        setIcon(icon);
        setText(alignment.getSubject());
        setToolTipText(TextUtility.html(alignment.getHTMLSummary()));

        return this;
    }

}
