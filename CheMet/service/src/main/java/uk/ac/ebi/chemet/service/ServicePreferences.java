package uk.ac.ebi.chemet.service;

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
