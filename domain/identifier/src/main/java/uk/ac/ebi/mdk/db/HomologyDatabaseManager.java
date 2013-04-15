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

package uk.ac.ebi.mdk.db;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import uk.ac.ebi.mdk.ResourcePreferences;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name HomologyDatabaseManager - 2011.10.10 <br> Singleton description
 */
public class HomologyDatabaseManager {

    private static final org.apache.log4j.Logger logger = org.apache.log4j
            .Logger.getLogger(HomologyDatabaseManager.class);

    private static final ResourcePreferences RESOURCE_PREFERENCES = ResourcePreferences
            .getInstance();
    private static final Pattern NAME_PATTERN = Pattern
            .compile("^([^.]+)(?:\\.\\d\\d)?\\.(?:phr|pin|pog|psd|psi|psq|msk|pal)");

    private Map<String, String> alias = Maps.newHashMap();
    private Multimap<String, File> files = HashMultimap.create();

    private HomologyDatabaseManager() {

        index(); // builds map

        alias.put("nr", "Non-redundant");
        alias.put("uniprot_sprot", "SwissProt");
        alias.put("swissport", "SwissProt");
        alias.put("uniprot_sprot_enzymes", "SwissProt (Enzymes)");
        alias.put("refseq_genomic", "RefSeq Genomic");
        alias.put("wgs", "Whole Genome Shotgun");
        alias.put("est_human", "Expressed Sequence Tags (Human)");

    }

    /**
     * Access an instance of the database manager
     *
     * @return
     */
    public static HomologyDatabaseManager getInstance() {
        return HomologyDatabaseManagerHolder.INSTANCE;
    }

    /** Instance holder */
    private static class HomologyDatabaseManagerHolder {
        private static final HomologyDatabaseManager INSTANCE = new HomologyDatabaseManager();
    }


    public File getRoot() {
        return (File) RESOURCE_PREFERENCES.getPreference("BLAST_DB_ROOT").get();
    }

    /**
     * Access a list of database names that preside under the blast database
     * root directory. This method will return the base names of the databases
     * (i.e. 'nr.01.pin', 'nr.01.psi' will be flattened to 'nr').
     *
     * @return
     */
    public Set<String> index() {
        files.clear();
        File root = getRoot();
        if (root != null) {
            for (File f : root.listFiles()) {
                Matcher m = getNameMatcher(f);
                if (m.find()) {
                    files.put(m.group(1), f);
                }
            }
        }

        return files.keySet();

    }

    /**
     * Access an alias for the database name. The nameToAlais is often more
     * meaningful (e.g. nr -> Non-redundant).
     *
     * @param name
     * @return
     */
    public String getAlias(String name) {
        return alias.containsKey(name) ? alias.get(name) : name;
    }

    private static Matcher getNameMatcher(File f) {
        return NAME_PATTERN.matcher(f.getName());
    }

    /**
     * Access all files for a specified database name. For example providing
     * "uniprot_sprot" will return "uniprot_sprot.psi", "uniprot_sprot.pin" and
     * the other database files.
     *
     * @param key database name or alias
     * @return all files for the given database name
     */
    public Collection<File> getFiles(String key) {

        if (files.containsKey(key)) {
            return files.get(key);
        }

        throw new InvalidParameterException("No database found");

    }

    /**
     * Check whether the manager has access to a path for a database name or
     * alias.
     *
     * @param key the key to check
     * @return whether there is a path available
     */
    public boolean hasPath(final String key) {
        return files.containsKey(key);
    }

    /**
     * Returns the path to the named database. The raw name is first search and
     * then aliases. If not database is found then an {@see
     * InvalidParameterException} is thrown.
     *
     * @param key name or alias (e.g. nr or Non-redundant)
     * @return full path (i.e. getRoot() + name) which can be used as the
     *         database argument when creating a blast search
     */
    public File getPath(String key) {

        if (files.containsKey(key)) {
            return new File(getRoot(), key);
        }

        throw new InvalidParameterException("No database found");

    }
}
