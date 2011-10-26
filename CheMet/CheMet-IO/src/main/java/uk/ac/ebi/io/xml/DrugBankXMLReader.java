/**
 * ENAXMLReader.java
 *
 * 2011.10.16
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
package uk.ac.ebi.io.xml;

import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

/**
 *          ENAXMLReader - 2011.10.16 <br>
 *          A class to read RNA XML files into CheMet core objects
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class DrugBankXMLReader {

    private static final Logger LOGGER = Logger.getLogger(DrugBankXMLReader.class);
    private DrugBankEntryParser entryParser;
    private XMLStreamReader2 xmlr;
    
    public DrugBankXMLReader(InputStream in) throws XMLStreamException {

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(in);

        int event;
        
        entryParser = new DrugBankEntryParser(xmlr);

        loop1:
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:

                    if (xmlr.getLocalName().equalsIgnoreCase("drug"))
                        // we wait here
                        break loop1;
                    break;
            }
        }

    }
    
    public DrugBankEntry getNext() throws XMLStreamException {
        return entryParser.getNext();
    }

    public void close() throws XMLStreamException {
        xmlr.closeCompletely();
    }
    
    

}
