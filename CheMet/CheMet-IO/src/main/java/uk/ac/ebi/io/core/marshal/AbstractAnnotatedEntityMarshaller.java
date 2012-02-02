/**
 * AbstractAnnotatedEntityMarshal.java
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

import java.io.IOException;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.entities.Entity;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.io.ReconstructionOutputStream;
import uk.ac.ebi.interfaces.io.marshal.AnnotatedEntityMarshaller;


/**
 *
 *          AbstractAnnotatedEntityMarshal 2012.01.31
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public abstract class AbstractAnnotatedEntityMarshaller extends AbstractMarshaller implements AnnotatedEntityMarshaller {

    private static final Logger LOGGER = Logger.getLogger(AbstractAnnotatedEntityMarshaller.class);

    private AnnotatedEntityMarshaller superclassMarshal;


    @Override
    public void setEntityFactory(EntityFactory factory) {
        superclassMarshal.setEntityFactory(factory);
    }


    public AnnotatedEntityMarshaller getSuperclassMarshal() {
        return superclassMarshal;
    }


    public void setSuperclassMarshal(AnnotatedEntityMarshaller superclassMarshal) {
        this.superclassMarshal = superclassMarshal;
    }


    public AbstractAnnotatedEntityMarshaller(Version v) {
        super(v);
    }


    public void write(ReconstructionOutputStream out, Entity entity) throws IOException {
        write(out, (AnnotatedEntity) entity);
    }
}
