package uk.ac.ebi.mdk.service.query.structure;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.LIPIDMapsIdentifier;
import uk.ac.ebi.mdk.service.index.structure.LipidMapsStructureIndex;

/**
 * @author John May
 */
public class LipidMapsStructureService extends AbstractStructureQueryService<LIPIDMapsIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(LipidMapsStructureService.class);

    public LipidMapsStructureService() {
        super(new LipidMapsStructureIndex());
    }

    @Override
    public LIPIDMapsIdentifier getIdentifier() {
        return new LIPIDMapsIdentifier();
    }
}
