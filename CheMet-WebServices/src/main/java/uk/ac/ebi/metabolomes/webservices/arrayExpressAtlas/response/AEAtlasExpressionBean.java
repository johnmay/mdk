/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.webservices.arrayExpressAtlas.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pmoreno
 */
public class AEAtlasExpressionBean implements Serializable{
    private Integer upExperiments;
    private Integer downExperiments;
    private Double upPValue;
    private Double downPValue;
    private List<AEAtlasSlimExperiment> experiments;

    public AEAtlasExpressionBean() {
        this.experiments = new ArrayList<AEAtlasSlimExperiment>();
    }

    /**
     * @return the upExperiments
     */
    public Integer getUpExperiments() {
        return upExperiments;
    }

    /**
     * @param upExperiments the upExperiments to set
     */
    public void setUpExperiments(Integer upExperiments) {
        this.upExperiments = upExperiments;
    }

    /**
     * @return the downExperiments
     */
    public Integer getDownExperiments() {
        return downExperiments;
    }

    /**
     * @param downExperiments the downExperiments to set
     */
    public void setDownExperiments(Integer downExperiments) {
        this.downExperiments = downExperiments;
    }

    /**
     * @return the upPValue
     */
    public Double getUpPValue() {
        return upPValue;
    }

    /**
     * @param upPValue the upPValue to set
     */
    public void setUpPValue(Double upPValue) {
        this.upPValue = upPValue;
    }

    /**
     * @return the downPValue
     */
    public Double getDownPValue() {
        return downPValue;
    }

    /**
     * @param downPValue the downPValue to set
     */
    public void setDownPValue(Double downPValue) {
        this.downPValue = downPValue;
    }

    /**
     * @return the experiments
     */
    public List<AEAtlasSlimExperiment> getExperiments() {
        return experiments;
    }

    /**
     * @param experiments the experiments to set
     */
    public void setExperiments(List<AEAtlasSlimExperiment> experiments) {
        this.experiments = experiments;
    }

    

}
