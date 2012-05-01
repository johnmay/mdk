package uk.ac.ebi.mdk.ws.kegg;

import jp.genome.ws.kegg.KEGGLocator;
import jp.genome.ws.kegg.KEGGPortType;
import jp.genome.ws.kegg.StructureAlignment;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KEGGDrugIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KeggGlycanIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.name.NameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.name.SynonymService;
import uk.ac.ebi.mdk.service.query.structure.StructureSearch;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import javax.xml.rpc.ServiceException;
import java.io.StringReader;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * @author John May
 */
public abstract class KEGGWSAdapter<I extends Identifier>
        implements StructureService<I>,
                   StructureSearch<I>,
                   PreferredNameService<I>,
                   NameService<I>,
                   SynonymService<I> {

    private static final Logger LOGGER = Logger.getLogger(KEGGWSAdapter.class);

    private KEGGPortType service;

    private MDLV2000Reader reader = new MDLV2000Reader();
    private MDLV2000Writer writer = new MDLV2000Writer();

    private static final IChemObjectBuilder BUILDER = DefaultChemObjectBuilder.getInstance();

    private Map<Class<? extends Identifier>, String> prefixes = new HashMap<Class<? extends Identifier>, String>(5);

    private Integer maxResults = 100;
    private Float similarity = 0.5f;


    public KEGGWSAdapter() throws ServiceException {

        prefixes.put(KEGGCompoundIdentifier.class, "cpd");

        service = new KEGGLocator().getKEGGPort();
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

                I identifier = getIdentifier();
                identifier.setAccession(id.substring(4));
                identifies.add(identifier);

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
                String mol = service.bget(query);

                reader.setReader(new StringReader(mol));

                return reader.read(BUILDER.newInstance(IAtomContainer.class));

            } catch (RemoteException ex) {
                LOGGER.error("RemoteException: " + ex.getMessage());
            } catch (CDKException ex) {
                LOGGER.error("CDKException: " + ex.getMessage());
            }

            // return an empty atom container
            return BUILDER.newInstance(IAtomContainer.class);

        }
        throw new InvalidParameterException("Identifier class is not supported");

    }


    public Collection<I> structureSearch(IAtomContainer molecule, boolean approximate) {

        // hook to sub-class identifier type to see which db we want to search
        I identifier = getIdentifier();
        Collection<I> identifiers = new ArrayList<I>();

        if (identifier instanceof KEGGCompoundIdentifier) {

            try {
                StringWriter sw = new StringWriter();
                writer.setWriter(sw);
                writer.writeMolecule(molecule);

                System.out.println(sw.toString());

                for (StructureAlignment alignment : service.search_compounds_by_subcomp(sw.toString(),
                                                                                        0,            // 0= start at first result
                                                                                        maxResults)) {

                    // if approximate is set the alignment score must equal 1.0f....
                    // if not set the score must be greate then the similarity
                    if (!approximate && alignment.getScore() > similarity
                            || alignment.getScore() == 1.0f) {
                        I target = getIdentifier();
                        target.setAccession(alignment.getTarget_id());
                        identifiers.add(target);
                    }
                }

            } catch (CDKException ex) {
                LOGGER.error("Could not create molfile from structure");
                ex.printStackTrace();
            } catch (Exception ex) {
                LOGGER.error("Could not create molfile from structure");
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
     *
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


    /**
     * Currently only used in structure search
     *
     * @param maxResults number of results
     */
    @Override
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    /**
     * Currently only used in structure search
     *
     * @param similarity new minimum similarity
     */
    @Override
    public void setMinSimilarity(float similarity) {
        this.similarity = similarity;
    }

    @Override
    public boolean isAvailable() {
        // whether the service connected?
        return service != null;
    }
}
