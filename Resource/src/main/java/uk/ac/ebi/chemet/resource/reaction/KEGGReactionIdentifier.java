package uk.ac.ebi.chemet.resource.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.base.AbstractIdentifier;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGReactionIdentifier extends AbstractIdentifier {

    private static final Logger LOGGER = Logger.getLogger(KEGGReactionIdentifier.class);

    public KEGGReactionIdentifier() {
        super();
    }

    public KEGGReactionIdentifier(String accession) {
        super(accession);
    }

    @Override
    public KEGGReactionIdentifier newInstance() {
        return new KEGGReactionIdentifier();
    }
}
