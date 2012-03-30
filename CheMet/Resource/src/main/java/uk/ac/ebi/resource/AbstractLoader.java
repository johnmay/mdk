/**
 * AbstractLoader.java
 *
 * 2011.09.15
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.MetaInfo;
import uk.ac.ebi.interfaces.MetaInfoLoader;
import uk.ac.ebi.interfaces.annotation.Descriptor;

/**
 *          AbstractLoader – 2011.09.15 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractLoader
        extends Properties
        implements MetaInfoLoader {

    private static final Logger LOGGER = Logger.getLogger(AbstractLoader.class);
    public static final String SHORT_DESCRIPTION = ".ShortDescription";
    public static final String LONG_DESCRIPTION = ".LongDescription";
    public static final String INDEX = ".Index";


    public AbstractLoader(InputStream stream) {
        try {
            super.load(stream);
        } catch (IOException ex) {
            LOGGER.error("Could not load annotation descriptions " + ex.getMessage());
        }
    }

    public String getShortDescription(Class c) {

        Descriptor descriptor = (Descriptor) c.getAnnotation(Descriptor.class);

        if (descriptor != null) {
            return descriptor.brief();
        }

        return getProperty(c.getSimpleName() + SHORT_DESCRIPTION);

    }

    public String getLongDescription(Class c) {

        Descriptor descriptor = (Descriptor) c.getAnnotation(Descriptor.class);

        if (descriptor != null) {
            return descriptor.description();
        }

        return getProperty(c.getSimpleName() + LONG_DESCRIPTION);
    }

    /**
     * Return the index of a given class
     * @param c
     * @return
     */
    public Byte getIndex(Class c) {
            return Byte.parseByte(getProperty(c.getSimpleName() + INDEX));
    }

    /**
     * Returns a bundled description object with the short description, long description and index
     * as public static final variables
     *
     * @param c
     * @return
     *
     */
    public MetaInfo getMetaInfo(Class c) {
        return new MetaInfo(getShortDescription(c),
                getLongDescription(c),
                getIndex(c));
    }
}
