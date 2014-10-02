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

package uk.ac.ebi.chemet.tools.annotation;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.AnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Map identifiers to entities by specifying how to look up entities and how an
 * identifier mapped to a specific entity should be handled. Various issues such
 * as unmapped keys, ambiguous identifiers and unknown resources can be
 * retrieved (see. {@link #unknown()} etc.).
 *
 * <blockquote><pre>
 * // key accessor is an inner class, this one uses abbreviation as key
 * KeyAccessor accessor = new KeyAccessor(){
 *     public String key(AnnotatedEntity entity) {
 *         return entity.getAbbreviation();
 *     }
 * }
 * AnnotationMapper mapper = new AnnotationMapper(entities, accessor);
 *
 * // map the following accessions to entities which are abbreviated 'atp'
 * mapper.map("atp", "CHEBI:12");
 * mapper.map("atp", "C00009");
 * mapper.map("atp", "5957");  // ambiguous accession
 *
 * // set has item "5957", needs to be explictly mapped
 * Set&lt;String&gt; ambiguous = mapper.ambiguous();
 *
 * // specify name, or
 * mapper.map("atp', "5957", "PubChem-Compound");
 * // create instance
 * mapper.map("atp', new PubChemCompoundIdentifier("5957"));
 *
 * </pre></blockquote>
 *
 * One can handle custom handling of mappings by creating and injecting a {@link
 * Handler} instance.
 *
 * @author John May
 */
public final class AnnotationMapper<K> {

    private final Handler handler;
    private final IdentifierFactory idFactory;
    private final AnnotationFactory annotations = DefaultAnnotationFactory.getInstance();

    /* keys which did not map */
    private final Set<K> unmapped = new HashSet<K>();

    /* id's did not match the regex (internal) */
    private final Set<String> invalid = new HashSet<String>();

    /* id's of unknown name */
    private final Set<String> unknown = new HashSet<String>();

    /* id's inference was ambiguous */
    private final Set<String> ambiguous = new HashSet<String>();

    private final Multimap<K, AnnotatedEntity> map;

    public AnnotationMapper(Collection<? extends AnnotatedEntity> entities, KeyAccessor<K> keyAccessor) {
        this(entities,
             keyAccessor, new BasicHandler(),
             DefaultIdentifierFactory.getInstance());
    }

    public AnnotationMapper(Collection<? extends AnnotatedEntity> entities,
                            KeyAccessor<K> keyAccessor,
                            Handler handler,
                            IdentifierFactory idFactory) {
        this.handler = handler;
        this.idFactory = idFactory;
        map = HashMultimap.create(entities.size(), 1);
        for (AnnotatedEntity entity : entities) {
            map.put(keyAccessor.key(entity), entity);
        }
    }

    /**
     * Given an accession, try and infer which resource the identifier is from.
     * If the accession is very general and can not be inferred it is added to
     * the {@link #ambiguous()} set. If a single resource was inferred it will
     * try to be mapped to any entities matching the given key.
     *
     * <blockquote><pre>
     *     IdentifierMap mapper = ...;
     *     mapper.map("atp", "C00002");
     *     mapper.map("atp", "CHEBI:57299");
     * </pre></blockquote>
     *
     * @param key       the key to locate the entity
     * @param accession the accession to infer a resource from
     * @return an entity <i>handled</i> the identifier
     * @see #ambiguous()
     */
    public boolean map(K key, String accession) {
        Collection<Class<? extends Identifier>> ids = idFactory
                .ofPattern(accession);
        if (ids.size() == 1) {
            Identifier identifier = idFactory.ofClass(ids.iterator().next());
            identifier.setAccession(accession);
            return map(key, identifier);
        } else if (ids.size() > 1) {
            ambiguous.add(accession);
        }
        return false;
    }

    /**
     * Find the identifier for the resource name and set the accession and add
     * then try to map the identifier to an entity which matches the key. If the
     * name cannot be found it is added to {@link #unknown()}. If the identifier
     * is invalid is will be added to {@link #invalid()}.
     *
     * <blockquote><pre>
     *     IdentifierMap mapper = ...;
     *     mapper.map("atp", "C00002", "KEGG Compound");
     *     mapper.map("atp", "CHEBI:57299", "ChEBI");
     * </pre></blockquote>
     *
     * @param key       look up entities
     * @param accession identifier accession
     * @param name      resource name
     * @return an entity <i>handled</i> the identifier
     * @see #invalid()
     * @see #unknown()
     */
    public boolean map(K key, String accession, String name) {
        Identifier identifier = idFactory.ofName(name);
        if (identifier != IdentifierFactory.EMPTY_IDENTIFIER) {
            identifier.setAccession(accession);
            if (identifier.isValid()) {
                return map(key, identifier);
            } else {
                invalid.add(accession);
            }
        } else {
            unknown.add(name);
        }
        return false;
    }

    /**
     * Map the identifier to all entities which match the given key. If the key
     * did not match any entity then the key is added to {@link #unmapped()}
     *
     * @param key look up entities
     * @param id  the id
     * @return an entity <i>handled</i> the identifier
     * @see #unmapped()
     */
    public boolean map(K key, Identifier id) {
        if (id == null)
            throw new IllegalArgumentException("no identifier provided");
        return map(key, annotations.getCrossReference(id));
    }

    public boolean map(K key, Annotation annotation) {
        final Collection<AnnotatedEntity> entities = map.get(key);
        if (!entities.isEmpty()) {
            boolean mapped = false;
            for (AnnotatedEntity e : entities)
                mapped = handler.handle(e, annotation) || mapped;
            return mapped;
        } else {
            unmapped.add(key);
        }
        return false;
    }

    /**
     * Set of keys which were not be mapped to entities.
     *
     * @return unmapped keys
     */
    public Set<K> unmapped() {
        return Collections.unmodifiableSet(unmapped);
    }

    /**
     * Set of accessions which were found by name but did not match the expected
     * pattern.
     *
     * <blockquote><pre>
     * AnnotationMapper mapper = ...;
     * mapper.map(key, "CHEBI:12", "HMBD");
     * </pre></blockquote>
     *
     * @return invalid accessions
     */
    public Set<String> invalid() {
        return Collections.unmodifiableSet(invalid);
    }

    /**
     * Set of unknown resource names, the name was provided but could not be
     * found in the identifier factory.
     *
     * @return unknown resource names
     */
    public Set<String> unknown() {
        return Collections.unmodifiableSet(unknown);
    }

    /**
     * Set of identifiers that could not be inferred because they were ambiguous
     * (matched more then one resource).
     *
     * @return ambiguous accessions
     */
    public Set<String> ambiguous() {
        return Collections.unmodifiableSet(ambiguous);
    }

    /**
     * Generate the key we will use to look up entities.
     *
     * @param <K> type of the key e.g. String
     */
    public static interface KeyAccessor<K> {
        K key(AnnotatedEntity entity);
    }

    /** Handle mapping of entities with identifiers */
    public static interface Handler {
        boolean handle(AnnotatedEntity entity, Annotation id);
    }

    /**
     * Basic implementation which adds the id as a cross-reference to the
     * entity.
     */
    private static class BasicHandler implements Handler {
        @Override
        public boolean handle(AnnotatedEntity entity, Annotation annotation) {
            entity.addAnnotation(annotation);
            return true;
        }
    }
}
