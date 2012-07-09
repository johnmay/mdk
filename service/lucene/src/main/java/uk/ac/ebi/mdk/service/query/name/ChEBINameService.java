package uk.ac.ebi.mdk.service.query.name;

import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.service.index.name.ChEBINameIndex;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;

import java.util.Collection;
import java.util.HashSet;

/**
 * ChEBINameService - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBINameService
        extends AbstractLuceneService<ChEBIIdentifier>
        implements IUPACNameService<ChEBIIdentifier>,
                   PreferredNameService<ChEBIIdentifier>,
                   SynonymService<ChEBIIdentifier>,
                   BrandNameService<ChEBIIdentifier>,
                   InternationalNonproprietaryNameService<ChEBIIdentifier>,
                   NameService<ChEBIIdentifier> {

    public ChEBINameService() {
        super(new ChEBINameIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchName(String name, boolean approximate) {
        // efficiency could be improved with multifield search
        identifiers.addAll(searchIUPACName(name, approximate));

       return getIdentifiers(construct(name, approximate, PREFERRED_NAME, SYNONYM, IUPAC, BRAND_NAME, INN));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getNames(ChEBIIdentifier identifier) {
        // use set as to avoid duplicates
        Collection<String> names = new HashSet<String>();

        names.add(getIUPACName(identifier));
        names.add(getPreferredName(identifier));
        names.add(getBrandName(identifier));
        names.add(getINN(identifier));
        names.addAll(getSynonyms(identifier));

        names.remove(""); // remove empty results

        return names;

    }

    /**
     * @inheritDoc
     */
    @Override
    public String getIUPACName(ChEBIIdentifier identifier) {
        return firstValue(identifier, IUPAC);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchIUPACName(String name, boolean approximate) {
        return getIdentifiers(construct(name, IUPAC, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchPreferredName(String name, boolean approximate) {
        return getIdentifiers(construct(name, PREFERRED_NAME, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getPreferredName(ChEBIIdentifier identifier) {
        return firstValue(identifier, PREFERRED_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchSynonyms(String name, boolean approximate) {
        return getIdentifiers(construct(name, SYNONYM, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getSynonyms(ChEBIIdentifier identifier) {
        return firstValues(identifier, SYNONYM);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getBrandName(ChEBIIdentifier identifier) {
        return firstValue(identifier, BRAND_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchBrandName(String name, boolean approximate) {
        return getIdentifiers(construct(name, BRAND_NAME, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getINN(ChEBIIdentifier identifier) {
        return firstValue(identifier, INN);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchINN(String name, boolean approximate) {
        return getIdentifiers(construct(name, INN, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public ChEBIIdentifier getIdentifier() {
        return new ChEBIIdentifier();
    }

}
