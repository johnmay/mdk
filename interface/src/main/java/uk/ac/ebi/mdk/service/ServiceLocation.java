package uk.ac.ebi.mdk.service;

import java.io.File;

/**
 * ServiceLocation - 05.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ServiceLocation {

    public String getName();

    /**
     * The location of the service
     * @return
     */
    public File getLocation();


    /**
     * When the service was last updated (if available)
     * @return
     */
    public long lastModified();

    /**
     * Whether the service is available
     * @return
     */
    public boolean isAvailable();

    /**
     * Access the backup location for this service
     * @return backup file
     */
    public File getBackup();

    /**
     * Indicate whether is possible to
     * revert the service to a previous
     * state
     */
    public boolean canRevert();

    /**
     * Creates a backup of this service
     * @return
     */
    public boolean backup();

    /**
     * Revert the service to a previous state
     * @return
     */
    public boolean revert();

    /**
     * Remove all traces of this service location
     */
    public void clean();

}
