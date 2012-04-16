/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.execs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;

/**
 *
 * @author pmoreno
 */
public class queryMolNames2ChEBI {

    public static void main(String args[]) throws IOException {
        BufferedReader molNames = null;
        try {
            String fileMolNames = args[0];
            molNames = new BufferedReader(new FileReader(fileMolNames));
            String name = molNames.readLine();
            ChEBIWebServiceConnection cwsc = new ChEBIWebServiceConnection();
            while (name != null) {
                HashMap<String, String> res = cwsc.searchByName(name);
                if (res.size() > 0) {
                    System.out.print("By name:\t" + name + "\t");
                    for (String id : res.keySet()) {
                        System.out.print(id + ",");
                    }
                    System.out.println();
                } else {
                    res = cwsc.searchByIupacName(name);
                    if (res.size() > 0) {
                        System.out.print("By Iupac:\t" + name + "\t");
                        for (String id : res.keySet()) {
                            System.out.print(id + ",");
                        }
                        System.out.println();
                    } else {
                        res = cwsc.searchBySynonym(name);
                        if (res.size() > 0) {
                            System.out.print("By syn:\t" + name + "\t");
                            for (String id : res.keySet()) {
                                System.out.print(id + ",");
                            }
                            System.out.println();
                        }
                        else {
                            System.out.println("Not found:\t"+name);
                        }
                    }
                }
                name = molNames.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(queryMolNames2ChEBI.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                molNames.close();
            } catch (IOException ex) {
                Logger.getLogger(queryMolNames2ChEBI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
