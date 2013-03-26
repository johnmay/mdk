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

package uk.ac.ebi.mdk.domain.identifier;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

/**
 * @author John May
 */
public interface IdentifierFactory {

    /**
     * Constructs an identifier of the correct type of the given name. If the
     * name is not found an {@link IdentifierFactory#EMPTY_IDENTIFIER} is
     * returned.
     *
     * @param name the resource name e.g. ChEBI
     * @return an instance of a typed identifier
     */
    public Identifier ofName(String name);

    /**
     * Constructs an identifier of the correct type of the given name and sets
     * the accession. If the name is not found an {@link
     * IdentifierFactory#EMPTY_IDENTIFIER} is returned.
     *
     * @param name the resource name e.g. ChEBI
     * @return an instance of a typed identifier
     */
    public Identifier ofName(String name, String accession);

    public boolean hasSynonym(String synonym);

    /**
     * Constructs an identifier of the correct type of the given synonym. If the
     * name is not found an {@link IdentifierFactory#EMPTY_IDENTIFIER} is
     * returned. It is possible to avoid the EMPTY_IDENTIFIER if {@link
     * #hasSynonym(String)} is invoked first.
     *
     * @param synonym the resource name e.g. ChEBI
     * @return an instance of a typed identifier
     */
    public Identifier ofSynonym(String synonym);

    /**
     * Constructs an identifier of the correct type of the given synonym and
     * sets the accession. If the name is not found an {@link
     * IdentifierFactory#EMPTY_IDENTIFIER} is returned. It is possible to avoid
     * the EMPTY_IDENTIFIER if {@link #hasSynonym(String)} is invoked first.
     *
     * @param synonym the resource name e.g. ChEBI
     * @return an instance of a typed identifier
     */
    public Identifier ofSynonym(String synonym, String accession);

    /**
     * A new identifier instance for a given class.
     *
     * @param type class type
     * @param <I> identifier type
     * @return new identifier
     */
    public <I extends Identifier> I ofClass(Class<I> type);

    /**
     * Find identifier classes which match the provided pattern.
     * @param accession an accession (e.g. 'CHEBI:12', 'C00009')
     * @return collecition of classes which match
     */
    public Collection<Class<? extends Identifier>> ofPattern(String accession);

    public static Identifier EMPTY_IDENTIFIER = new Identifier() {
        @Override
        public void setAccession(String accession) {

        }

        @Override
        public String getAccession() {
            return "";
        }

        @Override
        public Identifier newInstance() {
            return null;
        }

        @Override
        public URL getURL() {
            return null;
        }

        @Override
        public String getResolvableURL() {
            return null;
        }

        @Override
        public String getURN() {
            return "";
        }

        @Override public boolean isValid() {
            // convenient for if conditions
            return false;
        }

        @Override
        public Collection<String> getSynonyms() {
            return Collections.EMPTY_SET;
        }

        @Override
        public Resource getResource() {
            return null;
        }

        @Override
        public String getSummary() {
            return "";
        }

        @Override
        public String getShortDescription() {
            return "";
        }

        @Override
        public String getBrief() {
            return "";
        }

        @Override
        public String getLongDescription() {
            return "";
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {

        }

        @Override
        public void readExternal(ObjectInput in) throws IOException,
                                                        ClassNotFoundException {

        }

        @Override public int compareTo(Identifier o) {
            return -1;
        }
    };

}
