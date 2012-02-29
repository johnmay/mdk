package uk.ac.ebi.chemet.service.query.structure;

import uk.ac.ebi.chemet.service.index.structure.KEGGCompoundStructureIndex;
import uk.ac.ebi.interfaces.services.StructureQueryService;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;

import java.io.IOException;

/**
 * ${Name}.java - 21.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundStructureService
            extends AbstractStructureQueryService<KEGGCompoundIdentifier> {

    public KEGGCompoundStructureService() {
        super(new KEGGCompoundStructureIndex());
    }

    public KEGGCompoundIdentifier getIdentifier(){
        return new KEGGCompoundIdentifier();
    }

}