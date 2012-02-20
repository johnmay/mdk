package uk.ac.ebi.io.service.loader;

import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.location.ResourceLocation;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * ${Name}.java - 20.02.2012 <br/>
 * Interface describes a resource that can load data from a
 * ResourceLocation into a local file
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ResourceLoader {

    /**
     * Sets resource input locations for this loader
     * @param locations
     */
    public void setLocations(Collection<ResourceLocation> locations);
    
    /**
     * Load the resource
     * @throws MissingLocationException
     */
    public void load() throws MissingLocationException, IOException;

    /**
     * Remove all locally formatted copies of the resource
     */
    public void clean();

    /**
     * Determines whether the resource is available for update.
     * @return
     */
    public boolean isAvailable();

    /**
     * Access a list of the required keys for this loader
     * @return
     */
    public List<String> getRequiredKeys();

}
