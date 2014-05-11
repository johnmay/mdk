/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.domain.entity;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.annotation.Charge;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.annotation.InChI;
import uk.ac.ebi.mdk.domain.annotation.Molfile;
import uk.ac.ebi.mdk.domain.annotation.SMILES;
import uk.ac.ebi.mdk.domain.entity.collection.MetaboliteClassImplementation;
import uk.ac.ebi.mdk.domain.entity.metabolite.MetaboliteClass;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;

import java.util.Collection;
import java.util.UUID;


/**
 * Metabolite – 2011.09.05 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class MetaboliteImpl
        extends AbstractAnnotatedEntity
        implements Metabolite {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteImpl.class);

    private boolean generic = false;

    private Enum<? extends MetaboliteClass> type = MetaboliteClassImplementation.UNKNOWN;

    public static final String BASE_TYPE = "Metabolite";


    public MetaboliteImpl() {
        this(UUID.randomUUID());
    }

    public MetaboliteImpl(UUID uuid){
        super(uuid);
    }


    public MetaboliteImpl(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    /**
     * Convenience constructor wraps accession in a BasicChemicalIdentifier
     *
     * @param abbreviation
     * @param name
     */
    public MetaboliteImpl(String abbreviation, String name) {
        // accession is ignored here
        super(BasicChemicalIdentifier.nextIdentifier(), abbreviation, name);
    }

    public MetaboliteImpl(String name) {
        // accession is ignored here
        this("", name);
    }


    /**
     * Accessor to whether the molecule is generic (contains one or more -R
     * groups)
     *
     * @return
     */
    public boolean isGeneric() {
        return generic;
    }


    /**
     * Sets whether the molecule is generic (has -R group)
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
     * Molecule charge mutator
     */
    public void setCharge(Double charge) {
        addAnnotation(new Charge(charge));
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
        return super.hasAnnotation(AtomContainerAnnotation.class) || super.hasAnnotation(InChI.class) || super.hasAnnotation(SMILES.class) || super.hasAnnotation(Molfile.class);
    }


    /**
     * @inheritDoc
     */
    public Collection<ChemicalStructure> getStructures() {
        return getAnnotationsExtending(ChemicalStructure.class);
    }
}
