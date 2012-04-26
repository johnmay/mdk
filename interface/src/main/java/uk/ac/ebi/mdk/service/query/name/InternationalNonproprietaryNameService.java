package uk.ac.ebi.mdk.service.query.name;

import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * InternationalNonproprietaryNameService - 29.02.2012 <br/>
 * <p/>
 * Service provides search and lookup of International Nonpropietrary Names (INN).
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface InternationalNonproprietaryNameService<I extends Identifier>
        extends QueryService<I> {

    public static final Term INN = new Term("INN");
    
    /**
     * Access the International Nonpropietrary Name (INN) for a provided
     * identifier
     *
     * @param identifier query to look-up
     *
     * @return the INN or empty string
     */
    public String getINN(I identifier);


    /**
     * Collection of identifiers that match the International Nonpropietrary
     * Name (INN). If no matches are found an empty collection is returned
     *
     * @param name        the name to search for
     * @param approximate loosens search criteria
     *
     * @return collection of identifiers that have some degree of match with
     *         the provided name
     */
    public Collection<I> searchINN(String name, boolean approximate);

}
