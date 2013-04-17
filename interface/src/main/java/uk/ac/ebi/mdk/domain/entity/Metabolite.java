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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.entity;

import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.metabolite.MetaboliteClass;

import java.util.Collection;


/**
 * Describes a metabolite.
 *
 * @author johnmay
 * @see AnnotatedEntity
 */
public interface Metabolite extends AnnotatedEntity {

    /**
     * Access the charge of this metabolite. If no charge has been added this
     * should return {@see Double#NAN}
     *
     * @return charge value for the metabolite of NAN if not set
     */
    Double getCharge();

    /**
     * Set the charge on the metabolite.
     *
     * @param charge value
     */
    void setCharge(Double charge);

    /**
     * Set whether the molecule is generic. A generic metabolite is much like an
     * abstract class. It provides some information on the metabolite but is not
     * concrete.
     *
     * @param generic if the metabolic is generic
     */
    void setGeneric(boolean generic);

    /**
     * Access whether the metabolite is generic.
     *
     * @return whether the metabolite is generic
     */
    boolean isGeneric();

    /**
     * Set the metabolite type.
     *
     * @param type class of this metabolite
     */
    void setType(Enum<? extends MetaboliteClass> type);

    /**
     * Access the metabolite type.
     *
     * @return the metabolite class
     */
    Enum<? extends MetaboliteClass> getType();

    /**
     * Method determines whether this metabolite has a chemical structure
     * annotation (see. chemet-annotation) module for types of annotations.
     *
     * @return whether there is structure for this metabolite
     */
    boolean hasStructure();

    /**
     * Convenience method for accessing all associated chemical structures. If
     * no chemical structure is present on the metabolite an empty collection is
     * returned.
     *
     * @return collection of {@see ChemicalStructure} annotations
     */
    Collection<ChemicalStructure> getStructures();

}
