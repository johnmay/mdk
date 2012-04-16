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

/**
 *
 * @author pmoreno
 */
public class EPMartRESTfulWebServiceConnection {

    private Client clientPride;
    private WebResource webResource;
    private final String martBaseURL = "http://a3.windows.ebi.ac.uk:5555/biomart/martservice";
    private BioMartQuery query;
    private final String database = "enzyme";
    private final String dataSet = "rhea";
    private HashSet<String> attributesListFromServer;
    private HashSet<String> filtersListFromServer;

//    public EvidenceList getExpEvidencesFor(String prot_id) {
//        this.query.resetAttributes();
//        this.query.resetFilters();
//        this.setQueryForGettingExpEvidenceForProt(prot_id);
//        String[] resLines = this.sendQuery2BioMart();
//        String[] header = this.query.getHeaderForResults();
//        EvidenceList evList = new EvidenceList(prot_id);
//        for(String res : resLines) {
//            System.out.println(res);
//            String tokens[] = res.split("\t");
//            PrideEvidence pEv = new PrideEvidence();
//            for(int i=0;i<tokens.length;i++) {
//                EnzymePortalMartAttribute att = EnzymePortalMartAttribute.valueOf(header[i]);
//                switch(att) {
//                    case experiment_acc: {pEv.setExpAcc(tokens[i]);
//                                            continue;}
//                    case project_name:
//                    case pubmed_id: {pEv.addCitation(tokens[i]);
//                                        continue;}
//                    case bto_ac: {pEv.setTissue(tokens[i]);
//                                    continue;}
//                    case protein_score: {if(tokens[i].length() > 0) pEv.setScore(Double.parseDouble(tokens[i]));
//                                    continue;}
//                    case protein_threshold: {if(tokens[i].length() > 0) pEv.setProtThreshold(Double.parseDouble(tokens[i]));
//                                    continue;}
//                    default: continue;
//                }
//            }
//            pEv.triggerDeterminePositiveEv();
//            evList.addEvidence(pEv);
//        }
//        return evList;
//    }
    public String[] getProductsForRxnWithRheaID(String rheaID) {
        this.setQueryForGettingChebiIDsForReaction(rheaID);
        String[] result = this.sendQuery2BioMart();

        String rxnLeft = null;
        String rxnRight = null;
        ArrayList<String> products = new ArrayList<String>();
        int count = 0;
        for (String line : result) {
            String[] tokens = line.split("\t");
            if (tokens[tokens.length - 1].equals("left")) {
                if (rxnLeft == null) {
                    rxnLeft = "";
                } else {
                    rxnLeft += " + ";
                }
                rxnLeft += tokens[1] + " " + tokens[0];
            } else {
                if (rxnRight == null) {
                    rxnRight = "";
                } else {
                    rxnRight += " + ";
                }
                rxnRight += tokens[1] + " " + tokens[0];
                products.add(tokens[0]);
            }

        }
        System.out.println(rxnLeft + " -> " + rxnRight);
        return products.toArray(new String[products.size()]);
    }

    public String[] getRheIDsForChebiLeft(String chebiid) {
        this.setQueryForGettingRheIDsForChebiReactant(chebiid);
        return this.sendQuery2BioMart();
    }

    private void setQueryForGettingExpEvidenceForProt(String prot) {
        //TODO change argument to use list or array of Strings with proteins
        //this.checkAndAddFilter2Query(EnzymePortalMartFilter.species_filter, "Homo sapiens (Human)");
        this.checkAndAddFilter2Query(EnzymePortalMartFilter.xref_accession_filter, prot);
        //TODO change explicit human invocation to something loaded from a config/property file
//        EnzymePortalMartAttribute[] atts = {EnzymePortalMartAttribute.project_id, EnzymePortalMartAttribute.project_name,
//            EnzymePortalMartAttribute.experiment_acc, EnzymePortalMartAttribute.doi,
//            EnzymePortalMartAttribute.pubmed_id, EnzymePortalMartAttribute.brenda,
//            EnzymePortalMartAttribute.bto_ac, EnzymePortalMartAttribute.celltype, EnzymePortalMartAttribute.cl_ac,
//            EnzymePortalMartAttribute.sample_name, EnzymePortalMartAttribute.sample_description,
//            EnzymePortalMartAttribute.doid_ac, EnzymePortalMartAttribute.protein_score, EnzymePortalMartAttribute.protein_threshold,
//            EnzymePortalMartAttribute.uniprot_ac};

//        for (EnzymePortalMartAttribute att : atts) {
//            this.checkAndAddAttribute2Query(att);
//        }
    }

    private void setQueryForGettingChebiIDsForReaction(String rheaID) {
        this.query.resetAttributes();
        this.query.resetFilters();

        this.checkAndAddFilter2Query(EnzymePortalMartFilter.reaction_id_filter, rheaID);
        this.checkAndAddAttribute2Query(EnzymePortalMartAttribute.accession_106);
        this.checkAndAddAttribute2Query(EnzymePortalMartAttribute.rhea_id);
        this.checkAndAddAttribute2Query(EnzymePortalMartAttribute.formula_106);
        this.checkAndAddAttribute2Query(EnzymePortalMartAttribute.coefficient_1026);
        this.checkAndAddAttribute2Query(EnzymePortalMartAttribute.direction_1012);
        this.checkAndAddAttribute2Query(EnzymePortalMartAttribute.side_1014);

    }

    private void setQueryForGettingRheIDsForChebiReactant(String chebiid) {
        this.query.resetAttributes();
        this.query.resetFilters();

        this.checkAndAddFilter2Query(EnzymePortalMartFilter.reaction_side_filter, "left");
        this.checkAndAddFilter2Query(EnzymePortalMartFilter.compound_accession_filter, chebiid);

        this.checkAndAddAttribute2Query(EnzymePortalMartAttribute.rhea_id);
    }

    private String[] sendQuery2BioMart() {

        MultivaluedMap form = new MultivaluedMapImpl();
        boolean debug = false;
        if (debug) {
            System.out.println("Query XML is: \n" + query.produceXMLQuery());
        }
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
            Logger.getLogger(EPMartRESTfulWebServiceConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (String[]) lines.toArray(new String[lines.size()]);

    }

    public enum EnzymePortalMartAttribute {

        formula_106, coeff_type_1026, accession_106, explanation_107, side_1014,
        charge_106, reaction_participants_1__dm_un_reaction_1021, reaction_participants_1__dm_name_109,
        child_accession_106, name_106, reaction_participants_1__dm_sort_order_109,
        coefficient_1026, published_106, source_106, compound_id_1026, side_1026,
        reaction_participants_1__dm_display_name_109, rhea_id, fingerprint_1021,
        source_1021, status_1021, equation_1021, qualifiers_1021, direction_1021,
        reaction_comment_1021, un_reaction_1021, data_comment_1021, display_name_109,
        name_109, sort_order_109, direction_1012, display_name_1015, name_1015, sort_order_1015,
        coefficient_105, order_in_105, child_id_105, complex_reactions__dm_coefficient_105,
        complex_reactions__dm_order_in_105, parent_id_105, reaction_citations__dm_sort_order_109,
        pub_id_1025, reaction_citations__dm_name_109, source_1025,
        reaction_citations__dm_display_name_109,
        reaction_participants__dm_name_109,
        reaction_participants__dm_sort_order_109,
        reaction_participants__dm_display_name_109, db_code_1027,
        reaction_xrefs__dm_sort_order_109, reaction_xrefs__dm_name_109,
        reaction_xrefs__dm_display_name_109, db_accession_1027, order_in_1029,
        reactions_map__dm_name_1015, active_1018, ec1_1018, web_view_1029,
        ec2_1018, ec3_1018, ec4_1018, reactions_map__dm_name_109, active_102,
        description_1030, reactions_map__dm_sort_order_1015, description_102,
        enzyme_id_1029, name_102, reactions_map__dm_display_name_109, name_1030,
        source_1018, description_1031, description_1016, name_1031, active_1031,
        status_1018, history_1018, reactions_map__dm_sort_order_109, active_1030,
        reactions_map__dm_display_name_1015, note_1018;
    }

    public enum EnzymePortalMartFilter {

        xref_accession_filter, compound_accession_filter, compound_name_filter,
        reaction_side_filter, enzyme_id_filter, reaction_id_filter, source_1021,
        status_1021, equation_1021, qualifiers_1021, direction_1021, reaction_comment_1021,
        un_reaction_1021, data_comment_1021, display_name_109, name_109, sort_order_109,
        direction_1012, display_name_1015, name_1015, sort_order_1015;
    }

    public EPMartRESTfulWebServiceConnection() {
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

    private boolean checkAndAddFilter2Query(EnzymePortalMartFilter filt, String value) {
        if (checkFilterExists(filt)) {
            query.setFilter(filt.name(), value);
            return true;
        }
        return false;
    }

    private boolean checkAndAddAttribute2Query(EnzymePortalMartAttribute att) {
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

    private boolean checkAttributeExists(EnzymePortalMartAttribute att) {
        return this.attributesListFromServer.contains(att.name());
    }

    private boolean checkFilterExists(EnzymePortalMartFilter filt) {
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
        EPMartRESTfulWebServiceConnection con = new EPMartRESTfulWebServiceConnection();
        //System.out.println(con.getRegistry());
        //System.out.println(con.getDataSets("enzyme"));
        //System.out.println(con.getAttributes("rhea"));

        ArrayList<String> visitedRheaIds = new ArrayList<String>();
        ArrayList<String> visitedChebi = new ArrayList<String>();
        System.out.println("Visiting Rhea:"+args[0]);
        String[] prods = con.getProductsForRxnWithRheaID(args[0]);
        for (String prod : prods) {
            if (visitedChebi.contains(prod)) {
                continue;
            } else {
                String[] rheaIds = con.getRheIDsForChebiLeft(prod);
                visitedChebi.add(prod);
                for (String rheaID : rheaIds) {
                    if (visitedRheaIds.contains(rheaID)) {
                        continue;
                    } else {
                        System.out.println("Visiting Rhea: " + rheaID + " for "+prod);
                        String[] newProds = con.getProductsForRxnWithRheaID(rheaID);
                        visitedRheaIds.add(rheaID);
                    }

                }
            }
        }
        //System.out.println(con.getFilters("rhea"));
        //EvidenceList el = con.getExpEvidencesFor("ENSP00000382688");
        //System.out.println("Number of evidences: "+el.size());
        //System.out.println("Number of pos evidences: "+el.getNumberPosEvidence());
        //System.out.println("Tissues: ");
        //for(String tissue : el.getTissueListFromEvidences())
        //System.out.println(tissue);
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
            this.queryHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "\n<!DOCTYPE Query>";
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
