package uk.ac.ebi.mdk.ws;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.service.query.name.IUPACNameService;
import uk.ac.ebi.mdk.service.query.name.NameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.name.SynonymService;
import uk.ac.ebi.mdk.service.query.structure.StructureService;
import uk.ac.ebi.ws.chebi.*;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author John May
 */
public class ChEBIAdapter
        extends AbstractSoapService<ChEBIIdentifier>
        implements SynonymService<ChEBIIdentifier>,
                   NameService<ChEBIIdentifier>,
                   IUPACNameService<ChEBIIdentifier>,
                   PreferredNameService<ChEBIIdentifier>,
                   StructureService<ChEBIIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(ChEBIAdapter.class);

    private ChebiWebServiceServiceLocator locator = new ChebiWebServiceServiceLocator();
    private ChebiWebServicePortType service;

    public ChEBIAdapter() {
    }

    @Override
    public String getIUPACName(ChEBIIdentifier identifier) {

        Collection<String> names = new ArrayList<String>();

        try {
            for (DataItem item : service.getCompleteEntity(identifier.getAccession()).getIupacNames()) {
                names.add(item.getData());
            }
        } catch (RemoteException ex) {
            LOGGER.error("ChEBI Remote Exception: " + ex.getMessage());
        }

        return names.isEmpty() ? "" : names.iterator().next(); // first only

    }

    @Override
    public Collection<ChEBIIdentifier> searchIUPACName(String name, boolean approximate) {
        return searchBy(name, SearchCategory.value6);
    }

    @Override
    public Collection<ChEBIIdentifier> searchPreferredName(String name, boolean approximate) {
        return searchBy(name, SearchCategory.value3);
    }

    @Override
    public String getPreferredName(ChEBIIdentifier identifier) {
        try {
            Entity entity = service.getCompleteEntity(identifier.getAccession());
            return entity.getChebiAsciiName();
        } catch (RemoteException ex) {
            LOGGER.error("ChEBI Remote Exception: " + ex.getMessage());
        }
        return "";
    }

    @Override
    public Collection<ChEBIIdentifier> searchName(String name, boolean approximate) {
        return searchBy(name, SearchCategory.value5);
    }

    @Override
    public Collection<String> getNames(ChEBIIdentifier identifier) {
        Collection<String> names = new ArrayList<String>();
        try {
            names.add(service.getCompleteEntity(identifier.getAccession()).getChebiAsciiName());

            for (DataItem item : service.getCompleteEntity(identifier.getAccession()).getSynonyms()) {
                names.add(item.getData());
            }
            for (DataItem item : service.getCompleteEntity(identifier.getAccession()).getIupacNames()) {
                names.add(item.getData());
            }
        } catch (RemoteException ex) {
            LOGGER.error("ChEBI Remote Exception: " + ex.getMessage());
        }
        return names;
    }

    @Override
    public Collection<ChEBIIdentifier> searchSynonyms(String name, boolean approximate) {
        return searchBy(name, SearchCategory.value5);
    }

    @Override
    public Collection<String> getSynonyms(ChEBIIdentifier identifier) {

        Collection<String> synonyms = new ArrayList<String>();
        try {
            for (DataItem item : service.getCompleteEntity(identifier.getAccession()).getSynonyms()) {
                synonyms.add(item.getData());
            }
        } catch (RemoteException ex) {
            LOGGER.error("ChEBI Remote Exception: " + ex.getMessage());
        }
        return synonyms;
    }

    private Collection<ChEBIIdentifier> searchBy(String name, SearchCategory a) {

        Collection<ChEBIIdentifier> identifiers = new ArrayList<ChEBIIdentifier>();

        try {
            LiteEntityList ents = service.getLiteEntity(name, a, getMaxResults(), StarsCategory.value3);
            for (LiteEntity entity : ents.getListElement()) {
                identifiers.add(getIdentifier(entity.getChebiId()));
            }

        } catch (RemoteException ex) {
            LOGGER.error("Problems getting lite entity from ChEBI Web service", ex);
        }

        return identifiers;

    }

    @Override
    public ChEBIIdentifier getIdentifier() {
        return new ChEBIIdentifier();
    }

    @Override
    public boolean startup() {
        if (service != null) return true;
        try {
            service = locator.getChebiWebServicePort();
        } catch (ServiceException ex) {
            LOGGER.error("Startup failed on SOAP Web Service: " + ex.getMessage());
        }
        return service != null;
    }

    @Override
    public IAtomContainer getStructure(ChEBIIdentifier identifier) {

        Entity entity = null;
        try {

            entity = service.getCompleteEntity(identifier.getAccession());

            for (StructureDataItem item : entity.getChemicalStructures()) {
                if (item.getType().equals("mol")) {
                    return mol2Structure(item.getStructure());
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return mol2Structure(null);

    }
}
