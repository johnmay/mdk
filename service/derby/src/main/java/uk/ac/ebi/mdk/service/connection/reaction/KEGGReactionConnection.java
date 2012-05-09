package uk.ac.ebi.mdk.service.connection.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.connection.AbstractDerbyConnection;

/**
 * KEGGReactionConnection - 05.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGReactionConnection extends AbstractDerbyConnection {

    private static final Logger LOGGER = Logger.getLogger(KEGGReactionConnection.class);

    public KEGGReactionConnection() {
        super("KEGG Reaction", "reaction/kegg");
    }

}
