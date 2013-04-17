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

package uk.ac.ebi.mdk.service.loader;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.loader.location.DefaultLocationFactory;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.MultiIndexResourceLoader;
import uk.ac.ebi.mdk.service.location.LocationFactory;

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

    private LocationFactory factory = DefaultLocationFactory.getInstance();

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
            } else if(index.getLocation().exists()){
                index.clean();
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

    /**
     * @inheritDoc
     */
    @Override public boolean loaded() {
        return getIndices().iterator().next().isAvailable();
    }

    /**
     * @inheritDoc
     */
    @Override public Date updated() {
        Date date = new Date();
        long modified = getIndices().iterator().next().getLocation().lastModified();
        date.setTime(modified);
        if(modified == 0L)
            throw new IllegalArgumentException("no modified data, ensure with loader.loaded() before loader.updated()");
        return date;
    }
}
