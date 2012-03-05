package uk.ac.ebi.chemet.service.query.structure;

import uk.ac.ebi.chemet.service.index.structure.HMDBStructureIndex;
import uk.ac.ebi.resource.chemical.HMDBIdentifier;

import java.io.IOException;

/**
 * ${Name}.java - 21.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBStructureService
            extends AbstractStructureQueryService<HMDBIdentifier> {

    public HMDBStructureService()  {
        super(new HMDBStructureIndex());
    }

    public HMDBIdentifier getIdentifier(){
        return new HMDBIdentifier();
    }

}
