package uk.ac.ebi.chemet.service.index.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.index.KeywordNIOIndex;

/**
 * HMDBDataIndex - 27.02.2012 <br/>
 * <p/>
 * Provides a directory and analyzer for the chemical data i.e. charge and formula
 * for ChEBI. This is used in the {@see ChEBIDataLoader}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBIDataIndex extends KeywordNIOIndex {

    private static final Logger LOGGER = Logger.getLogger(ChEBIDataIndex.class);

    public ChEBIDataIndex(){
        super("ChEBI Data", "data/chebi");
    }

}
