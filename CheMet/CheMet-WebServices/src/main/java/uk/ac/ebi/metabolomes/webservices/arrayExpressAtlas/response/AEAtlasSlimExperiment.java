/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.response;

/**
 *
 * @author pmoreno
 */
class AEAtlasSlimExperiment {
    private Double pvalue;
    private String accession;
    private Boolean upExpression;

    /**
     * @return the pvalue
     */
    public Double getPvalue() {
        return pvalue;
    }

    /**
     * @param pvalue the pvalue to set
     */
    public void setPvalue(Double pvalue) {
        this.pvalue = pvalue;
    }

    /**
     * @return the accession
     */
    public String getAccession() {
        return accession;
    }

    /**
     * @param accession the accession to set
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     * @param upExpression the upExpression to set
     */
    public void setUpExpression(Boolean upExpression) {
        this.upExpression = upExpression;
    }

    public boolean isUpExpression() {
        return this.upExpression;
    }
}
