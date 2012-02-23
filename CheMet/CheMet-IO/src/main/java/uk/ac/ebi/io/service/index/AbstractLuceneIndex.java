package uk.ac.ebi.io.service.index;

import uk.ac.ebi.service.index.LuceneIndex;

import java.io.File;

/**
 * AbstractLuceneIndex.java - 21.02.2012 <br/> Description...
 *
 * Provides name storage and isAvailable/lastModified implementations
 * as well as backup and revert operations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractLuceneIndex
        implements LuceneIndex {

    private String name;
    private File backup;

    public AbstractLuceneIndex(String name){
        this.name = name;
    }
    
    public AbstractLuceneIndex(String name, File backup){
        this.name = name;
        this.backup = backup;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public long lastModified(){
        return getLocation().lastModified();
    }

    @Override
    public boolean isAvailable(){
        return getLocation().exists();
    }

    @Override
    public File getBackup() {
        if(backup == null){
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
        if(backup.exists()){
           delete(backup);
        }
        return getLocation().renameTo(backup);
    }

    @Override
    public boolean revert() {
        File backup = getBackup();
        if(getLocation().exists()){
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
