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
package uk.ac.ebi.mdk.domain.entity;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.annotation.chemical.Charge;
import uk.ac.ebi.annotation.chemical.InChI;
import uk.ac.ebi.chemet.resource.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.collection.MetaboliteClassImplementation;
import uk.ac.ebi.mdk.domain.entity.metabolite.MetaboliteClass;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;


/**
 *          Metabolite – 2011.09.05 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetaboliteImpl
        extends AbstractAnnotatedEntity
        implements Metabolite {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteImpl.class);

    private boolean generic = false;

    private Enum<? extends MetaboliteClass> type = MetaboliteClassImplementation.UNKNOWN;

    public static final String BASE_TYPE = "Metabolite";


    public MetaboliteImpl() {
    }


    public MetaboliteImpl(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    /**
     *
     * Convenience constructor wraps accession in a BasicChemicalIdentifier
     *
     * @param abbreviation
     * @param name
     * 
     */
    public MetaboliteImpl(String abbreviation, String name) {
        // accession is ignored here
        super(new BasicChemicalIdentifier(), abbreviation, name);
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
     * Access the charge annotation of this molecule
     *
     * @return
     */
    public Double getCharge() {
        return hasAnnotation(Charge.class)
               ? getAnnotations(Charge.class).iterator().next().getValue()
               : 0d;
    }


    /**
     *
     * Molecule charge mutator
     *
     */
    public void setCharge(Double charge) {
        addAnnotation(new Charge(charge));
    }


    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetaboliteImpl other = (MetaboliteImpl) obj;

        if (super.equals(other) == false) {
            return false;
        }

        if (this.generic != other.generic) {
            return false;
        }
        if (this.type != other.type) {
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
        return Objects.hashCode(hash, generic, type);
    }


    public Enum<? extends MetaboliteClass> getType() {
        return type;
    }


    public void setType(Enum<? extends MetaboliteClass> type) {
        this.type = type;
    }


    public Entity newInstance() {
        return new MetaboliteImpl();
    }


    /**
     * @inheritDoc
     */
    public boolean hasStructure() {
        return super.hasAnnotation(AtomContainerAnnotation.class) || super.hasAnnotation(InChI.class);
    }


    /**
     * @inheritDoc
     */
    public Collection<ChemicalStructure> getStructures() {
        return getAnnotationsExtending(ChemicalStructure.class);
    }
}
