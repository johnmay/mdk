/**
 * MetaboliteMarshal_0_8_5_0.java
 *
 * 2012.01.31
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
package uk.ac.ebi.io.core.marshal.versions;

import java.io.IOException;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.entities.reaction.Reversibility;
import uk.ac.ebi.chemet.entities.reaction.participant.Participant;
import uk.ac.ebi.core.Compartment;
import uk.ac.ebi.core.MetabolicReaction;
import uk.ac.ebi.core.Metabolite;
import uk.ac.ebi.core.reaction.MetabolicParticipant;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.io.ReconstructionInputStream;
import uk.ac.ebi.interfaces.io.ReconstructionOutputStream;
import uk.ac.ebi.interfaces.io.marshal.EntityMarshaller;
import uk.ac.ebi.io.core.marshal.AbstractAnnotatedEntityMarshaller;


/**
 *
 *          MetaboliteMarshal_0_8_5_0 2012.01.31
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class ReactionMarshaller_0_8_5_0 extends AbstractAnnotatedEntityMarshaller {

    private static final Logger LOGGER = Logger.getLogger(ReactionMarshaller_0_8_5_0.class);


    public ReactionMarshaller_0_8_5_0() {
        super(new Version(0, 8, 5, 0));
    }


    public EntityMarshaller newInstance() {
        return new ReactionMarshaller_0_8_5_0();
    }


    @Override
    public AnnotatedEntity read(ReconstructionInputStream in) throws IOException, ClassNotFoundException {

        MetabolicReaction rxn = new MetabolicReaction();

        int reactantCount = in.readInt();

        for (int i = 0; i < reactantCount; i++) {

            MetabolicParticipant p = new MetabolicParticipant();
            p.setCoefficient(in.readDouble());
            p.setCompartment(Compartment.valueOf(in.readByte()));
            p.setMolecule((Metabolite) in.getMetabolite(in.readInt()));

            rxn.addReactant(p);
        }

        int productCount = in.readInt();

        for (int i = 0; i < productCount; i++) {

            MetabolicParticipant p = new MetabolicParticipant();
            p.setCoefficient(in.readDouble());
            p.setCompartment(Compartment.valueOf(in.readByte()));
            p.setMolecule((Metabolite) in.getMetabolite(in.readInt()));

            rxn.addProduct(p);
        }

        rxn.setReversibility(Reversibility.valueOf(in.readByte()));

        return rxn;

    }


    @Override
    public void write(ReconstructionOutputStream out, AnnotatedEntity entity) throws IOException {

        MetabolicReaction rxn = (MetabolicReaction) entity;

        out.writeInt(rxn.getReactantCount());

        for (Participant<Metabolite, Double, Compartment> p : rxn.getReactantParticipants()) {
            out.writeDouble(p.getCoefficient());
            out.writeByte(p.getCompartment().getRanking());
            out.writeIndex(p.getMolecule());
        }

        out.writeInt(rxn.getProductCount());

        for (Participant<Metabolite, Double, Compartment> p : rxn.getProductParticipants()) {
            out.writeDouble(p.getCoefficient());
            out.writeByte(p.getCompartment().getRanking());
            out.writeIndex(p.getMolecule());
        }

        out.writeByte(rxn.getReversibility().getIndex());

    }
}
