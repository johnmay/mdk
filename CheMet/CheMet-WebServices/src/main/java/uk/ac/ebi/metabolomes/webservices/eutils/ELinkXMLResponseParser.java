/**
 * ELinkXMLResponseParser.java
 *
 * 2011.10.30
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
package uk.ac.ebi.metabolomes.webservices.eutils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * @name    ELinkXMLResponseParser
 * @date    2011.10.30
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class ELinkXMLResponseParser {
    
    private static final Logger LOGGER = Logger.getLogger(ELinkXMLResponseParser.class);
    
    /**
     * Parses the LinkSetBlock of ELink query response.
     * 
     * @param in the input stream through which the response the response can be read.
     * @return multimap with the mappings from the XML.
     * @throws XMLStreamException 
     */
    public Multimap<String,String> parseLinkSetBlock(InputStream in) throws XMLStreamException {
        
        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(in);
        
        int event;
        
        ListMultimap<String,String> multiMap = ArrayListMultimap.create();
        
        while(xmlr.hasNext()) {
            event = xmlr.next();
            
            switch (event) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:
                    if(xmlr.getLocalName().equalsIgnoreCase("LinkSet")) {
                        multiMap.putAll(parseLinkSetEntry(xmlr));
                    }
                    break;
            }
        }
        xmlr.closeCompletely();
        return multiMap;
    }
    
    private ListMultimap<String,String> parseLinkSetEntry(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        ListMultimap<String,String> multiMap = ArrayListMultimap.create();
        String pchemcompID=null;
        loop1:
        while(xmlr.hasNext()) {
            event = xmlr.next();
            
            switch (event) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:
                    if(xmlr.getLocalName().equalsIgnoreCase("IdList")) {
                        pchemcompID = parseIDFollowingIDTag(xmlr);
                    } else if(xmlr.getLocalName().equalsIgnoreCase("LinkSetDb")) {
                        List<String> pchemSubs = parseLinkSetDB(xmlr);
                        if(pchemcompID!=null && pchemcompID.length()>0) {
                            for (String substance : pchemSubs) {
                                multiMap.put(pchemcompID, substance);
                            }
                            //pchemcompID = null;
                        } else {
                            LOGGER.warn("Got substance ids but compound id is null or empty and it shouldn't!!!");
                        }
                    }
                    break;
                case XMLEvent.END_ELEMENT:
                    if(xmlr.getLocalName().equalsIgnoreCase("LinkSet"))
                        break loop1;
                    break;
            }
        }
        return multiMap;
    }
    
    private String parseIDFollowingIDTag(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        String id = "";
        boolean inId=false;
        loop1:
        while(xmlr.hasNext()) {
            event = xmlr.next();
            
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    if(xmlr.getLocalName().equalsIgnoreCase("Id"))
                        inId=true;
                    break;
                case XMLEvent.CHARACTERS:
                    if(inId) {
                        id = xmlr.getText();
                        inId=false;
                        break loop1;
                    }
                    break;
                default:
                    break;
            }
        }
        return id;
    }
    
    private List<String> parseLinkSetDB(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        List<String> substances = new ArrayList<String>();
        loop1:
        while(xmlr.hasNext()) {
            event = xmlr.next();
            
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    if(xmlr.getLocalName().equalsIgnoreCase("Link"))
                        substances.add(parseIDFollowingIDTag(xmlr));
                    break;
                case XMLEvent.END_ELEMENT:
                    if(xmlr.getLocalName().equalsIgnoreCase("LinkSetDb"))
                        break loop1;
                    break;
            }
        }
        return substances;
    }
}
