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

package uk.ac.ebi.mdk.service.query.structure;

import org.apache.lucene.index.Term;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

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

}
