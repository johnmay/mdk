package uk.ac.ebi.io.service.location;

import com.google.common.base.Joiner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * ${Name}.java - 20.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class SystemDirectoryLocation extends SystemLocation {

    private String key;
    private File directory;

    private InputStream stream;

    public SystemDirectoryLocation(String key, File directory) {
        super(key, directory);
        this.key = key;
        this.directory = directory;
    }

    @Override
    public boolean isAvailable() {
        return directory.exists();
    }

    @Override
    public InputStream open() throws IOException {
        if (stream == null) {
            String[] children = directory.list();
            String separator = System.getProperty("line.separator");
            StringBuilder stringBuilder = new StringBuilder(children.length * (directory.getAbsolutePath().length() + 20));
            for (String child : children) {
                stringBuilder.append(directory.getAbsolutePath())
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

    @Override
    public String getKey() {
        return key;
    }
}
