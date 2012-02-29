package uk.ac.ebi.chemet.service.index.name;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.index.KeywordNIOIndex;

/**
 * ChEBINameIndex - 28.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBINameIndex extends KeywordNIOIndex {

    private static final Logger LOGGER = Logger.getLogger(ChEBINameIndex.class);

    public ChEBINameIndex() {
        super("ChEBI Names", "name/chebi");
    }

}
