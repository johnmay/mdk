/**
 * SequenceIdentifier.java
 *
 * 2011.10.13
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
package uk.ac.ebi.mdk.domain.identifier.type;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;
import java.util.Iterator;

/**
 * @name    SequenceIdentifier - 2011.10.13 <br>
 *          A generic identifier describing a sequence.
 *          The interfaces allows resolution of a sequence header
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface SequenceIdentifier extends Identifier {

    /**
     * Returns the header code for the sequence e.g. sp of swissprot, tr for trembl
     */
    public Collection<String> getHeaderCodes();

    public SequenceIdentifier ofHeader(Iterator<String> token);

}
