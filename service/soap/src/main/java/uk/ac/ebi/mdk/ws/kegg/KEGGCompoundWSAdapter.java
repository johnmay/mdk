package uk.ac.ebi.mdk.ws.kegg;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import javax.xml.rpc.ServiceException;

/**
 * @author John May
 */
public class KEGGCompoundWSAdapter extends KEGGWSAdapter<KEGGCompoundIdentifier>
        implements StructureService<KEGGCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundWSAdapter.class);

    public KEGGCompoundWSAdapter() throws ServiceException {
        super();
    }

    @Override
    public KEGGCompoundIdentifier getIdentifier() {
        return new KEGGCompoundIdentifier();
    }
}
