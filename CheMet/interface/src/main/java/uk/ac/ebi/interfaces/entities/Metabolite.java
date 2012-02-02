/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.interfaces.entities;

import uk.ac.ebi.interfaces.MetaboliteClass;
import uk.ac.ebi.interfaces.AnnotatedEntity;


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


    public boolean hasStructureAssociated();
}
