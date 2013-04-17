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

package uk.ac.ebi.mdk.io;

import uk.ac.ebi.mdk.domain.observation.Observation;

import java.io.IOException;

/**
 * ObservationReader - 08.03.2012 <br/>
 * <p/>
 * Describes a class that can read observations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ObservationReader<O extends Observation> {

    public O readObservation() throws IOException;

}
