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
import uk.ac.ebi.mdk.service.SingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.index.LuceneIndex;

import java.io.IOException;
import java.util.Date;

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
    public void setIndex(LuceneIndex index) {
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
        } else if(index.getLocation().exists()){
            index.clean();
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

    /**
     * @inheritDoc
     */
    @Override public boolean loaded() {
        return index.isAvailable();
    }

    /**
     * @inheritDoc
     */
    @Override public Date updated() {
        Date date = new Date();
        long modified = getIndex().getLocation().lastModified();
        date.setTime(modified);
        if(modified == 0L)
            throw new IllegalArgumentException("no modified data, ensure with loader.loaded() before loader.updated()");
        return date;
    }
}
