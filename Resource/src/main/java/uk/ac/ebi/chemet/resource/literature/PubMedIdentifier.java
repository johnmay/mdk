package uk.ac.ebi.chemet.resource.literature;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.base.AbstractIdentifier;
import uk.ac.ebi.resource.MIR;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@MIR(15)
public class PubMedIdentifier extends AbstractIdentifier {

    private static final Logger LOGGER = Logger.getLogger(PubMedIdentifier.class);

    @Override
    public PubMedIdentifier newInstance() {
        return new PubMedIdentifier();
    }
}
