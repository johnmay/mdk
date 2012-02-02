/**
 * AbstractEntityMarshal.java
 *
 * 2012.01.31
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
package uk.ac.ebi.io.core.marshal;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.io.marshal.AnnotatedEntityMarshaller;
import uk.ac.ebi.interfaces.io.marshal.EntityMarshaller;


/**
 *
 *          AbstractEntityMarshal 2012.01.31
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public abstract class AbstractEntityMarshaller extends AbstractMarshaller implements EntityMarshaller {

    private static final Logger LOGGER = Logger.getLogger(AbstractEntityMarshaller.class);

    private EntityMarshaller superclassMarshal;


    @Override
    public void setEntityFactory(EntityFactory factory) {
        if (superclassMarshal != null) {
            superclassMarshal.setEntityFactory(factory);
        }
        super.setEntityFactory(factory);
    }


    public EntityMarshaller getSuperclassMarshal() {
        return superclassMarshal;
    }


    public void setSuperclassMarshal(EntityMarshaller superclassMarshal) {
        this.superclassMarshal = superclassMarshal;
    }


    public AbstractEntityMarshaller(Version v) {
        super(v);
    }
}
