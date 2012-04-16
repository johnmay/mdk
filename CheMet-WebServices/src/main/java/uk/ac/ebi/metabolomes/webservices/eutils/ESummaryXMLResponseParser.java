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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.apache.commons.lang.StringUtils;
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
public abstract class ESummaryXMLResponseParser<T extends ESummaryResult> {

    private static final Logger LOGGER = Logger.getLogger(ESummaryXMLResponseParser.class);

    /**
     * Parses the whole ESummaryResult XML object, delivering a List of ESummaryResults.
     * 
     * @param in the input stream through which the response the response can be read.
     * @return multimap with the mappings from the XML.
     * @throws XMLStreamException 
     */
    public List<T> parseESummaryResult(InputStream in) throws XMLStreamException {

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(in);

        int event;

        List<T> results = new ArrayList<T>();
        T currentResult = getNewESummaryResult();

        while (xmlr.hasNext()) {
            event = xmlr.next();

            switch1:
            switch (event) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:
                    //LOGGER.info("Start Element: "+xmlr.getLocalName());
                    //LOGGER.info("Attributes: "+getAttributes(xmlr));
                    if (xmlr.getLocalName().equalsIgnoreCase("Item")) {
                        boolean done = false;
                        
                        for (Enum keyword : currentResult.getScalarKeywords()) {
                            if(hasAttributeNameWithValue(xmlr, keyword.toString())) {
                                //LOGGER.info("Entering addScalarForKeyword: "+keyword.toString()+" for "+xmlr.getLocalName());
                                currentResult.addScalarForKeyword(keyword,getFollowingCharacters(xmlr));
                                break switch1;
                            }
                        }
                        for (Enum keyword : currentResult.getListKeywords()) {
                            if (hasAttributeNameWithValue(xmlr, keyword.toString())) {
                                //LOGGER.info("Entering addListForKeyword: "+keyword.toString()+" for "+xmlr.getLocalName());
                                currentResult.addListForKeyword(keyword, parseList(xmlr));
                                break switch1;
                            }
                        }
                    }
                    if(xmlr.getLocalName().equalsIgnoreCase("Id")) {
                        for (Enum keyword : currentResult.getScalarKeywords()) {
                            if(keyword.toString().equalsIgnoreCase("Id")) {
                                currentResult.addScalarForKeyword(keyword, getFollowingCharacters(xmlr));
                                break switch1;
                            }
                        }
                    }
                    /*
                    if (xmlr.getLocalName().equalsIgnoreCase("Item") && hasAttributeNameWithValue(xmlr, "SID")) {
                        currentResult.setId(getFollowingCharacters(xmlr));
                    } else if (xmlr.getLocalName().equalsIgnoreCase("Item") && hasAttributeNameWithValue(xmlr, "SourceNameList")) {
                        currentResult.setSourceNames(parseList(xmlr));
                    } else if (xmlr.getLocalName().equalsIgnoreCase("Item") && hasAttributeNameWithValue(xmlr, "SourceID")) {
                        currentResult.addSourceID(getFollowingCharacters(xmlr));
                    } else if (xmlr.getLocalName().equalsIgnoreCase("Item") && hasAttributeNameWithValue(xmlr, "DBUrl")) {
                        currentResult.setDBUrl(getFollowingCharacters(xmlr));
                    } else if (xmlr.getLocalName().equalsIgnoreCase("Item") && hasAttributeNameWithValue(xmlr, "SynonymList")) {
                        currentResult.setSynonyms(parseList(xmlr));
                    }*/

                    break;
                case XMLEvent.END_ELEMENT:
                    //LOGGER.info("End Element: "+xmlr.getLocalName());
                    if (xmlr.getLocalName().equalsIgnoreCase("DocSum")) {
                        currentResult.wrap();
                        results.add(currentResult);
                        currentResult = getNewESummaryResult();
                    }
                    break;
            }
        }
        xmlr.closeCompletely();
        return results;
    }

    private String parseIDFollowingIDTag(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        String id = "";
        boolean inId = false;
        loop1:
        while (xmlr.hasNext()) {
            event = xmlr.next();

            switch (event) {
                case XMLEvent.START_ELEMENT:
                    if (xmlr.getLocalName().equalsIgnoreCase("Id")) {
                        inId = true;
                    }
                    break;
                case XMLEvent.CHARACTERS:
                    if (inId) {
                        id = xmlr.getText();
                        inId = false;
                        break loop1;
                    }
                    break;
                default:
                    break;
            }
        }
        return id;
    }

    private boolean hasAttributeNameWithValue(XMLStreamReader2 xmlr, String string) {
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            if (xmlr.getAttributeLocalName(i).equalsIgnoreCase("Name") && xmlr.getAttributeValue(i).equalsIgnoreCase(string)) {
                return true;
            } else if (xmlr.getAttributeLocalName(i).equalsIgnoreCase("Name")) {
                return false;
            }
        }
        return false;
    }

    private String getFollowingCharacters(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        String characters = null;
        loop1:
        while (xmlr.hasNext()) {
            event = xmlr.next();

            switch (event) {
                case XMLEvent.CHARACTERS:
                    return xmlr.getText();
                case XMLEvent.END_ELEMENT:
                    if (xmlr.getLocalName().equalsIgnoreCase("Item")) {
                        break loop1;
                    }
                    break;
            }
        }
        return characters;
    }

    private List<String> parseList(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        List<String> listElements = new ArrayList<String>();
        boolean twoItemEndElementsInRow = false;
        boolean afterStartElement=false;
        loop1:
        while (xmlr.hasNext()) {
            event = xmlr.next();

            switch (event) {
                case XMLEvent.START_ELEMENT:
                    //LOGGER.debug("PL     Start Element: "+xmlr.getLocalName());
                    //LOGGER.debug("PL     Attributes: "+getAttributes(xmlr));
                    twoItemEndElementsInRow = false;
                    afterStartElement=true;
                    break;
                case XMLEvent.CHARACTERS:
                    //LOGGER.info("PL     Characters: "+xmlr.getText());
                    if (xmlr.getText().length() > 0 && afterStartElement) {
                        listElements.add(xmlr.getText());
                    }
                    afterStartElement=false;
                    break;
                case XMLEvent.END_ELEMENT:
                    //LOGGER.info("PL     End Element: "+xmlr.getLocalName());
                    if (xmlr.getLocalName().equalsIgnoreCase("Item") && twoItemEndElementsInRow) {
                        break loop1;
                    } else if (xmlr.getLocalName().equalsIgnoreCase("Item")) {
                        twoItemEndElementsInRow = true;
                    }
                    afterStartElement=false;
                    break;
            }
        }
        return listElements;
    }

    abstract T getNewESummaryResult();

    private String getAttributes(XMLStreamReader2 xmlr) {
        List<String> attributes = new ArrayList<String>();
        for(int i=0;i<xmlr.getAttributeCount();i++) {
            attributes.add(xmlr.getAttributeLocalName(i)+":"+xmlr.getAttributeValue(i));
        }
        return StringUtils.join(attributes, ", ");
    }

}
