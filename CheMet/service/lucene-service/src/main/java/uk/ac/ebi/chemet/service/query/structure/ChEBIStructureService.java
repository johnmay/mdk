package uk.ac.ebi.chemet.service.query.structure;

import uk.ac.ebi.chemet.service.index.structure.ChEBIStructureIndex;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

/**
 * ChEBIStructureService.java - 21.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBIStructureService
            extends AbstractStructureQueryService<ChEBIIdentifier> {

    public ChEBIStructureService() {
        super(new ChEBIStructureIndex());
    }

    public ChEBIIdentifier getIdentifier(){
        return new ChEBIIdentifier();
    }



}
