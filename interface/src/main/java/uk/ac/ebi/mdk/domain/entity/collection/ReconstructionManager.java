package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.util.Collection;

/**
 * @version $Rev$
 */
public interface ReconstructionManager {

    public Collection<Reconstruction> getProjects();

    public boolean hasProjects();

    public Reconstruction getActive();

}
