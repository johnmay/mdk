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

package uk.ac.ebi.mdk.domain;

import uk.ac.ebi.mdk.deprecated.IdPattern;
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.deprecated.MIRIAMLoader;
import uk.ac.ebi.mdk.deprecated.Synonyms;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.tool.MetaInfoLoader;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * IdentifierLoader – 2011.09.15 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class IdentifierLoader
        extends DefaultLoader
        implements MetaInfoLoader {

    private static final MIRIAMLoader MIRIAM_LOADER = MIRIAMLoader
            .getInstance();

    private Map<Class, IdentifierMetaInfo> loaded = new HashMap<Class, IdentifierMetaInfo>(32);

    private IdentifierLoader() {
        super();
    }


    private static class IdentifierLoaderHolder {
        private static IdentifierLoader INSTANCE = new IdentifierLoader();
    }


    public static IdentifierLoader getInstance() {
        return IdentifierLoaderHolder.INSTANCE;
    }


    /**
     * Returns the MIRIAM MIR Identifier
     *
     * @param type
     * @return
     */
    public int getMIR(Class<? extends Identifier> type) {

        MIR miriam = type.getAnnotation(MIR.class);

        if (miriam != null) {
            return miriam.value();
        }

        return 0; // default entry

    }


    /**
     * Returns the miriam entry for this identifier class
     *
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public MIRIAMEntry getEntry(Class type) {
        int mir = getMIR(type);
        return MIRIAM_LOADER.getEntry(mir);
    }


    /** @inheritDoc */
    @Override
    @SuppressWarnings("unchecked")
    public String getShortDescription(Class c) {

        int mir = getMIR(c);

        String name = super.getShortDescription(c);

        if (mir != 0 && name.equals("Unknown")) {
            MIRIAMEntry entry = MIRIAM_LOADER.getEntry(mir);
            return entry.getName();
        }

        return name;

    }


    /** @inheritDoc */
    @Override
    @SuppressWarnings("unchecked")
    public String getLongDescription(Class c) {

        int mir = getMIR(c);

        if (mir != 0) {
            MIRIAMEntry entry = MIRIAM_LOADER.getEntry(mir);
            return entry.getDescription();
        }

        return super.getLongDescription(c);

    }


    /**
     * Access the synonyms for this identifier
     *
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public Collection<String> getDatabaseSynonyms(Class type) {

        int mir = getMIR(type);

        Set<String> synonyms = new HashSet<String>();
        if (mir != 0) {
            synonyms.addAll(MIRIAM_LOADER.getEntry(mir).getSynonyms());
        }

        Synonyms annotation = (Synonyms) type.getAnnotation(Synonyms.class);
        if (annotation != null) {
            synonyms.addAll(Arrays.asList(annotation.value()));
        }

        return synonyms;
    }

    /**
     * Access the pattern.
     *
     * @param c the class
     * @return compiled pattern
     */
    private Pattern pattern(final Class<? extends Identifier> c) {
        IdPattern annotation = (IdPattern) c.getAnnotation(IdPattern.class);
        if (annotation != null) {
            return Pattern.compile(annotation.value());
        }
        int mir = getMIR(c);
        if (mir != 0)
            return MIRIAM_LOADER.getEntry(mir).getCompiledPattern();
        else
            return null;
    }

    public IdentifierMetaInfo load(Class c) {
        IdentifierMetaInfo metaInfo = getMetaInfo(c);
        loaded.put(c, metaInfo);
        return metaInfo;
    }

    private IdentifierMetaInfo loadMetaInfo(Class c) {
        @SuppressWarnings("unchecked")
        IdentifierMetaInfo metaInfo = new IdentifierMetaInfo(super.getMetaInfo(c),
                                                             getEntry(c),
                                                             getDatabaseSynonyms(c),
                                                             pattern(c));
        loaded.put(c, metaInfo);
        return metaInfo;
    }

    @Override
    public IdentifierMetaInfo getMetaInfo(Class c) {
        return loaded.containsKey(c) ? loaded.get(c) : loadMetaInfo(c);
    }


}
