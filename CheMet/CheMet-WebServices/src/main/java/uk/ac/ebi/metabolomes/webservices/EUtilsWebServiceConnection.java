/**
 * EUtilsWebServiceConnection.java
 *
 * 2011.08.04
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
package uk.ac.ebi.metabolomes.webservices;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.resource.chemical.PubChemCompoundIdentifier;

/**
 * @name    EUtilsWebServiceConnection
 * @date    2011.08.04
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Web service to access the E-Utils service from NCBI. Relies on Jersey Client. 
 * WARNING: see NCBI usage policies: 
 *
 */
public class EUtilsWebServiceConnection {
    
    private static final Logger LOGGER = Logger.getLogger(EUtilsWebServiceConnection.class);
    
    private Client client;
    private WebResource webResource;

    public Multimap<String, String> getPubChemSubstanceFromPubChemCompoundIdents(List<PubChemCompoundIdentifier> pubChemCompoundIdentifiers) {
        List<String> pubchemComps = new ArrayList<String>(pubChemCompoundIdentifiers.size());
        for (PubChemCompoundIdentifier pubChemCompoundIdentifier : pubChemCompoundIdentifiers) {
            pubchemComps.add(pubChemCompoundIdentifier.getAccession());
        }
        return getPubChemSubstanceFromPubChemCompound(pubchemComps);
    }

    
    
    public enum EntrezDB {
        pccompound, pcsubstance, mesh;
    }
    
    private final String baseURL = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
    
    public EUtilsWebServiceConnection() {
        client = Client.create();
        webResource = client.resource(baseURL);
    }
    
    /**
     * Queries the NCBI EUtils web service to retrieve through elink.cgi all the associations existing in a database 
     * (dbto) for the identifiers provided for an initial different database (dbfrom). The one to one associations are 
     * stored in the Multimap returned.
     * 
     * @param dbFromIds the list of string identifiers to search for
     * @param dbFrom    the database in Entrez corresponding to those identifiers
     * @param dbTo      the database where we want to find hits.
     * @return          multimap with all the one-to-many associations fromDB identifiers (keys) -to- toDB identifiers (values).
     */
    public Multimap<String,String> getDBToIDsFromDBFromIDs(List<String> dbFromIds,EntrezDB dbFrom, EntrezDB dbTo) {
        WebResource webRes = client.resource(baseURL+"elink.fcgi");
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("dbfrom", dbFrom.toString());
        queryParams.add("db", dbTo.toString());
        for (String id : dbFromIds) {
            queryParams.add("id", id);
        }
        
        ClientResponse resp = webRes.queryParams(queryParams).post(ClientResponse.class);
        
        if (resp.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ resp.getStatus());
        }
        Multimap<String,String> res = ArrayListMultimap.create();
        try {
            res.putAll(parseLinkSetBlock(resp.getEntityInputStream()));
        } catch (XMLStreamException ex) {
            LOGGER.warn("Could not parse output XML adequately...", ex);
        }
        
        return res;
    }
    
    
    /*public Multimap<PubChemCompoundIdentifier,PubChemSubstanceIdentifier> getPubChemSubstanceIdentFromPubChemCompoundIdents(List<PubChemCompoundIdentifier> compounds) {
        
    }*/
    
    public Multimap<String,String> getPubChemSubstanceFromPubChemCompound(List<String> pubchemCompoundIds) {
        return this.getDBToIDsFromDBFromIDs(pubchemCompoundIds, EntrezDB.pccompound, EntrezDB.pcsubstance);
    }
    
    /*public Multimap<String,String> getPubChemSubstanceFromPubChemCompound(List<String> pubchemCompoundIds) {
        WebResource webRes = client.resource(baseURL+"elink.fcgi");
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("dbfrom", EntrezDB.pccompound.toString());
        queryParams.add("db", EntrezDB.pcsubstance.toString());
        for (String cid : pubchemCompoundIds) {
            queryParams.add("id", cid);
        }
        
        ClientResponse resp = webRes.queryParams(queryParams).post(ClientResponse.class);
        
        if (resp.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ resp.getStatus());
        }
        
        //String result = resp.getEntity(String.class);
        Multimap<String,String> res = ArrayListMultimap.create();
        try {
            res.putAll(parseLinkSetBlock(resp.getEntityInputStream()));
        } catch (XMLStreamException ex) {
            LOGGER.warn("Could not parse output XML adequately...", ex);
        }
        
        //System.out.println(result);
        
        return res;
    }*/
    
    
    private Multimap<String,String> parseLinkSetBlock(InputStream in) throws XMLStreamException {
        
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
