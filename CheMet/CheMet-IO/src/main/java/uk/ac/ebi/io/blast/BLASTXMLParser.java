/**
 * BLASTXMLParser.java
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
package uk.ac.ebi.io.blast;

import java.util.Collection;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.TaskOptions;
import uk.ac.ebi.observation.sequence.LocalAlignment;

/**
 * @name    BLASTXMLParser - 2011.10.10 <br>
 *          Interface description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface BLASTXMLParser {

    public Collection<LocalAlignment> parse(Map<String, ? extends AnnotatedEntity> entities, TaskOptions options, XMLStreamReader2 xmlr) throws XMLStreamException;
}
