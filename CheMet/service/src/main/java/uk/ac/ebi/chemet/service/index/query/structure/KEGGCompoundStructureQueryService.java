package uk.ac.ebi.chemet.service.index.query.structure;

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
public class KEGGCompoundStructureQueryService
            extends AbstractStructureQueryService<KEGGCompoundIdentifier> {

    public KEGGCompoundStructureQueryService() throws IOException {
        super(new KEGGCompoundStructureIndex());
    }

    public KEGGCompoundIdentifier getIdentifier(){
        return new KEGGCompoundIdentifier();
    }

    public static void main(String[] args) throws IOException {
        StructureQueryService service = new KEGGCompoundStructureQueryService();
        long start = System.currentTimeMillis();
        for(int i = 0 ; i < 2000; i++){
            service.getMol(new KEGGCompoundIdentifier(String.format("C%05d", i)));
        }
        long end = System.currentTimeMillis();

        System.out.println("Average " + (end - start)/2000f + " ms");
    }

}
