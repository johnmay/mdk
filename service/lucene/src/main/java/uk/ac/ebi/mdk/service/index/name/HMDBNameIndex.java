package uk.ac.ebi.mdk.service.index.name;

/**
 * HMDBNameIndex - 21.02.2012 <br/> MetaInfo...
 *
 * Index is used in HMDLMetabocardsLoader and HMDBNameService
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBNameIndex extends DefaultNameIndex {
    public HMDBNameIndex(){
        super("HMDB Names", "name/hmdb");
    }
}
