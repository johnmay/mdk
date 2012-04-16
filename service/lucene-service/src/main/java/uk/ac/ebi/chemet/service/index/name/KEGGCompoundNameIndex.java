package uk.ac.ebi.chemet.service.index.name;

import java.io.File;

/**
 * HMDBNameIndex - 21.02.2012 <br/> MetaInfo...
 *
 * Index is used in KEGGCompoundLoader
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundNameIndex extends DefaultNameIndex {
    public KEGGCompoundNameIndex(){
        super("KEGG Compound Names", "name/kegg-compound");
    }

    public KEGGCompoundNameIndex(File file){
        super("KEGG Compound Names", file);
    }

}
