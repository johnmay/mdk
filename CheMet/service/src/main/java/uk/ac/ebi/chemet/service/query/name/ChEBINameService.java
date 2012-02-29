package uk.ac.ebi.chemet.service.query.name;

import uk.ac.ebi.chemet.service.index.name.ChEBINameIndex;
import uk.ac.ebi.chemet.service.query.AbstractQueryService;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.service.query.name.*;

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
        extends AbstractQueryService<ChEBIIdentifier>
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
        // use set as to avoid duplicates
        Collection<ChEBIIdentifier> identifiers = new HashSet<ChEBIIdentifier>();

        // efficiency could be improved with multifield search
        identifiers.addAll(searchIUPACName(name, approximate));
        identifiers.addAll(searchPreferredName(name, approximate));
        identifiers.addAll(searchBrandName(name, approximate));
        identifiers.addAll(searchINN(name, approximate));
        identifiers.addAll(searchSynonyms(name, approximate));

        return identifiers;

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

        return names;

    }

    /**
     * @inheritDoc
     */
    @Override
    public String getIUPACName(ChEBIIdentifier identifier) {
        return getFirstValue(identifier, IUPAC);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchIUPACName(String name, boolean approximate) {
        return getIdentifiers(create(name, IUPAC, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchPreferredName(String name, boolean approximate) {
        return getIdentifiers(create(name, PREFERRED_NAME, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getPreferredName(ChEBIIdentifier identifier) {
        return getFirstValue(identifier, PREFERRED_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchSynonyms(String name, boolean approximate) {
        return getIdentifiers(create(name, SYNONYM, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getSynonyms(ChEBIIdentifier identifier) {
        return getValues(create(identifier.getAccession(), IDENTIFIER), PREFERRED_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getBrandName(ChEBIIdentifier identifier) {
        return getFirstValue(identifier, BRAND_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchBrandName(String name, boolean approximate) {
        return getIdentifiers(create(name, BRAND_NAME, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getINN(ChEBIIdentifier identifier) {
        return getFirstValue(identifier, INN);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchINN(String name, boolean approximate) {
        return getIdentifiers(create(name, INN, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public ChEBIIdentifier getIdentifier() {
        return new ChEBIIdentifier();
    }
}
