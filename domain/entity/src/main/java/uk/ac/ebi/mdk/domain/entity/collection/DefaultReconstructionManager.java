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

package uk.ac.ebi.mdk.domain.entity.collection;

import uk.ac.ebi.caf.utility.preference.type.ListPreference;
import uk.ac.ebi.mdk.domain.DomainPreferences;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * ReconstructionManager.java Class to manage the multiple reconstructions and
 * maintain an 'active' reconstruction
 *
 * @author johnmay
 * @date Apr 13, 2011
 */
public class DefaultReconstructionManager implements ReconstructionManager {

    private static final org.apache.log4j.Logger LOGGER =
            org.apache.log4j.Logger.getLogger(
                    DefaultReconstructionManager.class);

    private UUID active;
    private LinkedHashMap<UUID, Reconstruction> reconstructions = new LinkedHashMap<UUID, Reconstruction>();

    private Properties properties = new Properties();
    private LinkedList<String> recent = new LinkedList<String>();

    private ListPreference RECENT_FILES = DomainPreferences.getInstance()
                                                           .getPreference("RECENT_RECONSTRUCTIONS");

    private final Object lock = new Object();

    private DefaultReconstructionManager() {
        // get the recently open files, remove entries that don't exists
        List<String> valid = new ArrayList<String>();
        for (String file : RECENT_FILES.get()) {
            File f = new File(file);
            if (f.exists()) {
                valid.add(f.getAbsolutePath());
            }
        }
        recent.addAll(valid.subList(0, Math.min(valid.size(), 10)));
        RECENT_FILES.put(valid); // having validated put the list back
    }

    private static class ProjectManagerHolder {
        private static DefaultReconstructionManager INSTANCE = new DefaultReconstructionManager();
    }

    /**
     * Access the singleton instance of the manager
     *
     * @return Instance of the manager
     */
    public static ReconstructionManager getInstance() {
        return ProjectManagerHolder.INSTANCE;
    }

    public Collection<Reconstruction> reconstructions() {
        return Collections.unmodifiableCollection(this.reconstructions
                                                          .values());
    }

    public boolean isEmpty() {
        return reconstructions.isEmpty();
    }

    @Override public Reconstruction get(Identifier identifier) {
        for (Reconstruction recon : reconstructions()) {
            if (recon.getIdentifier().equals(identifier)) {
                return recon;
            }
        }
        return null;
    }

    @Override
    public void add(Reconstruction reconstruction) {
        synchronized (lock) {
            String path = reconstruction.getContainer().getAbsolutePath();
            if (recent.contains(path)) {
                recent.remove(path);
            }
            recent.add(path);
            RECENT_FILES.put(recent);

            if (!reconstructions.containsKey(reconstruction.uuid())) {
                LOGGER.debug("Setting active with a project which can not be found in the current collection. A new entry will be created");
                reconstructions.put(reconstruction.uuid(), reconstruction);
            }
        }
    }

    /**
     * Access the active reconstruction
     *
     * @return
     */
    public Reconstruction active() {
        return reconstructions.get(active);
    }


    public boolean remove(Reconstruction reconstruction) {
        synchronized (lock) {
            if (reconstruction == null)
                return false;
            UUID uuid = reconstruction.uuid();
            if (active.equals(uuid)) {
                active = null;
            }
            return reconstructions.remove(uuid) != null;
        }
    }


    /**
     * Set the active reconstruction given a reconstruction object
     *
     * @param reconstruction
     */
    public void activate(Reconstruction reconstruction) {
        synchronized (lock) {
            String path = reconstruction.getContainer().getAbsolutePath();
            if (recent.contains(path)) {
                recent.remove(path);
            }
            recent.add(path);
            RECENT_FILES.put(recent);

            UUID uuid = reconstruction.uuid();

            if (!reconstructions.containsKey(uuid)) {
                LOGGER.debug("Setting active with a project which can not be found in the current collection. A new entry will be created");
                add(reconstruction);
            }

            active = uuid;
        }
    }


    /**
     * Access the number of project currently managed
     */
    public int size() {
        return reconstructions.size();
    }

    /**
     * Returns a list of recently opened reconstructions
     */
    public LinkedList<String> recent() {
        return recent;
    }
}
