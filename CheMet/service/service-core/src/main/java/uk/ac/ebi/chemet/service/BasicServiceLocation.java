package uk.ac.ebi.chemet.service;

import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.chemet.service.ServicePreferences;
import uk.ac.ebi.service.ServiceLocation;
import uk.ac.ebi.service.index.LuceneIndex;

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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public File getLocation() {
        return path;
    }

    @Override
    public long lastModified() {
        return getLocation().lastModified();
    }

    @Override
    public boolean isAvailable() {
        return getLocation().exists();
    }

    @Override
    public File getBackup() {
        if (backup == null) {
            backup = new File(getLocation().getParent(), getLocation().getName() + ".backup");
        }
        return backup;
    }

    @Override
    public boolean canRevert() {
        return getBackup().exists();
    }

    @Override
    public boolean backup() {
        File backup = getBackup();
        if (backup.exists()) {
            delete(backup);
        }
        return getLocation().renameTo(backup);
    }

    @Override
    public boolean revert() {
        File backup = getBackup();
        if (getLocation().exists()) {
            delete(getLocation());
        }
        return backup.renameTo(getLocation());
    }

    @Override
    public void clean() {
        delete(getLocation());
        delete(getBackup());
    }

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
