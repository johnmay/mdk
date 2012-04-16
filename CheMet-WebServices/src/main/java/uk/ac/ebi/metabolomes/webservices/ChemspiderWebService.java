/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import java.io.IOException;
import java.util.List;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.resource.ClientResource;

/**
 *
 * @author pmoreno
 */
public class ChemspiderWebService {
    
    private final String token = "b7048eec-07c1-496e-9266-ae18d34736b1";
    private final Reference referenceSimpleSearch = new Reference("www.chemspider.com/Search.asmx/AsyncSimpleSearch"); 
    /*
     * GET /Search.asmx/AsyncSimpleSearch?query=string&token=string HTTP/1.1
       Host: www.chemspider.com
     */
    
    public String makeSimpleSearch(String name) {
        
        Component c = new Component();
        Client client = c.getClients().add(Protocol.HTTP);
        Reference reference = new Reference("www.chemspider.com/Search.asmx/AsyncSimpleSearch"); 
        reference.setProtocol(Protocol.HTTP);
        reference.addQueryParameter("query", name);
        reference.addQueryParameter("token", this.token);
        ClientResource resource = new ClientResource(reference);
        resource.setNext(client);
        resource.setMethod(Method.GET);
        
        String res=null;
        try {
            res = resource.get().getText();
            System.out.println(res);
        } catch(IOException e) {
            System.err.println("Problems getting input from OLS:"+e.getMessage());
        }
        return res;
    } 
    
    
    
    
    
    
}
