/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.ebi.mdk.domain.entity.reaction;

import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;

import java.util.Collection;


/**
 * MetabolicReaction 2012.02.07
 *
 * @author johnmay
 * @author $Author$ (this version) <p/> Class description
 * @version $Rev$ : Last Changed $Date$
 */
public interface MetabolicReaction extends Reaction<MetabolicParticipant> {

    public void addModifier(GeneProduct product);

    public Collection<GeneProduct> getModifiers();

    /**
     * Remove the metabolite 'm' from this reaction. If the metabolite is null
     * no removal is attempted.
     *
     * @param m the metabolite to remove
     * @return whether any participants were removed
     */
    public boolean remove(Metabolite m);

    /**
     * Adds a metabolite as a reactant to the reaction. The metabolite will be
     * added with stoichiometric coefficient of 1.0 and the <i>Cytoplasm</i>
     * compartment.
     *
     * @param reactant metabolite to add to the left
     * @return whether the reactant was added
     */
    public boolean addReactant(Metabolite reactant);

    /**
     * Adds a metabolite as a product to the reaction. The metabolite will be
     * added with stoichiometric coefficient of 1.0 and the <i>Cytoplasm</i>
     * compartment.
     *
     * @param product metabolite to add to the left
     * @return whether the reactant was added
     */
    public boolean addProduct(Metabolite product);

}
