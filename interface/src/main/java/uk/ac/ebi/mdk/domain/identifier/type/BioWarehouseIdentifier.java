/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.identifier.type;

/**
 *
 * @author pmoreno
 */
public interface BioWarehouseIdentifier {
    
    public void setWID(Long wid);
    public void setDataSetWID(Long dataSetWID);
    public Long getWID();
    public Long getDataSetWID();
    
}
