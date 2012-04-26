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

import java.util.Set;


/**
 *
 *          Compartment 2012.02.06
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Interface describes a cell compartment
 *
 */
public interface Compartment {

    /**
     * Access the abbreviation for the compartment, such as, C or E
     * @return 
     */
    public String getAbbreviation();


    /**
     * Access a more descriptive name for the compartment, such as: Cytoplasm or
     * Extracellular
     * @return 
     */
    public String getDescription();


    /**
     * Access a collection of synonyms. such as, Extracellular, External
     * @return 
     */
    public Set<String> getSynonyms();


    public byte getRanking();
}
