
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

import uk.ac.ebi.interfaces.DescriptionLoader;
import uk.ac.ebi.chemet.resource.MIRIAMResourceLoader;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.MIRIAMEntry;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;


/**
 *          IdentifierLoader – 2011.09.15 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class IdentifierLoader
  extends AbstractLoader
  implements DescriptionLoader {

    private static final String RESOURCE_NAME = "IdentifierDescription.properties";
    private static final String MIR_EXTENSION = ".MIR";
    private static final MIRIAMResourceLoader miriam = MIRIAMResourceLoader.getInstance();


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
     *
     * Returns the MIR Identifier
     *
     * @param type
     * @return
     */
    public Short getMIR(Class<? extends AbstractIdentifier> type) {
        return Short.parseShort(super.getProperty(type.getSimpleName() + MIR_EXTENSION));
    }


    /**
     *
     * Returns the miriam entry for this identifier class
     *
     * @param type
     * @return
     */
    public MIRIAMEntry getEntry(Class type) {
        Short mir = getMIR(type);
        if( mir != 0 ) {
            return miriam.getEntry(mir);
        }
        return null;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription(Class type) {

        String key = type.getSimpleName() + SHORT_DESCRIPTION;


        if( super.containsKey(key) ) {
            return super.getShortDescription(type);
        }

        Short mir = getMIR(type);

        if( mir != 0 ) {
            MIRIAMEntry entry = miriam.getEntry(mir);
            return entry.getResourceName();
        }

        return "";

    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription(Class type) {

        String name = type.getSimpleName();

        if( super.containsKey(name) ) {
            return super.getLongDescription(type);
        }

        Short mir = getMIR(type);

        if( mir != 0 ) {
            MIRIAMEntry entry = miriam.getEntry(mir);
            return entry.getDefinition();
        }

        return "";

    }


    public IdentifierDescription getMetaInfo(Class type) {
        return new IdentifierDescription(getEntry(type),
                                         getShortDescription(type),
                                         getLongDescription(type),
                                         getIndex(type));
    }


}

