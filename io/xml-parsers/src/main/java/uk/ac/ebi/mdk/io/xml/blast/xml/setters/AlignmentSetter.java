/**
 * BLASTXMLSetter.java
 *
 * 2011.10.10
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
package uk.ac.ebi.mdk.io.xml.blast.xml.setters;

import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

import javax.xml.stream.XMLStreamException;

/**
 * @name    BLASTXMLSetter - 2011.10.10 <br>
 *          Allows quick setting of observation values when parsing an xml document
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface AlignmentSetter {

    public void set(LocalAlignment alignment, XMLStreamReader2 xmlr) throws XMLStreamException;
}
