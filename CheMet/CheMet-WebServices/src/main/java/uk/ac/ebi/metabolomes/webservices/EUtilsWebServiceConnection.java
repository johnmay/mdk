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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.jws.WebService;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.chemet.ws.exceptions.WebServiceException;
import uk.ac.ebi.metabolomes.webservices.eutils.ELinkXMLResponseParser;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemCompoundESummaryResult;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemCompoundXMLResponseParser;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemNamesResult;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemSubstanceESummaryResult;
import uk.ac.ebi.metabolomes.webservices.eutils.PubChemSubstanceXMLResponseParser;
import uk.ac.ebi.resource.chemical.PubChemCompoundIdentifier;
import uk.ac.ebi.resource.chemical.PubChemSubstanceIdentifier;

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
    private Long previousCallTime;
    public final Integer MAX_RECORDS_PER_QUERY = 5000;

    /**
     * Makes sure that we don't submit more entries than the imposed by the NCBI. It throws an exception if more than
     * 5000 entities are being submitted at once.
     * 
     * @param dbFromIds
     * @throws WebServiceException 
     */
    private void checkNumberOfSubmittedEntries(Collection<String> dbFromIds) throws WebServiceException {
        if(dbFromIds.size()>MAX_RECORDS_PER_QUERY)
            throw new WebServiceException("More than 5000 entries submitted, this is not permitted.... submitted "+dbFromIds.size()+" entries.");
    }

    private Pattern webEnvPattern = Pattern.compile("<WebEnv>(\\S+)<\\/WebEnv>");
    private Pattern queryKeyPattern = Pattern.compile("<QueryKey>(\\S+)<\\/QueryKey>");
    
    private EPostResult parseEpostOutput(InputStream entityInputStream) throws IOException {
        EPostResult res = new EPostResult();
        BufferedReader reader = new BufferedReader(new InputStreamReader(entityInputStream));
        String line;
        while( (line = reader.readLine())!=null ) {
            Matcher matcher = webEnvPattern.matcher(line);
            if(matcher.find()) {
                res.setWebEnv(matcher.group(1));
                continue;
            }
            matcher = queryKeyPattern.matcher(line);
            if(matcher.find()) {
                res.setQueryKey(matcher.group(1));
                continue;
            }
        }
        return res;
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
     * stored in the Multimap returned. Not more than 5,000 ids should be submitted at once.
     * 
     * @param dbFromIds the list of string identifiers to search for
     * @param dbFrom    the database in Entrez corresponding to those identifiers
     * @param dbTo      the database where we want to find hits.
     * @return          multimap with all the one-to-many associations fromDB identifiers (keys) -to- toDB identifiers (values).
     * @throws WebService exception if more than the allowed number of entries were submitted.
     */
    public Multimap<String, String> getDBToIDsFromDBFromIDs(List<String> dbFromIds, EntrezDB dbFrom, EntrezDB dbTo,String addTerm,String addTermValue) throws WebServiceException {
        checkNumberOfSubmittedEntries(dbFromIds);
        WebResource webRes = client.resource(baseURL + "elink.fcgi");
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("dbfrom", dbFrom.toString());
        queryParams.add("db", dbTo.toString());
        if(addTerm!=null && addTermValue!=null)
            queryParams.add(addTerm, addTermValue);
        for (String id : dbFromIds) {
            queryParams.add("id", id);
        }

        ClientResponse resp = submitPost(webRes, queryParams);

        if (resp.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + resp.getStatus());
        }
        Multimap<String, String> res = HashMultimap.create();
        // mapping should be one to many but there shouldn't be any replicated
        ELinkXMLResponseParser elinkXMLResponseParser = new ELinkXMLResponseParser();
        try {
            res.putAll(elinkXMLResponseParser.parseLinkSetBlock(resp.getEntityInputStream()));
        } catch (XMLStreamException ex) {
            LOGGER.warn("Could not parse output XML adequately...", ex);
        }

        return res;
    }
    
    public Multimap<String, String> getPubChemCompoundFromPubChemSubstanceIdents(List<PubChemSubstanceIdentifier> pubChemSubstanceIdentifiers) throws WebServiceException {
        List<String> pubchemSubs = new ArrayList<String>(pubChemSubstanceIdentifiers.size());
        for (PubChemSubstanceIdentifier pubChemSubstanceIdentifier : pubChemSubstanceIdentifiers) {
            pubchemSubs.add(pubChemSubstanceIdentifier.getAccession());
        }
        return getPubChemCompoundFromPubChemSubstance(pubchemSubs);
    }
    
    public Multimap<String, String> getPubChemSubstanceFromPubChemCompoundIdents(List<PubChemCompoundIdentifier> pubChemCompoundIdentifiers) throws WebServiceException {
        List<String> pubchemComps = new ArrayList<String>(pubChemCompoundIdentifiers.size());
        for (PubChemCompoundIdentifier pubChemCompoundIdentifier : pubChemCompoundIdentifiers) {
            pubchemComps.add(pubChemCompoundIdentifier.getAccession());
        }
        return getPubChemSubstanceFromPubChemCompound(pubchemComps);
    }
    
    public PubChemNamesResult getNamesForPubChemCompoundIdentifiers(Collection<PubChemCompoundIdentifier> pubchemCompoundIds) {
        Collection<String> pubchemComps = new ArrayList<String>(pubchemCompoundIds.size());
        for (PubChemCompoundIdentifier pubChemCompoundIdentifier : pubchemCompoundIds) {
            pubchemComps.add(pubChemCompoundIdentifier.getAccession());
        }
        return getNamesForPubChemCompounds(pubchemComps);
    }

    /*public Multimap<PubChemCompoundIdentifier,PubChemSubstanceIdentifier> getPubChemSubstanceIdentFromPubChemCompoundIdents(List<PubChemCompoundIdentifier> compounds) {
    
    }*/
    public Multimap<String, String> getPubChemSubstanceFromPubChemCompound(List<String> pubchemCompoundIds) throws WebServiceException {
        return this.getDBToIDsFromDBFromIDs(pubchemCompoundIds, EntrezDB.pccompound, EntrezDB.pcsubstance,"linkname","pccompound_pcsubstance_same");
    }
    
    public Multimap<String, String> getPubChemCompoundFromPubChemSubstance(List<String> pubchemSubstanceIds) throws WebServiceException {
        return this.getDBToIDsFromDBFromIDs(pubchemSubstanceIds, EntrezDB.pcsubstance, EntrezDB.pccompound,"linkname","pcsubstance_pccompound_same");
    }

    /**
     * Uses ESummary to submit a list of pubchem substance ids, for which cross references will be retrieved (taken from
     * actual cross references in pubchem + synonyms that comply with certain regexps).
     * 
     * @param pubchemSubstanceIds
     * @return multimap that links substance ids to crossreferences. 
     */
    public Multimap<String, CrossReference> getExternalIdentifiersForPubChemSubstances(Collection<String> pubchemSubstanceIds) throws WebServiceException {
        checkNumberOfSubmittedEntries(pubchemSubstanceIds);
        WebResource epostWebRes = client.resource(baseURL+"epost.fcgi");
        MultivaluedMap queryParamsEPost = new MultivaluedMapImpl();
        queryParamsEPost.add("db", "pcsubstance");
        /*for (String subsId : pubchemSubstanceIds) {
            queryParamsEPost.add("id", subsId);
        }*/
        queryParamsEPost.add("id", StringUtils.join(pubchemSubstanceIds, ","));
        ClientResponse respEpost = submitPost(epostWebRes, queryParamsEPost);
        
        if(respEpost.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + respEpost.getStatus() +" : "+ respEpost.toString());
        }
        
        /**
         * #parse WebEnv and QueryKey
         *  $web = $1 if ($output =~ /<WebEnv>(\S+)<\/WebEnv>/);
         *  $key = $1 if ($output =~ /<QueryKey>(\d+)<\/QueryKey>/);
         */
        
        Multimap<String, CrossReference> res = HashMultimap.create();
        
        EPostResult epostRes;
        try {
            epostRes = parseEpostOutput(respEpost.getEntityInputStream());
        } catch(IOException e) {
            LOGGER.error("Could not parse Epost output ", e);
            return res;
        }
        WebResource webRes = client.resource(baseURL + "esummary.fcgi");
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("db", "pcsubstance");
        queryParams.add("query_key", epostRes.getQueryKey());
        queryParams.add("WebEnv", epostRes.getWebEnv());
        //for (String id : pubchemSubstanceIds) {
            
        //}

        ClientResponse resp = submitPost(webRes, queryParams);
        
        LOGGER.info("Resp: "+resp.toString());

        if (resp.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + resp.getStatus() +" : "+ resp.toString());
        }

        

        PubChemSubstanceXMLResponseParser parser = new PubChemSubstanceXMLResponseParser();
        try {
            List<PubChemSubstanceESummaryResult> resultsParse = parser.parseESummaryResult(resp.getEntityInputStream());
            for (PubChemSubstanceESummaryResult pubChemSubstanceESummaryResult : resultsParse) {
                res.putAll(pubChemSubstanceESummaryResult.getId(), pubChemSubstanceESummaryResult.getCrossReferences());
            }
        } catch (XMLStreamException ex) {
            LOGGER.warn("Could not parse output XML adequately... returning empty result", ex);
        }
        return res;

    }
    
    /**
     * Given a set of PubChem Compound IDs (not more than 5000), this method returns the preferred name for each
     * one of the entries. For pubchem compound the prefer name tends to be the first one in the list of synonyms.
     * 
     * @param pubchemCompoundIds
     * @return cids 2 names map. 
     */
    public Map<String, String> getPreferredNameForPubChemCompounds(List<String> pubchemCompoundIds) {
        PubChemNamesResult result = getNamesForPubChemCompounds(pubchemCompoundIds);
        return result.getCompound2PreferredNameMap();
    }
    
    /**
     * Given a set of PubChem Compound IDs (not more than 5000), this method returns the preferred name for each
     * one of the entries. For pubchem compound the preferred name tends to be the first one in the list of synonyms.
     * 
     * @param pubchemCompoundIds
     * @return cids 2 names map. 
     */
    public PubChemNamesResult getNamesForPubChemCompounds(Collection<String> pubchemCompoundIds) {
        WebResource webRes = client.resource(baseURL + "esummary.fcgi");
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("db", "pccompound");
        queryParams.add("id", StringUtils.join(pubchemCompoundIds, ","));
        ClientResponse resp = submitPost(webRes, queryParams);
        
        LOGGER.info("Resp: "+resp.toString());

        if (resp.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + resp.getStatus() +" : "+ resp.toString());
        }

        
        Map<String,String> cids2name = new HashMap<String, String>();
        PubChemCompoundXMLResponseParser parser = new PubChemCompoundXMLResponseParser();
        PubChemNamesResult result = new PubChemNamesResult();
        try {
            List<PubChemCompoundESummaryResult> resultsParse = parser.parseESummaryResult(resp.getEntityInputStream());
            for (PubChemCompoundESummaryResult pubChemSubstanceESummaryResult : resultsParse) {
                result.addPreferredName(pubChemSubstanceESummaryResult.getId(), pubChemSubstanceESummaryResult.getPreferredName());
                result.addIUPACName(pubChemSubstanceESummaryResult.getId(), pubChemSubstanceESummaryResult.getIUPACName());
                result.addSynonyms(pubChemSubstanceESummaryResult.getId(), pubChemSubstanceESummaryResult.getSynonyms());
            }
        } catch (XMLStreamException ex) {
            LOGGER.warn("Could not parse output XML adequately... returning empty result", ex);
        }
        return result;   
    }

    /**
     * Checks whether the required delay has been waited or not, and waits if necessary until the required time has passed.
     */
    private void checkAndWaitForRequiredDelay() {
        Long currentCallTime = System.currentTimeMillis();
        try {
            while (previousCallTime != null && currentCallTime - previousCallTime < 3000) {
                Thread.sleep(3000);
                currentCallTime = System.currentTimeMillis();
                LOGGER.info("Waiting for required EUtils delay time...");
            }
        } catch (InterruptedException e) {
            LOGGER.warn("Problems in waiting for delay to avoid doing queries within 3 secs.");
        }
    }

    /**
     * Posts to the EUtils server should always be submitted by this method, which will check whether the required waiting 
     * time has been respected. This is currently not multithread safe.
     * 
     * @param webRes
     * @param queryParams
     * @return
     * @throws UniformInterfaceException 
     */
    private ClientResponse submitPost(WebResource webRes, MultivaluedMap queryParams) throws UniformInterfaceException {
        checkAndWaitForRequiredDelay();
        ClientResponse resp = webRes.post(ClientResponse.class,queryParams);
        previousCallTime = System.currentTimeMillis();
        return resp;
    }
    
    public class EPostResult {
        private String webEnv;
        private String queryKey;

        /**
         * @return the webEnv
         */
        public String getWebEnv() {
            return webEnv;
        }

        /**
         * @param webEnv the webEnv to set
         */
        public void setWebEnv(String webEnv) {
            this.webEnv = webEnv;
        }

        /**
         * @return the queryKey
         */
        public String getQueryKey() {
            return queryKey;
        }

        /**
         * @param queryKey the queryKey to set
         */
        public void setQueryKey(String queryKey) {
            this.queryKey = queryKey;
        }
        
        
    }
}
