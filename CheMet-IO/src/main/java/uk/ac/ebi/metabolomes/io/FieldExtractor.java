/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.io;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author pmoreno
 */
public interface FieldExtractor {

    public void setSelectedIdentifiers(List<String> identifiers);

    public void shiftRecord();

    public void feedLine(String line);
    
    public boolean proposeIdentifier();
    
    public String getProposedIdentifier();

    public SDFRecord getRecordFor(String xid);
    
    public Iterator<SDFRecord> getSDFRecordsIterator();
    
}
