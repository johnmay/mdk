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
public class AEAtlasGeneBean implements Serializable {
    private String id;
    private String name;
    private List<String> uniprotIDs;


    public AEAtlasGeneBean() {
        this.uniprotIDs = new ArrayList<String>();
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the uniprotIDs
     */
    public List<String> getUniprotIDs() {
        return uniprotIDs;
    }

    /**
     * @param uniprotIDs the uniprotIDs to set
     */
    public void setUniprotIDs(List<String> uniprotIDs) {
        this.uniprotIDs = uniprotIDs;
    }

}
