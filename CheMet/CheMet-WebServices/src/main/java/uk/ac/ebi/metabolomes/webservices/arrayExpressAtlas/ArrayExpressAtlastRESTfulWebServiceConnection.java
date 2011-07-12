/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import uk.ac.ebi.metabolomes.experimental.ArrayExpEvidence;
import uk.ac.ebi.metabolomes.experimental.Evidence;
import uk.ac.ebi.metabolomes.experimental.EvidenceList;
import uk.ac.ebi.metabolomes.webservices.ExpressionWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.response.AEAtlasExpressionBean;
import uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.response.AEAtlasGeneBean;
import uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.response.AEAtlasResponseParser;
import uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.response.AEAtlasResultBean;
/**
 *
 * @author pmoreno
 */
public class ArrayExpressAtlastRESTfulWebServiceConnection extends ExpressionWebServiceConnection{

    private Client clientAES;
    private WebResource webResource;
    private final String aesBaseURL = "http://www.ebi.ac.uk/gxa/api?";
    private MultivaluedMap geneQueryParams;
    private MultivaluedMap expQueryParams;


    public ArrayExpressAtlastRESTfulWebServiceConnection() {
        this.init();
    }

    public enum AESChangeType {
        up, down, updown, upOnlyIn, downOnlyIn;
    }

    public enum AESSpecies {
        Arabidopsis_thaliana, Bacillus_subtilis,
        Caenorhabditis_elegans, Danio_rerio, Drosophila_melanogaster,
        Homo_sapiens, Mus_musculus, Rattus_norvegicus,
        Saccharomyces_cerevisiae,Schizosaccharomyces_pombe;
    }

    @Override
    public EvidenceList getExpEvidencesFor(String id) {
        throw new UnsupportedOperationException();
        /*this.resetGeneQuery();
        this.setUniprotAccOnGeneQuery(id);
        List<AEAtlasResultBean> res = this.sendGeneQuery();
        EvidenceList list = new EvidenceList(id);
        for(AEAtlasResultBean indRes : res) {
            Evidence ev = new ArrayExpEvidence();
            for(AEAtlasExpressionBean expr : indRes.getExpressions()) {
                //expr.getExperiments()

            }
        }
        throw new UnsupportedOperationException("Not supported yet.");*/
    }

    private void init() {
        clientAES = Client.create();
        webResource = clientAES.resource(aesBaseURL);
        geneQueryParams = new MultivaluedMapImpl();
        expQueryParams = new MultivaluedMapImpl();
    }

    public void resetGeneQuery() {
        geneQueryParams.clear();
    }

    public void setXMLFormat() {
        geneQueryParams.add("format", "xml");
    }

    public void setUniprotAccOnGeneQuery(String uniprotAcc) {
        geneQueryParams.add("geneUniprotIs", uniprotAcc);
    }

    public void setSpecieOnGeneQuery(AESSpecies specie) {
        geneQueryParams.add("species", specie.name().replace("_", " "));
    }

    public void setEfoOnGeneQuery(String efo, AESChangeType changeType) {
        if(!changeType.equals(AESChangeType.downOnlyIn) && !changeType.equals(AESChangeType.upOnlyIn)) {
            geneQueryParams.add(changeType.name()+"InEfo", efo);
        }
    }

    public void setExperimentOnGeneQuery(String expID) {
        //geneQueryParams.add("experiment", expID);
        geneQueryParams.add("updownIn", expID);
    }

    public void setCellTypeOnGeneQuery(String cellType, AESChangeType changeType) {
        geneQueryParams.add(changeType.name()+"InCelltype", cellType);
    }

    public void printSendGeneQuery() {
        String res = webResource.queryParams(geneQueryParams).get(String.class);
        System.out.println("res:"+res);
        this.resetGeneQuery();
    }

    public List<AEAtlasResultBean> sendGeneQuery() {
        return AEAtlasResponseParser.parseResponse(webResource.queryParams(geneQueryParams).get(String.class));
    }

    public static void main(String[] args) {
        ArrayExpressAtlastRESTfulWebServiceConnection aes = new ArrayExpressAtlastRESTfulWebServiceConnection();
        aes.setUniprotAccOnGeneQuery("P29622");
        aes.setEfoOnGeneQuery("EFO_0000802", AESChangeType.updown);
        // This probably drives the result to a ExperimentType setting.
        // Returning an experiment type result.
        // aes.setExperimentOnGeneQuery("E-GEOD-803"); // Gene cards experiment
        // Novartis Mouse cell types: E-AFMX-4
        // Novartis Human cell types: E-AFMX-5
        // Novartis Human reanalized: E-TABM-145c
        // Novartis Human reanalized: E-TABM-145a
        // E-MTAB-62 Human gene expression atlas of 5372 samples representing 369 different cell and tissue types, disease states and cell lines
        // E-GEOD-803 Transcription profiling of human normal tissue
        // E-GEOD-7307 Transcription profiling of panel of 677 samples of normal and diseased human tissues
        //http://www.ebi.ac.uk:80/gxa/api?geneIs=P29622&updownIn=E-GEOD-803&rows=100&start=0&format=xml
        aes.setXMLFormat();
        //aes.printSendGeneQuery();
        List<AEAtlasResultBean> results = aes.sendGeneQuery();
        for(AEAtlasResultBean res : results) {
            AEAtlasGeneBean gene = res.getGene();
            System.out.println("Gene:"+gene.getUniprotIDs());
            List<AEAtlasExpressionBean> expressions = res.getExpressions();
            System.out.println("Expressions:");
            for(AEAtlasExpressionBean exp : expressions) {
                System.out.println("UpExpsPValue:"+"\t"+exp.getUpExperiments()+"\t"+exp.getUpPValue());
                System.out.println("DownExpsPValue:"+"\t"+exp.getDownExperiments()+"\t"+exp.getDownPValue());
            }
        }
    }

}
