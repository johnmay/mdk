package uk.ac.ebi.io.service.location;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA. User: johnmay Date: 20/02/2012 Time: 10:09 To change this template use File | Settings |
 * File Templates.
 */
public interface ResourceLocation {

    public boolean isAvailable();

    public InputStream open() throws IOException;

    public void close() throws IOException;

    public String getKey();

}
