package uk.ac.ebi.chemet.service.query.structure;

import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.service.index.structure.KEGGCompoundStructureIndex;

/**
 * ${Name}.java - 21.02.2012 <br/> MetaInfo...
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
