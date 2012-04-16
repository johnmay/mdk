/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.experimental;

import java.util.HashSet;

/**
 *
 * @author pmoreno
 */
public abstract class Evidence {

    protected String sourceService;
    protected Double score;
    protected Double pvalue;
    protected String expAcc;
    protected Boolean positiveEv;
    protected String tissue;
    protected String cellType;
    protected String diseaseState;
    protected HashSet<String> citation;

    public Evidence() {
        this.init();
    }

    private void init() {
        this.citation = new HashSet<String>();
    }

    public HashSet<String> getCitation() {
        return citation;
    }

    public void addCitation(String citationID) {
        this.citation.add(citationID);
    }
    

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    public String getDiseaseState() {
        return diseaseState;
    }

    public void setDiseaseState(String diseaseState) {
        this.diseaseState = diseaseState;
    }

    public String getExpAcc() {
        return expAcc;
    }

    public void setExpAcc(String expAcc) {
        this.expAcc = expAcc;
    }

    public boolean isPositiveEv() {
        if(positiveEv != null)
            return positiveEv;
        else
            return false;

    }

    public boolean isNegativeEv() {
        if(positiveEv != null )
            return !positiveEv;
        else
            return false;
    }

    public void setPositiveEv(boolean positiveEv) {
        this.positiveEv = positiveEv;
    }

    public Double getPvalue() {
        return pvalue;
    }

    public void setPvalue(Double pvalue) {
        this.pvalue = pvalue;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getSourceService() {
        return sourceService;
    }

    public void setSourceService(String sourceService) {
        this.sourceService = sourceService;
    }

    public String getTissue() {
        return tissue;
    }

    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    public boolean hasTissue() {
        return this.tissue != null;
    }

    protected abstract void determinePositiveEv();
}
