/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.util.inchi;

/**
 *
 * @author pmoreno
 */
public class InChIResult {
    private String inchi;
    private String inchiKey;
    private String auxInfo;

    public InChIResult() {
    }

    public InChIResult( String inchi , String inchiKey , String auxInfo ) {
        this.inchi = inchi;
        this.inchiKey = inchiKey;
        this.auxInfo = auxInfo;
    }  

    public String getAuxInfo() {
        return auxInfo;
    }

    protected void setAuxInfo(String auxInfo) {
        this.auxInfo = auxInfo;
    }

    public String getInchi() {
        return inchi;
    }

    protected void setInchi(String inchi) {
        this.inchi = inchi;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    protected void setInchiKey(String inchiKey) {
        this.inchiKey = inchiKey;
    }


}
