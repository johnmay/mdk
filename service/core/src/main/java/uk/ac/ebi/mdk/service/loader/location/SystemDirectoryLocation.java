package uk.ac.ebi.mdk.service.loader.location;

import uk.ac.ebi.mdk.service.location.ResourceDirectoryLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * SystemDirectoryLocation.java - 20.02.2012 <br/>
 * <p/>
 * A SystemDirectoryLocation is a resource located on the system that is a directory. The InputStream for this will
 * provide the full path's to the file's separated by line separator.
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1719 $
 */
public class SystemDirectoryLocation
        implements ResourceDirectoryLocation {

    private File directory;
    private Iterator<File> fileIterator;
    private File activeFile;
    private InputStream activeStream;

    private int counter = 0;
    private int size = 0;

    public SystemDirectoryLocation(File directory) {
        this.directory = directory;

        List<File> files = Arrays.asList(directory.listFiles());

        this.size         = files.size();
        this.fileIterator = files.iterator();

    }

    /**
     * Indicates whether the directory exists and whether it is in-fact a directory
     *
     * @inheritDoc
     */
    @Override
    public boolean isAvailable() {
        return directory.exists() && directory.isDirectory();
    }

    @Override
    public boolean hasNext() {
        return fileIterator.hasNext();
    }

    /**
     * Should be called after next()
     *
     * @return
     */
    @Override
    public String getEntryName() {
        if (activeFile == null)
            throw new NoSuchElementException("Make sure next() is called first");
        return activeFile.getName();
    }

    @Override
    public InputStream next()  throws IOException  {

        if (fileIterator.hasNext()) {

            activeFile = fileIterator.next();

            if (activeStream != null) {
                activeStream.close();
            }

            counter++;
            activeStream = new FileInputStream(activeFile);

            return activeStream;

        }

        throw new NoSuchElementException("No more streams in system directory");

    }

    @Override public double progress() {
        return counter / (double) size;
    }

    @Override
    public void close() throws IOException {
        if (activeStream != null)
            activeStream.close();
    }

    /**
     * @inheritDoc
     */
    public String toString() {
        return directory.toString();
    }

}
