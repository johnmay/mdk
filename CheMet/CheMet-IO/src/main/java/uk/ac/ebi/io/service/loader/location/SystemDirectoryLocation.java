package uk.ac.ebi.io.service.loader.location;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

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
public class SystemDirectoryLocation extends SystemLocation {

    private InputStream stream;

    public SystemDirectoryLocation(String key, File directory) {
        super(key, directory);
    }


    @Override
    public InputStream open() throws IOException {
        if (stream == null) {
            String[] children = getLocation().list();
            String separator = System.getProperty("line.separator");
            StringBuilder stringBuilder = new StringBuilder(children.length * (getLocation().getAbsolutePath().length() + 20));
            for (String child : children) {
                stringBuilder.append(getLocation().getAbsolutePath())
                        .append(File.separator)
                        .append(child)
                        .append(separator);
            }
            stream = new ByteArrayInputStream(stringBuilder.toString().getBytes(Charset.forName("UTF-8")));
        }
        return stream;
    }

    @Override
    public void close() throws IOException {
        if (stream != null) {
            stream.close();
        }
    }

}
