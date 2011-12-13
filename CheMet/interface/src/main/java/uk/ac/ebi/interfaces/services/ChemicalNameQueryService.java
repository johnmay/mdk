/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.interfaces.services;

import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 *
 * @author pmoreno
 */
public interface ChemicalNameQueryService<I extends Identifier> extends NameQueryService<I> {
    
    public String getIUPACName(I identifier);
    
}
