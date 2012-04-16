/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.HashMap;

/**
 *
 * @author pmoreno
 */
public class UniProtWebServiceConnection {

    private Client client;
    private WebResource webResource;
    private final String baseURL = "http://www.uniprot.org/uniprot/"; // this should go to a config file
    private String query;
    private String taxId;
    private String tissue;
    private String ecNumber;
    private String response;

    /**
     * @param tissue the tissue to set
     */
    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public enum UniprotColumns {

        citation("citation"), clusters("clusters"),
        comments("comments"), database("database"),
        domains("domains"), domain("domain"), ec("ec"),
        id("id"), entry_name("entry name"), existence("existence"),
        families("families"), features("features"), genes("genes"),
        go("go"), go_id("go-id"), interpro("interpro"),
        interactor("interactor"), keywords("keywords"),
        keyword_id("keyword-id"), last_modified("last-modified"),
        length("length"), organism("organism"),
        organism_id("organism-id"), pathway("pathway"),
        protein_names("protein names"), reviewed("reviewed"),
        score("score"), sequence("sequence"),
        coord3d("3d"), subcellular_locations("subcellular locations"), tissue("tissue"),
        taxon("taxon"), tools("tools"), version("version"), virus_hosts("virus hosts");
        private final String displayName;

        UniprotColumns(final String display) {
            this.displayName = display;
        }

        @Override
        public String toString() {
            return this.displayName;
        }
    }

    public UniProtWebServiceConnection() {
        this.init();
    }

    private void init() {
        client = Client.create();
        webResource = client.resource(baseURL);
        this.response = "";
    }

    public void setTaxID(String taxID) {
        this.taxId = taxID;
    }

    public void setEcNumber(String ecNumber) {
        this.ecNumber = ecNumber;
    }

    

    public String[] search(String text) {
        MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        if (this.taxId != null) {
            if(!text.equals(""))
                text += " AND ";
            text += "organism:" + this.taxId;
        }
        if (this.ecNumber != null) {
            if(!text.equals(""))
                text += " AND ";
            text += "ec:" + this.ecNumber;
        }
        if(this.tissue !=null ) {
            if(!text.equals(""))
                text += " AND ";
            text += "tissue:"+this.tissue;
        }
        if(text!=null || !text.equals(""))
            queryParams.add("query", text);
        queryParams.add("format", "tab");
        queryParams.add("columns", "id,entry name,citation,families");
        this.response = webResource.queryParams(queryParams).get(String.class);
        String[] lines = this.response.split("\n");
        String[] result = new String[lines.length - 1];
        for (int i = 1; i < lines.length; i++) {
            result[i - 1] = lines[i].split("\t")[0];
        }
        return result;
    }

    public String getTaxID(String uniprotId) {
        MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        queryParams.add("query", "accession:" + uniprotId);
        queryParams.add("format", "tab");
        queryParams.add("columns", UniprotColumns.taxon.toString());
        try {
            this.response = webResource.queryParams(queryParams).get(String.class);
            String[] lines = this.response.split("\n");
            String[] result = new String[lines.length - 1];
            for (int i = 1; i < lines.length; i++) {
                result[i - 1] = lines[i].split("\t")[0];
            }
            if (result == null || result.length < 1) {
                return null;
            }
            return result[0];
        } catch (ClientHandlerException e) {
            return null;
        } 
    }

    public String[] getColumns(String uniprotId, UniprotColumns[] cols) {
        if (cols.length < 1) {
            return null;
        }
        MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        queryParams.add("query", "accession:" + uniprotId);
        queryParams.add("format", "tab");
        String colsString = "";
        for (UniprotColumns col : cols) {
            colsString += "," + col.toString();
        }
        colsString = colsString.substring(1);
        queryParams.add("columns", colsString);
        this.response = webResource.queryParams(queryParams).get(String.class);
        String[] lines = this.response.split("\n");
        String[] result;
        if (lines.length >= 2) {
            result = lines[1].split("\t");
            return result;
        }
        return null;
//        if(lines.length>2) {
//            // Accession de-merged into various accessions
//        }

    }

    public HashMap<String, String> searchBy(String text) {
        MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        if (this.taxId != null) {
            text += " AND taxonomy:" + this.taxId;
        }
        if (this.ecNumber != null) {
            text += " AND ec:" + this.ecNumber;
        }
        queryParams.add("query", text);
        queryParams.add("format", "tab");
        queryParams.add("columns", "id,protein names");
        this.response = webResource.queryParams(queryParams).get(String.class);
        String[] lines = this.response.split("\n");
        HashMap<String, String> result = new HashMap<String, String>();
        for (int i = 1; i < lines.length; i++) {
            String[] fields = lines[i].split("\t");
            result.put(fields[0], fields[1]);
        }
        return result;
    }

    public String[] getIDsOnlySearchBy(String text) {
        MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        if (this.taxId != null) {
            text += " AND taxonomy:" + this.taxId;
        }
        if (this.ecNumber != null) {
            text += " AND ec:" + this.ecNumber;
        }
        queryParams.add("query", text);
        queryParams.add("format", "tab");
        queryParams.add("columns", "id");
        this.response = webResource.queryParams(queryParams).get(String.class);
        return this.response.split("\n");
    }

    public String getFasta(String uniprotID) {
        WebResource fastaURL = client.resource(baseURL+uniprotID+".fasta");
        return fastaURL.get(String.class);
    }

    public static void main(String[] args) {
        UniProtWebServiceConnection uniProtWebServiceConnection = new UniProtWebServiceConnection();
        uniProtWebServiceConnection.setTaxID("9606");
        uniProtWebServiceConnection.search("Peroxisome proliferator activated receptors");
        System.out.println("Specie for P62988:" + uniProtWebServiceConnection.getTaxID("P62988"));
        UniprotColumns[] colsToAsk = {UniprotColumns.taxon};
        String res[] = uniProtWebServiceConnection.getColumns("P62258", colsToAsk);
        System.out.println("Tissues for P62258:" + res[0]);
        Float f = new Float(12.6765432547);
        Float f2 = new Float(3.73829172653);
        System.out.println("Float test 2 integers:" + f);
        System.out.println("Float test 1 integer: " + f2);
        HashMap<String,String> resP = uniProtWebServiceConnection.searchBy("NP_995317.1");
        UniprotColumns[] cols = {UniprotColumns.ec};
        for(String id : resP.keySet()) {
            System.out.println("Results for NP_995317.1:\t"+id+"\t"+resP.get(id));
            System.out.println("EC:\t"+uniProtWebServiceConnection.getColumns(id, cols)[0]);
        }

        System.out.println("Fasta:\n"+uniProtWebServiceConnection.getFasta("P12345")+"END OF FASTA");
    }
}
