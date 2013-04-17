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

package uk.ac.ebi.mdk.service.exception;

/**
 * MissingLocationException.java - 20.02.2012 <br/>
 * <p/>
 * Exception should thrown when a loader is missing a required {@see ResourceLocation}
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class MissingLocationException extends RuntimeException {

    /**
     * Construct an instance of the exception with the specified message
     *
     * @param message details of what resource location is missing
     */
    public MissingLocationException(String message) {
        super(message);
    }


}
