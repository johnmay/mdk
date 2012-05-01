package uk.ac.ebi.mdk.ws;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import javax.xml.rpc.ServiceException;

/**
 * @author John May
 */
public class KEGGCompoundAdapter extends KEGGAdapter<KEGGCompoundIdentifier>
        implements StructureService<KEGGCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundAdapter.class);

    public KEGGCompoundAdapter() throws ServiceException {
        super();
    }

    @Override
    public KEGGCompoundIdentifier getIdentifier() {
        return new KEGGCompoundIdentifier();
    }

    public static void main(String[] args) throws Exception {
        KEGGCompoundAdapter adapter = new KEGGCompoundAdapter();
        System.out.println(adapter.searchName("ADP", true));
    }

}
