package uk.ac.ebi.chemet.service.connection;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.chemet.service.BasicServiceLocation;
import uk.ac.ebi.chemet.service.ServicePreferences;
import uk.ac.ebi.service.ServiceLocation;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * AbstractDerbyConnection - 05.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractDerbyConnection extends BasicServiceLocation implements ServiceLocation {

    private static final Logger LOGGER = Logger.getLogger(AbstractDerbyConnection.class);

    public static final String driver   = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String protocol = "jdbc:derby:";

    private Connection connection;

    private Properties properties = new Properties();

    public AbstractDerbyConnection(String name, File path) {
        super(name, path);
    }

    /**
     * Create a derby connection relative to the SERVICE_ROOT preference
     *
     * @param name
     * @param relativePath
     */
    public AbstractDerbyConnection(String name, String relativePath) {
        super(name, relativePath);
    }

    public Connection getConnection() throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection(protocol + getLocation().getAbsolutePath() + ";create=true", properties);
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null)
            connection.close();
    }

}
