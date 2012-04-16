package uk.ac.ebi.service.query.name;

import org.apache.lucene.index.Term;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.service.query.QueryService;

import java.util.Collection;

/**
 * BrandNameService - 29.02.2012 <br/>
 * <p/>
 * Interface describes a service that will provided look-up and search of brand names.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface BrandNameService<I extends Identifier>
        extends QueryService<I> {

    public static final Term BRAND_NAME = new Term("BrandName");

    /**
     * Look-up the brand name for a provided identifier.
     *
     * @param identifier the identifier to look-up the brand
     *                   name for
     *
     * @return brand name or empty string
     */
    public String getBrandName(I identifier);


    /**
     * Search for identifiers in the data-set who's brand name matches
     * the provided  name.
     *
     * @param name        a brand name
     * @param approximate lossen search criteria
     *
     * @return collection of identifiers that match the brand name (empty collection if none)
     */
    public Collection<I> searchBrandName(String name, boolean approximate);

}
