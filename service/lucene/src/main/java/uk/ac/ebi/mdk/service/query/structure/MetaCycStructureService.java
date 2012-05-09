package uk.ac.ebi.mdk.service.query.structure;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.service.index.structure.MetaCycStructureIndex;

/**
 * @author John May
 */
public class MetaCycStructureService extends AbstractStructureQueryService<BioCycChemicalIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(MetaCycStructureService.class);

    public MetaCycStructureService() {
        super(new MetaCycStructureIndex());
    }

    @Override
    public BioCycChemicalIdentifier getIdentifier() {
        return new BioCycChemicalIdentifier();
    }
}
