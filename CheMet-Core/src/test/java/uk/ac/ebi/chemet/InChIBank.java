/**
 * InChIBank.java
 *
 * 2011.08.13
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
package uk.ac.ebi.chemet;

import java.util.HashMap;

import uk.ac.ebi.mdk.domain.identifier.InChI;

/**
 * @name    InChIBank
 * @date    2011.08.13
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class InChIBank extends HashMap<String , String> {

    public static final String L_ORNITHINE = "L-ornithine";
    public static final String ORNITHINE = "ornithine";
    public static final String ARGININE_NON_STANDARD = "NON_STANDARD_ARGININE";

    public InChIBank() {
        put( L_ORNITHINE , "InChI=1S/C5H12N2O2/c6-3-1-2-4(7)5(8)9/h4H,1-3,6-7H2,(H,8,9)/t4-/m0/s1" );
        put( ORNITHINE , "InChI=1S/C5H12N2O2/c6-3-1-2-4(7)5(8)9/h4H,1-3,6-7H2,(H,8,9)" );
        put( ARGININE_NON_STANDARD , "InChI=1/C6H14N4O2/c7-4(5(11)12)2-1-3-10-6(8)9/h4H,1-3,7H2,(H,11,12)(H4,8,9,10)" );
    }

    public InChI getInChI( String name ) {
        if ( containsKey( name ) ) {
            return new InChI( get( name ) );
        }
        return null;
    }
}
