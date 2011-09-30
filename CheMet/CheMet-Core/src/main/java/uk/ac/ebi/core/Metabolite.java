
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
import uk.ac.ebi.interfaces.Identifier;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;
import uk.ac.ebi.resource.chemical.BasicChemicalIdentifier;


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
    private MetaboliteClass type = MetaboliteClass.UNKNOWN;


    public Metabolite() {
    }


    public Metabolite(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    /**
     *
     * Convenience constructor wraps accession in a BasicChemicalIdentifier
     *
     * @param accession
     * @param abbreviation
     * @param name
     * 
     */
    public Metabolite(String accession, String abbreviation, String name) {
        super(new BasicChemicalIdentifier(accession), abbreviation, name);
    }


    /**
     *
     * Accessor to whether the molecule is generic (contains one or more -R groups)
     *
     * @return
     */
    public boolean isGeneric() {
        return generic;
    }


    /**
     *
     * Sets whether the molecule is generic (has -R group)
     *
     */
    public void setGeneric(boolean generic) {
        this.generic = generic;
    }


    /**
     *
     * Queries whether the metabolite entry has any {@see ChemicalStructure} attached
     *
     * @return
     * 
     */
    public boolean hasStructureAssociated() {
        return getChemicalStructures().iterator().hasNext();
    }


    public Collection<ChemicalStructure> getChemicalStructures() {
        return super.getAnnotations(ChemicalStructure.class);
    }


    /**
     *
     * Returns the first chemical structure (note. should be used in conjunction with
     * {@see hasStructureAssociated()})
     *
     * @return
     * 
     */
    public ChemicalStructure getFirstChemicalStructure() {
        return getChemicalStructures().iterator().next();
    }


    /**
     * @inheritDoc
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        type = MetaboliteClass.valueOf(in.readUTF());
        generic = in.readBoolean();
    }


    /**
     * @inheritDoc
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeUTF(type.name());
        out.writeBoolean(generic);
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
        if( this.type != other.type ) {
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
        return 47 * hash + Objects.hashCode(generic, type);
    }


    public MetaboliteClass getType() {
        return type;
    }


    public void setType(MetaboliteClass type) {
        this.type = type;
    }

    


}

