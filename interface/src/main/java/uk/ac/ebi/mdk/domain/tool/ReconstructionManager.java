package uk.ac.ebi.mdk.domain.tool;

import uk.ac.ebi.interfaces.entities.Reconstruction;

import java.util.Collection;

/**
 * @version $Rev$
 */
public interface ReconstructionManager {

    public Collection<Reconstruction> getProjects();

    public boolean hasProjects();

}
