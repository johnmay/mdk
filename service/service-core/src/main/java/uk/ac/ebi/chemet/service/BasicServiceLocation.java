package uk.ac.ebi.chemet.service;

import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.mdk.service.ServiceLocation;

import java.io.File;

/**
 * BasicServiceLocation.java - 21.02.2012 <br/> Description...
 * <p/>
 * Provides name storage and isAvailable/lastModified implementations
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
