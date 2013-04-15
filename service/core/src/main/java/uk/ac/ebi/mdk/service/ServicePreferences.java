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

package uk.ac.ebi.mdk.service;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.AbstractPreferenceLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * ServicePreferences - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ServicePreferences extends AbstractPreferenceLoader {

    private static final Logger LOGGER = Logger.getLogger(ServicePreferences.class);

    private ServicePreferences() {

    }

    private static class ServicePreferencesHolder {
        private static final ServicePreferences INSTANCE = new ServicePreferences();
    }

    @Override
    public InputStream getConfig() throws IOException {
        return getClass().getResourceAsStream("preferences.properties");
    }

    public static ServicePreferences getInstance() {
        return ServicePreferencesHolder.INSTANCE;
    }
}
