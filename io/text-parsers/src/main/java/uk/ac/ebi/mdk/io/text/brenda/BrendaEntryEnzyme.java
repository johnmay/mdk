/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.io.text.brenda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class BrendaEntryEnzyme {

    private List<String> ecNumber;
    private String sysName;
    private String recommendedName;
    private List<Reaction> reac;
    private Map<String, Integer> comment; // for comments and citations
    private List<String> protDummyIDs;
    private Map<String, BrendaProtein> brendaProteins;
    private Map<String, Citation> citationsObj;
    private String ecComment;
    private Set<String> sourceTissues;
    private List<String> localization;
    private List<String> extProtDB;
    private List<String> extProtDBIdent;
    private List<Reaction> naturalSubsProdRxn;
    private Map<Integer, List<Citation>> nsp2citations; // Holds the index of the naturalSubs and the citation numbers of the citations
    private Map<Integer, List<Citation>> tissue2citations;
    private Map<Integer, List<Citation>> localization2citations;
    private HashMap<String, String> extProtIdDB;
    private List<Reaction> substrateProductRxns;

    public BrendaEntryEnzyme() {
        reac = new ArrayList<Reaction>();
        citationsObj = new HashMap<String, Citation>();
        ecNumber = new ArrayList<String>();
        sourceTissues = new HashSet<String>();
        localization = new ArrayList<String>();
        extProtDB = new ArrayList<String>();
        extProtDBIdent = new ArrayList<String>();
        extProtIdDB = new HashMap<String, String>();
        naturalSubsProdRxn = new ArrayList<Reaction>();
        nsp2citations = new HashMap<Integer, List<Citation>>();
        tissue2citations = new HashMap<Integer, List<Citation>>();
        localization2citations = new HashMap<Integer, List<Citation>>();
        substrateProductRxns = new ArrayList<Reaction>();
        protDummyIDs = new ArrayList<String>();

        brendaProteins = new HashMap<String, BrendaProtein>();
    }

    public boolean addEcNumber(String ec) {
        return ecNumber.add(ec);
    }

    public void addRecommendedName(String name) {
        this.recommendedName = name;
    }

    public void setSystematicName(String name) {
        this.sysName = name;
    }

    public void addCitationList(List<String> citations) {
        //this.citations.addAll(citations);
        for (String citKey : citations) {
            this.citationsObj.put(citKey, new Citation());
        }
    }

    public void setCitationTitle(String key, String title) {
        this.citationsObj.get(key).setTitle(title);
    }

    public void setCitationPubMedId(String key, int pubmedId) {
        this.citationsObj.get(key).setPubmedId(pubmedId);
    }

    public boolean addReaction(Reaction r) {
        if (this.isRxnValid(r)) {
            return this.reac.add(r);
        } else {
            return false;
        }
    }

    public boolean addNatSubsProdRxn(Reaction r) {
        if (this.isRxnValid(r)) {
            return this.naturalSubsProdRxn.add(r);
        } else {
            return false;
        }
    }

    public void addNatSubsProdRxn(Reaction r, String dummyProtId) {
        if( this.isRxnValid(r)) {
            if(!this.naturalSubsProdRxn.contains(r))
                this.naturalSubsProdRxn.add(r);
            if(this.brendaProteins.containsKey(dummyProtId))
                this.brendaProteins.get(dummyProtId).addNatSubProdRxn(r);
        }
    }

    public void addNatSubsProdRxns(List<Reaction> rxns) {
        this.naturalSubsProdRxn.addAll(rxns);
    }

    private boolean isRxnValid(Reaction r) {
        for (String metab : r.getMetabolites()) {
            if (metab.toLowerCase().trim().equals("more")) {
                return false;
            } else if (metab.toLowerCase().trim().equals("?")) {
                return false;
            } else if (metab.length() == 0) {
                return false;
            }
        }
        return true;
    }

    public void addEcComment(String comm) {
        this.ecComment = comm;
    }

    public boolean citationKeyExists(String key) {
        if (citationsObj.containsKey(key)) {
            return true;
        }
        /*		if( citations.indexOf(key) >= 0 )
        return true;*/
        return false;
    }

    public void addSourceTissue(String st) {
        if (st != null) {
            sourceTissues.add(st.toLowerCase());
        }
    }

    public void addSourceTissue(String st, String dummyProtId) {
        if (st != null) {
            if (this.brendaProteins.containsKey(dummyProtId)) {
                brendaProteins.get(dummyProtId).addSourceTissue(st);
            }
            if (!this.sourceTissues.contains(st)) {
                this.sourceTissues.add(st);
            }
        }
    }

    public void addLocalization(String lo) {
        if(lo!=null && !localization.contains(lo))
            localization.add(lo);
    }

    public void addLocalization(String lo,String dummyProtId) {
        if(lo!=null) {
            if(!localization.contains(lo))
                localization.add(lo);
            if(this.brendaProteins.containsKey(dummyProtId))
                this.brendaProteins.get(dummyProtId).addLocalization(lo);
        }
    }

    public void copyEssentialsFrom(BrendaEntryEnzyme orig) {
        this.ecNumber = orig.ecNumber;
        this.ecComment = orig.ecComment;
    }

    public void addProteinCrossRef(String db, String id) {
        this.extProtIdDB.put(id, db);
    }

    public void addProteinCrossRef(String dummyId, String db, String id) {
        this.extProtIdDB.put(id, db);
        if(this.brendaProteins.containsKey(dummyId)) {
            this.brendaProteins.get(dummyId).addCrossRef(db, id);
        }
    }

    public HashMap<String, String> getProteinCrossRefs() {
        return extProtIdDB;
    }

    public List<Reaction> getReactions() {
        return reac;
    }

    public boolean addCitationLink2lastAddedNSP(String num) {
        if (!this.citationKeyExists(num)) {
            return false;
        }
        int currentNSPIndex = this.naturalSubsProdRxn.size() - 1;
        List<Citation> cits;
        // First check if for the "current" NSP, ie the last one added, there are citations
        if (this.nsp2citations.containsKey(this.naturalSubsProdRxn.size() - 1)) // if there are, we just add the new one
        {
            cits = nsp2citations.get(currentNSPIndex);
        } else // we need to create the ArrayList
        {
            cits = new ArrayList<Citation>();
        }
        cits.add(this.citationsObj.get(num));
        nsp2citations.put(currentNSPIndex, cits);
        this.naturalSubsProdRxn.get(this.naturalSubsProdRxn.size() - 1).addCitations(cits);
        return true;
    }

    public List<Reaction> getNaturalSubsProdRxns() {
        return this.naturalSubsProdRxn;
    }

    public boolean addCitationLink2lastAddedTissue(String num) {
        if (!this.citationKeyExists(num)) {
            return false;
        }
        int currentSourceTissueIndex = this.sourceTissues.size() - 1;
        List<Citation> cits;
        // First check if for the "current" NSP, ie the last one added, there are citations
        if (this.tissue2citations.containsKey(this.sourceTissues.size() - 1)) // if there are, we just add the new one
        {
            cits = tissue2citations.get(currentSourceTissueIndex);
        } else // we need to create the ArrayList
        {
            cits = new ArrayList<Citation>();
        }
        cits.add(this.citationsObj.get(num));
        tissue2citations.put(currentSourceTissueIndex, cits);
        return true;
    }

    public boolean addCitationLink2lastAddedLoc(String num) {
        if (!this.citationKeyExists(num)) {
            return false;
        }
        int currentLocalizationIndex = this.localization.size() - 1;
        List<Citation> cits;
        // First check if for the "current" NSP, ie the last one added, there are citations
        if (this.localization2citations.containsKey(this.localization.size() - 1)) // if there are, we just add the new one
        {
            cits = localization2citations.get(currentLocalizationIndex);
        } else // we need to create the ArrayList
        {
            cits = new ArrayList<Citation>();
        }
        cits.add(this.citationsObj.get(num));
        localization2citations.put(currentLocalizationIndex, cits);
        return true;
    }

    public ArrayList<String> getProteinLinks() {
        ArrayList<String> ans = new ArrayList<String>();
        //if(this.extProtDBIdent.size() > 0)
        for (String key : this.extProtIdDB.keySet()) {
            ans.add(ecNumber.get(0) + "\t" + extProtIdDB.get(key) + "\t" + key);
        }
        //else if(ecNumber.size()>0)
        //return ecNumber.get(0)+"\t";
        return ans;
    }

    /**
     * @return the citationsObj
     */
    public Set<Citation> getCitations() {
        Set<Citation> cits = new HashSet<Citation>();
        cits.addAll(this.citationsObj.values());
        return cits;
    }

    public Set<String> getTissues() {
        return this.sourceTissues;
    }

    public String getSystematicName() {
        return this.sysName;
    }

    public String getEcNumber() {
        // TODO Check this
        return ecNumber.get(0);
    }

    public Set<String> getLocations() {
        Set<String> loc = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        for (String l : this.localization) {
            if (l != null) {
                loc.add(l);
            }
        }
        return loc;
    }

    public Set<String> getMetabolitesFromRERxns() {
        Set<String> metabolitesRE = new HashSet<String>();
        for (Reaction reaction : this.reac) {
            metabolitesRE.addAll(reaction.getMetabolites());
        }
        return metabolitesRE;
    }

    public Set<String> getMetabolitesFromNSPRxns() {
        Set<String> metabolites = new HashSet<String>();
        for (Reaction reaction : this.naturalSubsProdRxn) {
            metabolites.addAll(reaction.getMetabolites());
        }
        return metabolites;
    }

    /**
     * @return the recommendedName
     */
    public String getRecommendedName() {
        return recommendedName;
    }

    /**
     * @param recommendedName the recommendedName to set
     */
    public void setRecommendedName(String recommendedName) {
        this.recommendedName = recommendedName;
    }

    public boolean addSubstrateProductRxn(Reaction rxn) {
        if (this.isRxnValid(rxn)) {
            return this.substrateProductRxns.add(rxn);
        } else {
            return false;
        }
    }

    public List<Reaction> getSubstrateProductRxns() {
        return this.substrateProductRxns;
    }

    public void addCitationKeysForProtein(String dummyProteinEntryIdentifier, List<String> citations) {
        if (this.brendaProteins.containsKey(dummyProteinEntryIdentifier)) {
            for (String citKey : citations) {
                this.brendaProteins.get(dummyProteinEntryIdentifier).addCitationKey(citKey);
            }
        }
    }

    public List<Citation> getCitationsForProtein(String dummyProteinIdentifer) {
        List<Citation> cits = new ArrayList<Citation>();
        if (this.brendaProteins.containsKey(dummyProteinIdentifer)) {
            for (String citKey : this.brendaProteins.get(dummyProteinIdentifer).getCitationKeys()) {
                cits.add(this.citationsObj.get(citKey));
            }
        }
        return cits;
    }

    /**
     * @return the protDummyIDs
     */
    public List<String> getProtDummyIDs() {
        return new ArrayList<String>(this.brendaProteins.keySet());
    }

    public void addProtDummyID(String dummyID) {
        //this.protDummyIDs.add(dummyID);
        this.brendaProteins.put(dummyID, new BrendaProtein());
    }

    public Collection<BrendaProtein> getBrendaProteinsForEntry() {
        return this.brendaProteins.values();
    }

    public class BrendaProtein {

        private String dummyId;
        private List<String> citationKeys;
        private List<String> localizations;
        private List<String> sourceTissues;
        private List<Reaction> nspReactions;
        private HashMap<String, String> crossRefs;

        public BrendaProtein() {
            this.init();
        }

        private void init() {
            this.citationKeys = new ArrayList<String>();
            this.crossRefs = new HashMap<String, String>();
            this.localizations = new ArrayList<String>();
            this.nspReactions = new ArrayList<Reaction>();
            this.sourceTissues = new ArrayList<String>();
        }

        /**
         * @return the dummyId
         */
        public String getDummyId() {
            return dummyId;
        }

        /**
         * @param dummyId the dummyId to set
         */
        public void setDummyId(String dummyId) {
            this.dummyId = dummyId;
        }

        /**
         * @return the citationKeys
         */
        public List<String> getCitationKeys() {
            return citationKeys;
        }

        /**
         * @return the localizations
         */
        public List<String> getLocalizations() {
            return localizations;
        }

        /**
         * @return the sourceTissues
         */
        public List<String> getSourceTissues() {
            return sourceTissues;
        }

        /**
         * @return the nspReactions
         */
        public List<Reaction> getNspReactions() {
            return nspReactions;
        }

        /**
         * @return the crossRefs
         */
        public HashMap<String, String> getCrossRefs() {
            return crossRefs;
        }

        private void addSourceTissue(String st) {
            if(!this.sourceTissues.contains(st))
                this.sourceTissues.add(st);
        }

        private void addCitationKey(String citKey) {
            if(!this.citationKeys.contains(citKey))
                this.citationKeys.add(citKey);
        }

        private void addCrossRef(String db, String id) {
            this.crossRefs.put(db, id);
        }

        private void addLocalization(String lo) {
            if(!this.localizations.contains(lo))
                this.localizations.add(lo);
        }

        private void addNatSubProdRxn(Reaction r) {
            if(!this.nspReactions.contains(r))
                this.nspReactions.add(r);
        }
    }
}
