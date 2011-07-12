/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scripts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import uk.ac.ebi.metabolomes.webservices.UniProtWebServiceConnection;

/**
 *
 * @author pmoreno
 */
public class ensemblGene2UniprotID {

    private static UniProtWebServiceConnection connection = new UniProtWebServiceConnection();

    public static String[] getUniprotsIDForEnsemblGeneID(String ensemblGeneID) {
        return connection.search(ensemblGeneID);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        String line = reader.readLine();
        FileWriter result = new FileWriter(args[0]+"_uniprotIDs.txt");
        while(line!=null) {
            String[] uniprotIDs = getUniprotsIDForEnsemblGeneID(line);
            for(String uniprotID : uniprotIDs) {
                result.write(line+"\t"+uniprotID+"\n");
            }
            line = reader.readLine();
        }
        result.close();
    }

}
