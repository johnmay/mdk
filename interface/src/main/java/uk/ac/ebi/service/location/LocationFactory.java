package uk.ac.ebi.service.location;

import java.io.IOException;

/**
 * LocationFactory - 29.02.2012 <br/>
 * <p/>
 * Provides creation of ResourceLocation's
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface LocationFactory {

    public enum Compression {
        ZIP_ARCHIVE,
        GZIP_ARCHIVE,
        NONE
    };

    public enum Location {
        HTTP,
        FTP,
        LOCAL_FS
    };

    /**
     * Create a new ResourceFileLocation from the provided path, compression type and the location (i.e. HTTP,
     * FTP or Local_FS)
     * @param path the path to the resource
     * @param compression type of compression used
     * @param location location on the file system
     * @return instance of a ResourceFileLocation that is open-able given the parameters
     * @throws IOException
     */
    public ResourceFileLocation newFileLocation(String path, Compression compression, Location location) throws IOException;

    public ResourceDirectoryLocation newDirectoryLocation(String path, Compression compression, Location location) throws IOException;

}
