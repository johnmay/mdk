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

/**
 * Describes a service to access IUPAC names.
 *
 * @author johnmay
 * @see IUPACNameService
 */
public interface IUPACNameAccess<I> {

    /**
     * Access the IUPAC name for a provided identifier. The IUPAC name
     * represents a resolvable standardised naming system. It is important to
     * note that the same structure may have different IUPAC names from
     * different databases. For example ATP in ChEBI has the IUPAC name
     * "adenosine 5'-(tetrahydrogen triphosphate)" whilst ATP in HMDB IUPAC name
     * is "[[[(2S,3S,4R,5R)-5-(6-aminopurin-9-yl)-3,4-dihydroxy
     * -oxolan-2-yl]methoxy-hydroxy-phosphoryl]oxy-hydroxy-phosphoryl]oxyphosphonic
     * acid"
     *
     * @param identifier identifier to retrieve the IUPAC name for
     * @return the IUPAC name as defined in there query resource
     */
    public String getIUPACName(I identifier);
}
