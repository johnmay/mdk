package uk.ac.ebi.mdk.service.loader.structure;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.index.structure.MetaCycStructureIndex;

/**
 * @author John May
 */
public class MetaCycStructureLoader extends BioCycStructureLoader {

    private static final Logger LOGGER = Logger.getLogger(MetaCycStructureLoader.class);

    public MetaCycStructureLoader() {
        super(new MetaCycStructureIndex());
    }

}
