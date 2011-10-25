package uk.ac.ebi.io.remote;

/**
 * RemoteResource.java
 *
 * 2011.10.15
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * @name    RemoteResource - 2011.10.15 <br>
 *          Interface describing a remote resource that is cached locally.
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface RemoteResource {

    /**
     * Returns the local size of the file. Note the remote size does not
     * have to correspond with the remote size as some data may be filtered
     * @return The size in bytes
     */
    public long getLocalSize();

    /**
     * Returns the remote size of the file
     * @return
     */
    public long getRemoteSize();

    /**
     * The date when the file was last updated
     * @return
     */
    public Date getLastUpdated();

    /**
     * Perform update by downloading the remote file
     */
    public void update() throws IOException;

    /**
     * Returns the location of where the remote data is stored
     * @return
     */
    public File getLocal();

    public URL getRemote();
}
