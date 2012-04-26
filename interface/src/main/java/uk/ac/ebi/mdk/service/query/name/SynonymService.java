package uk.ac.ebi.mdk.service.query.name;

import org.apache.lucene.index.Term;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * SynonymService.java - 21.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface SynonymService<I extends Identifier>
        extends QueryService<I> {
    
    public static Term SYNONYM = new Term("Synonym");

    public Collection<I> searchSynonyms(String name, boolean approximate);

    public Collection<String> getSynonyms(I identifier);

    
}
