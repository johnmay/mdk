package uk.ac.ebi.chemet.service.loader;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.loader.location.DefaultLocationFactory;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.MultiIndexResourceLoader;

import java.util.*;

/**
 * ${Name}.java - 20.02.2012 <br/>
 * <p/>
 * Abstract loaded defines the handling of the resource locations and availability methods
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractMultiIndexResourceLoader
        extends AbstractResourceLoader
        implements MultiIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(AbstractMultiIndexResourceLoader.class);

    private DefaultLocationFactory factory = DefaultLocationFactory.getInstance();

    private Map<String, LuceneIndex> indexMap = new HashMap<String, LuceneIndex>();

    public AbstractMultiIndexResourceLoader(LuceneIndex index) {
        indexMap.put(index.getName(), index);
    }

    public AbstractMultiIndexResourceLoader() {

    }

    /**
     * @inheritDoc
     */
    public void addIndex(String key, LuceneIndex index) {
        indexMap.put(key, index);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<LuceneIndex> getIndices() {
        return indexMap.values();
    }

    /**
     * @inheritDoc
     */
    @Override
    public LuceneIndex getIndex(String key) {
        return indexMap.get(key);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void backup() {

        for (LuceneIndex index : getIndices()) {
            if (index.isAvailable()) {
                index.backup();
            }
        }

    }

    /**
     * @inheritDoc
     */
    @Override
    public void revert() {
        for (LuceneIndex index : getIndices()) {
            if (index.canRevert()) {
                index.revert();
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void clean() {
        for (LuceneIndex index : getIndices()) {
            index.clean();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean canBackup() {
        boolean backup = false;
        for (LuceneIndex index : getIndices()) {
            backup = index.isAvailable() || backup;
        }
        return backup;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean canRevert() {
        boolean revert = false;
        for (LuceneIndex index : getIndices()) {
            revert = index.canRevert() || revert;
        }
        return revert;
    }
}
