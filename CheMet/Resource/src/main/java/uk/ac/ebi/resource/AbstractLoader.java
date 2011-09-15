
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
import uk.ac.ebi.chemet.interfaces.entities.DescriptionLoader;
import uk.ac.ebi.core.Description;


/**
 *          AbstractLoader – 2011.09.15 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractLoader
  extends Properties
  implements DescriptionLoader {

    private static final Logger LOGGER = Logger.getLogger(AbstractLoader.class);


    public AbstractLoader(InputStream stream) {
        try {
            super.load(stream);
        } catch( IOException ex ) {
            LOGGER.error("Could not load annotation descriptions " + ex.getMessage());
        }
    }


    public String getShortDescription(Class clazz) {
        return getProperty(clazz.getSimpleName() + ".ShortDescription");
    }


    public String getLongDescription(Class clazz) {
        return getProperty(clazz.getSimpleName() + ".LongDescription");
    }


    /**
     * Return the index of a given class
     * @param clazz
     * @return
     */
    public Byte getIndex(Class clazz) {
        return Byte.parseByte(getProperty(clazz.getSimpleName() + ".Index"));
    }


    /**
     * Returns a bundled description object with the short description, long description and index
     * as public static final variables
     *
     * @param clazz
     * @return
     * 
     */
    public Description get(Class clazz) {
        return new Description(getShortDescription(clazz),
                               getLongDescription(clazz),
                               getIndex(clazz));
    }


}

