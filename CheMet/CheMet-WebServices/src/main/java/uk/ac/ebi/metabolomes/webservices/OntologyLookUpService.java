/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.ServiceException;
import uk.ac.ebi.ook.web.services.Query;
import uk.ac.ebi.ook.web.services.QueryServiceLocator;
import uk.ac.ebi.metabolomes.webservices.util.CandidateEntry;
import uk.ac.ebi.metabolomes.webservices.util.EntryDecider;

/**
 *
 * @author pmoreno
 */
public class OntologyLookUpService {
    private QueryServiceLocator locator;
    private EntryDecider decider;

    public OntologyLookUpService() {
        this.locator = new QueryServiceLocator();
        this.decider = new EntryDecider();

    }

    public Map<String,String> getTermsByName(String query, String ontology) throws ServiceException, RemoteException {
        Query olsQuery = locator.getOntologyQuery();
        return olsQuery.getTermsByName(query, ontology, false);
    }

    public Map<String,String> getTermsPrefixed(String query) throws ServiceException, RemoteException {
        Query olsQuery = locator.getOntologyQuery();
        return olsQuery.getPrefixedTermsByName(query, false);
    }

    public Set<CandidateEntry> getRankedCandidates(String query, String ontology, Integer maxRes) throws ServiceException, RemoteException {
        Set<CandidateEntry> res = decider.decideTissueCandidate(query, this.getTermsByName(query, ontology));
        if(maxRes!=null && res.size()>maxRes) {
            int element=1;
            for(CandidateEntry entry : res) {
                if(element>maxRes)
                    res.remove(entry);
                element++;
            }
        }
        return res;
    }
    
    public Set<CandidateEntry> getRankedCandidates(String query, Integer maxRes) throws ServiceException, RemoteException {
        Set<CandidateEntry> res = decider.decideTissueCandidate(query, this.getTermsPrefixed(query));
        if(maxRes!=null && res.size()>maxRes) {
            int element=1;
            for(CandidateEntry entry : res) {
                if(element>maxRes)
                    res.remove(entry);
                element++;
            }
        }
        return res;
    }



    public static void main(String[] args) throws ServiceException, RemoteException {
        OntologyLookUpService lus = new OntologyLookUpService();

        Map<String,String> res = lus.getTermsByName("brain", "EFO");
        for (String tissue : res.keySet()) {
            System.out.println(tissue+"\t"+res.get(tissue));
        }

        System.out.println("Prefixed:");

        Map<String,String> res2 = lus.getTermsPrefixed("brain");
        for (String tissue : res2.keySet()) {
            System.out.println(tissue+"\t"+res2.get(tissue));
        }
    }
}
