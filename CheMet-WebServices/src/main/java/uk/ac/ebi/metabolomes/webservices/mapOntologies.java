/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.rpc.ServiceException;
import uk.ac.ebi.ook.Constants;
import uk.ac.ebi.ook.web.services.Query;
import uk.ac.ebi.ook.web.services.QueryService;
import uk.ac.ebi.ook.web.services.QueryServiceLocator;
/**
 *
 * @author pmoreno
 */
public class mapOntologies {

    public static void main(String args[]) throws IOException {
        Query olsQuery = null;
        QueryServiceLocator locator;
        locator = new QueryServiceLocator();
        String ontFrom = args[0];
        String ontTo = args[1];
        String parentOntFrom = args[2];
        int depthOntFromParent = Integer.parseInt(args[3]); // use 10
        String mappedFilePrefix = args[4];
        int relationsToConsider[] = new int[2];
        relationsToConsider[0] = Constants.IS_A_RELATION_TYPE_ID;
        relationsToConsider[1] = Constants.PART_OF_RELATION_TYPE_ID;
        HashMap<String, String> childrenFrom = new HashMap<String, String>();
        //HashMap<String, String> testQuery = new HashMap<String, String>();
        //HashMap<String, String> children = new HashMap<String, String>();
        try {
            olsQuery = locator.getOntologyQuery();
            childrenFrom = olsQuery.getTermChildren(parentOntFrom, ontFrom, depthOntFromParent, relationsToConsider);
            //testQuery = olsQuery.getTermsByName("animal", ontFrom, false);
            //children = olsQuery.getTermRelations(parentOntFrom, ontFrom);
        } catch (ServiceException ex) {
            Logger.getLogger(mapOntologies.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
                Logger.getLogger(mapOntologies.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Starting from "+ontFrom+"'s term:"+parentOntFrom+" using depth "+depthOntFromParent);
        System.out.println("Found "+childrenFrom.keySet().size()+" members.");
        System.out.println("Searching those terms on the other ontology");
        HashMap<String, ArrayList> ontFrom2ontTo = new HashMap<String, ArrayList>();
        HashMap<String, String> ontToAllRes = new HashMap<String, String>();
        HashMap<String, String> notMapped = new HashMap<String, String>();
        int withOneMapping=0;
        int withMoreThanOneMapping=0;
        int withExactMapping=0;
        for(String key : childrenFrom.keySet()) {
            try {
                //System.out.println(key + " : " + childrenFrom.get(key));
                HashMap<String, String> qFromTo = olsQuery.getTermsByExactName(childrenFrom.get(key), ontTo);
                if(qFromTo != null && qFromTo.size() > 0)
                    withExactMapping++;
                if (qFromTo == null || qFromTo.size() == 0) {
                    HashMap<String, String> aux = olsQuery.getTermsByName(childrenFrom.get(key), ontTo, true);
                    for(String keyAux : aux.keySet())
                        qFromTo.put(aux.get(keyAux), keyAux);
                }
                if (qFromTo != null || qFromTo.size() > 0) {
                    ontToAllRes.putAll(qFromTo);
                    ArrayList<String> res = new ArrayList<String>();
                    res.addAll(qFromTo.keySet());
                    ontFrom2ontTo.put(key, res);
                    if(res.size()==1)
                        withOneMapping++;
                    if(res.size()>1)
                        withMoreThanOneMapping++;
                }
                if(qFromTo == null || qFromTo.size() == 0)
                    notMapped.put(key, childrenFrom.get(key));
            } catch (RemoteException ex) {
                Logger.getLogger(mapOntologies.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

        PrintWriter notMappedOut = new PrintWriter(new FileWriter(mappedFilePrefix+".notmapped.xls"));
        int mappedThroughParent=0;
        HashMap<String, String> ontFromTermMatchedThroughParent = new HashMap<String, String>();
        for(String fromOntTerm : notMapped.keySet()) {
                HashMap<String, String> parentTerms = olsQuery.getTermParents(fromOntTerm, ontFrom);
                for(String parentTerm : parentTerms.keySet()) {
                    if(ontFrom2ontTo.containsKey(parentTerm)) {
                        ontFrom2ontTo.put(fromOntTerm,ontFrom2ontTo.get(parentTerm));
                        ontFromTermMatchedThroughParent.put(fromOntTerm, parentTerm);
                        mappedThroughParent++;
                    }
                    else {
                        notMappedOut.println(fromOntTerm+"\t"+notMapped.get(fromOntTerm));
                    }
                }
        }
        notMappedOut.close();
        PrintWriter mapped = new PrintWriter(new FileWriter(mappedFilePrefix+".mapped.xls"));
        PrintWriter mappThroughParent = new PrintWriter(new FileWriter(mappedFilePrefix+".mappedThroughParent.xls"));
        for(String key : ontFrom2ontTo.keySet()) {
            if(ontFrom2ontTo.get(key).size() == 1 ) {
                if(ontFromTermMatchedThroughParent.containsKey(key)) {
                    String result = (String) (ontFrom2ontTo.get(key)).get(0);
                    mappThroughParent.println(childrenFrom.get(key)+"\t"+key+"\t"+result);
                }
                else {
                    String result = (String) (ontFrom2ontTo.get(key)).get(0);
                    mapped.println(childrenFrom.get(key)+"\t"+key+"\t"+result);
                }
            }
        }
        mapped.close();
        PrintWriter multiMappings = new PrintWriter(new FileWriter(mappedFilePrefix+".multiMap.xls"));
        for(String key : ontFrom2ontTo.keySet()) {
            if(ontFrom2ontTo.get(key).size() > 1) {
                multiMappings.println(childrenFrom.get(key)+"\t"+key);
                for(Object res : ontFrom2ontTo.get(key))
                    multiMappings.println("\t"+(String)res+"\t"+ontToAllRes.get((String)res));
            }
        }
        multiMappings.close();
        System.out.println("One mapping: "+withOneMapping);
        System.out.println("Exact mapping:"+withExactMapping);
        System.out.println(">1 mapping "+withMoreThanOneMapping);
        System.out.println("Mapped through parent:"+mappedThroughParent);
    }

    private static String join(ArrayList<String> t) {
        String res = "";
        for(String tp : t)
            res += tp+";";
        return res;
    }

}
