package uk.ac.ebi.mdk.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;

import java.sql.SQLException;

/**
 * KEGGReactionServiceTest - 07.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGReactionServiceTest {

    private static final Logger LOGGER = Logger.getLogger(KEGGReactionServiceTest.class);

    @Test
    public void testService() throws SQLException {

        EntityFactory factory = DefaultEntityFactory.getInstance();
        KEGGReactionService service = new KEGGReactionService(factory);

        System.out.println(service.getReaction(new ECNumber("1.1.1.85")));

    }

}
