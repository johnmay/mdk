package uk.ac.ebi.metabolomes.webservices;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.ServiceException;

import keggapi.KEGGLocator;
import keggapi.KEGGPortType;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.ws.exceptions.UnfetchableEntry;
import uk.ac.ebi.chemet.ws.exceptions.MissingStructureException;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;

public class KeggCompoundWebServiceConnection extends ChemicalDBWebService {

    private KEGGPortType serv;
    private static String serviceProviderName = "BioCyc";
    private Logger logger = Logger.getLogger(KeggCompoundWebServiceConnection.class.getName());
    private final int bgetMaxQueries = 100;
    private int maxResults;

    @Override
    public void setMaxResults(int max) {
        maxResults = max;
    }

    private enum KeggDBs {

        COMPOUND("cpd"), DRUG("dr"), GLYCAN("gl");
        private final String prefix;

        KeggDBs(String prefix) {
            this.prefix = prefix;
        }

        String getDBPrefixForBget() {
            return this.prefix;
        }
    }

    public static void main(String args[]) {
        //KeggCompoundWebServiceConnection keggConn = new KeggCompoundWebServiceConnection();
        ChemicalDBWebService keggConn = new KeggCompoundWebServiceConnection();
        if (args != null && args.length > 0) {
            ArrayList<IAtomContainer> mols = keggConn.downloadMolsToCDKObject(args);
            for (IAtomContainer mol : mols) {
                System.out.println("ID: " + mol.getID());
            }
        }
        String[] compIds = {"C07688", "C07689", "D00608", "D00609"};
        try {
            String res = ((KeggCompoundWebServiceConnection) keggConn).executeBget(compIds);
            System.out.println(res);
            HashMap<String, String> testres = ((KeggCompoundWebServiceConnection) keggConn).resolveNewIDsForObsoleteEntrys(res);
            for (String key : testres.keySet()) {
                System.out.println(key + "\t" + testres.get(key));
                ArrayList<String> name = new ArrayList<String>();
                name.add(testres.get(key));
                ArrayList<IAtomContainer> mols = keggConn.downloadMolsToCDKObject(name);
                if (mols.size() > 0 && mols.get(0) != null) {
                    System.out.println("We have struct for: " + mols.get(0).getID());
                }
            }

        } catch (RemoteException ex) {
        }


        ChemicalDBWebService webservice = new KeggCompoundWebServiceConnection();
        System.out.println("Synonyms:" + ((KeggCompoundWebServiceConnection) webservice).getSynonyms("C00002"));
        System.out.println(webservice.searchWithName("Adenosine 5'-triphosphate"));


    }

    public KeggCompoundWebServiceConnection() {
        this.init();
    }

    public HashMap<String, String> mapAnyObsoleteID2NewID(ArrayList<String> obsIDs) {
        String[] cpds = obsIDs.toArray(new String[obsIDs.size()]);
        try {
            return this.resolveNewIDsForObsoleteEntrys(this.executeBget(cpds));
        } catch (RemoteException ex) {
            logger.error("Could not retrieve compounds ids through bget: ", ex);
            return null;
        }
    }

    private String executeBget(String[] cpds) throws RemoteException {
        String totalAns = "";
        logger.debug("Amount of cpds: " + cpds.length);
        for (int i = 0; i < cpds.length; i += this.bgetMaxQueries) {
            String bgetQuery = "";
            String[] subCPDS = new String[Math.min(this.bgetMaxQueries, cpds.length - i)];
            int lengthOfThisIteration = Math.min(this.bgetMaxQueries, cpds.length - i);
            // j is the pointer for cpds, j-i is the pointer for subCPDS
            for (int j = i; j < i + subCPDS.length; j++) {
                subCPDS[j - i] = cpds[j];
            }
            for (String cpd : subCPDS) {
                bgetQuery += this.resolveDBPrefix(cpd) + ":" + cpd + " ";
            }
            logger.debug("Answer before bget: " + totalAns.length());
            totalAns += serv.bget(bgetQuery);
            logger.debug("Answer after bget: " + totalAns.length());
        }
        return totalAns;
    }

    /**
     * Returns the KEGG String entry (bget) for the given identifier
     * @param identifier
     * @return
     */
    public String getEntry(KEGGCompoundIdentifier identifier) {
        try {
            String accession = identifier.getAccession();
            String dbAccession = this.resolveDBPrefix(accession) + ":" + accession;

            String result = serv.bget(dbAccession);

            if (result.isEmpty()) {
                throw new UnfetchableEntry(identifier.getAccession(), getServiceProviderName(), "Empty entry");
            }

            return result;


        } catch (RemoteException ex) {
            throw new UnfetchableEntry(identifier.getAccession(), getServiceProviderName(), ex.getMessage());
        }


    }

    /**
     * @deprecated use {@see searchWithName(String)} as this method allows use of the ChemicalDBWebService interfaces
     */
    @Deprecated
    public String[] findCompoundByName(String name) {
        try {
            return serv.search_compounds_by_name(name);
        } catch (RemoteException ex) {
            logger.error("Could not retrieve compounds by name", ex);
            return null;
        }
    }

    // TODO: move a general KEGGIdentifier class/interface
    private String resolveDBPrefix(String id) {
        if (id.toLowerCase().startsWith("c")) {
            return KeggDBs.COMPOUND.getDBPrefixForBget();
        }
        if (id.toLowerCase().startsWith("d")) {
            return KeggDBs.DRUG.getDBPrefixForBget();
        }
        if (id.toLowerCase().startsWith("g")) {
            return KeggDBs.GLYCAN.getDBPrefixForBget();
        }
        return null;
    }


    /*
     * This function should be able to handle this output from kegg
     *
     * ENTRY       C07689            Obsolete  Compound
     * NAME        Transferred to D00609
     * ///
     *
     * */
    private HashMap<String, String> resolveNewIDsForObsoleteEntrys(String keggBgetResponse) {
        String[] lines = keggBgetResponse.split("\n");
        String obsId = null;
        String newId = null;
        HashMap<String, String> res = new HashMap<String, String>();
        for (String line : lines) {
            if (line.startsWith("ENTRY") && line.contains("Obsolete")) {
                String tokens[] = line.split("\\s+");
                if (tokens.length > 2) {
                    obsId = tokens[1];
                }
            } else if (line.startsWith("NAME") && line.contains("Transferred to")) {
                String tokens[] = line.split("\\s+");
                if (tokens.length > 2) {
                    newId = tokens[tokens.length - 1];
                }
            } else if (line.startsWith("///")) {
                if (obsId != null && newId != null) {
                    res.put(obsId, newId);
                }
                obsId = null;
                newId = null;
            }
        }
        return res;
    }

    private void init() {
        KEGGLocator locator = new KEGGLocator();
        try {
            serv = locator.getKEGGPort();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     *
     * @param accession
     * @return
     * @throws UnfetchableEntry
     * @throws MissingStructureException
     */
    public IAtomContainer getAtomContainer(String accession)
            throws UnfetchableEntry, MissingStructureException {
        try {
            String mldString = getMDLString(accession);
            MDLV2000Reader reader = new MDLV2000Reader(new StringReader(mldString));
            IAtomContainer mol = (IAtomContainer) reader.read(builder.newInstance(IMolecule.class));

            if (mol == null) {
                throw new UnfetchableEntry();
            }

            return mol;

        } catch (CDKException ex) {
            throw new UnfetchableEntry();
        }

    }
    private IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();

    @Override
    public ArrayList<IAtomContainer> downloadMolsToCDKObject(String[] ids) {
        // TODO Auto-generated method stub
        ArrayList<IAtomContainer> mols = new ArrayList<IAtomContainer>();
        for (String id : ids) {
            try {
                String molTxt = this.downloadMolToString(id);
                MDLV2000Reader reader = new MDLV2000Reader(new StringReader(molTxt));
                IAtomContainer mol = (IAtomContainer) reader.read(builder.newInstance(
                        IMolecule.class));
                if (mol == null) {
                    logger.warn("Null CDK object for Kegg ID:" + id + ", number of ids: "
                            + ids.length);
                    continue;
                }
                mol.setID(id);
                mols.add(mol);
            } catch (CDKException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                logger.error("Could not generate CDK molecule for id " + id, e);
            }
        }
        return mols;
    }

    public String downloadMolToString(String id) {
        String molTxt = null;
        try {
            molTxt = serv.bget("-f m " + this.resolveDBPrefix(id) + ":" + id);
        } catch (RemoteException e) {
            logger.error("Could not get id " + id + " from KeggWebService", e);
        }
        return molTxt;
    }

    public String[] downloadCompoundIDsForECNumber(String ecnumber) {
        String[] cpdIds = null;
        try {
            cpdIds = serv.get_compounds_by_enzyme("ec:" + ecnumber);
        } catch (RemoteException ex) {
            logger.error("Could not retrieve compounds for ec number " + ecnumber, ex);
        }
        return cpdIds;
    }

    public String[] downloadCompoundIDsForReaction(String rxnID) {
        String[] cpdIds = null;
        try {
            cpdIds = serv.get_compounds_by_reaction("rn:" + rxnID);
        } catch (RemoteException ex) {
            logger.error("Could not retrieve compounds for reaction " + rxnID, ex);
        }
        return cpdIds;
    }

    public Boolean[] areCompoundsGeneric(String[] cpds) {
        if (cpds == null) {
            return null;
        }
        Boolean[] ans = new Boolean[cpds.length];
        try {
            String bgetAns = executeBget(cpds);
        } catch (RemoteException ex) {
            logger.error("Error in bget query", ex);
        }
        return null;
    }

    @Override
    public String getServiceProviderName() {
        // TODO Auto-generated method stub
        return this.serviceProviderName;
    }

    @Override
    public HashMap<String, String> searchByInChI(String inchi) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getMDLString(String id) throws UnfetchableEntry,
            MissingStructureException {
        try {
            String mdlString = serv.bget("-f m " + this.resolveDBPrefix(id) + ":" + id);
            if (mdlString == null) {
                throw new MissingStructureException(id);
            }
            return mdlString;
        } catch (RemoteException e) {
            throw new UnfetchableEntry();
        }

    }

    /**
     * Returns the main name of a KEGG entry. Note this method is taxing as the whole entry is downloaded and the parsed.
     * It is recommended to use this with a cache
     * @param accession
     * @return
     * @throws UnfetchableEntry
     */
    @Override
    public String getName(String accession) throws UnfetchableEntry {
        return getName(new KEGGCompoundIdentifier(accession));
    }

    public String getName(KEGGCompoundIdentifier identifier) throws UnfetchableEntry {

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
                    return name;
                }
            }
        }

        return "";
    }

    @Override
    public Collection<String> getSynonyms(String accession) throws UnfetchableEntry {
        return getSynonyms(new KEGGCompoundIdentifier(accession));
    }

    public Collection<String> getSynonyms(KEGGCompoundIdentifier identifier) throws UnfetchableEntry {

        Collection<String> names = new HashSet();

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
    public String getName(Identifier identifier) {
        return getName((KEGGCompoundIdentifier) identifier);
    }

    @Override
    public Collection<String> getSynonyms(Identifier identifier) {
        return getSynonyms((KEGGCompoundIdentifier) identifier);
    }

    /**
     * Returns a set of KEGG Compound identifiers. Note this does not search glycan or drug databases
     */
    @Override
    public Set<KEGGCompoundIdentifier> searchWithName(String name) {
        Set<KEGGCompoundIdentifier> identifiers = new HashSet();
        try {
            List<String> ids = Arrays.asList(serv.search_compounds_by_name(name));
            if(ids.size() > maxResults){
                ids = ids.subList(0, maxResults);
            }
            for (String id : ids) {
                identifiers.add(new KEGGCompoundIdentifier(id.substring(4)));
            }
        } catch (RemoteException ex) {
            throw new UnfetchableEntry(name, getServiceProviderName(), ex.getMessage());
        }
        return identifiers;
    }

    @Override
    public Map<String, String> search(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public KEGGCompoundIdentifier getIdentifier() {
        return (KEGGCompoundIdentifier) DefaultIdentifierFactory.getInstance().ofClass(KEGGCompoundIdentifier.class);
    }
}
