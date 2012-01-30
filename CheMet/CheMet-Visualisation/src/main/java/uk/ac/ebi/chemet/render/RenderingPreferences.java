/**
 * RenderingPreferences.java
 *
 * 2012.01.29
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

package uk.ac.ebi.chemet.render;


import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.AbstractPreferenceLoader;

/**
 *
 *          RenderingPreferences 2012.01.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Singleton description
 *
 */
public class RenderingPreferences extends AbstractPreferenceLoader {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( RenderingPreferences.class );

    private RenderingPreferences() {
    }

    public static RenderingPreferences getInstance() {
        return RenderingPreferencesHolder.INSTANCE;
    }

    private static class RenderingPreferencesHolder {
        private static final RenderingPreferences INSTANCE = new RenderingPreferences();
    }


    @Override
    public InputStream getConfig() throws IOException {
        return getClass().getResourceAsStream("preferences.properties");
    }
    
    
    
    
 }
