/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.parser.hmdb;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pmoreno
 */
public class HMDBEntry {



    private String hmdb_id;
    private String name;
    private List<String> synonyms;
    private ArrayList<String> tissues;
    private String chebiID;
    private String pubChemID;
    private String biocycID;
    private String metlinID;
    private String cas;
    private String iupacName;


    public String getPubChemID() {
        return pubChemID;
    }

    public void setPubChemID(String pubChemID) {
        this.pubChemID = pubChemID;
    }

    public String getChebiID() {
        return chebiID;
    }

    public void setChebiID(String chebiID) {
        this.chebiID = chebiID;
    }

     public HMDBEntry(String name, String hmdb_id) {

		this.name = name;
		this.hmdb_id = hmdb_id;
                this.init();
	}

    public HMDBEntry() {
        this.init();
    }

    public void init() {
        this.tissues = new ArrayList<String>();
        this.synonyms = new ArrayList<String>();
    }
 /*   public HMDBEntry(String hmdb_id, String synonyms) {

		
		this.hmdb_id = hmdb_id;
		this.synonyms = synonyms;
	}
    public HMDBEntry(String name) {

		this.name = name;
               
	}
  *
  */
    public List<String> getSynonyms() {
        return synonyms;
    }

    public void addSynonyms(String synonyms) {
        this.synonyms.add(synonyms);
    }

    
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }
     public String getHmdb_id() {

        return hmdb_id;
    }

    public void setHmdb_id(String hmdb_id) {
        this.hmdb_id = hmdb_id;
    }
 
    public void addTissue(String tissue) {
        this.tissues.add(tissue);
    }

    public String[] getTissues() {
        return this.tissues.toArray(new String[this.tissues.size()]);
    }

    /**
     * @return the biocycID
     */
    public String getBiocycID() {
        return biocycID;
    }

    /**
     * @param biocycID the biocycID to set
     */
    public void setBiocycID(String biocycID) {
        this.biocycID = biocycID;
    }

    /**
     * @return the metlinID
     */
    public String getMetlinID() {
        return metlinID;
    }

    /**
     * @param metlinID the metlinID to set
     */
    public void setMetlinID(String metlinID) {
        this.metlinID = metlinID;
    }

    /**
     * @return the cas
     */
    public String getCas() {
        return cas;
    }

    /**
     * @param cas the cas to set
     */
    public void setCas(String cas) {
        this.cas = cas;
    }

    /**
     * @return the iupacName
     */
    public String getIupacName() {
        return iupacName;
    }

    /**
     * @param iupacName the iupacName to set
     */
    public void setIupacName(String iupacName) {
        this.iupacName = iupacName;
    }
  
}


 


