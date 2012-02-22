package uk.ac.ebi.io.service.loader.location;

import java.io.File;

/**
 * ResourceDirectoryLocation.java - 22.02.2012 <br/>
 * <p/>
 * Interface describes a directory that contains multiple files. Currently
 * this is only implemented by the {@see SystemDirectoryLocation} but
 * may be extended to include remote directories in future. Due to the nature
 * of remote directories the API for directory access is likely to change.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ResourceDirectoryLocation
        extends ResourceLocation {

    /**
     * List the file in the directory location, please
     * note this is likely to change if an FTP directory
     * is implemented as it may be better to return a list of
     * remote resources
     * @return
     */
    public File[] list();

}
