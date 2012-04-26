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
package uk.ac.ebi.mdk.domain.identifier.type;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;
import java.util.Iterator;

/**
 * @name    SequenceIdentifier - 2011.10.13 <br>
 *          A generic identifier describing a sequence.
 *          The interfaces allows resolution of a sequence header
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface SequenceIdentifier extends Identifier {

    /**
     * Returns the header code for the sequence e.g. sp of swissprot, tr for trembl
     */
    public Collection<String> getHeaderCodes();

    public SequenceIdentifier ofHeader(Iterator<String> token);

}
