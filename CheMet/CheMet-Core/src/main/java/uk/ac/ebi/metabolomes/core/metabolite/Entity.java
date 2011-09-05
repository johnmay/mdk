
/**
 * Entity.java
 *
 * 2011.09.05
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
package uk.ac.ebi.metabolomes.core.metabolite;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import mnb.annotation.entity.ChemicalStructureAnnotation;
import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.core.AnnotatedComponent;


/**
 *          Entity – 2011.09.05 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class Entity
  extends AnnotatedComponent
  implements Externalizable {

    private static final Logger LOGGER = Logger.getLogger(Entity.class);
    private boolean generic = false;
    private MetaboliteClass metaboliteClass = MetaboliteClass.UNKNOWN;

    /**
     *
     * Accessor to whether the molecule is generic (contains one or more -R groups)
     *
     * @return
     */
    public boolean isGeneric() {
        return generic;
    }


    public void setGeneric(boolean generic) {
        this.generic = generic;
    }


    public boolean hasStructureAssociated() {
        return super.getAnnotations().has(ChemicalStructureAnnotation.class);
    }


    public List<ChemicalStructureAnnotation> getChemicalStructures() {
        return super.getAnnotations().getChemicalStructureAnnotations();
    }


    public ChemicalStructureAnnotation getFirstChemicalStructureAnnotation() {
        return hasStructureAssociated() ? getChemicalStructures().get(0) :
               new ChemicalStructureAnnotation();
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        metaboliteClass = (MetaboliteClass) in.readObject();
        generic = in.readBoolean();
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(metaboliteClass);
        out.writeBoolean(generic);
    }


}

