package uk.ac.ebi.mdk.ws;

import com.chemspider.CompoundInfo;
import com.chemspider.SearchLocator;
import com.chemspider.SearchSoap;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import uk.ac.ebi.chemet.resource.chemical.ChemSpiderIdentifier;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * @author John May
 */
public class ChemSpiderAdapter
        extends AbstractSoapService<ChemSpiderIdentifier>
        implements StructureService<ChemSpiderIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(ChemSpiderAdapter.class);

    private static final IChemObjectBuilder BUILDER = DefaultChemObjectBuilder.getInstance();

    private SearchSoap service;
    private SearchLocator locator = new SearchLocator();

    @Override
    public ChemSpiderIdentifier getIdentifier() {
        return new ChemSpiderIdentifier();
    }

    @Override
    public IAtomContainer getStructure(ChemSpiderIdentifier identifier) {

        try {
            CompoundInfo info = service.getCompoundInfo(Integer.parseInt(identifier.getAccession()), "b7048eec-07c1-496e-9266-ae18d34736b1");

            InChIGeneratorFactory inchiFactory = InChIGeneratorFactory.getInstance();
            InChIToStructure inchi2structure = inchiFactory.getInChIToStructure(info.getInChI(), BUILDER);

            return inchi2structure.getAtomContainer();


        } catch (CDKException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return BUILDER.newInstance(IAtomContainer.class);

    }

    @Override
    public boolean startup() {
        if (service != null) return true;
        try {
            service = locator.getSearchSoap();
        } catch (ServiceException ex) {
            LOGGER.error("Startup failed on SOAP Web Service: " + ex.getMessage());
        }
        return service != null;
    }

}
