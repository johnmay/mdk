/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.domain.entity.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;


/**
 *          MetaboliteParticipant – 2011.09.27 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetabolicParticipantImplementation
        extends ParticipantImplementation<Metabolite, Double, Compartment>
        implements MetabolicParticipant {

    private static final Logger LOGGER = Logger.getLogger(MetabolicParticipantImplementation.class);


    public MetabolicParticipantImplementation() {
        super(null, 1d, Organelle.CYTOPLASM);
    }


    public MetabolicParticipantImplementation(Metabolite molecule, Compartment compartment) {
        super(molecule, 1d, compartment);
    }


    public MetabolicParticipantImplementation(Metabolite molecule, Double coefficient, Compartment compartment) {
        super(molecule, coefficient, compartment);
    }


    public MetabolicParticipantImplementation(Metabolite molecule, Double coefficient) {
        super(molecule, coefficient, Organelle.CYTOPLASM);
    }


    public MetabolicParticipantImplementation(Metabolite molecule) {
        super(molecule, 1d, Organelle.CYTOPLASM);
    }

    @Override
    public MetabolicParticipant newInstance() {
        return new MetabolicParticipantImplementation();
    }
}
