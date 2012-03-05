package uk.ac.ebi.chemet.service.index.name;

/**
 * HMDBNameIndex - 21.02.2012 <br/> Description...
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
}
