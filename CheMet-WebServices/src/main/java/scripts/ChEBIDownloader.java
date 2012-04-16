/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scripts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SDFWriter;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;

/**
 *
 * @author pmoreno
 */
public class ChEBIDownloader {

    private SDFWriter writer;
    private ChEBIWebServiceConnection cebiwsc;
    private String selection;

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public void setWriter(String fileOut) throws IOException {
        this.writer = new SDFWriter(new FileWriter(fileOut));
    }

    private void init() {
        cebiwsc = new ChEBIWebServiceConnection();
    }

    public ChEBIDownloader() {
        this.init();
    }

    public void execute() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(this.selection));
        String line = reader.readLine();
        ArrayList<String> chebiIDsToSend = new ArrayList<String>();
        int molCounter=0;
        while(line!=null) {
            while(line!=null && chebiIDsToSend.size() <= 50) {
                chebiIDsToSend.add(line);
                line = reader.readLine();
            }
            ArrayList<IAtomContainer> mols = cebiwsc.downloadMolsToCDKObject(chebiIDsToSend);
            for(IAtomContainer mol : mols) {
                molCounter++;
                try {
                    writer.write(mol);
                } catch (CDKException ex) {
                    System.out.println("Cannot write mol num:"+molCounter);
                }
            }
            chebiIDsToSend.clear();
        }

        writer.close();

    }

    public static void main(String[] args) throws IOException {
        String selection = args[0];
        String out = args[1];

        ChEBIDownloader chEBIDownloader = new ChEBIDownloader();
        chEBIDownloader.setSelection(selection);
        chEBIDownloader.setWriter(out);
        chEBIDownloader.execute();
    }


}
