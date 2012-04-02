/**
 * IdentiferECMapper.java
 *
 * 2011.07.14
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
package uk.ac.ebi.chemet.io.mapping;

import au.com.bytecode.opencsv.CSVReader;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.base.DynamicIdentifier;
import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 * @name    IdentiferECMapper
 * @date    2011.07.14
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Basic implementation of FileHashMapper to map GenericIdentifiers to EC Numbers
 *
 */
public class IdentiferECMapper
        extends FileHashMapper<Identifier , Set<ECNumber>> {

    private static final Logger LOGGER = Logger.getLogger( IdentiferECMapper.class );

    public IdentiferECMapper( CSVReader reader , int keyIndex , int valueIndex ) {
        super( reader , keyIndex , valueIndex );
    }

    @Override
    public Identifier parseKey( String keyString ) {
        return new DynamicIdentifier( keyString );
    }

    @Override
    public Set<ECNumber> parseValue( String valueString ) {
        Set<ECNumber> ecs = new HashSet<ECNumber>( 4 );

        if (valueString.isEmpty()){
            return ecs;
        }

        // to handle multiple ec numbers first split the string on semi-colon (standard divider)
        String[] ecStrings = valueString.split( ";" );
        // add all to the set
        for ( String ecString : ecStrings ) {
            ecs.add( new ECNumber( ecString ) );
        }
        return ecs;
    }

    @Override
    public Set<ECNumber> clash( Set<ECNumber> oldValue ,
                                Set<ECNumber> newValue ) {
        oldValue.addAll( newValue );
        return oldValue;
    }
}
