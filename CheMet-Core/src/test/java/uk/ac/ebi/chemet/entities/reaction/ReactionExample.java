/**
 * ReactionExample.java
 *
 * 2011.08.12
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
package uk.ac.ebi.chemet.entities.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.chemical.InChI;
import uk.ac.ebi.core.CompartmentImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.GenericParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.InChIParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.ParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.reaction.*;

import static uk.ac.ebi.chemet.TestMoleculeFactory.*;
import uk.ac.ebi.mdk.domain.entity.reaction.AtomContainerParticipant;


/**
 * @name    ReactionExample
 * @date    2011.08.12
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class ReactionExample {

    private static final Logger LOGGER = Logger.getLogger(ReactionExample.class);


    public static void main(String[] args) {

        // base class
        {
            AbstractReaction r = new AbstractReaction<Participant<String, Integer>>();
            r.addReactant(new ParticipantImplementation<String, Integer, String>("A", 1));
            r.addReactant(new ParticipantImplementation<String, Integer, String>("B", 1));
            r.addProduct(new ParticipantImplementation<String, Integer, String>("C", 1));
            r.addProduct(new ParticipantImplementation<String, Integer, String>("D", 1));
            // set the reversibilty of the reaction (not used in comparisson)
            r.setDirection(Direction.BIDIRECTIONAL);
            System.out.println(r);
        }

        // InChIs
        InChIReaction inchiReaction = new InChIReaction();
        inchiReaction.addReactant(new InChIParticipant(new InChI("InChI=1S/B...")));
        inchiReaction.addProduct(new InChIParticipant("InChI=1S/C..."));
        inchiReaction.addProduct(new InChIParticipant(new InChI("InChI=1S/D..."), 1.0, CompartmentImplementation.CYTOPLASM));

        /* Adding Generics
         Note: All molecules have implict H's added and then converted
         to explict. This is becasue in this case a primary alochol (C-O)
         would match loads */
        AtomContainerReaction r = new AtomContainerReaction();
        r.addReactant(new AtomContainerParticipant(primary_alcohol()));                        // -R group detected and added as GenericParticipant
        r.addProduct(new GenericParticipant(aldehyde_no_r())); // no (R) – need to force GenericParticipant use

        AtomContainerReaction r2 = new AtomContainerReaction();
        r2.addReactant(new AtomContainerParticipant(_5bcholestane3a7a26triol()));
        r2.addProduct(new AtomContainerParticipant(_3a7adihydroxy5Bcholestan26al()));

        /* Transposed r2 */
        AtomContainerReaction r3 = new AtomContainerReaction();
        r3.addReactant(new AtomContainerParticipant(_3a7adihydroxy5Bcholestan26al()));
        r3.addProduct(new AtomContainerParticipant(_5bcholestane3a7a26triol()));


        /* WARNING! When using the generics the hash codes will not be the same */
        System.out.println(r.hashCode() == r2.hashCode());       // false
        System.out.println(r.equals(r2));                      // true

        System.out.println(r2.hashCode() == r3.hashCode());      // true
        System.out.println(r2.equals(r3));                     // true

        System.out.println(r.hashCode() == r3.hashCode());       // false
        System.out.println(r.equals(r3));                      // true

    }
}
