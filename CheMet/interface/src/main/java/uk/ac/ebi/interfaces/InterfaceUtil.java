package uk.ac.ebi.interfaces;


/**
 * Util.java
 *
 * 2012.02.06
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
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.entities.Entity;


/**
 *
 *          Util 2012.02.06
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class InterfaceUtil {

    private static final Logger LOGGER = Logger.getLogger(InterfaceUtil.class);

    private static final Map<Class, Class> ENTITY_MAP = new HashMap<Class, Class>(20);


    /**
     * 
     * Access the entity class of the specified entity. This is used for
     * internal interface referencing e.g. MetaboliteImplementation
     * will return Metabolite.
     * 
     * @param c
     * @return 
     * 
     */
    public static Class<? extends Entity> getEntityClass(Class<? extends Entity> c) {

        if (ENTITY_MAP.containsKey(c)) {
            return ENTITY_MAP.get(c);
        }

        for (Class i : c.getInterfaces()) {
            if (Entity.class.isAssignableFrom(c)) {
                ENTITY_MAP.put(c, i);
                return i;
            }
        }

        LOGGER.warn("No direct interface found for " + c);

        return Entity.class;

    }
}
