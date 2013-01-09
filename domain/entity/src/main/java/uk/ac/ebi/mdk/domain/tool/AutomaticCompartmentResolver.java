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

package uk.ac.ebi.mdk.domain.tool;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.*;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.tool.CompartmentResolver;

import java.util.*;

import static uk.ac.ebi.mdk.domain.identifier.Taxonomy.Kingdom.EUKARYOTA;

/**
 * Provides automated resolution of compartments from a given string. This should be used
 * for low-level resolution where you can handle unresolvable cases appropriately.
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
public class AutomaticCompartmentResolver implements CompartmentResolver {

    private static final Logger LOGGER = Logger.getLogger(AutomaticCompartmentResolver.class);

    private ListMultimap<String, Compartment> compartments = ArrayListMultimap.create();
    private Set<String>                       ambiguous    = new HashSet<String>();

    /**
     * Default constructor uses several compartment enumerations for the resolver
     *
     * @see Organelle
     * @see uk.ac.ebi.mdk.domain.entity.reaction.compartment.Membrane
     * @see uk.ac.ebi.mdk.domain.entity.reaction.compartment.CellType
     * @see Tissue
     * @see uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organ
     */
    public AutomaticCompartmentResolver() {

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
     * Modest constraint that only include the cell type, tissue and organ compartments
     * if the kingdom is a eukaryote. By default the organelle and membrance compartments
     * are always added.
     *
     * @param kingdom kingdom to constrain the compartments
     *
     * @see CellType
     * @see Organ
     * @see Tissue
     * @see Organelle
     * @see Membrane
     */
    public AutomaticCompartmentResolver(Taxonomy.Kingdom kingdom){

        for (Compartment compartment : Organelle.values()) {
            addCompartment(compartment);
        }
        for (Compartment compartment : Membrane.values()) {
            addCompartment(compartment);
        }

        if(EUKARYOTA.equals(kingdom)){
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
        put(compartment.getAbbreviation(),
            compartment);
        put(compartment.getDescription(),
            compartment);
        put("[" + compartment.getAbbreviation() + "]",
            compartment);
        for(String synonym : compartment.getSynonyms())
            put(synonym, compartment);
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
     * Determine whether the string notation of the compartment is ambiguous. This
     * will also check whether the compartment can be resolved.
     * <p/>
     * <pre>{@code
     * AutomaticCompartmentResolver resolver = new AutomaticCompartmentResolver();
     * for(String name : Arrays.asList("c", "g", "e", "a")){
     *     if(!resolver.isAmbiguous(name)){
     *         System.out.println(resolver.getCompartment(name).getDescription());
     *     } else {
     *         System.err.println("Could not resolve compartment: " + name);
     *     }
     * }
     * }</pre>
     *
     * @param compartment compartment to attempt resolution for
     *
     * @return whether the provided compartment name is ambiguous or not pressent
     */
    @Override
    public boolean isAmbiguous(String compartment) {
        compartment = compartment.toLowerCase(Locale.ENGLISH);
        return ambiguous.contains(compartment) || !compartments.containsKey(compartment);
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

        // normalise
        compartment = compartment.toLowerCase(Locale.ENGLISH);

        if (ambiguous.contains(compartment)) {
            LOGGER.warn("Ambiguous compartment name provided to resolved: " + compartment);
        }

        if (compartments.containsKey(compartment)) {
            return compartments.get(compartment).iterator().next();
        }

        return Organelle.UNKNOWN;

    }

    @Override
    public List<Compartment> getCompartments(String compartment) {
        compartment = compartment.toLowerCase(Locale.ENGLISH);
        return compartments.get(compartment);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<Compartment> getCompartments() {
        return compartments.values();
    }
}
