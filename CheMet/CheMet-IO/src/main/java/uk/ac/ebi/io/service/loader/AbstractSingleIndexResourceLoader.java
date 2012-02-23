package uk.ac.ebi.io.service.loader;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.core.CorePreferences;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.index.LuceneIndex;
import uk.ac.ebi.io.service.loader.location.DefaultLocationDescription;
import uk.ac.ebi.io.service.loader.location.LocationDescription;
import uk.ac.ebi.io.service.loader.location.LocationFactory;
import uk.ac.ebi.io.service.loader.location.ResourceLocation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * ${Name}.java - 20.02.2012 <br/>
 * <p/>
 * Abstract loaded defines the handling of the resource locations and availability methods
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractSingleIndexResourceLoader
        extends AbstractResourceLoader
        implements SingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(AbstractSingleIndexResourceLoader.class);

    private LuceneIndex index;

    /**
     * Construct with a single index
     * @param index
     */
    public AbstractSingleIndexResourceLoader(LuceneIndex index) {
        this.index = index;
    }

    /**
     * @inheritDoc
     */
    @Override
    public LuceneIndex getIndex() {
        return index;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getName() {
        return index.getName();
    }


    /**
     * @inheritDoc
     */
    @Override
    public void backup() {
        if (index.isAvailable()) {
            index.backup();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void revert() {
        if (index.canRevert()) {
            index.revert();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void clean() {
        index.clean();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean canBackup() {
        return index.isAvailable();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean canRevert() {
        return index.canRevert();
    }
}
