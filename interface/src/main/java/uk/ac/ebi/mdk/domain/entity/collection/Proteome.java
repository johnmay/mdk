package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.interfaces.entities.GeneProduct;

import java.util.Collection;
import java.util.List;

/**
 * @version $Rev$
 */
public interface Proteome extends Collection<GeneProduct> {


    /**
     * Access via accession
     *
     * @param accession
     *
     * @return
     */
    public List<GeneProduct> get(String accession);

}
