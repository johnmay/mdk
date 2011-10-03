package uk.ac.ebi.metabolomes.webservices;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.chemet.ws.exceptions.UnfetchableEntry;
import uk.ac.ebi.chemet.ws.exceptions.MissingStructureException;
import uk.ac.ebi.interfaces.Identifier;

/**
 * This abstract class encompasses web services that connect to chemical databases.
 * Among the functions of connecting to a Chemical database should retrieve molecules
 * in CDK format and search. Other objects such as InChIs or such could be retrieved,
 * but they should not be calculated here. InChIs and other properties should be calculated
 * by separate classes just relying on the CDK object.
 * The CDK objects returned should provide with the IDs of the molecules in their source databases.
 *
 * @author  Pablo Moreno
 */
public abstract class ChemicalDBWebService {

    protected Logger logger;

    public boolean downloadStructureFiles(String[] ids, String path) throws IOException {
        ArrayList<IAtomContainer> mols = this.downloadMolsToCDKObject(ids);
        MDLV2000Writer out;
        for (IAtomContainer mol : mols) {
            out = new MDLV2000Writer(new FileOutputStream(path + mol.getID() + ".mol"));
            try {
                out.write(mol);
            } catch (CDKException e) {
                logger.error("Could not open mol file for writing with MDLWriter", e);
            }
            out.close();
        }
        return true;
    }

    // TODO This abstract class shouldn't calculate inchis, only retrieve them if it corresponds.
    public HashMap<String, String> getInChIKeys(String[] ids) {
        HashMap<String, InChIGenerator> generators = this.getInChIGeneratorsLoaded(ids);
        HashMap<String, String> inchiKeys = new HashMap<String, String>();
        for (String key : generators.keySet()) {
            try {
                inchiKeys.put(key, generators.get(key).getInchiKey());
            } catch (CDKException e) {
                logger.error(
                        "Could not load inchi generator, or an error was produced when trying to generate and inchi",
                        e);
            }
        }
        return inchiKeys;
    }

    public HashMap<String, InChIGenerator> getInChIGeneratorsLoaded(String[] ids) {
        ArrayList<IAtomContainer> mols = this.downloadMolsToCDKObject(ids);
        HashMap<String, InChIGenerator> inchiGen = new HashMap<String, InChIGenerator>();
        try {
            InChIGeneratorFactory factory = InChIGeneratorFactory.getInstance();
            for (IAtomContainer mol : mols) {
                //System.out.println("Provider:"+this.getServiceProviderName()+"\t"+mol.getID());
                if (!this.checkMoleculeForInChI(mol)) {
                    continue; // avoid calculations if molecule doesn't pass
                }
                InChIGenerator gen = factory.getInChIGenerator(mol);
                inchiGen.put(mol.getID(), gen);
            }
        } catch (CDKException e) {
            logger.error(
                    "Could not load inchi generator, or an error was produced when trying to generate and inchi",
                    e);
        }
        return inchiGen;
    }

    public HashMap<String, InChIGenerator> getInChIGeneratorsLoaded(ArrayList<String> ids) {
        return this.getInChIGeneratorsLoaded(this.arrayList2StringArray(ids));
    }

    public HashMap<String, String> getInChIKeysLocalCalc(ArrayList<String> ids) throws Exception {
        return getInChIKeys(ids);
    }

    public HashMap<String, String> getInChIsLocalCalc(ArrayList<String> ids) throws Exception {
        return getInChIs(ids);
    }

    public abstract HashMap<String, String> searchByInChI(String inchi);

    public HashMap<String, String> getInChIKeys(ArrayList<String> ids) throws Exception {
        return getInChIKeys(this.arrayList2StringArray(ids));
    }

    public ArrayList<IAtomContainer> downloadMolsToCDKObject(ArrayList<String> ids) {
        return this.downloadMolsToCDKObject(this.arrayList2StringArray(ids));
    }

    public abstract ArrayList<IAtomContainer> downloadMolsToCDKObject(String[] ids);

    public HashMap<String, String> getInChIs(String[] ids) throws Exception {
        HashMap<String, InChIGenerator> generators = this.getInChIGeneratorsLoaded(ids);
        HashMap<String, String> inchi = new HashMap<String, String>();
        for (String key : generators.keySet()) {
            inchi.put(key, generators.get(key).getInchi());
        }
        return inchi;
    }

    public HashMap<String, String> getInChIs(ArrayList<String> ids) throws Exception {
        String[] idsStr = new String[ids.size()];
        for (int i = 0; i < idsStr.length; i++) // this really shouldn't be here
        {
            idsStr[i] = ids.get(i).replaceFirst("CHEBI:", "");
        }
        return getInChIs(idsStr);
    }

    private String[] arrayList2StringArray(ArrayList<String> ids) {
        String[] idsArray = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            idsArray[i] = ids.get(i);
        }
        return idsArray;
    }

    public abstract String getServiceProviderName();

    private boolean checkMoleculeForInChI(IAtomContainer mol) {
        // Has pseudoatoms?
        if (mol.getAtomCount() == 0) {
            return false;
        }
        for (IAtom a : mol.atoms()) {
            String className = a.getClass().getName();
            if (className.equalsIgnoreCase("org.openscience.cdk.PseudoAtom")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a set of identifiers found by searching with the given name. Note classes
     * implementing this method should call search on all possible names/synonyms
     * @param name
     * @return
     */
    public abstract Set<? extends Identifier> searchWithName(String name);

    /**
     * Returns a map of identifiers and compound names found by searching with the given query
     * @param query
     * @return
     */
    public abstract Map<String, String> search(String query);

    /**
     *
     * Abstract method that should return the MDL/MOL/SDF string for the given id
     *
     * @param id The identifier of the entity to be retrieved
     *
     * @return String of the MDL file (the version cannot be garanteed)
     *
     * @throws UnfetchableEntry    Thrown if a record cannot be retrieved for the given
     *                                   identifier
     * @throws MissingStructureException Thrown if a record was retrieved but a structure
     *                                   could not be provided
     *
     */
    public abstract String getMDLString(String accession) throws UnfetchableEntry,
            MissingStructureException;

    /**
     * Returns the main database name for the given accession
     * @param accession The accession to fetch
     * @throws UnfetchableEntry RuntimeException thrown if a record cannot be found or the
     *         client fails
     * @return
     */
    public abstract String getName(String accession);

    /**
     * Returns the main database name for the given accession
     * @param Identfier The identifier of the name to fetch
     * @throws UnfetchableEntry RuntimeException thrown if a record cannot be found or the
     *         client fails
     * @return
     */
    public abstract String getName(Identifier identifier);

    /**
     * Returns all possible synonyms for the given accession
     * @param accession
     * @throws UnfetchableEntry RuntimeException thrown if a record cannot be found or the
     *          client fails
     * @return
     */
    public abstract Collection<String> getSynonyms(String accession);

    /**
     * Returns all possible synonyms for the given accession
     * @param identfier object
     * @throws UnfetchableEntry RuntimeException thrown if a record cannot be found or the
     *          client fails
     * @return
     */
    public abstract Collection<String> getSynonyms(Identifier identifier);
}
