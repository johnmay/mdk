package uk.ac.ebi.io.service.loader;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.core.CorePreferences;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.location.LocationFactory;
import uk.ac.ebi.io.service.loader.location.ResourceLocation;
import uk.ac.ebi.io.service.index.LuceneIndex;

import java.io.File;
import java.security.InvalidParameterException;
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

    private Map<String, LuceneIndex> indexMap = new HashMap<String, LuceneIndex>();

    public AbstractResourceLoader(LuceneIndex index) {
        indexMap.put(index.getName(), index);
    }

    public AbstractResourceLoader() {

    }

    public void addIndex(String key, LuceneIndex index) {
        indexMap.put(key, index);
    }

    public LuceneIndex getIndex() {
        if (indexMap.size() > 1) {
            throw new InvalidParameterException("Resource loader has more then one index!");
        }
        return indexMap.values().iterator().next();
    }

    @Override
    public Collection<LuceneIndex> getIndexes() {
        return indexMap.values();
    }

    @Override
    public LuceneIndex getIndex(String key) {
        return indexMap.get(key);
    }

    public void addLocation(String key, ResourceLocation location) {
        locationMap.put(key, location);
    }

    public <T extends ResourceLocation> void addReqiredResource(String name,
                                                                String description,
                                                                Class<T> c,
                                                                T defaultLocation) {
        addRequiredResource(new LocationDescription(name,
                                                    description,
                                                    c,
                                                    defaultLocation));
    }

    public <T extends ResourceLocation> void addResource(String name,
                                                         String description,
                                                         Class<T> c) {
        addRequiredResource(new LocationDescription(name,
                                                    description,
                                                    c
        ));
    }

    /**
     * Access the name of the first index, note that multiple indexes
     * are not stored in order so this method should be over-ridden
     * if you intend to load multiple indexes.
     *
     * @return
     */
    public String getName() {
        if (indexMap.size() > 1) {
            return indexMap.values().iterator().next().getName();
        }
        return "Unnamed loader";
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
    public void addRequiredResource(LocationDescription resource) {

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


    public Map<String, LocationDescription> getRequiredResources() {
        return requiredResources;
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


    /**
     * @inheritDoc
     */
    @Override
    public void backup() {

        for (LuceneIndex index : getIndexes()) {
            if (index.isAvailable()) {
                index.backup();
            }
        }

    }


    @Override
    public void revert() {
        for (LuceneIndex index : getIndexes()) {
            if (index.canRevert()) {
                index.revert();
            }
        }
    }

    @Override
    public void clean() {
        for (LuceneIndex index : getIndexes()) {
            index.clean();
        }
    }

    @Override
    public boolean canBackup() {
        boolean backup = false;
        for (LuceneIndex index : getIndexes()) {
            backup = index.isAvailable() || backup;
        }
        return backup;
    }

    @Override
    public boolean canRevert() {
        boolean revert = false;
        for (LuceneIndex index : getIndexes()) {
            revert = index.canRevert() || revert;
        }
        return revert;
    }
}
