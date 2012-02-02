package uk.ac.ebi.interfaces.io.marshal;

/**
 * ReconstructionFactory.java
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
import uk.ac.ebi.interfaces.io.ReconstructionOutputStream;
import java.io.IOException;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.io.ReconstructionInputStream;


/**
 *
 *          ReconstructionFactory 2012.01.31
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public interface AnnotatedEntityMarshaller extends EntityMarshaller {

    public AnnotatedEntity read(ReconstructionInputStream in) throws IOException, ClassNotFoundException;


    public void write(ReconstructionOutputStream out, AnnotatedEntity entity) throws IOException;
}
