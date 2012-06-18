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
 * MolecularChargeService - 28.02.2012 <br/>
 * <p/>
 * Defines a service that can look-up charge on an given identifier. There
 * is currently no support for searching charge as the results sets would
 * likely be very large. If a specific charge is required one approach
 * could be to search with a different criteria (i.e. molecular formula)
 * and then filter those results for the desired charge
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface MolecularChargeService<I extends Identifier>
        extends QueryService<I> {

    public static final Term MOLECULAR_CHARGE = new Term("MolecularCharge");

    /**
     * Look-up the charge value for a given identifier. If no charge
     * is found then {@see Double.NAN} is returned.
     *
     * @param identifier query identifier
     *
     * @return the charge of the identifier, Double.NAN if no charge is found.
     */
    public Double getCharge(I identifier);

}
