/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

/**
 * Describe a service to access molecular formulae.
 *
 * @author johnmay
 * @see MolecularFormulaService
 */
public interface MolecularFormulaAccess<I> {

    /**
     * Retrieve a molecular formula for the specified identifier. This method
     * will look-up the molecular formula for the provided identifier. If no
     * formula is found it will return an empty string
     *
     * @param identifier the identifier to fetch the formula for
     * @return molecular formula as a string
     */
    public String getMolecularFormula(I identifier);

    /**
     * Convenience method for retrieving a CDK {@see IMolecularFormula}
     * instance. See {@see getMolecularFormula(I)} for more details. If the no
     * formula is found an empty {@see IMolecularFormula} is returned.
     *
     * @param identifier query to look-up
     * @return an IMolecularFormula instance (not null)
     */
    public IMolecularFormula getIMolecularFormula(I identifier);


}
