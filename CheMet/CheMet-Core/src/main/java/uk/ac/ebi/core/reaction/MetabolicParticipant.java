/**
 * MetaboliteParticipant.java
 *
 * 2011.09.27
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
package uk.ac.ebi.core.reaction;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.channels.UnsupportedAddressTypeException;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.CompartmentImplementation;
import uk.ac.ebi.chemet.entities.reaction.participant.Participant;
import uk.ac.ebi.core.DefaultEntityFactory;
import uk.ac.ebi.core.metabolite.MetaboliteCollection;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.resource.chemical.BasicChemicalIdentifier;


/**
 *          MetaboliteParticipant – 2011.09.27 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetabolicParticipant extends Participant<Metabolite, Double, CompartmentImplementation> {

    private static final Logger LOGGER = Logger.getLogger(MetabolicParticipant.class);


    public MetabolicParticipant() {
    }


    public MetabolicParticipant(Metabolite molecule, CompartmentImplementation compartment) {
        super(molecule, 1d, compartment);
    }


    public MetabolicParticipant(Metabolite molecule, Double coefficient, CompartmentImplementation compartment) {
        super(molecule, coefficient, compartment);
    }


    public MetabolicParticipant(Metabolite molecule, Double coefficient) {
        super(molecule, coefficient, CompartmentImplementation.CYTOPLASM);
    }


    public MetabolicParticipant(Metabolite molecule) {
        super(molecule, 1d, CompartmentImplementation.CYTOPLASM);
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Metabolite m = DefaultEntityFactory.getInstance().newInstance(Metabolite.class);
        m.readExternal(in);
        System.out.println(m.getIdentifier());
        setMolecule(m);
        setCoefficient(in.readDouble());
        setCompartment(CompartmentImplementation.valueOf(in.readUTF()));
    }


    public void readExternal(ObjectInput in, MetaboliteCollection metabolites) throws IOException,
                                                                                      ClassNotFoundException {
        //setMolecule(metabolites.get(in.readInt()));
        setCoefficient(in.readDouble());
        setCompartment(CompartmentImplementation.valueOf(in.readUTF()));
        throw new UnsupportedOperationException("Use ReconstructionInputStream!");

    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.print("writing external metabolite..");
        getMolecule().writeExternal(out);
        out.writeDouble(getCoefficient());
        out.writeUTF(getCompartment().name());
    }


    public void writeExternal(ObjectOutput out, MetaboliteCollection metabolites) throws IOException {
        out.writeInt(metabolites.indexOf(getMolecule()));
        out.writeDouble(getCoefficient());
        out.writeUTF(getCompartment().name());
    }
}
