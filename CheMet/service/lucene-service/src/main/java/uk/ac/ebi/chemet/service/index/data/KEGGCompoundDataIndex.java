package uk.ac.ebi.chemet.service.index.data;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.index.KeywordNIOIndex;

/**
 * KEGGCompoundDataIndex - 28.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundDataIndex extends KeywordNIOIndex {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundDataIndex.class);

    public KEGGCompoundDataIndex(){
        super("KEGG Compound Data", "data/kegg-compound");
    }

}
