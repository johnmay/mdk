/**
 * RemoteResourceManager.java
 *
 * 2011.10.27
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
package uk.ac.ebi.io.remote;

import uk.ac.ebi.interfaces.services.RemoteResource;
import com.google.common.base.Joiner;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *          RemoteResourceManager - 2011.10.27 <br>
 *          Singleton description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class RemoteResourceManager {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RemoteResourceManager.class);
    private List<RemoteResource> resources = new ArrayList<RemoteResource>(Arrays.asList(new DrugBankCrossRefs(),
                                                                                         new ChEBINames(),
                                                                                         new ChEBICrossRefs(),
                                                                                         new ChEBIChemicalData(),
                                                                                         new KEGGCompoundNames(),
                                                                                         new KEGGCompoundMols()));

    private RemoteResourceManager() {
    }

    public static RemoteResourceManager getInstance() {
        return RemoteResourceManagerHolder.INSTANCE;
    }

    private static class RemoteResourceManagerHolder {

        private static final RemoteResourceManager INSTANCE = new RemoteResourceManager();
    }

    /**
     * Returns a list of resources not present on the local file system
     */
    public Collection<RemoteResource> getAbsentResources() {
        List<RemoteResource> absent = new ArrayList();
        for (RemoteResource r : resources) {
            if (r.getLocal().exists() == false) {
                absent.add(r);
            }
        }
        return absent;
    }

    /**
     * Returns a collection of all resources currently managed
     * @return
     */
    public Collection<RemoteResource> getResources() {
        return resources;
    }

    /**
     * Returns all resources older then a certain age (days). This method is a
     * convenience method delegating to {@see getResouces(long, boolean)}
     *
     * @param days
     * @param includeAbsent if set to true resources which are not present will
     *                      be included in the returned collection
     * @return
     */
    public Collection<RemoteResource> getResouces(int days, boolean includeAbsent) {
        long age = days * 86000000;
        return getResouces(age, includeAbsent);
    }

    /**
     * Access all resources older then a certain age (milliseconds)
     *
     * @param age           resource modify more then the age will be returned
     * @param includeAbsent if set to true resources which are not present will
     *                      be included in the returned collection
     */
    public Collection<RemoteResource> getResouces(long age, boolean includeAbsent) {

        List<RemoteResource> old = new ArrayList();
        long now = System.currentTimeMillis();

        for (RemoteResource r : resources) {

            if ((now - r.getLastUpdated().getTime()) > age) {
                if (includeAbsent) {
                    old.add(r);
                } else {
                    if (r.getLocal().exists()) {
                        old.add(r);
                    }
                }
            }

        }
        return old;
    }

    /**
     * Lists all resources in the manager
     * @param stream
     */
    public void list(PrintStream stream) {
        for (RemoteResource resource : resources) {
            stream.println(Joiner.on("\t").join(Arrays.asList(resource.getLastUpdated(), resource.getLocalSize(), resource.getLocal(), resource.getRemote())));
        }
    }
}
