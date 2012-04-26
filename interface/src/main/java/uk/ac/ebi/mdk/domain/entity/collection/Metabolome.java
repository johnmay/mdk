package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.Collection;

/**
 * @version $Rev$
 */
public interface Metabolome extends Collection<Metabolite> {

    /**
     * Access metabolites by name
     * @param name
     * @return
     */
    public Collection<Metabolite> get(String name);

}
