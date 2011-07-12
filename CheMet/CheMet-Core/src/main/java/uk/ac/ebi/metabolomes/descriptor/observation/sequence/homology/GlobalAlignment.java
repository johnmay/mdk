/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology;

import uk.ac.ebi.metabolomes.descriptor.observation.AbstractObservation;

/**
 * GlobalAlignment.java
 *
 *
 * @author johnmay
 * @date Apr 6, 2011
 */
public class GlobalAlignment extends AbstractObservation {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( GlobalAlignment.class );
    private static final long serialVersionUID = -6440719101419743783L;


    public GlobalAlignment() {
    }

    @Override
    public String getObservationName() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public String getObservationDescription() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }



}
