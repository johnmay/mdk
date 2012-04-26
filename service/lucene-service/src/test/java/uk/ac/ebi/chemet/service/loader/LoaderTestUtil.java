package uk.ac.ebi.chemet.service.loader;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.loader.location.RemoteLocation;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.File;
import java.io.IOException;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LoaderTestUtil {

    private static final Logger LOGGER = Logger.getLogger(LoaderTestUtil.class);

    public static File createTemporaryDirectory(String prefix) throws IOException {

        File root = new File(System.getProperty("java.io.tmpdir"));
        File temp = new File(root, prefix + "-" + System.currentTimeMillis());

        if (temp.exists()) {
            if (!temp.delete()) {
                throw new IOException("Could not delete clashing temporary file/directory");
            }
        }

        if (!temp.mkdirs()) {
            throw new IOException("Could not create temporary directory");
        }

        return temp;

    }
    
    public static ResourceFileLocation getLocation(String path) {
        return new RemoteLocation(LoaderTestUtil.class.getResource(path));
    }

    /**
     * Creates the inspector and loads the data
     *
     * @param index
     *
     * @return
     *
     * @throws IOException
     */
    public static LuceneIndexInspector getIndexInspector(LuceneIndex index) throws IOException {
        return new LuceneIndexInspector(index).load();
    }   
    

    /**
     * Test that the index was create an the inspector can read it
     * @param index
     * @return
     */
    public static boolean testInspector(LuceneIndex index) {

        LuceneIndexInspector inspector = null;
        try {
            inspector = new LuceneIndexInspector(index);
        } catch (IOException ex) {
        } finally {
            if (inspector != null) {
                try {
                    inspector.close();
                } catch (IOException ex) {
                }
            }
        }

        return inspector != null;

    }


}
