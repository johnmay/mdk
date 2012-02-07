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
import org.apache.log4j.Logger;
import uk.ac.ebi.core.CompartmentImplementation;
import uk.ac.ebi.chemet.entities.reaction.participant.ParticipantImplementation;
import uk.ac.ebi.core.DefaultEntityFactory;
import uk.ac.ebi.core.metabolite.MetaboliteCollection;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.reaction.Compartment;
import uk.ac.ebi.interfaces.reaction.CompartmentalisedParticipant;


/**
 *          MetaboliteParticipant – 2011.09.27 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetabolicParticipant 
    extends ParticipantImplementation<Metabolite, Double, Compartment> {

    private static final Logger LOGGER = Logger.getLogger(MetabolicParticipant.class);


    public MetabolicParticipant() {
    }


    public MetabolicParticipant(Metabolite molecule, CompartmentImplementation compartment) {
        super(molecule, 1d, compartment);
    }


    public MetabolicParticipant(Metabolite molecule, Double coefficient, Compartment compartment) {
        super(molecule, coefficient, compartment);
    }


    public MetabolicParticipant(Metabolite molecule, Double coefficient) {
        super(molecule, coefficient, CompartmentImplementation.CYTOPLASM);
    }


    public MetabolicParticipant(Metabolite molecule) {
        super(molecule, 1d, CompartmentImplementation.CYTOPLASM);
    }


   
}
