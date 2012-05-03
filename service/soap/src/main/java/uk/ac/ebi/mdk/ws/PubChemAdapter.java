package uk.ac.ebi.mdk.ws;

import gov.nih.nlm.ncbi.pubchem.ws.*;
import gov.nih.nlm.ncbi.pubchem.ws.holders.DataBlobTypeHolder;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.mdk.service.query.QueryService;
import uk.ac.ebi.mdk.service.query.structure.StructureSearch;
import uk.ac.ebi.mdk.service.query.structure.StructureService;
import uk.ac.ebi.mdk.service.query.structure.SubstructureSearch;
import uk.ac.ebi.mdk.service.query.structure.SuperstructureSearch;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.StringHolder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author John May
 */
public class PubChemAdapter
        extends AbstractSoapService<PubChemCompoundIdentifier>
        implements QueryService<PubChemCompoundIdentifier>,
                   StructureService<PubChemCompoundIdentifier>,
                   StructureSearch<PubChemCompoundIdentifier>,
                   SubstructureSearch<PubChemCompoundIdentifier>,
                   SuperstructureSearch<PubChemCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(PubChemAdapter.class);

    private PUGLocator locator = new PUGLocator();
    private PUGSoap service;

    public PubChemAdapter() {
    }

    @Override
    public IAtomContainer getStructure(PubChemCompoundIdentifier identifier) {

        ArrayOfInt aoi = new ArrayOfInt(new int[]{Integer.parseInt(identifier.getAccession())});

        try {
            String listKey = service.inputList(aoi, PCIDType.eID_CID);
            StringHolder sh = new StringHolder();
            DataBlobTypeHolder blob = new DataBlobTypeHolder();
            service.download(listKey, FormatType.eFormat_SDF, CompressType.eCompress_None, false, 0, Boolean.TRUE, sh, blob);

            return mol2Structure(new String(blob.value.getData()));


        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return mol2Structure("");
    }

    @Override
    public Collection<PubChemCompoundIdentifier> structureSearch(IAtomContainer molecule, boolean approximate) {


        try {
            String structurekey = service.inputStructureBase64(structure2Mol(molecule).getBytes(), FormatType.eFormat_SDF);
            return collectIdentifiers(service.similaritySearch2D(structurekey,
                                                                 new SimilaritySearchOptions(),
                                                                 new LimitsType(2, getMaxResults(), null)));
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // return empty list
        return collectIdentifiers("");
    }

    @Override
    public Collection<PubChemCompoundIdentifier> substructureSearch(IAtomContainer molecule) {

        try {
            String structurekey = service.inputStructureBase64(structure2Mol(molecule).getBytes(), FormatType.eFormat_SDF);
            // need to configure options
            return collectIdentifiers(service.substructureSearch(structurekey,
                                                                 new StructureSearchOptions(),
                                                                 new LimitsType(2, getMaxResults(), null)));
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // return empty list
        return collectIdentifiers("");
    }

    @Override
    public Collection<PubChemCompoundIdentifier> superstructureSearch(IAtomContainer molecule) {
        try {
            String structurekey = service.inputStructureBase64(structure2Mol(molecule).getBytes(), FormatType.eFormat_SDF);
            // need to configure options
            return collectIdentifiers(service.superstructureSearch(structurekey,
                                                                   new StructureSearchOptions(),
                                                                   new LimitsType(2, getMaxResults(), null)));
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // return empty list
        return collectIdentifiers("");
    }

    public Collection<PubChemCompoundIdentifier> collectIdentifiers(String listKey) {

        Collection<PubChemCompoundIdentifier> cids = new ArrayList<PubChemCompoundIdentifier>();

        if (listKey == null || listKey.isEmpty())
            return cids;

        try {
            while (service.getOperationStatus(listKey) == StatusType.eStatus_Running) {
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException ex) {
                    LOGGER.info("Sleep was interrupted");
                }
            }

            ArrayOfInt aoi = service.getIDList(listKey, 0, service.getListItemsCount(listKey));
            for (int id : aoi.get_int()) {
                cids.add(getIdentifier(Integer.toString(id)));
            }
        } catch (RemoteException ex) {
            LOGGER.error("Remote exception occurred: " + ex.getMessage());
        }

        return cids;
    }

    @Override
    public PubChemCompoundIdentifier getIdentifier() {
        return new PubChemCompoundIdentifier();
    }

    @Override
    public boolean startup() {
        if (service != null) return true;
        try {
            service = locator.getPUGSoap();
        } catch (ServiceException ex) {
            LOGGER.error("Startup failed on SOAP Web Service: " + ex.getMessage());
        }
        return service != null;
    }

    public static void main(String[] args) throws ServiceException {
        KEGGCompoundAdapter keggws = new KEGGCompoundAdapter();
        PubChemAdapter adapter = new PubChemAdapter();
        //System.out.println(adapter.structureSearch(keggws.getStructure(new KEGGCompoundIdentifier("C00002")), true));
        System.out.println(adapter.getStructure(new PubChemCompoundIdentifier("73")).getAtomCount());
    }

}
