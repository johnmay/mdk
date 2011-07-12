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
public class AEAtlasResultBean implements Serializable {

    private AEAtlasGeneBean gene;
    private List<AEAtlasExpressionBean> expressions;

    public AEAtlasResultBean() {
        this.expressions = new ArrayList<AEAtlasExpressionBean>();
    }

    /**
     * @return the gene
     */
    public AEAtlasGeneBean getGene() {
        return gene;
    }

    /**
     * @param gene the gene to set
     */
    public void setGene(AEAtlasGeneBean gene) {
        this.gene = gene;
    }

    /**
     * @return the expressions
     */
    public List<AEAtlasExpressionBean> getExpressions() {
        return expressions;
    }

    /**
     * @param expressions the expressions to set
     */
    public void setExpressions(List<AEAtlasExpressionBean> expressions) {
        this.expressions = expressions;
    }

    

}
