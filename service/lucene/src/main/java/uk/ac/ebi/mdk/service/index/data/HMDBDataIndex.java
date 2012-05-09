package uk.ac.ebi.mdk.service.index.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;

/**
 * HMDBDataIndex - 27.02.2012 <br/>
 * <p/>
 * Provides a directory and analyzer for the chemical data i.e. charge and formula
 * for HMDB. This is used in the {@see HMDBMetabocardsLoader}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBDataIndex extends KeywordNIOIndex {

    private static final Logger LOGGER = Logger.getLogger(HMDBDataIndex.class);

    public HMDBDataIndex(){
        super("HMDB Data", "data/hmdb");
    }

}
