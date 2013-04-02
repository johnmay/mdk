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

package uk.ac.ebi.mdk.service.query.name;

import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * BrandNameService - 29.02.2012 <br/>
 * <p/>
 * Interface describes a service that will provided look-up and search of brand names.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface BrandNameService<I extends Identifier>
        extends QueryService<I> {

    public static final Term BRAND_NAME = new Term("BrandName");

    /**
     * Look-up the brand name for a provided identifier.
     *
     * @param identifier the identifier to look-up the brand
     *                   name for
     *
     * @return brand name or empty string
     */
    public String getBrandName(I identifier);


    /**
     * Search for identifiers in the data-set who's brand name matches
     * the provided  name.
     *
     * @param name        a brand name
     * @param approximate lossen search criteria
     *
     * @return collection of identifiers that match the brand name (empty collection if none)
     */
    public Collection<I> searchBrandName(String name, boolean approximate);

}
