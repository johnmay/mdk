/**
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package uk.ac.ebi.annotation.util;

import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.MetaInfoLoader;
import uk.ac.ebi.resource.AbstractLoader;


/**
 * AnnotationLoader – 2011.09.08 <br> Class loads annotation descriptions from a
 * resource properties file
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @author $Author$ (this version)
 */
public class AnnotationLoader
        extends AbstractLoader
        implements MetaInfoLoader {

    private static final Logger LOGGER = Logger.getLogger(AnnotationLoader.class);

    private static final String RESOURCE_NAME = "AnnotationDescription.properties";


    private AnnotationLoader() {
        super(AnnotationLoader.class.getResourceAsStream(RESOURCE_NAME));
    }


    private static class AnnotationDescriptionHolder {

        private static AnnotationLoader INSTANCE = new AnnotationLoader();
    }


    public static AnnotationLoader getInstance() {
        return AnnotationDescriptionHolder.INSTANCE;
    }


    @Override
    public Byte getIndex(Class clazz) {
        try {
            return super.getIndex(clazz);
        } catch (Exception e) {
            LOGGER.warn("No index found for " + clazz);
            return 0;
        }
    }
}
