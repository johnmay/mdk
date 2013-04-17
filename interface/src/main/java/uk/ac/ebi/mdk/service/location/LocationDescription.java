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

package uk.ac.ebi.mdk.service.location;

/**
 * LocationDescription - 23.02.2012 <br/>
 * <p/>
 * Class describes a required {@see ResourceLocation} for a
 * {@see ResourceLoader}. It provides methods for name and
 * description access as well as type storage and a default
 * location
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface LocationDescription {

    /**
     * Access a key for this location description
     * @return
     */
    public String getKey();
    
    /**
     * Short name of the required location (e.g. ChEBI SDF File)
     *
     * @return the name
     */
    public String getName();

    /**
     * A longer description, usually describing some bound the contents
     * of the file should follow (e.g. SDF file containing 'ChEBI ID' property fields)
     *
     * @return description
     */
    public String getDescription();

    /**
     * Access a bounding type on the location (e.g. ResourceFileLocation)
     * indicates we need a file an not a directory
     *
     * @return
     */
    public Class<? extends ResourceLocation> getType();

    /**
     * Determine whether the location has an associated default
     * @return whether the description has a default location
     */
    public boolean hasDefault();

    /**
     * Access the default location (if specified)
     * @return default location
     */
    public ResourceLocation getDefault();

}
