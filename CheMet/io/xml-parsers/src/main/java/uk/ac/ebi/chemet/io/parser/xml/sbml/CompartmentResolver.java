package uk.ac.ebi.chemet.io.parser.xml.sbml;

import uk.ac.ebi.interfaces.reaction.Compartment;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface CompartmentResolver {
    
    public Compartment getCompartment(String compartment);
    
}
