/*
 * Copyright (c) 2012. EMBL-EBI
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.io;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;

/**
 * IdentifierWriter - 12.03.2012 <br/>
 * <p/>
 * Writes a single identifier type to a stream. Each type of identifier
 * should have a different writer, however basic cases (i.e. where there is just
 * an accession) could use a basic identifie writer.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface IdentifierWriter<I extends Identifier> {

    /**
     * Write the identifier data
     *
     * @param identifier
     *
     * @throws IOException
     */
    public void write(I identifier) throws IOException;

}
