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

import uk.ac.ebi.caf.utility.preference.type.FilePreference;

import java.io.File;

/**
 * BasicServiceLocation.java - 21.02.2012 <br/> Description...
 * <p/>
 * Provides name storage and startup/lastModified implementations
 * as well as backup and revert operations on a single file
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class BasicServiceLocation
        implements ServiceLocation {

    private static final FilePreference SERVICE_ROOT = ServicePreferences.getInstance().getPreference("SERVICE_ROOT");

    private String name;
    private File path;
    private File backup;

    public BasicServiceLocation(String name, String path) {
        this(name, new File(SERVICE_ROOT.get(), path));
    }

    public BasicServiceLocation(String name, File path) {
        this.name = name;
        this.path = path;
    }

    public BasicServiceLocation(String name, File path, File backup) {
        this.name = name;
        this.path = path;
        this.backup = backup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getLocation() {
        return path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long lastModified() {
        return getLocation().lastModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAvailable() {
        return getLocation().exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getBackup() {
        if (backup == null) {
            backup = new File(getLocation().getParent(), getLocation().getName() + ".backup");
        }
        return backup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRevert() {
        return getBackup().exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean backup() {
        File backup = getBackup();
        if (backup.exists()) {
            delete(backup);
        }
        return getLocation().renameTo(backup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean revert() {
        File backup = getBackup();
        if (getLocation().exists()) {
            delete(getLocation());
        }
        return backup.renameTo(getLocation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clean() {
        delete(getLocation());
        delete(getBackup());
    }

    /**
     * Deletes the file and all it's children
     * @param dir file to delete
     * @return whether the file was deleted
     */
    private static boolean delete(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = delete(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

}
