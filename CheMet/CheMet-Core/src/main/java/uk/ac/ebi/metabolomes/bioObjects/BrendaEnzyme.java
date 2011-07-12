
package uk.ac.ebi.metabolomes.bioObjects;

import java.util.*;

public class BrendaEnzyme {

	private Vector<String> ecNumber;
	private String sysName;
	private String recommendedName;
	private Reaction reac;
	private HashMap<String, Integer> comment; // for comments and citations
	//private ArrayList<String> citations;
	//private HashMap<Integer, String> citationsDesc;
	//private HashMap<Integer, Integer> pubMedIds;
	private HashMap<String, Citation> citationsObj;
	private String ecComment;
	private ArrayList<String> sourceTissues;
	private ArrayList<String> localization;
	private ArrayList<String> extProtDB;
	private ArrayList<String> extProtDBIdent;
	private ArrayList<Reaction> naturalSubsProdRxn;
	private HashMap<Integer, ArrayList<Citation>> nsp2citations; // Holds the index of the naturalSubs and the citation numbers of the citations
	private HashMap<Integer, ArrayList<Citation>> tissue2citations;
	private HashMap<Integer, ArrayList<Citation>> localization2citations;
	private HashMap<String, String> extProtIdDB;

	public BrendaEnzyme() {
		reac = new Reaction();
	//	citations = new ArrayList<String>();
		citationsObj = new HashMap<String, Citation>();
		ecNumber = new Vector<String>();
	//	citationsDesc = new HashMap<Integer, String>();
	//	pubMedIds = new HashMap<Integer, Integer>();
		sourceTissues = new ArrayList<String>();
		localization = new ArrayList<String>();
		extProtDB = new ArrayList<String>();
		extProtDBIdent = new ArrayList<String>();
		extProtIdDB = new HashMap<String, String>();
		naturalSubsProdRxn = new ArrayList<Reaction>();
	//	nsp2citations = new HashMap<Integer, ArrayList<String>>();
	//	tissue2citations = new HashMap<Integer, ArrayList<String>>();
	//	localization2citations = new HashMap<Integer, ArrayList<String>>();
		nsp2citations = new HashMap<Integer, ArrayList<Citation>>();
		tissue2citations = new HashMap<Integer, ArrayList<Citation>>();
		localization2citations = new HashMap<Integer, ArrayList<Citation>>();
	}
	public boolean addEcNumber(String ec) {
		return ecNumber.add(ec);
	}
	public void addRecommendedName(String name) {
		this.recommendedName = name;
	}
	public void addSystematicName(String name) {
		this.sysName = name;
	}
	public void addCitationList(List<String> citations) {
		//this.citations.addAll(citations);
		for(String citKey : citations) {
			this.citationsObj.put(citKey, new Citation());
		}
	}
/*	public void addCitationDesc(int number, String desc) {
		this.citationsDesc.put(number, desc);
	}*/
	public void setCitationTitle(String key, String title) {
		this.citationsObj.get(key).setTitle(title);
	}
/*	public void addCitationPubMedId(int number, Integer pubmedId) {
		this.pubMedIds.put(number,pubmedId);
	}*/
	public void setCitationPubMedId(String key, int pubmedId) {
		this.citationsObj.get(key).setPubmedId(pubmedId);
	}
	public void addReaction(Reaction r) {
		this.reac = r;
	}
	public boolean addNatSubsProdRxn(Reaction r) {
		return this.naturalSubsProdRxn.add(r);
	}
	public void addEcComment(String comm) {
		this.ecComment = comm;
	}
	public boolean citationKeyExists(String key) {
		if( citationsObj.containsKey(key))
			return true;
/*		if( citations.indexOf(key) >= 0 )
			return true;*/
		return false;
	}
	public void addSourceTissue(String st) {
		sourceTissues.add(st);
	}
	public void addLocalization(String lo) {
		localization.add(lo);
	}
	public void copyEssentialsFrom(BrendaEnzyme orig) {
		this.ecNumber = orig.ecNumber;
		this.ecComment = orig.ecComment;
	}
	public void addProteinLink(String db, String id) {
//		this.extProtDB.add(db);
//		this.extProtDBIdent.add(id);
		this.extProtIdDB.put(id, db);
	}
	public HashMap<String, String> getProteinCrossRefs() {
		return extProtIdDB;
	}
	public Reaction getReaction() {
		return reac;
	}
//	public boolean nspRxnExists(Reaction rxn) {
//		for(Reaction r : this.naturalSubsProdRxn) {
//			if( rxn.equals(r) )
//				return true;
//		}
//		return false;
//	}
	public boolean addCitationLink2lastAddedNSP(String num) {
		if(!this.citationKeyExists(num))
			return false;
		int currentNSPIndex = this.naturalSubsProdRxn.size()-1;
		ArrayList<Citation> cits;
		// First check if for the "current" NSP, ie the last one added, there are citations
		if(this.nsp2citations.containsKey(this.naturalSubsProdRxn.size()-1))
			// if there are, we just add the new one
			cits = nsp2citations.get(currentNSPIndex);
		else
			// we need to create the ArrayList
			cits = new ArrayList<Citation>();
		cits.add(this.citationsObj.get(num));
		nsp2citations.put(currentNSPIndex, cits);
		this.naturalSubsProdRxn.get(this.naturalSubsProdRxn.size()-1).addCitations(cits);
		return true;
	}
	public ArrayList<Reaction> getNaturalSubsProdRxns() {
		return this.naturalSubsProdRxn;
	}
	public boolean addCitationLink2lastAddedTissue(String num) {
		if(!this.citationKeyExists(num))
			return false;
		int currentSourceTissueIndex = this.sourceTissues.size()-1;
		ArrayList<Citation> cits;
		// First check if for the "current" NSP, ie the last one added, there are citations
		if(this.tissue2citations.containsKey(this.sourceTissues.size()-1))
			// if there are, we just add the new one
			cits = tissue2citations.get(currentSourceTissueIndex);
		else
			// we need to create the ArrayList
			cits = new ArrayList<Citation>();
		cits.add(this.citationsObj.get(num));
		tissue2citations.put(currentSourceTissueIndex, cits);
		return true;
	}
	public boolean addCitationLink2lastAddedLoc(String num) {
		if(!this.citationKeyExists(num))
			return false;
		int currentLocalizationIndex = this.localization.size()-1;
		ArrayList<Citation> cits;
		// First check if for the "current" NSP, ie the last one added, there are citations
		if(this.localization2citations.containsKey(this.localization.size()-1))
			// if there are, we just add the new one
			cits = localization2citations.get(currentLocalizationIndex);
		else
			// we need to create the ArrayList
			cits = new ArrayList<Citation>();
		cits.add(this.citationsObj.get(num));
		localization2citations.put(currentLocalizationIndex, cits);
		return true;
	}
	public ArrayList<String> getProteinLinks() {
		ArrayList<String> ans = new ArrayList<String>();
		//if(this.extProtDBIdent.size() > 0)
		for(String key : this.extProtIdDB.keySet())
			ans.add(ecNumber.get(0)+"\t"+extProtIdDB.get(key)+"\t"+key);
		//else if(ecNumber.size()>0)
			//return ecNumber.get(0)+"\t";
		return ans;
	}
	/**
	 * @return the citationsObj
	 */
	public Set<Citation> getCitations() {
		Set<Citation> cits = new TreeSet<Citation>();
		cits.addAll(this.citationsObj.values());
		return cits;
	}

	public Set<String> getTissues() {
		Set<String> tissues = new TreeSet<String>();
		for(String t : sourceTissues) {
			tissues.add(t.toLowerCase());
		}
		return tissues;
	}

	public String getSystematicName() {
		return this.sysName;
	}
	public String getEcNumber() {
		// TODO Check this
		return ecNumber.firstElement();
	}
	public Set<String> getLocations() {
		Set<String> loc = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		for(String l : this.localization) {
			loc.add(l);
		}
		return loc;
	}
}
