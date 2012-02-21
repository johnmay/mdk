package uk.ac.ebi.io.service.loader;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.core.CorePreferences;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.location.LocationFactory;
import uk.ac.ebi.io.service.loader.location.ResourceLocation;
import uk.ac.ebi.io.service.loader.location.ResourceLocationKey;
import uk.ac.ebi.io.service.index.LuceneIndex;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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

    private Map<String, ResourceLocationKey> requiredKeys;

    private LuceneIndex index;

    public AbstractResourceLoader(LuceneIndex index) {
        this.index = index;
        
        for(String key : getRequiredKeys().keySet()){
            if(!locationMap.containsKey(key)){
                // force loading of user defined keys
                try{
                    addLocation(LocationFactory.getInstance().newDefaultLocation(key, ""));
                } catch (IOException ex){
                    // ignore
                }
            }
        }
        
    }

    public LuceneIndex getIndex() {
        return index;
    }

    /**
     * Access a location for the specified key
     *
     * @param key key for the required location
     * @return location for the key
     * @throws MissingLocationException if now location is found
     */
    public ResourceLocation getLocation(String key) throws MissingLocationException {
        if (locationMap.containsKey(key)) {
            return locationMap.get(key);
        }
        throw new MissingLocationException("Could not find location for key " + key);
    }

    /**
     * Adds the location to the loader
     *
     * @param location
     */
    public void addLocation(ResourceLocation location) {
        locationMap.put(location.getKey(), location);
    }

    /**
     * Adds a new default location to the loader
     *
     * @param location
     */
    public void addLocation(String key, String location) throws IOException {
        addLocation(factory.newDefaultLocation(key, location));
    }

    /**
     * Sets the locations for the loader to access
     *
     * @param locations
     */
    @Override
    public void setLocations(Collection<ResourceLocation> locations) {
        locationMap.clear();
        for (ResourceLocation location : locations) {
            locationMap.put(location.getKey(), location);
        }
    }

    /**
     * Access the location of the named resource under the RESOURCE_ROOT preferences. i.e. if you provide
     * "/structure/sdf" and the resource root is /resources/chemet the file will be /resources/chemet/structure/sdf
     *
     * @param name
     * @return
     */
    public static File getResourceRootChild(String name) {
        FilePreference filePreference = CorePreferences.getInstance().getPreference("RESOURCE_ROOT");
        return new File(filePreference.get(), name);
    }

    /**
     * Deletes a directory and all it's children
     *
     * @param dir
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
        for (String key : getRequiredKeys().keySet()) {
            if (!locationMap.containsKey(key)
                    || !locationMap.get(key).isAvailable()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Access a list of required keys for this loader. The keys are accessed via the {@see ResourceLocationKey} language
     * annotation.
     *
     * @return list of required keys
     */
    public Map<String,ResourceLocationKey> getRequiredKeys() {

        if (requiredKeys != null) {
            return requiredKeys;
        }

        requiredKeys = new HashMap<String,ResourceLocationKey>();

        for (Field field : getClass().getDeclaredFields()) {
            ResourceLocationKey annotation = field.getAnnotation(ResourceLocationKey.class);
            if (annotation != null) {
                try {
                    requiredKeys.put((String) field.get(new String()), annotation);
                } catch (IllegalAccessException exception) {
                    LOGGER.error("Could not access filed " + field.getName() + " in " + getClass() + " please ensure it is public");
                }
            }
        }

        return requiredKeys;
    }

}
