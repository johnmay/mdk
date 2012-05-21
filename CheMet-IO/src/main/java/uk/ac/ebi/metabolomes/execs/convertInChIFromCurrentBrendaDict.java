/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.execs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import uk.ac.ebi.mdk.tool.inchi.InChIConversionUtility;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;

/**
 *
 * @author pmoreno
 */
public class convertInChIFromCurrentBrendaDict {

    public static void main(String args[]) throws IOException {
        String fileName = args[0];
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        ChEBIWebServiceConnection cwsc = new ChEBIWebServiceConnection();
        String line = file.readLine();
        line = file.readLine();
        while(line!=null) {
            String[] tokens = line.split("\t");
            String[] inchiAndKey = InChIConversionUtility.convertStdInChI2InChI(tokens[1]);
            HashMap<String, String> res = cwsc.searchByInChI(inchiAndKey[0]);
            String uniqueChebiRes = "NA";
            System.out.println("Chebi res size: "+res.keySet().size());
            if(res.keySet().size() == 1)
                uniqueChebiRes = res.keySet().iterator().next();
            System.out.println(tokens[0]+"\t"+uniqueChebiRes+"\t"+tokens[1]+"\t"+tokens[2]+"\t"+inchiAndKey[0]+"\t"+inchiAndKey[1]);
            line = file.readLine();
        }
    }

}
