/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.experimental;

/**
 *
 * @author pmoreno
 */
public class PrideEvidence extends Evidence{

    private final String serviceName = "PRIDE";

    private Double prot_threshold;

    public PrideEvidence() {
        super();
        this.setSourceService(this.serviceName);
    }

    public void setProtThreshold(double threshold) {
        this.prot_threshold = threshold;
    }

    public void triggerDeterminePositiveEv() {
        this.determinePositiveEv();
    }

    protected void determinePositiveEv() {
        if(this.prot_threshold != null && this.score != null)
            this.setPositiveEv(this.score >= this.prot_threshold);
        else
            this.setPositiveEv(true);
    }
}
