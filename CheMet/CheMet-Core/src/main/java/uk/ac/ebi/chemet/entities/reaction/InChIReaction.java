/**
 * InChIReaction.java
 *
 * 2011.08.08
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

import java.util.Comparator;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.entities.Compartment;
import uk.ac.ebi.metabolomes.identifier.InChI;

/**
 * @name    InChIReaction
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Implementation of Reaction using InChI to represent the reaction participants
 *
 */
public class InChIReaction
        extends Reaction<InChI , Double , Compartment> {

    private static final Logger LOGGER = Logger.getLogger( InChIReaction.class );

    public InChIReaction() {
        super( new Comparator<InChI>() {

            public int compare( InChI o1 , InChI o2 ) {
                return o1.compareTo( o2 );
            }
        } );
    }
}
