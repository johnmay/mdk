package uk.ac.ebi.mdk.service.query.structure;

import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.mdk.service.index.structure.ChEBIStructureIndex;

/**
 * ChEBIStructureService.java - 21.02.2012 <br/> MetaInfo...
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
