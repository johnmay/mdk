/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A metabolome instance linked to a {@link Reconstruction}. When a new
 * metabolite is added it will be {@link Reconstruction#register(uk.ac.ebi.mdk.domain.entity.Entity)}ed
 * with the reconstruction.
 *
 * @author johnmay
 */
public final class MetabolomeImpl implements Metabolome {

    private final Map<Identifier, Metabolite> metabolites = new TreeMap<Identifier, Metabolite>();

    private final Reconstruction reconstruction;

    /**
     * Create a new metabolome for the provided reconstruction.
     *
     * @param reconstruction to which this metabolome belongs
     */
    public MetabolomeImpl(Reconstruction reconstruction) {
        this.reconstruction = checkNotNull(reconstruction);
    }

    /**
     * @inheritDoc
     */
    @Override public boolean add(Metabolite m) {
        return reconstruction.register(checkNotNull(m)) && _add(m);
    }

    private boolean _add(Metabolite m) {
        if (metabolites.containsKey(m.getIdentifier()))
            throw new IllegalArgumentException("clashing metabolite identifiers");
        metabolites.put(m.getIdentifier(), m);
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override public boolean remove(Metabolite m) {
        reconstruction.deregister(checkNotNull(m));
        return metabolites.remove(m.getIdentifier()) != null;
    }

    /**
     * @inheritDoc
     */
    @Override public Collection<Metabolite> ofName(String name) {

        String clean = checkNotNull(name).trim().toLowerCase();

        Collection<Metabolite> metabolites = new ArrayList<Metabolite>();

        for (Metabolite metabolite : this) {
            String cleanOther = metabolite.getName().trim().toLowerCase();
            if (cleanOther.equals(clean)) {
                metabolites.add(metabolite);
            }
        }

        return metabolites;

    }

    /**
     * @inheritDoc
     */
    @Override public boolean contains(Metabolite m) {
        // id may not be unique, reference equality currently needed
        return ofIdentifier(checkNotNull(m).getIdentifier()) == m;
    }

    @Override public List<Metabolite> toList() {
        return Collections
                .unmodifiableList(new ArrayList<Metabolite>(metabolites
                                                                    .values()));
    }

    @Override public boolean isEmpty() {
        return metabolites.isEmpty();
    }

    @Override public int size() {
        return metabolites.size();
    }

    @Override public Iterator<Metabolite> iterator() {
        return metabolites.values().iterator();
    }

    @Override public Metabolite ofIdentifier(Identifier identifier) {
        return metabolites.get(identifier);
    }

}
