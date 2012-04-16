/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.bioObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pmoreno
 */
public class TransportReaction extends Reaction {

    private HashMap<String,String> participantName2Compartment;
    private static final Pattern inPattern = Pattern.compile("\\/in$");
    private static final Pattern outPattern = Pattern.compile("\\/out$");

    public TransportReaction(Reaction r) {
        super();
        this.participantName2Compartment = new HashMap<String, String>();
        HashMap<String,Float> reactProds = r.getReactantsProducts();

        for (String metabolite : reactProds.keySet()) {
            Float coefficient = reactProds.get(metabolite);
            Matcher inMatcher = inPattern.matcher(metabolite);
            if(inMatcher.find()) {
                metabolite = inMatcher.replaceAll("");
                this.participantName2Compartment.put(metabolite, "in");
            } else {
                Matcher outMatcher = outPattern.matcher(metabolite);
                if(outMatcher.find()) {
                    metabolite = outMatcher.replaceAll("");
                    this.participantName2Compartment.put(metabolite, "out");
                }
            }

            if(coefficient < 0) {
                super.addReactant(metabolite, coefficient);
            } else {
                super.addProduct(metabolite, coefficient);
            }

        }
    }

    public static boolean isATransportReaction(Reaction r) {
        if(r instanceof TransportReaction)
            return true;

        int withPattern=0;
        for (String metabs : r.getMetabolites()) {
            if(inPattern.matcher(metabs).find())
                withPattern++;
            else if(outPattern.matcher(metabs).find())
                withPattern++;
        }
        if(withPattern>=2)
            return true;
        return false;
    }
}
