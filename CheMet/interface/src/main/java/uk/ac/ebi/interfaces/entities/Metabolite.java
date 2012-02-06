/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.interfaces.entities;

import java.util.Collection;
import uk.ac.ebi.interfaces.MetaboliteClass;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.annotation.ChemicalStructure;


/**
 *
 * @author johnmay
 */
public interface Metabolite extends AnnotatedEntity {

    public Double getCharge();


    public void setCharge(Double charge);


    public void setGeneric(boolean readBoolean);


    public boolean isGeneric();


    public void setType(MetaboliteClass valueOf);


    public MetaboliteClass getType();


    /**
     * Method replaces hasStructureAssociated to be more direct
     * @return 
     */
    public boolean hasStructure();
    
    /**
     * Convenience method for accessing all associated chemical structures
     * @return 
     */
    public Collection<ChemicalStructure> getStructures();
    
}
