/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scripts;

import java.util.HashMap;
import uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.ArrayExpressAtlastRESTfulWebServiceConnection;

/**
 *
 * @author pmoreno
 */
public class GetProteinExpressionForListOfProts {

    private ArrayExpressAtlastRESTfulWebServiceConnection aearestwsc;
    private String specie;
    private String experiment;
    private String uniprotID;
    private String tissue;
    private HashMap<String, String> results;

    /**
     * @param specie the specie to set
     */
    public void setSpecie(String specie) {
        this.specie = specie;
    }

    /**
     * @param experiment the experiment to set
     */
    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }

    /**
     * @param uniprotID the uniprotID to set
     */
    public void setUniprotID(String uniprotID) {
        this.uniprotID = uniprotID;
    }

    /**
     * @param tissue the tissue to set
     */
    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public GetProteinExpressionForListOfProts() {
        this.aearestwsc = new ArrayExpressAtlastRESTfulWebServiceConnection();
    }

    public void exec() {
        this.aearestwsc.setExperimentOnGeneQuery(this.experiment);
        this.aearestwsc.setSpecieOnGeneQuery(ArrayExpressAtlastRESTfulWebServiceConnection.AESSpecies.valueOf(this.specie));
        this.aearestwsc.setEfoOnGeneQuery(this.tissue, ArrayExpressAtlastRESTfulWebServiceConnection.AESChangeType.upOnlyIn);
        this.setUniprotID(this.uniprotID);
        this.aearestwsc.printSendGeneQuery();
    }

    

}
