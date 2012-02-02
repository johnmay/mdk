/**
 * MetaboliteMarshal_0_8_5_0.java
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
package uk.ac.ebi.io.core.marshal.versions;

import java.io.IOException;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.interfaces.entities.Entity;
import uk.ac.ebi.interfaces.io.ReconstructionInputStream;
import uk.ac.ebi.interfaces.io.ReconstructionOutputStream;
import uk.ac.ebi.interfaces.io.marshal.EntityMarshaller;
import uk.ac.ebi.io.core.marshal.AbstractEntityMarshaller;
import uk.ac.ebi.resource.IdentifierFactory;


/**
 *
 *          MetaboliteMarshal_0_8_5_0 2012.01.31
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class EntityMarshaller_0_8_5_0 extends AbstractEntityMarshaller {

    private static final Logger LOGGER = Logger.getLogger(EntityMarshaller_0_8_5_0.class);


    public EntityMarshaller_0_8_5_0() {
        super(new Version(0, 8, 5, 0));
    }


    public EntityMarshaller newInstance() {
        return new EntityMarshaller_0_8_5_0();
    }


    @Override
    public Entity read(ReconstructionInputStream in) throws IOException, ClassNotFoundException {

        Entity entity = getSuperclassMarshal().read(in);

        entity.setIdentifier(IdentifierFactory.getInstance().read(in));
        entity.setName(in.readUTF());
        entity.setAbbreviation(in.readUTF());

        return entity;

    }


    @Override
    public void write(ReconstructionOutputStream out, Entity e) throws IOException {

        getSuperclassMarshal().write(out, e);

        IdentifierFactory.getInstance().write(out, e.getIdentifier());
        out.writeUTF(e.getName());
        out.writeUTF(e.getAbbreviation());

    }
}
