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

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.InChI;
import uk.ac.ebi.mdk.domain.entity.reaction.filter.InChIFilter;

/**
 * @name    InChIReaction
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Implementation of Reaction using InChI to represent the reaction participants
 *
 */
public class InChIReaction
        extends AbstractReaction<InChIParticipant> {

    private static final Logger LOGGER = Logger.getLogger( InChIReaction.class );

    public InChIReaction() {
    }
    public InChIReaction(InChIFilter filter) {
        super(filter);
    }

    public void addReactant( InChI inChI , Double d , Compartment compartment ) {
        super.addReactant( new InChIParticipant( inChI , d , compartment ) );
    }

    public void addProduct( InChI inChI , Double d , Compartment compartment ) {
        super.addProduct( new InChIParticipant( inChI , d , compartment ) );
    }
}
