/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scripts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import uk.ac.ebi.metabolomes.webservices.UniProtWebServiceConnection;

/**
 *
 * @author pmoreno
 */
public class GetProteinsIDForSpecieAndTissue {

    private UniProtWebServiceConnection connection;
    private String tissue;
    private String specie;
    private String[] result;

    /**
     * @param tissue the tissue to set
     */
    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    /**
     * @param specie the specie to set
     */
    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public GetProteinsIDForSpecieAndTissue() {
        connection = new UniProtWebServiceConnection();
    }

    public void exec() {
        connection.setTaxID(this.specie);
        connection.setTissue(this.tissue);

        this.result = connection.search("");
    }

    /**
     * @return the result
     */
    public String[] getResult() {
        return result;
    }

    public static void main(String[] args) throws IOException{
        GetProteinsIDForSpecieAndTissue getter = new GetProteinsIDForSpecieAndTissue();
        String tissue = "Hepatocyte";
        getter.setSpecie("9606");
        getter.setTissue(tissue);
        getter.exec();
        FileWriter writer = new FileWriter("ProteinsFrom"+tissue+".txt");
        String[] res = getter.getResult();
        for(String line : res ) {
            writer.write(line+"\n");
        }
        writer.close();
    }


}
