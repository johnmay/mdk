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

package uk.ac.ebi.mdk.service.location;

/**
 * ResourceLocation - 21.02.2012 <br/>
 * <p/>
 * Describes a location of a resource that can be loaded. This interface
 * serves a base for other resource locations to build upon and it's primary
 * function is determine whether the resource is available to be loaded.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ResourceLocation {

    /**
     * Method determines if the location defined by the resource
     * is available for the loader to use. On a local system file
     * this could be whether the file exists, whilst on a remote
     * location (e.g. FTP) it could check the connection.
     *
     * @return whether the resource location is available
     */
    public boolean isAvailable();

}
