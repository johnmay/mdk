/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas;

import ae3.service.webservices.AnyType2AnyTypeMap;
import ae3.service.webservices.AnyType2AnyTypeMap.Entry;
import ae3.service.webservices.AtlasWebServiceImpl;
import ae3.service.webservices.AtlasWebServiceImplPortType;
import ae3.service.webservices.ArrayOfAnyType2AnyTypeMap;
import ae3.service.webservices.ArrayOfString;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.ServiceException;
import uk.ac.ebi.metabolomes.experimental.ArrayExpEvidence;
import uk.ac.ebi.metabolomes.experimental.EvidenceList;
//import uk.ac.ebi.ook.web.services.Query;
import uk.ac.ebi.metabolomes.webservices.ExpressionWebServiceConnection;
import uk.ac.ebi.ook.web.services.QueryService;
import uk.ac.ebi.ook.web.services.QueryServiceLocator;
/**
 *
 * @author pmoreno
 * @deprecated uses old SOAP service. Use ArrayExpressAtlasRESTfulWebService
 */
@Deprecated
public class ArrayExpressAtlasWebServiceConnection extends ExpressionWebServiceConnection{

    private AtlasWebServiceImpl as;
    private AtlasWebServiceImplPortType asp;
    private uk.ac.ebi.ook.web.services.Query olsQuery;
    private Properties efo2bto;
    private final String efo2btoPropPath = "/efo2bto.properties";///uk/ac/ebi/metabolomes/webservices/

    public enum DataFields {
        experiment_description, gene_species, updn, gene_highlights, gene_id, ef,
        updn_pvaladj, experiment_id, efv, gene_identifier, experiment_accession, gene_name;
    }

    public enum Factors {
        biometric, cellline, celltype, clinhistory, clininfo, clintreatment,
        compound, devstage, diseaseloc, diseasestaging, diseasestate, dose,
        ecotype, envhistory, genmodif, genotype, histology, indgeneticchar,
        individual, initialtime, light, media, observation, organism, organismpart,
        phenotype, sex, strainorline, targetcelltype, temperature, test, testresult,
        time, tumorgrading, vehicle;
    }

    public enum OntologyNames {
        BTO, EFO, GO;
    }
    public ArrayExpressAtlasWebServiceConnection() {
        this.init();
    }

    private void init() {
        as = new AtlasWebServiceImpl();
        asp = as.getAtlasWebServiceImplPort();
        QueryService locator = new QueryServiceLocator();
        try {
            olsQuery = locator.getOntologyQuery();
        } catch (ServiceException ex) {
            Logger.getLogger(ArrayExpressAtlasWebServiceConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        efo2bto = new Properties();
        try {
            efo2bto.load(this.getClass().getResourceAsStream(efo2btoPropPath));
        } catch (IOException ex) {
            System.out.println("Problems with function");
            Logger.getLogger(ArrayExpressAtlasWebServiceConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String query(String gene, String org, String updn, String exp) {
        ArrayOfAnyType2AnyTypeMap arr = asp.query(gene, exp, org, updn);
        List<AnyType2AnyTypeMap> aMap = arr.getAnyType2AnyTypeMap();
        String ret = "";
        for(AnyType2AnyTypeMap m : aMap) {
            List<Entry> ent = m.getEntry();
            for(Entry e : ent) {
                String key = e.getKey().toString();
                String val = e.getValue().toString();
                ret += key+":"+val+"\t";
            }
        }
        return ret;
    }

    private ArrayOfAnyType2AnyTypeMap batchQuery(String[] genes) {
        ArrayOfString genes2Query = new ArrayOfString();
        for(String gene : genes) {
            genes2Query.getString().add(gene);
        }
        ArrayOfAnyType2AnyTypeMap res = asp.batchQuery(genes2Query, new ArrayOfString(), "", "updn");
        return res;


    }

    public EvidenceList getExpEvidencesFor(String id) {
       String fakeArray[] = new String[1];
       fakeArray[0] = id;
       ArrayOfAnyType2AnyTypeMap res = this.batchQuery(fakeArray);
       List<AnyType2AnyTypeMap> aMap = res.getAnyType2AnyTypeMap(); // we just take the first

       String ret = "";
       EvidenceList evList = new EvidenceList(id);
       for(AnyType2AnyTypeMap m : aMap) {
           List<Entry> ent = m.getEntry();
           ArrayExpEvidence ev = new ArrayExpEvidence();
           for(Entry e : ent) {
               switch (DataFields.valueOf(e.getKey().toString())) {
                   case experiment_description:
                   {
                       ev.setExpDescription(e.getValue().toString());
                       continue;
                   }
                   case updn:
                   {
                       ev.setUpdn( Integer.parseInt(e.getValue().toString()) );
                       continue;
                   }
                   case updn_pvaladj:
                   {
                       ev.setPvalue(Double.parseDouble(e.getValue().toString()));
                       continue;
                   }
                   case experiment_accession:
                   {
                       ev.setExpAcc(e.getValue().toString());
                       continue;
                   }
                   case ef:
                   {
                       ev.setFactor(e.getValue().toString());
                       continue;
                   }
                   case efv:
                   {
                       ev.setFactorValue(e.getValue().toString());
                       continue;
                   }
                   default:
                       continue;

               }

           }
           if( ev.getFactor().equalsIgnoreCase(Factors.organismpart.toString()) ) {
               ev.setFactorValue(this.getEFOIdFromEFOName(ev.getFactorValue()));
               resolveTissueForEvidence(ev);
           }
           evList.addEvidence(ev);
       }
       return evList;

    }

    private void resolveTissueForEvidenceFromOLS(ArrayExpEvidence ev) {
        Map efoTerms;
        Map brendaTerms;
        Map xrefFromEFO=null;
        try {
            //efoTerms = olsQuery.getTermsByName(ev.getFactorValue(), OntologyNames.EFO.name(), true);
            //efoTerms = olsQuery.getTermsByExactName(ev.getFactorValue().toLowerCase(), OntologyNames.EFO.name());
            //brendaTerms = olsQuery.getTermsByExactName(ev.getFactorValue().toLowerCase(), OntologyNames.BTO.name());
            //brendaTerms = olsQuery.getTermsByName(ev.getFactorValue(), OntologyNames.BTO.name(), true);
            if(ev.getFactorValue()!=null) // && ev.getFactorValue().matches("EFO_"))
                xrefFromEFO = olsQuery.getTermXrefs(ev.getFactorValue(),OntologyNames.EFO.name());
        } catch (RemoteException ex) {
            Logger.getLogger(ArrayExpressAtlasWebServiceConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean setToBrenda=false;
        if(xrefFromEFO!=null) {
            for(String key : (Set<String>) xrefFromEFO.keySet()) {
                //System.out.println("Key : "+key+"\t"+xrefFromEFO.get(key));
                if(key.startsWith("xref_analog") && ((String)xrefFromEFO.get(key)).startsWith("BTO:")) {
                    try {
                        String brendaTissue = olsQuery.getTermById((String) xrefFromEFO.get(key), OntologyNames.BTO.name());
                        ev.setTissue(brendaTissue);
                        setToBrenda=true;
                    } catch (RemoteException ex) {
                        Logger.getLogger(ArrayExpressAtlasWebServiceConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if(!setToBrenda && ev.getFactorValue()!=null && !ev.getFactorValue().equals(""));
            try {
            ev.setTissue(olsQuery.getTermById(ev.getFactorValue(), OntologyNames.EFO.name()));
        } catch (RemoteException ex) {
            Logger.getLogger(ArrayExpressAtlasWebServiceConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getEFOIdFromEFOName(String efoName) {
        String id = "";
        Map efoTerms = null;
        try {
            efoTerms = olsQuery.getTermsByExactName(efoName.toLowerCase(), OntologyNames.EFO.name());
            //efoTerms = olsQuery.getTermsByName(efoName, OntologyNames.EFO.name(), true);
        } catch (RemoteException ex) {
            Logger.getLogger(ArrayExpressAtlasWebServiceConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(efoTerms != null && efoTerms.size() == 1)
            id = (String) (efoTerms.keySet().iterator().next());
        return id;
    }

    private void resolveTissueForEvidenceFromPropFile(ArrayExpEvidence ev) {
        String btoEntry = efo2bto.getProperty(ev.getFactorValue());
        if(btoEntry != null)
            ev.setFactorValue(btoEntry);
    }

    private void resolveTissueForEvidence(ArrayExpEvidence ev) {
        //this.resolveTissueForEvidenceFromPropFile(ev);
        this.resolveTissueForEvidenceFromOLS(ev);
    }

    public String getGeneAtlas(String gene) {
        AnyType2AnyTypeMap res = asp.getAtlasGene(gene);
        List<Entry> ent = res.getEntry();
        String ret = "";
        for(Entry e : ent) {
            Object keyOb = e.getKey();
            Object valOb = e.getValue();
            String key = keyOb.toString();
            String val = valOb.toString();
            ret += key+":"+val+"\n";
        }
        return ret;

    }

    public static void main(String args[]) {
        ArrayExpressAtlasWebServiceConnection aea = new ArrayExpressAtlasWebServiceConnection();
        //String res = aea.query("ENSG00000105220", "", "", "updn");
        //System.out.println(res);
        //String geneAtlas = aea.getGeneAtlas("ENSG00000105220");
        //System.out.println("Second part:\n\n");
        //System.out.println(geneAtlas);
        //System.out.println("third part:\n\n");
        String input[] = new String[1];
        input[0] = "ENSG00000105220";
        //String batch = aea.batchQuery(input);
        EvidenceList evL = aea.getExpEvidencesFor("ENSG00000105220");
        System.out.println("Positive evidences: "+evL.getNumberPosEvidence());
        System.out.println("Negative evidences: "+evL.getNumberNegEvidence());
        String[] tissues = evL.getTissueListFromEvidences();
        for(String tissue : tissues) {
            System.out.println("For tissue "+tissue+" positive evidences: "+evL.getNumPosEvidenceForTissue(tissue));
            System.out.println("For tissue "+tissue+" negative evidences: "+evL.getNumNegEvidenceForTissue(tissue));
        }
        //System.out.println(batch);
    }

}
