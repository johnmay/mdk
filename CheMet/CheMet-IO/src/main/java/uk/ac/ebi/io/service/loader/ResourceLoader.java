package uk.ac.ebi.io.service.loader;

import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.location.ResourceLocation;
import uk.ac.ebi.io.service.index.LuceneIndex;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * ResourceLoader - 20.02.2012 <br/>
 * <p/>
 * Interface describes a resource that can load data from a
 * ResourceLocation into a local file
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ResourceLoader {

    /**
     * add resource input locations for this loader
     *
     * @param location
     */
    public void addLocation(String key, ResourceLocation location);

    /**
     * Load the resource
     *
     * @throws MissingLocationException
     */
    public void load() throws MissingLocationException, IOException;

    /**
     * Move what this loader will load into a backup directory
     */
    public void backup();

    /**
     * Revert changes made by the loader
     */
    public void revert();

    /**
     * clean all indexes
     */
    public void clean();

    public boolean canBackup();

    public boolean canRevert();


    /**
     * Determines whether the resource is available for update.
     *
     * @return
     */
    public boolean isAvailable();

    /**
     * Access a list of the required keys for this loader including
     * their short description
     *
     * @return
     */
    public Map<String, LocationDescription> getRequiredResources();


    /**
     * Access the lucene index for this loader. this can only be used if there is only one index other wise it should
     * throw an invalid parameter exception
     *
     * @return
     */
    public LuceneIndex getIndex();


    public Collection<LuceneIndex> getIndexes();


    public LuceneIndex getIndex(String key);


    /**
     * Access the name of this loader
     *
     * @return
     */
    public String getName();


    /**
     * Access the stored location for the specified key
     *
     * @param key
     *
     * @return
     *
     * @throws MissingLocationException
     */
    public <T extends ResourceLocation> T getLocation(String key) throws MissingLocationException;

}
