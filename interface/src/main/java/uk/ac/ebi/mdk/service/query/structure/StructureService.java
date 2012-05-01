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

package uk.ac.ebi.mdk.service.query.structure;

import org.apache.lucene.index.Term;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * StructureService - 28.02.2012 <br/>
 * <p/>
 * Provides a CDK atom container for a given identifier
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface StructureService<I extends Identifier> extends QueryService<I> {

    public static final Term ATOM_CONTAINER = new Term("AtomContainer");
    public static final Term FINGERPRINT_BIT = new Term("Fingerprint.Bit");

    /**
     * Access the CDK IAtomContainer for the given identifier
     *
     * @param identifier
     *
     * @return
     */
    public IAtomContainer getStructure(I identifier);

    /**
     * Search for similar structures
     * <p/>
     * <b>NOTE: API may change in future</b>
     *
     * @param molecule the structure to search
     *
     * @return identifiers in this service which have similar structure
     */
    public Collection<I> searchStructure(IAtomContainer molecule);

}
