package uk.ac.ebi.mdk.service.loader.location;

import uk.ac.ebi.mdk.service.location.LocationFactory;
import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.prefs.Preferences;

/**
 * Created by IntelliJ IDEA. User: johnmay Date: 20/02/2012 Time: 09:53 To change this template use File | Settings |
 * File Templates.
 */
public class DefaultLocationFactory implements LocationFactory {


    //    private final Pattern REMOTE_LOCATION = Pattern.compile("\\A(http(?:s)?|ftp|file)://");
    //    private final Pattern GZIP            = Pattern.compile("\\.gz\\z");

    private final Preferences preferences;

    private DefaultLocationFactory() {
        preferences = Preferences.userNodeForPackage(getClass());
    }

    /**
     * Obtain a use-able instance of the location factory
     *
     * @return singleton-instance
     */
    public static LocationFactory getInstance() {
        return LocationFactoryHolder.INSTANCE;
    }

    private static class LocationFactoryHolder {
        private static DefaultLocationFactory INSTANCE = new DefaultLocationFactory();
    }

    /**
     * @inheritDoc
     */
    @Override
    public ResourceFileLocation newFileLocation(String path, Compression compression, Location location) throws IOException {

        if (location == Location.HTTP || location == Location.FTP) {
            return newRemoteFileLocation(path, compression);
        } else if (location == Location.LOCAL_FS) {
            return newSystemFileLocation(path, compression);
        }

        throw new InvalidParameterException("Location not yet supported");

    }

    /**
     * Create a RemoteLocation with the provided compression and the given path
     *
     * @param path        path to the location
     * @param compression compression type (i.e. GZIP/ZIP or NONE)
     *
     * @return instance of a RemoteFileLocation appropriate to the compression level
     *
     * @throws IOException
     */
    public RemoteLocation newRemoteFileLocation(String path, Compression compression) throws IOException {
        switch (compression) {
            case GZIP_ARCHIVE:
                return new GZIPRemoteLocation(path);
            case ZIP_ARCHIVE:
                return new ZIPRemoteLocation(path);
            default:
                return new RemoteLocation(path);
        }
    }

    /**
     * Create a SystemLocation with the provided compression and the given path
     *
     * @param path        path to the location
     * @param compression compression type (i.e. GZIP/ZIP or NONE)
     *
     * @return instance of a SystemFileLocation appropriate to the compression level
     *
     * @throws IOException
     */
    public SystemLocation newSystemFileLocation(String path, Compression compression) throws IOException {
        switch (compression) {
            case GZIP_ARCHIVE:
                return new GZIPSystemLocation(path);
            case ZIP_ARCHIVE:
                return new ZIPSystemLocation(path);
            default:
                return new SystemLocation(path);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public ResourceDirectoryLocation newDirectoryLocation(String desc, Compression compression, Location location) throws IOException {

        if (desc.isEmpty())
            return null;

        if (desc.endsWith(".zip") && desc.startsWith("http://"))
            return new ZIPRemoteLocation(desc);

        // only one type at the moment
        return new SystemDirectoryLocation(new File(desc));

    }
}
