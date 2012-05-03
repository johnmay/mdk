package uk.ac.ebi.mdk.service.query.crossreference;

import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.chemet.resource.protein.SwissProtIdentifier;
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
        extends AbstractCrossreferenceService<SwissProtIdentifier>
        implements CrossReferenceService<SwissProtIdentifier> {

    public UniProtCrossReferenceService() {
        super(new UniProtCrossReferenceIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public SwissProtIdentifier getIdentifier() {
        return new SwissProtIdentifier();
    }


    public static void main(String[] args) {
        CrossReferenceService service = new UniProtCrossReferenceService();
        System.out.println(service.searchCrossReferences(new ECNumber("1.1.1.1")));
    }

}

