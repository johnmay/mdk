/**
 * IdentifierLoader.java
 *
 * 2011.09.15
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.resource;

import java.util.*;

import uk.ac.ebi.interfaces.MetaInfoLoader;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.MIRIAMEntry;


/**
 * IdentifierLoader – 2011.09.15 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class IdentifierLoader
        extends AbstractLoader
        implements MetaInfoLoader {

    private static final String RESOURCE_NAME = "IdentifierDescription.properties";

    private static final String MIR_EXTENSION = ".MIR";

    private static final String SYNONYMS = ".Synonyms";

    private static final MIRIAMLoader MIRIAM_LOADER = MIRIAMLoader.getInstance();

    private Map<Class, IdentifierMetaInfo> loaded = new HashMap<Class, IdentifierMetaInfo>(32);

    private IdentifierLoader() {
        super(IdentifierLoader.class.getResourceAsStream(RESOURCE_NAME));
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
     *
     * @return
     */
    public int getMIR(Class<? extends AbstractIdentifier> type) {

        MIRIAMIdentifier miriam = type.getAnnotation(MIRIAMIdentifier.class);

        if (miriam != null) {
            return miriam.mir();
        }

        return Short.parseShort(super.getProperty(type.getSimpleName() + MIR_EXTENSION));


    }


    /**
     * Returns the miriam entry for this identifier class
     *
     * @param type
     *
     * @return
     */
    public MIRIAMEntry getEntry(Class type) {
        int mir = getMIR(type);
        return MIRIAM_LOADER.getEntry(mir);
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription(Class type) {

        String key = type.getSimpleName() + SHORT_DESCRIPTION;


        if (super.containsKey(key)) {
            return super.getShortDescription(type);
        }

        int mir = getMIR(type);

        if (mir != 0) {
            MIRIAMEntry entry = MIRIAM_LOADER.getEntry(mir);
            return entry.getName();
        }

        return "";

    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription(Class type) {

        String key = type.getSimpleName() + LONG_DESCRIPTION;

        if (super.containsKey(key)) {
            return super.getLongDescription(type);
        }

        int mir = getMIR(type);

        if (mir != 0) {
            MIRIAMEntry entry = MIRIAM_LOADER.getEntry(mir);
            return entry.getDescription();
        }

        return "";

    }


    /**
     * Access the synonyms for this identifier
     *
     * @param type
     *
     * @return
     */
    public Collection<String> getDatabaseSynonyms(Class type) {

        String key = type.getSimpleName() + SYNONYMS;

        int mir = getMIR(type);

        Collection<String> synonyms = new ArrayList();
        if (mir != 0) {
            synonyms.addAll(MIRIAM_LOADER.getEntry(mir).getSynonyms());
        }

        if (super.containsKey(key)) {
            synonyms.addAll(Arrays.asList(super.getProperty(key).split(",")));
        }

        return synonyms;
    }


    public IdentifierMetaInfo load(Class c) {
        IdentifierMetaInfo metaInfo = getMetaInfo(c);
        loaded.put(c, metaInfo);
        return metaInfo;
    }

    private IdentifierMetaInfo loadMetaInfo(Class c) {
        IdentifierMetaInfo metaInfo = new IdentifierMetaInfo(getEntry(c),
                                                                   getShortDescription(c),
                                                                   getLongDescription(c),
                                                                   getIndex(c),
                                                                   getDatabaseSynonyms(c));
        loaded.put(c, metaInfo);
        return metaInfo;
    }

    @Override
    public IdentifierMetaInfo getMetaInfo(Class c) {
        return loaded.containsKey(c) ? loaded.get(c) : loadMetaInfo(c);
    }


}
