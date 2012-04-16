/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.experimental;

/**
 *
 * @author pmoreno
 */
public class ArrayExpEvidence extends Evidence{

    private final String serviceName = "ArrayExpressAtlas";
    private String factor;
    private String factorValue;
    private Integer updn;
    private String expDescription;

    public ArrayExpEvidence() {
        super();
        this.setSourceService(sourceService);
    }

    public String getExpDescription() {
        return expDescription;
    }

    public void setExpDescription(String expDescription) {
        this.expDescription = expDescription;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getFactorValue() {
        return factorValue;
    }

    public void setFactorValue(String factorValue) {
        this.factorValue = factorValue;
    }

    public Integer getUpdn() {
        return updn;
    }

    public void setUpdn(Integer updn) {
        this.updn = updn;
        this.determinePositiveEv();
    }

    protected void determinePositiveEv() {
        if(this.updn != null && this.updn > 0 )
            this.setPositiveEv(true);
        else if(this.updn != null && this.updn < 0)
            this.setPositiveEv(false);
    }

}
