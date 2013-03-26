package uk.ac.ebi.mdk.ui.edit.reaction;

/**
 * ParticipantEditor.java
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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.templates.MoleculeFactory;
import uk.ac.ebi.caf.component.factory.ComboBoxFactory;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.DefaultReconstructionManager;
import uk.ac.ebi.mdk.domain.entity.collection.ReconstructionManager;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.*;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.ui.render.molecule.MoleculeRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * ParticipantEditor 2012.02.13
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Class defines an editor for reaction participants
 * @version $Rev$ : Last Changed $Date$
 */
public class ParticipantEditor extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(ParticipantEditor.class);

    private MetabolicParticipant participant;

    // user-entry
    private JComboBox compartment;

    private JTextField metabolite;

    private JLabel structure;

    private JTextField stoichiometry;

    private static ReconstructionManager MANAGER = DefaultReconstructionManager.getInstance();

    // factories
    private static final MoleculeRenderer RENDERER = MoleculeRenderer.getInstance();


    public ParticipantEditor() {


        setOpaque(false);
        setLayout(new FormLayout("right:p, 1dlu, p", "min, 2dlu, min, 2dlu, min"));

        compartment = ComboBoxFactory.newComboBox((Object[]) getCompartments());
        compartment.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String desc = ((Compartment) value).getDescription();
                this.setText(desc.substring(0, Math.min(desc.length(), 20)));
                this.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
                return this;
            }
        });
        metabolite = FieldFactory.newField(12);
        stoichiometry = FieldFactory.newField(3);
        structure = LabelFactory.newLabel("");

        CellConstraints cc = new CellConstraints();
        add(compartment, cc.xyw(1, 1, 3));
        add(structure, cc.xyw(1, 3, 3, CellConstraints.CENTER, cc.CENTER));
        add(stoichiometry, cc.xy(1, 5));
        add(metabolite, cc.xy(3, 5));

    }


    public void setParticipant(MetabolicParticipant participant) {
        this.participant = participant;

        metabolite.setText(participant.getMolecule().getName());
        compartment.setSelectedItem(((Compartment) participant.getCompartment()));
        if (!compartment.getSelectedItem().equals(((Compartment) participant.getCompartment()))) {
            compartment.setSelectedItem(Organelle.UNKNOWN); // fail safe
        }
        stoichiometry.setText(participant.getCoefficient().toString());


        try {
            if (participant.getMolecule().hasStructure()) {
                structure.setIcon(new ImageIcon(RENDERER.getImage(participant.getMolecule().getStructures().iterator().next().getStructure(),
                                                                  new Rectangle(128, 128))));
                structure.setText("");
            } else {
                structure.setText("No structure");
            }
        } catch (CDKException ex) {
            LOGGER.warn("Unable to set structure: " + ex.getMessage());
        }

    }


    /**
     * Access the participant with the new edited values.
     * If the name is empty (i.e. no molecule) null is returned
     */
    public MetabolicParticipant getParticipant() {

        String name = metabolite.getText().trim();

        if (name.isEmpty()) {
            return null;
        }

        Double coef = 1d;

        try {
            coef = Double.parseDouble(stoichiometry.getText());
        } catch (NumberFormatException ex) {
            LOGGER.warn("Invalid coefficient");
        }

        if (participant == null) {
            participant = new MetabolicParticipantImplementation();
        }

        Reconstruction recon = MANAGER.active();
        Collection<Metabolite> candidates = recon.getMetabolome().get(metabolite.getText());

        Metabolite entity;
        if (candidates.iterator().hasNext()) {
            entity = candidates.iterator().next();
        } else {
            entity = DefaultEntityFactory.getInstance().newInstance(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(),
                                                                    metabolite.getText().trim(),
                                                                    metabolite.getText().trim());
            recon.addMetabolite(entity);
        }

        Metabolite molecule = participant.getMolecule();
        if (molecule != entity) {
            participant.setMolecule(entity);
        }

        participant.setCoefficient(coef);

        participant.setCompartment((Compartment) compartment.getSelectedItem());


        return participant;

    }


    private static Compartment[] getCompartments() {

        List<Compartment> compartments = new ArrayList<Compartment>();

        compartments.addAll(Arrays.asList(Organelle.values()));
        compartments.addAll(Arrays.asList(Membrane.values()));
        compartments.addAll(Arrays.asList(CellType.values()));
        compartments.addAll(Arrays.asList(Tissue.values()));
        compartments.addAll(Arrays.asList(Organ.values()));
        compartments.add(Organelle.UNKNOWN);

        return compartments.toArray(new Compartment[0]);

    }


    public static void main(String[] args) {

        EntityFactory ef = DefaultEntityFactory.getInstance();

        Metabolite m = ef.newInstance(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(), "ATP", "atp");
        m.addAnnotation(new AtomContainerAnnotation(MoleculeFactory.make123Triazole()));
        MetabolicParticipant mp = new MetabolicParticipantImplementation(m, 2.5d, Organelle.CYTOPLASM);

        ParticipantEditor pe = new ParticipantEditor();
        pe.setParticipant(mp);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pe);
        frame.pack();
        frame.setVisible(true);

    }
}
