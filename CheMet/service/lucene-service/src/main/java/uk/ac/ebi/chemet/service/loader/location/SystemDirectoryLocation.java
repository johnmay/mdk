package uk.ac.ebi.chemet.service.loader.location;

import uk.ac.ebi.service.location.ResourceDirectoryLocation;

import java.io.*;

/**
 * SystemDirectoryLocation.java - 20.02.2012 <br/>
 * <p/>
 * A SystemDirectoryLocation is a resource located on the system that is a directory. The InputStream for this will
 * provide the full path's to the file's separated by line separator.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class SystemDirectoryLocation
        implements ResourceDirectoryLocation {

    private File directory;

    public SystemDirectoryLocation(File directory) {
        this.directory = directory;
    }

    /**
     * Indicates whether the directory exists and whether it is in-fact a directory
     * @inheritDoc
     */
    @Override
    public boolean isAvailable() {
        return directory.exists() && directory.isDirectory();
    }

    /**
     * Simple call to the {@see File#listFiles()} method
     *
     * @inheritDoc
     */
    @Override
    public File[] list() {
        return directory.listFiles();
    }

    /**
     * @inheritDoc
     */
    public String toString(){
        return directory.toString();
    }

}
