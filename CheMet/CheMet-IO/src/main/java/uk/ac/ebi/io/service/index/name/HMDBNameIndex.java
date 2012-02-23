package uk.ac.ebi.io.service.index.name;

import uk.ac.ebi.io.service.index.KeywordNIOIndex;

/**
 * HMDBNameIndex - 21.02.2012 <br/> Description...
 *
 * Index is used in HMDLMetabocardsLoader and HMDBNameService
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBNameIndex extends KeywordNIOIndex{
    public HMDBNameIndex(){
        super("HMDB Names", "name/hmdb");
    }
}
