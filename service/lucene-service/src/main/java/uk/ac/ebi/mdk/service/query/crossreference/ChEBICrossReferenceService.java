package uk.ac.ebi.mdk.service.query.crossreference;

import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.mdk.service.index.crossreference.ChEBICrossReferenceIndex;
import uk.ac.ebi.mdk.service.query.CrossReferenceService;

/**
 * ChEBICrossReferenceService - 29.02.2012 <br/>
 * <p/>
 * Provides access to cross-references in ChEBI. Note that the current implementation only
 * works on primary identifiers
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBICrossReferenceService
        extends AbstractCrossreferenceService<ChEBIIdentifier>
        implements CrossReferenceService<ChEBIIdentifier> {

    public ChEBICrossReferenceService() {
        super(new ChEBICrossReferenceIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public ChEBIIdentifier getIdentifier() {
        return new ChEBIIdentifier();
    }

}

