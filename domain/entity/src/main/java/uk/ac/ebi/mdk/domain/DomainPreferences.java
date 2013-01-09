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

package uk.ac.ebi.mdk.domain;

import uk.ac.ebi.caf.utility.preference.AbstractPreferenceLoader;

import java.io.IOException;
import java.io.InputStream;


/**
 *
 *          DomainPreferences 2012.01.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Singleton description
 *
 */
public class DomainPreferences extends AbstractPreferenceLoader {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DomainPreferences.class);


    private DomainPreferences() {
    }


    public static DomainPreferences getInstance() {
        return CorePreferencesHolder.INSTANCE;
    }


    @Override
    public InputStream getConfig() throws IOException {
        return getClass().getResource("preferences.properties").openStream();
    }


    private static class CorePreferencesHolder {

        private static final DomainPreferences INSTANCE = new DomainPreferences();
    }
}
