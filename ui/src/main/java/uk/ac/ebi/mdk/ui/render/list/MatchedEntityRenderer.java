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
package uk.ac.ebi.mdk.ui.render.list;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.DefaultReconstructionManager;
import uk.ac.ebi.mdk.domain.entity.collection.ReconstructionManager;
import uk.ac.ebi.mdk.domain.observation.MatchedEntity;
import uk.ac.ebi.mdk.ui.render.molecule.MoleculeRenderer;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;

/**
 * LocalAlignmentListCellRenderer - 2011.12.12 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class MatchedEntityRenderer implements ListCellRenderer {

    private final Box    panel;
    private final JLabel entityLabel, reconLabel, structureLabel;
    private AnnotatedEntity subject;

    public MatchedEntityRenderer() {
        panel = Box.createHorizontalBox();
        panel.add((structureLabel = LabelFactory.newLabel("...")));
        panel.add(Box.createHorizontalStrut(50));
        panel.add((entityLabel = LabelFactory.newLabel("...")));
        panel.add(Box.createHorizontalStrut(20));
        panel.add((reconLabel = LabelFactory.newLabel("...")));
        panel.setPreferredSize(new Dimension(512, 256));
        structureLabel.setSize(new Dimension(256, 256));
    }

    public void setSubject(AnnotatedEntity subject) {
        this.subject = subject;
    }

    @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        MatchedEntity match = (MatchedEntity) value;

        // todo: item specific rendering (i.e. chemical structures)
        structureLabel.setText("");
        structureLabel.setIcon(null);
        entityLabel.setText(match.entityId().getAccession());
        reconLabel.setText("(" + match.reconId().getAccession() + ")");

        ReconstructionManager recons = DefaultReconstructionManager.getInstance();
        Reconstruction recon = recons.get(match.reconId());

        if (recon != null) {
            Collection<Metabolite> metabolites = recon.metabolome().ofIdentifier(match.entityId());
            if (metabolites.size() == 1) {
                Collection <ChemicalStructure> structures = metabolites.iterator().next().getStructures();
                if (!structures.isEmpty()) {
                    try {
                        IAtomContainer container = structures.iterator().next().getStructure();
                        structureLabel.setIcon(new ImageIcon(MoleculeRenderer.getInstance().getImage(container, 256, true)));
                    } catch (CDKException e) {
                        Logger.getLogger(getClass()).error(e);
                    }
                }
            }
        }
        
        panel.revalidate();

        return panel;
    }
}
