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

package uk.ac.ebi.mdk.service.query.data;

import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

/**
 * Describes a service that provides access and searching of molecular formulae.
 * The approximate search to search for formulas that have the same number of
 * heavy atoms but different numbers of protons. The formula's can be
 * searched/returned both as a String and {@see IMolecularFormula} for
 * convenience.
 *
 * @author johnmay
 */
public interface MolecularFormulaService<I extends Identifier>
        extends MolecularFormulaSearch<I>,
                MolecularFormulaAccess<I>,
                QueryService<I> {

    public static final Term MOLECULAR_FORMULA = new Term("MolecularFormula");
}
