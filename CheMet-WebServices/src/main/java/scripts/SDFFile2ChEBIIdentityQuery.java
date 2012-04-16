/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scripts;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;

/**
 *
 * @author pmoreno
 */
public class SDFFile2ChEBIIdentityQuery {

    private ChEBIWebServiceConnection cebiwsc;
    // @todo change for an IteratingMDLReader, so that it can be set up us such
    // as well.
    private String path2SDF;
    private HashMap<String, String> perfectMatch;
    private HashMap<String, String> partialMatch;
    private Float tanimotoCutoff;

    private void init() {
        cebiwsc = new ChEBIWebServiceConnection();
        cebiwsc.setMaxResults(5);
        perfectMatch = new HashMap<String, String>();
        partialMatch = new HashMap<String, String>();
    }

    public SDFFile2ChEBIIdentityQuery() {
        this.init();
    }

    public void setPath2SDF(String path2SDF) {
        this.path2SDF = path2SDF;
    }

    public void setTanimotoCutoff(Float tanimotoCutoff) {
        this.tanimotoCutoff = tanimotoCutoff;
    }

    public void execute() throws IOException {
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        IteratingMDLReader reader = new IteratingMDLReader(new FileReader(this.path2SDF), builder);
        IMolecule mol=null;
        int molCounter = 1;
        while (reader.hasNext()) {
            mol = (IMolecule) reader.next();
            if (!isGeneric(mol)) {
                HashMap<String, Float> res = cebiwsc.identitySearch(mol, this.tanimotoCutoff);
                if (res != null) {
                    String molId = mol.getID() != null ? mol.getID() : molCounter + "";
                    for (String chebiID : res.keySet()) {
                        if (res.get(chebiID) == 1) {
                            perfectMatch.put(molId, chebiID);
                        } else if (res.get(chebiID) > this.tanimotoCutoff) {
                            if (!partialMatch.containsKey(molId)) {
                                partialMatch.put(molId, chebiID);
                            } else if (res.get(chebiID) > res.get(partialMatch.get(molId))) {
                                partialMatch.put(molId, chebiID);
                            }
                        }
                    }
                }
            }
            if(molCounter % 50 == 0)
                System.out.println("Mols done:"+molCounter+"\tPerfect:"+perfectMatch.size()+"\tPartial:"+partialMatch.size());
            molCounter++;
            
            
            
        }
        reader.close();
    }

    public HashMap<String, String> getPartialMatch() {
        return partialMatch;
    }

    public HashMap<String, String> getPerfectMatch() {
        return perfectMatch;
    }



    private boolean isGeneric(IAtomContainer mol) {

        for (IAtom atom : mol.atoms()) {
            if (atom instanceof PseudoAtom) {
                return true;
            }
        }


        for (IAtom atom : mol.atoms()) {
            if (atom instanceof PseudoAtom) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) throws IOException  {
        String sdfPath = args[0];
        SDFFile2ChEBIIdentityQuery chEBIQuerier = new SDFFile2ChEBIIdentityQuery();
        chEBIQuerier.setPath2SDF(sdfPath);
        float tanimotoCut = new Float(0.8);
        chEBIQuerier.setTanimotoCutoff(tanimotoCut);
        chEBIQuerier.execute();
        HashSet<String> diffChebis = new HashSet<String>();
        HashSet<String> diffChebisNonExact = new HashSet<String>();
        for(String molId : chEBIQuerier.getPerfectMatch().keySet()) {
            System.out.println(molId+"\t"+chEBIQuerier.getPerfectMatch().get(molId)+"\tPerfect");
            diffChebis.add(chEBIQuerier.getPerfectMatch().get(molId));
        }
        for(String molId : chEBIQuerier.getPartialMatch().keySet()) {
            System.out.println(molId+"\t"+chEBIQuerier.getPartialMatch().get(molId)+"\tPartial");
            diffChebisNonExact.add(chEBIQuerier.getPartialMatch().get(molId));
        }

        System.out.println("Different identical matches:"+diffChebis.size());
        System.out.println("Different aprox matchs ("+tanimotoCut+"):"+diffChebisNonExact.size());
    }
}
