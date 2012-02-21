package uk.ac.ebi.io.service.index.name;

import uk.ac.ebi.io.service.index.KeywordNIOIndex;

/**
 * ${Name}.java - 21.02.2012 <br/> Description...
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
