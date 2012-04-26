package uk.ac.ebi.chemet.service.loader;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.connection.AbstractDerbyConnection;

/**
 * AbstractDerbyLoader - 05.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractDerbyLoader extends AbstractResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(AbstractDerbyLoader.class);

    private AbstractDerbyConnection connection;

    public AbstractDerbyLoader(AbstractDerbyConnection connection){
        this.connection = connection;
    }

    public AbstractDerbyConnection getConnection(){
        return connection;
    }

    @Override
    public boolean canBackup() {
        return connection.isAvailable();
    }

    @Override
    public boolean canRevert() {
        return connection.canRevert();
    }

    @Override
    public void backup() {
        connection.backup();
    }

    @Override
    public void revert() {
        connection.revert();
    }

    @Override
    public void clean() {
        connection.clean();
    }

    @Override
    public String getName() {
        return connection.getName();
    }
}
