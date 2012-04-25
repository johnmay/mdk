package uk.ac.ebi.mdk.domain.tool;

import uk.ac.ebi.interfaces.reaction.Compartment;

import java.util.Collection;

/**
 * Compartment resolver returns a compartment object (normally an enumeration).
 *
 * @author johnmay
 */
public interface CompartmentResolver {

    /**
     * Whether the compartment is ambiguous
     * @param compartment string notation
     * @return whether the notation is ambiguous
     */
    public boolean isAmbiguous(String compartment);

    /**
     * Get an appropriate compartment instance for the provided name
     *
     * @param compartment name or abbreviation of a compartment (i.e. 'c', 'e', 'cytoplasm')
     *
     * @return compartment instance that can be used in a metabolic reaction
     */
    public Compartment getCompartment(String compartment);

    /**
     * Access all the compartments currently stored in the resolver
     *
     * @return
     */
    public Collection<Compartment> getCompartments();

}
