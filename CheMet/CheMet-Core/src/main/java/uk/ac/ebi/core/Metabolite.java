
/**
 * Metabolite.java
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
package uk.ac.ebi.core;

import com.google.common.base.Objects;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainer;
import uk.ac.ebi.annotation.chemical.ChemicalStructure;
import uk.ac.ebi.core.AnnotatedEntity;
import uk.ac.ebi.core.metabolite.MetaboliteClass;


/**
 *          Metabolite – 2011.09.05 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class Metabolite
  extends AnnotatedEntity
  implements Externalizable {

    private static final Logger LOGGER = Logger.getLogger(Metabolite.class);
    private boolean generic = false;
    private MetaboliteClass metaboliteClass = MetaboliteClass.UNKNOWN;
    private String name;

    // attributes to control link to reactions

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
        return true;
    }


    public Collection<ChemicalStructure> getChemicalStructures() {
        return super.getAnnotations(ChemicalStructure.class);
    }


    public ChemicalStructure getFirstChemicalStructureAnnotation() {
        List<ChemicalStructure> structures = new ArrayList<ChemicalStructure>(
          getChemicalStructures());
        return structures.isEmpty() ? new ChemicalStructure(new AtomContainer()) : structures.get(0);
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        name = in.readUTF();
        metaboliteClass = MetaboliteClass.valueOf(in.readUTF());
        generic = in.readBoolean();
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeUTF(name);
        out.writeUTF(metaboliteClass.name());
        out.writeBoolean(generic);
    }


    @Override
    public String toString() {
        return name;
    }


    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object obj) {

        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final Metabolite other = (Metabolite) obj;

        if( super.equals(other) == false ) {
            return false;
        }

        if( this.generic != other.generic ) {
            return false;
        }
        if( this.metaboliteClass != other.metaboliteClass ) {
            return false;
        }
        if( (this.name == null) ? (other.name != null) : !this.name.equals(other.name) ) {
            return false;
        }
        return true;
    }


    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        return 47 * hash + Objects.hashCode(generic, metaboliteClass, name);
    }


}

