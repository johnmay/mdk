/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.jniinchi.INCHI_RET;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.PubChemWebServiceConnection;

/**
 *
 * @author pmoreno
 */
public class SmilesChEBIQuerier {

    public static void main(String[] args) throws IOException, CDKException {
        ChEBIWebServiceConnection cebiwsc = new ChEBIWebServiceConnection();
        PubChemWebServiceConnection pcwsc = new PubChemWebServiceConnection();
        BufferedReader smiles = new BufferedReader(new FileReader(args[0]));
        SmilesParser sp = new SmilesParser(NoNotificationChemObjectBuilder.getInstance());
        InChIGeneratorFactory icigf = InChIGeneratorFactory.getInstance();
        String line = smiles.readLine();
        int noRes = 0;
        while (line != null) {
            String[] tokens = line.split("\t");
            String smile = tokens[tokens.length - 1];
            String pchmID = tokens[2];
            smile = smile.replaceAll("\"", "").trim();
            HashMap<String, Float> res = cebiwsc.searchBySmiles(smile);
            boolean noResFound = true;
            if (res.size() != 0) {
                //System.out.println("No results:"+smile);
                //line = smiles.readLine();
                //noRes++;
                //continue;

                ArrayList<String> chebIDs = new ArrayList<String>(res.keySet());
                ArrayList<Entity> entities = cebiwsc.getCompleteEntities(chebIDs);
                for (String chebiIDKey : res.keySet()) {
                    for (Entity entity : entities) {
                        if (entity.getChebiId().equals(chebiIDKey)) {
                            if (entity.getSmiles().equals(smile)) {
                                noResFound = false;
                                System.out.println(smile + "\t" + pchmID + "\t" + chebiIDKey + "\tSMILES\t" + entity.getChebiAsciiName() + "\t" + entity.getSmiles());
                            }
                            break;
                        }

                    }
                }
            }
            String inchi = null;
            String inchiKey = null;
            if (noResFound) {
                // try with inchis
                IAtomContainer atomContainer = sp.parseSmiles(smile);
                String[] pchmids = new String[1];
                pchmids[0] = pchmID;
                ArrayList<IAtomContainer> mols=pcwsc.downloadMolsToCDKObject(pchmids);
                if(mols.size()>0) {
                atomContainer = mols.get(0);

                InChIGenerator generator = icigf.getInChIGenerator(atomContainer);
                if (!generator.getReturnStatus().equals(INCHI_RET.ERROR)) {
                    inchi = generator.getInchi();
                    inchiKey = generator.getInchiKey();
                    HashMap<String, String> resInChI = cebiwsc.searchByInChI(generator.getInchiKey());
                    if (resInChI.size() > 0) {
                        ArrayList<Entity> entitiesInChI = cebiwsc.getCompleteEntities(new ArrayList<String>(resInChI.keySet()));
                        for (String chebiID : resInChI.keySet()) {
                            for (Entity entity : entitiesInChI) {
                                if (entity.getChebiId().equals(chebiID)) {
                                    System.out.println(smile + "\t" + pchmID + "\t" + chebiID + "\tINCHI\t" + entity.getChebiAsciiName() + "\t" + entity.getSmiles() + "\t" + resInChI.get(chebiID));
                                    noResFound = false;
                                }
                            }
                        }

                    }
                }


                if (noResFound) {
                    // try structure
                    HashMap<String, Float> resStruct = cebiwsc.identitySearch(atomContainer, new Float(0.0));
                    if (resStruct.size() > 0) {
                        ArrayList<Entity> entitiesStruct = cebiwsc.getCompleteEntities(new ArrayList<String>(resStruct.keySet()));
                        for (String chebiID : resStruct.keySet()) {
                            for (Entity entity : entitiesStruct) {
                                if (entity.getChebiId().equals(chebiID)) {
                                    System.out.println(smile + "\t" + pchmID + "\t" + chebiID + "\tIDENTITY\t" + entity.getChebiAsciiName() + "\t" + entity.getSmiles());
                                    noResFound = false;
                                }
                            }
                        }
                    }
                }



                }

            }

            if (noResFound) {
                noRes++;
                System.out.println(smile + "\t" + pchmID + "\tNOTFOUND\t" + inchi + "\t" + inchiKey);
            }
            line = smiles.readLine();
        }

        System.out.println("No results for:" + noRes);

    }
}
