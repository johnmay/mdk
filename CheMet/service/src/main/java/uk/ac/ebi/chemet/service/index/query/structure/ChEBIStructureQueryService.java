package uk.ac.ebi.chemet.service.index.query.structure;

import uk.ac.ebi.chemet.service.index.structure.ChEBIStructureIndex;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

import java.io.IOException;

/**
 * ChEBIStructureQueryService.java - 21.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBIStructureQueryService
            extends AbstractStructureQueryService<ChEBIIdentifier> {

    public ChEBIStructureQueryService() throws IOException {
        super(new ChEBIStructureIndex());
    }

    public ChEBIIdentifier getIdentifier(){
        return new ChEBIIdentifier();
    }



}
