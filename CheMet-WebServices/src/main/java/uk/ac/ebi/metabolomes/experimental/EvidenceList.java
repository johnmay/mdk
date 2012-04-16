/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.experimental;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author pmoreno
 */
public class EvidenceList {

    private ArrayList<Evidence> evs;
    private String bioObjectID;

    public EvidenceList(String objectID) {
        this.init();
        this.bioObjectID = objectID;
    }

    private void init() {
        evs = new ArrayList<Evidence>();
        bioObjectID = "";
    }

    public void addEvidence(Evidence e) {
        evs.add(e);
    }

    public void removeEvidence(Evidence e) {
        evs.remove(e);
    }

    public int getNumberPosEvidence() {
        int pos = 0;
        for(Evidence ev : this.evs)
            if(ev.isPositiveEv()) pos++;
        return pos;
    }

    public int getNumberNegEvidence() {
        int neg = 0;
        for(Evidence ev : this.evs)
            if(ev.isNegativeEv()) neg++;
        return neg;
    }

    public int getNumPosEvidenceForTissue(String tissue) {
        int pos = 0;
        for(Evidence ev : this.evs)
            if(ev.hasTissue() && ev.getTissue().equalsIgnoreCase(tissue) && ev.isPositiveEv()) pos++;
        return pos;
    }

    public int getNumNegEvidenceForTissue(String tissue) {
        int neg=0;
        for(Evidence ev : this.evs)
            if(ev.hasTissue() && ev.getTissue().equalsIgnoreCase(tissue) && ev.isNegativeEv()) neg++;
        return neg;
    }

    public String[] getTissueListFromEvidences() {
        Set<String> tissues = new TreeSet<String>();
        for(Evidence ev : this.evs)
            if(ev.hasTissue())
                tissues.add(ev.getTissue());
        String aux[] = new String[tissues.size()];
        return tissues.toArray(aux);

    }

    public int size() {
        return evs.size();
    }
}
