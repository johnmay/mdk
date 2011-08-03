/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.execs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import uk.ac.ebi.metabolomes.experimental.EvidenceList;
//import uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.ArrayExpressAtlasWebServiceConnection;
import uk.ac.ebi.metabolomes.experimental.EvidenceList;
import uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.ArrayExpressAtlastRESTfulWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.ArrayExpressAtlastRESTfulWebServiceConnection;
/**
 *
 * @author pmoreno
 */
public class getArrayExpressEvidenceForUniprotList {

    public static void main(String[] args) throws IOException {
        //ArrayExpressAtlasWebServiceConnection aea = new ArrayExpressAtlasWebServiceConnection();
        ArrayExpressAtlastRESTfulWebServiceConnection aeaRF = new ArrayExpressAtlastRESTfulWebServiceConnection();
        BufferedReader file = new BufferedReader(new FileReader(args[0]));
        String line = file.readLine();
        ArrayList<String> uniprots = new ArrayList<String>();
        System.out.println("Uniprot\tLiver\tKidney\tUnderLiver\tUnderKidney");
        while(line!=null) {
            String uniprot = line.replaceAll(";", "");
            uniprots.add(uniprot);
            //String queryRes = aea.query(uniprot, "Homo Sapiens", "updn", "E-GEOD-935");
            //EvidenceList evL = aea.getExpEvidencesFor(uniprot);
            EvidenceList evL_RF = aeaRF.getExpEvidencesFor(uniprot);
            //String[] tissues = evL.getTissueListFromEvidences();
            String[] tissuesRF = evL_RF.getTissueListFromEvidences();
            //System.out.println(uniprot+'\t'+evL.getNumPosEvidenceForTissue("liver")+
            //        "\t"+evL.getNumPosEvidenceForTissue("kidney")+
            //        "\t"+evL.getNumNegEvidenceForTissue("liver")+
            //        "\t"+evL.getNumNegEvidenceForTissue("kidney"));
            System.out.print("RFVersion:");
            System.out.println(uniprot+'\t'+evL_RF.getNumPosEvidenceForTissue("liver")+
                    "\t"+evL_RF.getNumPosEvidenceForTissue("kidney")+
                    "\t"+evL_RF.getNumNegEvidenceForTissue("liver")+
                    "\t"+evL_RF.getNumNegEvidenceForTissue("kidney"));
            //System.out.println(queryRes);

            line = file.readLine();
        }
    }

}
