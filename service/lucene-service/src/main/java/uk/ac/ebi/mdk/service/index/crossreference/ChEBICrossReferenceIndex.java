package uk.ac.ebi.mdk.service.index.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;

/**
 * ChEBICrossReferenceIndex - 29.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBICrossReferenceIndex extends KeywordNIOIndex {

    private static final Logger LOGGER = Logger.getLogger(ChEBICrossReferenceIndex.class);

    public ChEBICrossReferenceIndex(){
        super("ChEBI Cross-references", "xref/chebi");
    }

}
