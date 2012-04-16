/**
 * DefaultLoader.java
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

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.Brief;
import uk.ac.ebi.chemet.Description;
import uk.ac.ebi.core.MetaInfo;
import uk.ac.ebi.interfaces.MetaInfoLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * DefaultLoader – 2011.09.15 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class DefaultLoader implements MetaInfoLoader {

    private static final Logger LOGGER = Logger.getLogger(DefaultLoader.class);

    protected Map<Class, MetaInfo> loaded = new HashMap<Class, MetaInfo>();

    protected DefaultLoader() {
    }

    public static DefaultLoader getInstance() {
        return DefaultLoaderHolder.INSTANCE;
    }

    private static class DefaultLoaderHolder {
        public static DefaultLoader INSTANCE = new DefaultLoader();
    }


    public String getShortDescription(Class c) {

        if (loaded.containsKey(c)) {
            return loaded.get(c).brief;
        }

        if (c.isAnnotationPresent(Brief.class)) {
            Brief brief = (Brief) c.getAnnotation(Brief.class);
            return brief.value();
        }

        return "Unknown";

    }

    public String getLongDescription(Class c) {

        if (loaded.containsKey(c)) {
            return loaded.get(c).description;
        }

        if (c.isAnnotationPresent(Description.class)) {
            Description desc = (Description) c.getAnnotation(Description.class);
            return desc.value();
        }

        return "No description available";
    }


    private MetaInfo loadMetaInfo(Class c) {
        MetaInfo metaInfo = new MetaInfo(getShortDescription(c),
                                         getLongDescription(c));
        loaded.put(c, metaInfo);
        return metaInfo;
    }

    public MetaInfo getMetaInfo(Class c) {
        return loaded.containsKey(c) ? loaded.get(c) : loadMetaInfo(c);
    }
}
