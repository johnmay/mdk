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

package uk.ac.ebi.mdk.domain.identifier;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *          IdentifierSet – 2011.09.18 <br>
 *          A class for handling multiple identifiers providing efficient read/write externalization
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class IdentifierSet
        implements Externalizable {

    private static final Logger LOGGER = Logger.getLogger(IdentifierSet.class);
    private HashMultimap<Class, Identifier> identifiers = HashMultimap.create();

    /**
     * Adds an identifier to the set
     * @param identifier
     * @return
     */
    public boolean add(Identifier identifier) {
        return identifiers.put(identifier.getClass(), identifier);
    }

    /**
     *
     * Remove an identifier from the set
     *
     * @param identifier
     * @return
     */
    public boolean remove(Identifier identifier) {
        return identifiers.remove(identifier.getClass(), identifier);
    }

    /**
     *
     * Accessor to all the identifiers currently pressent in the set
     *
     * @return A collection of all annotations held within the object
     *
     */
    public Collection<Identifier> getIdentifiers() {
        return identifiers.values();
    }

    /**
     *
     * Accessor to all identifiers of the given type
     *
     * @param type
     * @return
     *
     */
    public <T> Collection<T> getIdentifiers(final Class<T> c) {

        return (Collection<T>) identifiers.get(c);
    }

    /**
     *
     * Accessor to all identifiers extending a given type. For example if you provide a
     * AbstractChemicalIdentifier class all chemical identifiers held within the set will also be
     * returned (e.g. {@see ChEBIIdentifier} and {@see KEGGCompoundIdentifier})
     *
     * @param type
     * @return
     */
    public Collection<Identifier> getSubIdentifiers(final Identifier base) {
        return getSubIdentifiers(base.getClass());
    }

    /**
     *
     * {@see getSubIdentifiers(Identifier)}
     *
     * @param type
     * @return
     *
     */
    public Set<Identifier> getSubIdentifiers(Class base) {
        Set<Identifier> identifierSubset = new HashSet<Identifier>();
        for (Identifier id : getIdentifiers()) {
            if (base.isInstance(id)) {
                identifierSubset.add(id);
            }
        }
        return identifierSubset;
    }

    public boolean contains(Identifier identifier) {
        return identifiers.containsValue(identifier);
    }

    /**
     * @inheritDoc
     */
    public void writeExternal(ObjectOutput out) throws IOException {

        throw new UnsupportedOperationException("deprecated");

    }

    /**
     * @inheritDoc
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        throw new UnsupportedOperationException("deprecated");

    }


    @Override
    public String toString() {
        return Joiner.on(", ").join(identifiers.values());
    }
}
