package uk.ac.ebi.mdk.domain.entity;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.UUID;

/**
 * An association map between one entity type an another using their UUIDs.
 *
 * @author John May
 */
public final class AssociationMap {

    private final Multimap<UUID, UUID> associations;

    private AssociationMap(int capacity) {
        this.associations = HashMultimap.create(capacity, 4);
    }

    /**
     * Associate one entity with another and vise versa.
     *
     * @param s an entity
     * @param t another entity
     */
    public void associate(Entity s, Entity t) {
        associations.put(s.uuid(), t.uuid());
        associations.put(t.uuid(), s.uuid());
    }

    /**
     * Dissociate one entity with another and vise versa.
     *
     * @param s an entity
     * @param t another entity
     */
    public void dissociate(Entity s, Entity t) {
        associations.remove(s.uuid(), t.uuid());
        associations.remove(t.uuid(), s.uuid());
    }

    /**
     * Clear all associations for the provided entity
     * @param entity an entity
     */
    public void clear(Entity entity) {
        UUID id = entity.uuid();
        for (UUID uuid : associations.removeAll(id)) {
            associations.remove(uuid, id);
        }
    }

    /**
     * All associations for the given entity, provided as UUIDs.
     * @param e an entity
     * @return associations, specified by UUIDs
     */
    public Collection<UUID> associations(Entity e) {
        return associations.get(e.uuid());
    }


    public static AssociationMap create(int capacity) {
        return new AssociationMap(capacity);
    }
}
