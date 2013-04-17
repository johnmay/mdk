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

package uk.ac.ebi.mdk.service.query.data;

import org.openscience.cdk.interfaces.IMolecularFormula;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * Describe a service to search molecular formulae.
 *
 * @author johnmay
 * @see MolecularFormulaService
 */
public interface MolecularFormulaSearch<I> {

    /**
     * Search for a identifiers in the data-set which match the provided
     * molecular formula. The approximate flag determines whether the proton
     * count should be matched
     *
     * @param formula     formula to search
     * @param approximate whether to ignore proton differences
     * @return identifiers from this data set that match the query
     */
    public Collection<I> searchMolecularFormula(String formula, boolean approximate);


    /**
     * Convenience method for searching with a CDK {@see IMolecularFormula}
     * instance. This method simpily wraps {@see searchMolecularFormula(String,
     *boolean)}
     *
     * @param formula     formula to search
     * @param approximate whether to ignore proton differences
     * @return identifiers from this data set that match the query
     */
    public Collection<I> searchMolecularFormula(IMolecularFormula formula, boolean approximate);
}
