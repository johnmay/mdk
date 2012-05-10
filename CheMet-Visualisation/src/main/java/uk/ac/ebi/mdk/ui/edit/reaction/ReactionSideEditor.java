/**
 * ReactionSideEditor.java
 *
 * 2012.02.13
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
package uk.ac.ebi.mdk.ui.edit.reaction;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.ExpandingComponentList;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;


/**
 *
 *          ReactionSideEditor 2012.02.13
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class ReactionSideEditor extends ExpandingComponentList<ParticipantEditor> {

    private static final Logger LOGGER = Logger.getLogger(ReactionSideEditor.class);

    private static ImageIcon PLUS = new ImageIcon(new BufferedImage(15, 128, BufferedImage.TYPE_3BYTE_BGR));


    {
        PLUS.getImage().getGraphics().setColor(Color.WHITE);
        PLUS.getImage().getGraphics().fillRect(0, 0, 15, 128);
        ReactionRenderer.drawPlus((Graphics2D) PLUS.getImage().getGraphics(),
                                  new Rectangle2D.Double(0, 0, 15, 128));
    }


    public ReactionSideEditor(Window window) {
        super(window, BoxLayout.LINE_AXIS);
        setRestrict(false);
    }


    @Override
    public ParticipantEditor newComponent() {
        return new ParticipantEditor();
    }


    public void setParticipants(List<MetabolicParticipant> participants) {
        super.setSize(participants.size());
        for (int i = 0; i < participants.size(); i++) {
            super.getComponent(i).setParticipant(participants.get(i));
        }

        // ensure there's always a chance to add a new participant
        if (super.getSize() == 0) {
            append();
        }

    }


    @Override
    public JComponent newSpacer() {
        JLabel label = LabelFactory.newLabel(PLUS);
        return label;
    }


    public List<MetabolicParticipant> getParticipants() {
        List<MetabolicParticipant> participants = new ArrayList<MetabolicParticipant>();
        for (int i = 0; i < getSize(); i++) {
            MetabolicParticipant participant = super.getComponent(i).getParticipant();
            if (participant != null) {
                participants.add(participant);
            }
        }
        return participants;
    }
}
