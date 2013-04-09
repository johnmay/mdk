package uk.ac.ebi.mdk.service.query.crossreference;

import uk.ac.ebi.mdk.domain.identifier.UniProtIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.identifier.SwissProtIdentifier;
import uk.ac.ebi.mdk.service.index.crossreference.UniProtCrossReferenceIndex;
import uk.ac.ebi.mdk.service.query.CrossReferenceService;

/**
 * UniProtCrossReferenceService - 29.02.2012 <br/>
 * <p/>
 * Provides access to cross-references to UniProt.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtCrossReferenceService
        extends AbstractCrossreferenceService<UniProtIdentifier>
        implements CrossReferenceService<UniProtIdentifier> {

    public UniProtCrossReferenceService() {
        super(new UniProtCrossReferenceIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public UniProtIdentifier getIdentifier() {
        return new UniProtIdentifier();
    }






}

