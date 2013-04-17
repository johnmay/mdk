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

package uk.ac.ebi.mdk.domain.tool;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.*;
import uk.ac.ebi.mdk.tool.CompartmentResolver;

import java.util.*;

/**
 * Provides suggestions for the provided compartment ranked by the levenshtein distance.
 * <p/>
 * Example usage:
 * <pre>{@code
 * AutomaticCompartmentResolver resolver = new AutomaticCompartmentResolver();
 * for(String name : Arrays.asList("c", "g", "e", "a")){
 *     if(resolver.canResolve(name)){
 *         System.out.println(resolver.getCompartment(name).getDescription());
 *     } else {
 *         System.err.println("Could not resolve compartment: " + name);
 *     }
 * }
 * }</pre>
 *
 * @author johnmay
 */
public class PrefixCompartmentResolver implements CompartmentResolver {

    private static final Logger LOGGER = Logger.getLogger(PrefixCompartmentResolver.class);

    private ListMultimap<String, Compartment> compartments = ArrayListMultimap.create();
    private Set<String>                       ambiguous    = new HashSet<String>();

    /**
     * Default constructor uses several compartment enumerations for the resolver
     *
     * @see uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle
     * @see uk.ac.ebi.mdk.domain.entity.reaction.compartment.Membrane
     * @see uk.ac.ebi.mdk.domain.entity.reaction.compartment.CellType
     * @see uk.ac.ebi.mdk.domain.entity.reaction.compartment.Tissue
     * @see uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organ
     */
    public PrefixCompartmentResolver() {

        for (Compartment compartment : Organelle.values()) {
            addCompartment(compartment);
        }
        for (Compartment compartment : Membrane.values()) {
            addCompartment(compartment);
        }
        for (Compartment compartment : CellType.values()) {
            addCompartment(compartment);
        }
        for (Compartment compartment : Tissue.values()) {
            addCompartment(compartment);
        }
        for (Compartment compartment : Organ.values()) {
            addCompartment(compartment);
        }

    }


    /**
     * Adds a compartment to the resolver as three possible keys 1) abbreviation,
     * 2) description and 3) abbreviation surrounded by '[...]'. For example
     * the compartment Cytoplasm will be added to the resolver with the following
     * keys: 'c', 'cytoplasm', and '[c]'. The keys are normalised to lower case by
     * default
     *
     * @param compartment the compartment to add
     */
    public void addCompartment(Compartment compartment) {
        put(compartment.getDescription(),
            compartment);
    }

    /**
     * Stores the compartment in a map with the given key. If a compartment
     * with that key already exists then key will stored as ambiguous.
     *
     * @param key         key to store
     * @param compartment compartment instance
     */
    public void put(String key, Compartment compartment) {

        key = key.toLowerCase(Locale.ENGLISH);

        if (compartments.containsKey(key)) {
            ambiguous.add(key); // store clash
            return;
        }

        compartments.put(key, compartment);

    }

    /**
     * Convenience method inverting the result of {@see #isAmbiguous(String)}.
     * <p/>
     * <pre>{@code
     * AutomaticCompartmentResolver resolver = new AutomaticCompartmentResolver();
     * for(String name : Arrays.asList("c", "g", "e", "a")){
     *     if(resolver.canResolve(name)){
     *         System.out.println(resolver.getCompartment(name).getDescription());
     *     } else {
     *         System.err.println("Could not resolve compartment: " + name);
     *     }
     * }
     * }</pre>
     *
     * @param compartment compartment to attempt resolution for
     *
     * @return whether the compartment can be resolved
     *
     * @see #isAmbiguous(String)
     */
    public boolean canResolve(String compartment) {
        return !isAmbiguous(compartment);
    }

    /**
     *
     */
    @Override
    public boolean isAmbiguous(String compartment) {
        return getCompartments(compartment).size() > 1;
    }

    /**
     * Access an appropriate instance of a compartment for the given string notation. If
     * the notation is ambiguous a warning will be logged. If no appropriate instance is
     * available then an Unknown compartment is returned
     *
     * @param compartment name or abbreviation of a compartment (i.e. 'c', 'e', 'cytoplasm')
     *
     * @return appropriate instance
     */
    @Override
    public Compartment getCompartment(String compartment) {

        List<Compartment> suggestions = getCompartments(compartment);

        if (suggestions.size() > 1) {
            LOGGER.warn("Ambiguous compartment name provided to resolved: " + compartment);
        }

        return suggestions.isEmpty() ? Organelle.UNKNOWN : suggestions.get(0);

    }

    @Override
    public List<Compartment> getCompartments(String compartment) {

        String comparmentLC = compartment.toLowerCase(Locale.ENGLISH);

        List<Compartment> suggestions = new ArrayList<Compartment>();

        for (String key : compartments.keySet()) {
            if (key.startsWith(comparmentLC)) {
                suggestions.addAll(compartments.get(key));
            }
        }

        return suggestions;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<Compartment> getCompartments() {
        return compartments.values();
    }
}
