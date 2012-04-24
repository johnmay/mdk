/**
 * ENAFeatureParser.java
 *
 * 2011.10.17
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

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.chemet.resource.basic.BasicChemicalIdentifier;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.DefaultIdentifierFactory;

/**
 *          DrugBankEntryParser - 2011.10.17 <br>
 *          Class parses a Drug entry in the DrugBank XML parser. Currently, only the ID of the DrugBank drug and its
 *          cross references, including CAS, are being parsed. Further contents need to be parsed if desired.
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 */
public class DrugBankEntryParser {

    private static final Logger LOGGER = Logger.getLogger(DrugBankEntryParser.class);
    private static DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();
    
    
    private DrugBankEntry entry;
    
    private Map<String, String> qualifiers = new HashMap();
    private final XMLStreamReader2 xmlr;

    private void parseDrugbankId(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        // <drugbank-id>DB00001</drugbank-id>
        loop1:
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.CHARACTERS:
                    String accession = xmlr.getText();
                    entry.setAccession(accession);
                    break;
                case XMLEvent.END_ELEMENT:
                    break loop1;
            }
        }
    }

    private void parseCASNumber(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        loop1:
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.CHARACTERS:
                    Identifier casId = factory.ofSynonym("CAS");
                    casId.setAccession(xmlr.getText());
                    entry.addIdentifier(casId);
                    break;
                case XMLEvent.END_ELEMENT:
                    break loop1;
            }
        }
    }

    private void parseExternalIdentifiers(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        loop1:
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    if (xmlr.getLocalName().equalsIgnoreCase("external-identifier")) {
                        parseExternalIdentifier(xmlr);
                    }
                    break;
                case XMLEvent.END_ELEMENT:
                    if(xmlr.getLocalName().equalsIgnoreCase("external-identifiers"))
                        break loop1;
                    break;
            }
        }
    }

    private void parseExternalIdentifier(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        // <resource>ChEBI</resource>
        // <identifier>18405</identifier>
        //</external-identifier>
        boolean resource = false;
        boolean ident = false;
        Identifier identifierToAdd = null;
        String accession = null;

        loop1:
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    if (xmlr.getLocalName().equalsIgnoreCase("resource")) {
                        resource = true;
                    } else if(xmlr.getLocalName().equalsIgnoreCase("identifier")) {
                        ident = true;
                    }
                    break;
                case XMLEvent.CHARACTERS:
                    if (resource) {
                        resource = false;
                        try {
                            identifierToAdd = factory.ofSynonym(xmlr.getText());
                        } catch(InvalidParameterException e) {
                            LOGGER.warn("Could not handle "+xmlr.getText());
                            if(xmlr.getText().contains("UniProtKB")) {
                                LOGGER.info("Skipping "+xmlr.getText()+" identifier");
                            } else if(xmlr.getText().trim().length()<1) {
                                LOGGER.warn("Empty place holder found, skipping it");
                            } else {
                                identifierToAdd = new BasicChemicalIdentifier();
                                ((BasicChemicalIdentifier)identifierToAdd).setShortDescription(xmlr.getText());
                            }
                        }
                    } else if (ident) {
                        accession = xmlr.getText();
                        ident=false;
                    }
                    break;
                case XMLEvent.END_ELEMENT:
                    if (xmlr.getLocalName().equalsIgnoreCase("external-identifier")) {
                        break loop1;
                    } 
                    break;
            }
        }

        if (identifierToAdd != null && accession != null) {
            identifierToAdd.setAccession(accession);
            entry.addIdentifier(identifierToAdd);
        }
    }
    // <drugbank-id>DB00001</drugbank-id>
    private final Pattern drugbankIdPattern = Pattern.compile("drugbank-id");

    /**
     * Creates a new feature form xmlr
     * 
     * @param xmlr
     */
    public DrugBankEntryParser(XMLStreamReader2 xmlr) throws XMLStreamException {
        this.xmlr = xmlr;
    }
    
    public DrugBankEntry getNext() throws XMLStreamException {
        int event;

        entry = new DrugBankEntry();
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.START_DOCUMENT:
                    //LOGGER.info("START DOCUMENT name:"+xmlr.getLocalName()+" type:"+event);
                    break;
                case XMLEvent.START_ELEMENT:
                    //LOGGER.info("START ELEMENT name:"+xmlr.getLocalName()+" type:"+event);
                    // <drugbank-id>DB00001</drugbank-id>
                    if (drugbankIdPattern.matcher(xmlr.getLocalName()).matches()) {
                        parseDrugbankId(xmlr);
                    } // <name>Lepirudin</name>
                    // else if(xmlr.getLocalName().equalsIgnoreCase("name"))
                    // <cas-number>120993-53-5</cas-number>
                    else if (xmlr.getLocalName().equalsIgnoreCase("cas-number")) {
                        parseCASNumber(xmlr);
                    } // <external-identifiers>
                    else if (xmlr.getLocalName().equalsIgnoreCase("external-identifiers")) {
                        parseExternalIdentifiers(xmlr);
                    } else if(xmlr.getLocalName().equalsIgnoreCase("drug-interactions")) {
                        // this is necessary since inside drug interaction you get <drug> tags
                        // which conflict with the whole entry tags.
                        parseDrugInteractions(xmlr);
                    }
                    break;

                case XMLEvent.END_ELEMENT:
                    //LOGGER.info("END ELEMENT name:"+xmlr.getLocalName()+" type:"+event);
                    if (xmlr.getLocalName().equals("drug")) {
                        return entry;
                    }
                    break;

            }
        }
        return null;
    }

    private void parseDrugInteractions(XMLStreamReader2 xmlr) throws XMLStreamException {
        int event;
        loop1:
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.END_ELEMENT:
                    if(xmlr.getLocalName().equalsIgnoreCase("drug-interactions"))
                        break loop1;
                    break;
            }
        }
    }
}
