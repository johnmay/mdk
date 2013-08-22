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

package uk.ac.ebi.mdk.domain.entity.reaction.filter;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.InChI;
import uk.ac.ebi.mdk.domain.entity.reaction.InChIParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.ParticipantImplementation;

import java.util.HashSet;
import java.util.Set;

/**
 * InChIFilter – 2011.08.14
 * Filters an InChIParticpant for those which are not H2O, H+, O2, CO2. The default rejections can be removed by using the
 * {@see removeRejection(InChI)} method and using the public InChI attributes InChIFilter.HYDRON, InChIFilter.WATER,
 * InChIFilter.CARBON_DIOXIDE and InChIFilter.DIOXYGEN
 *
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@SuppressWarnings("unchecked")
public class InChIFilter extends AbstractParticipantFilter {

    private static final Logger LOGGER = Logger.getLogger( InChIFilter.class );
    private Set rejections = new HashSet<InChI>();
    public static final InChI HYDRON = new InChI( "InChI=1S/p+1" );
    public static final InChI WATER = new InChI( "InChI=1S/H2O/h1H2" );
    public static final InChI CARBON_DIOXIDE = new InChI( "InChI=1S/CO2/c2-1-3" );
    public static final InChI DIOXYGEN = new InChI( "InChI=1S/O2/c1-2" );

    public  InChIFilter() {
        rejections.add( HYDRON );
        rejections.add( WATER );
        rejections.add( CARBON_DIOXIDE );
        rejections.add( DIOXYGEN );
    }

    @Override
    public boolean reject( ParticipantImplementation p ) {
        if ( p instanceof InChIParticipant ) {
            return reject( ( InChIParticipant ) p );
        }
        return true;
    }

    /**
     * Determines whether to reject (i.e. filter-out) the provided participant
     * @param p The InChIParticipant
     * @return Whether to filter
     */
    public boolean reject( InChIParticipant p ) {
        return rejections.contains( p.getMolecule() );
    }

    /**
     * Allows adding of custom rejected InChI objects
     * @param rejected A new InChI to reject
     */
    public void addRejection( InChI rejected ) {
        rejections.add( rejected );
    }

    /**
     * Allows removal of a rejected InChI from the filter
     * @param accepted
     */
    public void removeRejection( InChI accepted ) {
        rejections.remove( accepted );
    }

}
