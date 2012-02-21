package uk.ac.ebi.io.service.index;

/**
 * AbstractLuceneIndex.java - 21.02.2012 <br/> Description...
 *
 * Provides name storage and isAvailable/lastModified implementations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractLuceneIndex
        implements LuceneIndex {

    private String name;
    
    public AbstractLuceneIndex(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    public long lastModified(){
        return getLocation().lastModified();
    }

    public boolean isAvailable(){
        return getLocation().exists();
    }

}
