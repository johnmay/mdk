/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.service;

import java.io.File;

/**
 * ServiceLocation - 05.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ServiceLocation {

    public String getName();

    /**
     * The location of the service
     * @return
     */
    public File getLocation();


    /**
     * When the service was last updated (if available)
     * @return
     */
    public long lastModified();

    /**
     * Whether the service is available
     * @return
     */
    public boolean isAvailable();

    /**
     * Access the backup location for this service
     * @return backup file
     */
    public File getBackup();

    /**
     * Indicate whether is possible to
     * revert the service to a previous
     * state
     */
    public boolean canRevert();

    /**
     * Creates a backup of this service
     * @return
     */
    public boolean backup();

    /**
     * Revert the service to a previous state
     * @return
     */
    public boolean revert();

    /**
     * Remove all traces of this service location
     */
    public void clean();

}
