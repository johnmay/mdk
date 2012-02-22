package uk.ac.ebi.io.service.loader;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.core.CorePreferences;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.location.LocationFactory;
import uk.ac.ebi.io.service.loader.location.ResourceLocation;
import uk.ac.ebi.io.service.index.LuceneIndex;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * ${Name}.java - 20.02.2012 <br/>
 * <p/>
 * Abstract loaded defines the handling of the resource locations and availability methods
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractResourceLoader implements ResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(AbstractResourceLoader.class);

    private LocationFactory factory = LocationFactory.getInstance();

    private Map<String, ResourceLocation> locationMap = new HashMap<String, ResourceLocation>(6);

    private Map<String, LocationDescription> requiredResources = new HashMap<String, LocationDescription>();

    private LuceneIndex index;

    public AbstractResourceLoader(LuceneIndex index) {
        this.index = index;
    }

    public LuceneIndex getIndex() {
        return index;
    }

    public void addLocation(String key, ResourceLocation location) {
        locationMap.put(key, location);
    }

    /**
     * Access a location for the specified key
     *
     * @param key key for the required location
     *
     * @return location for the key
     *
     * @throws MissingLocationException if now location is found
     */
    public <T extends ResourceLocation> T getLocation(String key) throws MissingLocationException {
        if (locationMap.containsKey(key)) {
            return (T) locationMap.get(key);
        }
        key = LocationDescription.createKey(key);
        if (locationMap.containsKey(key)) {
            return (T) locationMap.get(key);
        }
        throw new MissingLocationException("Could not find location for key " + key);
    }

    /**
     * Add a required resource location to the loader.
     *
     * @param resource
     */
    public void addResource(LocationDescription resource) {

        requiredResources.put(resource.getKey(), resource);

        // if the resource has a default location, add it to the location map
        if (resource.hasDefaultLocation()) {
            locationMap.put(resource.getKey(), resource.getDefaultLocation());
        }

    }

    /**
     * Access the location of the named resource under the RESOURCE_ROOT preferences. i.e. if you provide
     * "/structure/sdf" and the resource root is /resources/chemet the file will be /resources/chemet/structure/sdf
     *
     * @param name
     *
     * @return
     */
    public static File getResourceRootChild(String name) {
        FilePreference filePreference = CorePreferences.getInstance().getPreference("RESOURCE_ROOT");
        return new File(filePreference.get(), name);
    }
    
    
    public Map<String,LocationDescription> getRequiredResources(){
        return requiredResources;
    }

    /**
     * Deletes a directory and all it's children
     *
     * @param dir
     *
     * @return
     */
    public static boolean delete(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = delete(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    /**
     * Determines whether the resource is available for update. If a required key is missing or the location is not
     * available this method will return false
     *
     * @return
     */
    public boolean isAvailable() {
        for (String key : getRequiredResources().keySet()) {
            if (!locationMap.containsKey(key)
                    || !locationMap.get(key).isAvailable()) {
                return false;
            }
        }
        return true;
    }


}
