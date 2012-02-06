/**
 * InChIParticipant.java
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
package uk.ac.ebi.chemet.entities.reaction.participant;

import org.apache.log4j.Logger;
import uk.ac.ebi.core.CompartmentImplementation;
import uk.ac.ebi.metabolomes.identifier.InChI;

/**
 * @name    InChIParticipant
 * @date    2011.08.12
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class InChIParticipant extends Participant<InChI , Double , CompartmentImplementation> {

    private static final Logger LOGGER = Logger.getLogger( InChIParticipant.class );

    public InChIParticipant( InChI molecule , Double coefficient , CompartmentImplementation compartment ) {
        super( molecule , coefficient , compartment );
    }

    public InChIParticipant( InChI molecule , Double coefficient ) {
        super( molecule , coefficient );
    }

    public InChIParticipant( InChI molecule ) {
        super( molecule );
    }

    public InChIParticipant( String inchi ) {
        super( new InChI( inchi ) );
    }


    /**
     * Override to avoid checking if classes are comparable
     * @param o
     * @return
     */
    @Override
    public int compareTo( Participant<InChI , Double , CompartmentImplementation> o ) {
        int coefComparison = ( ( Comparable ) this.coefficient ).compareTo( o.coefficient );
        if ( coefComparison != 0 ) {
            return coefComparison;
        }
        int compComparison = ( ( Comparable ) this.compartment ).compareTo( o.compartment );
        if ( compComparison != 0 ) {
            return compComparison;
        }
        return ( ( Comparable ) this.molecule ).compareTo( o.molecule );
    }
}
