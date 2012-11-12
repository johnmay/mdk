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
package uk.ac.ebi.mdk.domain.entity;

import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;

import java.util.List;


/**
 * Reaction 2012.02.07
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Interface describes a reaction
 * @version $Rev$ : Last Changed $Date$
 */
public interface Reaction<P extends Participant>
        extends AnnotatedEntity {

    public List<P> getReactants();


    public List<P> getProducts();


    public List<P> getParticipants();


    public boolean addReactant(P participant);


    public boolean addProduct(P participant);

    /**
     * Remove a reactant participant from this reaction.
     *
     * @param participant the participant to remove
     * @return whether the participant was removed
     */
    public boolean removeReactant(P participant);

    /**
     * Remove a product participant from this reaction.
     *
     * @param participant the participant to remove
     * @return whether the participant was removed
     */
    public boolean removeProduct(P participant);


    // public boolean removeReactant(P participant);
    // public boolean removeProduct(P participant);
    public int getReactantCount();


    public int getProductCount();


    public int getParticipantCount();


    public Direction getDirection();


    public void setDirection(Direction direction);


    public void transpose();


    /**
     * Empty reaction of participants and metabolites (preserve annotations and
     * name, abbrev, direction etc.)
     */
    public void clear();
}
