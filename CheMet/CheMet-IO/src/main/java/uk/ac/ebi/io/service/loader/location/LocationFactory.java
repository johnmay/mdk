package uk.ac.ebi.io.service.loader.location;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA. User: johnmay Date: 20/02/2012 Time: 09:53 To change this template use File | Settings |
 * File Templates.
 */
public class LocationFactory {


    private final Pattern REMOTE_LOCATION = Pattern.compile("\\A(http(?:s)?|ftp|file)://");
    private final Pattern GZIP            = Pattern.compile("\\.gz\\z");

    private final Preferences preferences;

    private LocationFactory() {
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
        private static LocationFactory INSTANCE = new LocationFactory();
    }

    /**
     * Creates a new ResourceLocation that is not default for the specified key.
     * @param key key to identify the location by
     * @param location the location
     * @return instance of an appropriate resource location
     * @throws IOException if there was a problem parsing a URL
     */
    public ResourceLocation newLocation(String key, String location) throws IOException {
        return newLocation(key, location, false);
    }

    /**
     * Creates a new ResourceLocation that is the default for the specified key.
     * @param key key to identify the location by
     * @param location the location
     * @return instance of an appropriate resource location
     * @throws IOException if there was a problem parsing a URL
     */
    public ResourceLocation newDefaultLocation(String key, String location) throws IOException {
        return newLocation(key, location, true);
    }

    /**
     * Creates a ResourceLocation specifying whether the provided location is the default for this key. If the location
     * is the default location the user preferences are checked if the user has a preferred path.
     * @param key key to identify the location by
     * @param location the location
     * @param defaultLocation whether this is the default location
     * @return instance of an appropriate resource location
     * @throws IOException if there was a problem parsing a URL
     */
    public ResourceLocation newLocation(String key, String location, boolean defaultLocation) throws IOException {

        Matcher protocolMatcher = REMOTE_LOCATION.matcher(location);

        // if this is the default location, check preferences to see if the
        // user has specified a custom one. Otherwise store the new location in
        // the preferences node for this package
        if (defaultLocation) {
            location = preferences.get(key, location);
        } else {
            preferences.put(key, location);
        }

        Matcher gzipMatcher = GZIP.matcher(location);
        boolean gzip = gzipMatcher.find();

        if (protocolMatcher.find()) {
            String protocol = protocolMatcher.group(1);
            if (protocol.equals("http")) {
                return gzip ? new GZIPRemoteLocation(new URL(location)): new RemoteLocation(new URL(location));
            } else if (protocol.equals("ftp")) {
                return gzip ? new GZIPRemoteLocation(new URL(location)): new RemoteLocation(new URL(location));
            } else if (protocol.equals("https")) {
                return gzip ? new GZIPRemoteLocation(new URL(location)): new RemoteLocation(new URL(location));
            }
        }

        File file = new File(location);
        if(file.exists() && file.isDirectory()){
            return new SystemDirectoryLocation(file);
        }
        
        // default to system location
        return gzip ? new GZIPSystemLocation(file) : new SystemLocation(file);

    }

}
