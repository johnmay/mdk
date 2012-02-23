package uk.ac.ebi.io.service.loader;

import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.location.LocationDescription;
import uk.ac.ebi.io.service.loader.location.ResourceLocation;

import java.io.IOException;
import java.util.Map;

/**
 * ResourceLoader - 20.02.2012 <br/>
 * <p/>
 * Interface describes a resource that can load data from a
 * ResourceLocation into a local file via the {@see update()}
 * method.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ResourceLoader {


    /**
     * Determines whether required locations {@see LocationDescription} are
     * accounted for via various {@see ResourceLocation}s. Each location
     * is checked with {@see ResourceLocation#isAvailable()} but this may
     * not guarantee a error free {@see update()}
     *
     * @return can update resource
     */
    public boolean canUpdate();

    /**
     * Determine whether the loader has files available to back-up (i.e.
     * {@see update()} has run successfully at least once.
     *
     * @return can backup loader output
     */
    public boolean canBackup();

    /**
     * Determine whether the loader has files available to revert (i.e.
     * {@see backup()} has been called).
     *
     * @return can revert loader output
     */
    public boolean canRevert();

    /**
     * Update the resource output by parsing the {@see ResourceLocation}s. The
     * locations are accessed via the {@see getLocation(String)} method. If an
     * attempt is made to retrieve a location that does not existing then a
     * {@see MissingLocationException} is thrown. The {@see canUpdate()} method
     * can be called to ensure all required locations are available and the
     * loader should be able to 'update'. This method is allowed to throw an
     * {@see IOException} but should be reserved for major failures. There may
     * well be cases where an IOException will be thrown on a single record of
     * thousands. This should be logged and not thrown (note: logging access
     * has not currently been implemented).
     *
     * @throws MissingLocationException if a required location is missing
     * @throws IOException              could not load resources
     */
    public void update() throws MissingLocationException, IOException;

    /**
     * Create a backup of this loaders output, this method should be called
     * before any calls to {@see update()} to ensure that {@see revert()}
     * can be called should a problem occur. The call to this method before
     * {@see update()} is not enforced but automatically calling it may be
     * beneficial in a user-centric update manager.
     */
    public void backup();

    /**
     * Revert changes made by the last call to {@see update()}. This method
     * will replace the current loader output with the backup (if available)
     */
    public void revert();

    /**
     * Remove all files and directories produced by this loader including
     * the backups. This method will render dependant query services useless,
     * therefore if this method is used in a user-interface the application
     * should be prompted whether they really want to 'clean' the loader.
     */
    public void clean();


    /**
     * Access a description for this loader. In single index loaders
     * this method can just return the name of the {@see LuceneIndex}.
     *
     * @return name of the loader
     */
    public String getName();


    /**
     * Access the {@see LocationDescription}s of the {@see ResourceLocation}s
     * that are required for the {@see update()} method. The key's in map
     * should be used when adding {@see ResourceLocation}s in the method
     * {@see addLocation(String, ResourceLocation)}; This method is primarily
     * useful for user-interface update managers where the user can change
     * the location's of the resources
     *
     * @return map of location key's to their descriptions
     */
    public Map<String, LocationDescription> getRequiredResources();


    /**
     * Add location for a required resource. The correct key can be determined
     * from the {@see getRequiredResources()} method.
     *
     * @param key      key that will be used in {@see getLocation(String)}
     * @param location the location to add
     */
    public void addLocation(String key, ResourceLocation location);


    /**
     * Access the stored location for the specified key. If the location
     * does not exist for the specified key the an exception is thrown.
     * Note: this is a generic method allow multiple return types providing
     * they extend {@see ResourceLocation}. What this means is that if a
     * {@see LocationDescription} requires a {@see ResourceFileLocation}
     * you can call this method assigning to a variable of that type.
     * {@code ResourceFileLocation location = getLocation("chebi.sdf")}
     * <p/>
     * This is not type save so if the returned type does not extend the
     * generic-type a ClassCastException will be thrown
     *
     * @param key key to fetch a location
     *
     * @return instance extending ResourceLocation
     *
     * @throws MissingLocationException thrown if the location has not been added
     */
    public <T extends ResourceLocation> T getLocation(String key) throws MissingLocationException;

}
