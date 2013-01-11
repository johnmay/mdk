package uk.ac.ebi.mdk.service.query;

import jp.genome.ws.kegg.KEGGLocator;
import jp.genome.ws.kegg.KEGGPortType;
import jp.genome.ws.kegg.StructureAlignment;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGDrugIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KeggGlycanIdentifier;
import uk.ac.ebi.mdk.service.query.name.NameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.name.SynonymService;
import uk.ac.ebi.mdk.service.query.structure.StructureSearch;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John May
 * @deprecated KEGG SOAP API has been retired
 */
@Deprecated
public abstract class KEGGAdapter<I extends Identifier>
        extends AbstractSoapService<I>
        implements StructureService<I>,
                   StructureSearch<I>,
                   PreferredNameService<I>,
                   NameService<I>,
                   SynonymService<I> {

    private static final Logger LOGGER = Logger.getLogger(KEGGAdapter.class);

    private KEGGLocator locator = new KEGGLocator();
    private KEGGPortType service;

    private Map<Class<? extends Identifier>, String> prefixes = new HashMap<Class<? extends Identifier>, String>(5);

    public KEGGAdapter() {
        prefixes.put(KEGGCompoundIdentifier.class, "cpd");
    }

    @Override
    public Collection<String> getNames(I identifier) {
        return getSynonyms(identifier);
    }

    @Override
    public Collection<I> searchPreferredName(String name, boolean approximate) {
        return searchName(name, approximate);
    }

    @Override
    public Collection<I> searchSynonyms(String name, boolean approximate) {
        return searchName(name, approximate);
    }

    @Override
    public Collection<String> getSynonyms(I identifier) {
        Collection<String> names = new ArrayList<String>();

        String entry = getEntry(identifier);

        String[] lines = entry.split("\n");
        String type = "";
        for (String line : lines) {
            if (line.length() > 12) {
                String newType = line.substring(0, 12).trim();
                type = newType.isEmpty() ? type : newType;
                if (type.trim().equals("NAME")) {
                    String name = line.substring(12, line.length()).trim();
                    if (name.endsWith(";")) {
                        name = name.substring(0, name.length() - 1);
                    }
                    names.add(name);
                }
            }
        }

        return names;
    }

    @Override
    public Collection<I> searchName(String name, boolean approximate) {

        LOGGER.info("Approximate match is not used here!");

        Collection<I> identifies = new ArrayList<I>();

        try {
            for (String id : search(name)) {
                identifies.add(getIdentifier(id.substring(4)));
            }
        } catch (RemoteException ex) {
            LOGGER.error("Remote exception: " + ex.getMessage());
        }

        return identifies;
    }

    public String[] search(String name) throws RemoteException {
        I identifier = getIdentifier();
        if (identifier instanceof KEGGCompoundIdentifier) {
            return service.search_compounds_by_name(name);
        } else if (identifier instanceof KeggGlycanIdentifier) {
            return service.search_glycans_by_name(name);
        } else if (identifier instanceof KEGGDrugIdentifier) {
            return service.search_drugs_by_name(name);
        }
        throw new InvalidParameterException("Not a valid KEGG ligand identifier");
    }

    @Override
    public String getPreferredName(I identifier) {
        Collection<String> names = getNames(identifier);
        return names.isEmpty() ? "" : names.iterator().next();
    }

    @Override
    public IAtomContainer getStructure(I identifier) {

        if (prefixes.containsKey(identifier.getClass())) {

            try {

                String prefix = prefixes.get(identifier.getClass());
                String query = "-f m " + prefix + ":" + identifier.getAccession();

                return mol2Structure(service.bget(query));

            } catch (RemoteException ex) {
                LOGGER.error("RemoteException: " + ex.getMessage());
            }

            // return an empty atom container
            return mol2Structure(null);

        }
        throw new InvalidParameterException("Identifier class is not supported");

    }


    public Collection<I> structureSearch(IAtomContainer molecule, boolean approximate) {

        // hook to sub-class identifier type to see which db we want to search
        I identifier = getIdentifier();
        Collection<I> identifiers = new ArrayList<I>();

        if (identifier instanceof KEGGCompoundIdentifier) {

            try {

                for (StructureAlignment alignment : service.search_compounds_by_subcomp(structure2Mol(molecule),
                                                                                        0,            // 0= start at first result
                                                                                        getMaxResults())) {

                    // if approximate is set the alignment score must equal 1.0f....
                    // if not set the score must be greate then the similarity
                    if (!approximate && alignment.getScore() > getMinSimilarity()
                            || alignment.getScore() == 1.0f) {
                        identifiers.add(getIdentifier(alignment.getTarget_id().substring(4)));
                    }
                }

            } catch (RemoteException ex) {
                LOGGER.error("Could not search structure");
                ex.printStackTrace();
            }

            return identifiers;

        }

        throw new UnsupportedOperationException("Not supported yet");

    }

    /**
     * Returns the KEGG String entry (bget) for the given identifier
     *
     * @param identifier
     * @return
     */
    public String getEntry(I identifier) {
        try {
            String accession = identifier.getAccession();
            String query = prefixes.get(identifier.getClass()) + ":" + accession;

            String result = service.bget(query);

            return result == null ? "" : result;


        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

        return "";

    }


    @Override
    public boolean startup() {
        if (service != null) return true;
        try {
            service = locator.getKEGGPort();
        } catch (ServiceException ex) {
            LOGGER.error("Startup failed on SOAP Web Service: " + ex.getMessage());
        }
        return service != null;
    }
}
