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

package uk.ac.ebi.chemet.io.observation;

import uk.ac.ebi.interfaces.Observation;

import java.io.IOException;
import java.util.Collection;

/**
 * ObservationInput - 11.03.2012 <br/>
 * <p/>
 * Describes input for multiple observations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ObservationInput {

    /**
     * Read the next observation in the input.
     * @param <O>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public <O extends Observation> O read() throws IOException, ClassNotFoundException;

    public <O extends Observation> O read(Class<O> c) throws IOException, ClassNotFoundException;

    public Class readClass() throws IOException, ClassNotFoundException;

    public Collection<Observation> readCollection() throws IOException, ClassNotFoundException;
    
}
