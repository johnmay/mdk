/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.webservices;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import uk.ac.ebi.metabolomes.experimental.EvidenceList;
import uk.ac.ebi.metabolomes.experimental.PrideEvidence;

/**
 *
 * @author pmoreno
 */
public class PRIDEMartRESTfulWebServiceConnection extends ExpressionWebServiceConnection {

    private Client clientPride;
    private WebResource webResource;
    private final String martBaseURL = "http://www.ebi.ac.uk/pride/biomart/martservice";
    private BioMartQuery query;
    private final String database = "pride";
    private final String dataSet = "pride";
    private HashSet<String> attributesListFromServer;
    private HashSet<String> filtersListFromServer;


    public EvidenceList getExpEvidencesFor(String prot_id) {
        this.query.resetAttributes();
        this.query.resetFilters();
        this.setQueryForGettingExpEvidenceForProt(prot_id);
        String[] resLines = this.sendQuery2BioMart();
        String[] header = this.query.getHeaderForResults();
        EvidenceList evList = new EvidenceList(prot_id);
        for(String res : resLines) {
            System.out.println(res);
            String tokens[] = res.split("\t");
            PrideEvidence pEv = new PrideEvidence();
            for(int i=0;i<tokens.length;i++) {
                PrideMartAttribute att = PrideMartAttribute.valueOf(header[i]);
                switch(att) {
                    case experiment_acc: {pEv.setExpAcc(tokens[i]);
                                            continue;}
                    case project_name:
                    case pubmed_id: {pEv.addCitation(tokens[i]);
                                        continue;}
                    case bto_ac: {pEv.setTissue(tokens[i]);
                                    continue;}
                    case protein_score: {if(tokens[i].length() > 0) pEv.setScore(Double.parseDouble(tokens[i]));
                                    continue;}
                    case protein_threshold: {if(tokens[i].length() > 0) pEv.setProtThreshold(Double.parseDouble(tokens[i]));
                                    continue;}
                    default: continue;
                }
            }
            pEv.triggerDeterminePositiveEv();
            evList.addEvidence(pEv);
        }
        return evList;
    }

        private void setQueryForGettingExpEvidenceForProt(String prot) {
        //TODO change argument to use list or array of Strings with proteins
        this.checkAndAddFilter2Query(PrideMartFilter.species_filter, "Homo sapiens (Human)");
        this.checkAndAddFilter2Query(PrideMartFilter.xref_accession_filter, prot);
        //TODO change explicit human invocation to something loaded from a config/property file
        PrideMartAttribute[] atts = {PrideMartAttribute.project_id, PrideMartAttribute.project_name,
            PrideMartAttribute.experiment_acc, PrideMartAttribute.doi,
            PrideMartAttribute.pubmed_id, PrideMartAttribute.brenda,
            PrideMartAttribute.bto_ac, PrideMartAttribute.celltype, PrideMartAttribute.cl_ac,
            PrideMartAttribute.sample_name, PrideMartAttribute.sample_description,
            PrideMartAttribute.doid_ac, PrideMartAttribute.protein_score, PrideMartAttribute.protein_threshold,
            PrideMartAttribute.uniprot_ac};

        for (PrideMartAttribute att : atts) {
            this.checkAndAddAttribute2Query(att);
        }
    }

    private String[] sendQuery2BioMart() {

        MultivaluedMap form = new MultivaluedMapImpl();
        System.out.println("Query XML is: \n"+query.produceXMLQuery());
        form.add("query", query.produceXMLQuery());
        ClientResponse cResp = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class, form);
        BufferedReader reader = new BufferedReader(new InputStreamReader(cResp.getEntityInputStream()));
        ArrayList<String> lines = new ArrayList<String>();
        try {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

        } catch (IOException ex) {
            Logger.getLogger(PRIDEMartRESTfulWebServiceConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (String[]) lines.toArray(new String[lines.size()]);

    }

    public enum PrideMartAttribute {

        project_id, project_name, experiment_acc, experiment_title, experiment_short_title,
        contact_name, institution, contact_information, reference_line, pubmed_id, doi, sample_name,
        sample_description, newt_name, newt_ac, brenda, bto_ac, celltype, cl_ac, go, go_ac, disease, doid_ac,
        submitted_accession, submitted_ac_protein_database, submitted_ac_protein_db_version, protein_sequence,
        protein_score, protein_threshold, search_engine, protein_spectrum_ref, uniprot_id__dm_seq_length,
        uniprot_id__dm_md5_hash, uniprot_ac, uniprot_id, ipi_ac, genbank_ac, genbank_gi, tair_ac_attribute,
        ensembl_no_taxon__dm_accession, human_accession, mouse_accession, ensembl_rat__dm_accession,
        ensembl_rabbit__dm_accession, ensembl_rhesusmacaque__dm_accession, ensembl_cow__dm_accession,
        ensembl_chicken__dm_accession, ensembl_tetraodon__dm_accession, ensembl_fugu__dm_accession,
        ensembl_mosquito__dm_accession, xenopus_accession, ensembl_zebrafish__dm_accession,
        ensembl_fly__dm_accession, ensembl_medaka__dm_accession, ensembl_yfmosquito__dm_accession,
        ensembl_erinaceus__dm_accession, peptide_sequence, start_coord, end_coord, peptide_spectrum_ref,
        psi_mod_ac, psi_mod_term, mod_location, mono_mass_delta, avg_mass_delta;
    }

    public enum PrideMartFilter {

        project_id_filter, project_filter, experiment_ac, species_filter, tissue_filter, celltype_filter,
        go_filter, diseaseFilter, submitted_accession_option, protein_score_greater_than, protein_score_less_than,
        protein_threshold_greater_than, protein_threshold_less_than, search_engine, submittted_ac_protein_database,
        xref_accession_filter, uniprot_ac, peptide_sequence, psi_mod_term_filter, psi_mod_accession_filter;
    }

    public PRIDEMartRESTfulWebServiceConnection() {
        this.init();

    }

    private void init() {
        clientPride = Client.create();
        webResource = clientPride.resource(martBaseURL);
        query = new BioMartQuery(this.dataSet);
        query.setUniqueRows(true);
        if (this.databaseExistsInMart(database)) {
            if (this.dataSetExistsInMart(database, dataSet)) {
                query.setDataSet(dataSet);
            }
        }
        attributesListFromServer = new HashSet<String>();
        for (String line : this.getAttributes(dataSet).split("\n")) {
            attributesListFromServer.add(line.split("\\s")[0]);
        }
        filtersListFromServer = new HashSet<String>();
        for (String line : this.getFilters(dataSet).split("\\s")) {
            filtersListFromServer.add(line.split("\\s")[0]);
        }
    }



    private boolean checkAndAddFilter2Query(PrideMartFilter filt, String value) {
        if (checkFilterExists(filt)) {
            query.setFilter(filt.name(), value);
            return true;
        }
        return false;
    }

    private boolean checkAndAddAttribute2Query(PrideMartAttribute att) {
        if (this.checkAttributeExists(att)) {
            query.setAttribute(att.name());
        }
        return false;
    }

    private String getRegistry() {
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("type", "registry");
        String resp = webResource.queryParams(queryParams).get(String.class);
        return resp;
    }

    private boolean dataSetExistsInMart(String database, String dataset) {
        String dataSets = this.getDataSets(database);
        String[] tokens = dataSets.split("\\s+");
        if (tokens[1].equals(dataset)) {
            return true;
        }
        return false;
    }

    private boolean databaseExistsInMart(String database) {
        String reg = this.getRegistry();
        String lines[] = reg.split("\n");
        for (String line : lines) {
            if (line.startsWith("<MartURLLocation")) {
                for (String token : line.trim().split("\\s")) {
                    if (token.startsWith("name=\"")) {
                        if (token.split("=")[1].equals("\"" + database + "\"")) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    private boolean checkAttributeExists(PrideMartAttribute att) {
        return this.attributesListFromServer.contains(att.name());
    }

    private boolean checkFilterExists(PrideMartFilter filt) {
        return this.filtersListFromServer.contains(filt.name());
    }

    private String getDataSets(String mart) {
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("type", "datasets");
        queryParams.add("mart", mart);
        String resp = webResource.queryParams(queryParams).get(String.class);
        return resp;
    }

    private String getAttributes(String dataSet) {
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("type", "attributes");
        queryParams.add("dataset", dataSet);
        String resp = webResource.queryParams(queryParams).get(String.class);
        return resp;
    }

    private String getFilters(String dataSet) {
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        queryParams.add("type", "filters");
        queryParams.add("dataset", dataSet);
        String resp = webResource.queryParams(queryParams).get(String.class);
        return resp;
    }

    public static void main(String[] args) {
        PRIDEMartRESTfulWebServiceConnection con = new PRIDEMartRESTfulWebServiceConnection();
        System.out.println(con.getRegistry());
        System.out.println(con.getDataSets("pride"));
        System.out.println(con.getAttributes("pride"));
        System.out.println(con.getFilters("pride"));
        EvidenceList el = con.getExpEvidencesFor("ENSP00000382688");
        System.out.println("Number of evidences: "+el.size());
        System.out.println("Number of pos evidences: "+el.getNumberPosEvidence());
        System.out.println("Tissues: ");
        for(String tissue : el.getTissueListFromEvidences())
            System.out.println(tissue);
    }

    public class BioMartQuery {

        private String queryHeader;
        private String virtualSchemaName;
        private String formatter;
        private String dataSet;
        private HashMap<String, String> filters;
        private TreeSet<String> attributes;
        private int uniqueRows;

        public BioMartQuery(String dataSet) {
            this.init(dataSet);
        }

        private void init(String dataSet) {
            this.queryHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "\n<!DOCTYPE Query>";
            this.uniqueRows = 0;
            this.virtualSchemaName = "default";
            this.formatter = "TSV";
            this.dataSet = dataSet;
            this.filters = new HashMap<String, String>();
            this.attributes = new TreeSet<String>();
        }

        public void resetFilters() {
            this.filters = new HashMap<String, String>();
        }
        public void resetAttributes() {
            this.attributes = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        }
        public String[] getHeaderForResults() {
            return this.attributes.toArray(new String[this.attributes.size()]);
        }

        private String getQueryLine() {
            return "<Query  virtualSchemaName = \"" + this.virtualSchemaName + "\" formatter = \"" + this.formatter + "\" header = \"0\" uniqueRows = \"" + this.uniqueRows + "\" count = \"\" datasetConfigVersion = \"0.6\" >\"";

        }

        private String getDataSetLine() {
            return "<Dataset name = \"" + this.dataSet + "\" interface = \"default\" >";
        }

        private String getEndDataSetLine() {
            return "</Dataset>";
        }

        private String getEndQueryLine() {
            return "</Query>";
        }

        public void setDataSet(String dataSet) {
            this.dataSet = dataSet;
        }

        public void setUniqueRows(boolean unique) {
            if (unique) {
                this.uniqueRows = 1;
            } else {
                this.uniqueRows = 0;
            }
        }

        public void setVirtualSchemaName(String name) {
            this.virtualSchemaName = name;
        }

        public void setFilter(String filterName, String filterValue) {
            if (this.filters.containsKey(filterName)) {
                filterValue = this.filters.get(filterName) + "," + filterValue;
            }
            this.filters.put(filterName, filterValue);
        }

        private String getFilterLines() {
            String res = "";
            for (String filterName : this.filters.keySet()) {
                res += "<Filter name = \"" + filterName + "\" value = \"" + this.filters.get(filterName) + "\"/>\n";
            }
            return res;

        }

        public void setAttribute(String attName) {
            this.attributes.add(attName);
        }

        private String getAttributeLines() {
            String res = "";
            for (String attr : this.attributes) {
                res += "<Attribute name = \"" + attr + "\" />";
            }
            return res;
        }

        public String produceXMLQuery() {
            String query = this.queryHeader + "\n" + this.getQueryLine() + "\n" + this.getDataSetLine() + "\n" + this.getFilterLines() + this.getAttributeLines() + this.getEndDataSetLine() + "\n" + this.getEndQueryLine();
            return query;
        }
    }
}
