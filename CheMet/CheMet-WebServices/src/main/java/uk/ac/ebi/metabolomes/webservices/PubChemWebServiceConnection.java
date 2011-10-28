package uk.ac.ebi.metabolomes.webservices;

import com.google.common.io.Files;
import gov.nih.nlm.ncbi.pubchem.CompressType;
import gov.nih.nlm.ncbi.pubchem.FormatType;
import gov.nih.nlm.ncbi.pubchem.PCIDType;
import gov.nih.nlm.ncbi.pubchem.PUGLocator;
import gov.nih.nlm.ncbi.pubchem.PUGSoap;
import gov.nih.nlm.ncbi.pubchem.StatusType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.iterator.IIteratingChemObjectReader;
import org.openscience.cdk.io.iterator.IteratingPCCompoundXMLReader;
import org.xmlpull.v1.XmlPullParserException;
import uk.ac.ebi.chemet.ws.exceptions.UnfetchableEntry;
import uk.ac.ebi.chemet.ws.exceptions.MissingStructureException;
import uk.ac.ebi.chemet.ws.exceptions.WebServiceException;
import uk.ac.ebi.interfaces.identifiers.Identifier;

public class PubChemWebServiceConnection extends ChemicalDBWebService {

    private final Logger LOGGER = Logger.getLogger(PubChemWebServiceConnection.class);
    private PUGLocator locator;
    private PUGSoap soap;
    private String defaultPath;
    private InChIGeneratorFactory factory;
    private final String propertyCID = "PubChem CID";
    private final String propertyInchiKey = "InChIKey (Standard)";
    private final String serviceProviderName = "PubChem";
    
    
    
    public PubChemWebServiceConnection() {
        init();
    }

    private void init() {
        this.locator = new PUGLocator();
        try {
            this.soap = locator.getPUGSoap();
        } catch (ServiceException e) {
            LOGGER.error("Error with connection to pubchem web service", e);
        }
    }

    public ArrayList<IAtomContainer> downloadMolsToCDKObject(String[] ids) {
        int[] idsInt = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idsInt[i] = Integer.parseInt(ids[i]);
        }
        return this.downloadMolsToCDKObject(idsInt);
    }

    public IIteratingChemObjectReader downloadMolsToIteratingCDKReader(String[] ids) throws Exception {
        int[] idsInt = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idsInt[i] = Integer.parseInt(ids[i]);
        }
        return this.downloadMolsToIteratingCDKReader(idsInt);
    }

    public IIteratingChemObjectReader downloadMolsToIteratingCDKReader(int[] ids) throws Exception {
        // TODO refine the thrown exception
        String listKey;
        String downloadKey;
        IteratingPCCompoundXMLReader readerXML = null;
        listKey = soap.inputList(ids, PCIDType.eID_CID);
        downloadKey = soap.download(listKey, FormatType.eFormat_XML,
                CompressType.eCompress_None, false);
        // Wait for the download to be prepared
        StatusType status;
        while ((status = soap.getOperationStatus(downloadKey)) == StatusType.eStatus_Running
                || status == StatusType.eStatus_Queued) {
            LOGGER.info("Waiting for download to finish...");
            Thread.sleep(10000);
        }
        if (status == StatusType.eStatus_Success) {
            URL url = new URL(soap.getDownloadUrl(downloadKey));
            LOGGER.info("Success! Download URL = " + url.toString());

            // get input stream from URL
            URLConnection fetch = url.openConnection();
            InputStream input = fetch.getInputStream();

            ArrayList<IAtomContainer> v = new ArrayList<IAtomContainer>();
            IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();

            readerXML = new IteratingPCCompoundXMLReader(input, builder);
        }

        return readerXML;
    }

    public ArrayList<IAtomContainer> downloadMolsToCDKObject(int[] ids) {
        // This method should be changed so that an exception is thrown
        // instead of returning null.
        
        ArrayList<IAtomContainer> v = new ArrayList<IAtomContainer>();
        try {
            IIteratingChemObjectReader readerXML = this.downloadMolsToIteratingCDKReader(ids);
            while (readerXML.hasNext()) {
                //IChemObject n = ;
                //Map props = n.getProperties();//PubChem CID
                IAtomContainer mol = (IAtomContainer) readerXML.next();
                mol.setID((String) mol.getProperty(this.propertyCID));
                v.add(mol);
            }
            return v;
        } catch (Exception e) {
            LOGGER.error("Problems generating IAtomContainer arraylist from PubChem download", e);
            //throw new WebServiceException("Problems generating IAtomContainer arraylist from PubChem download");
        }
        return null;
    }

    public Integer downloadMolsToIndividualMolFiles(String[] pchemCompIds, String destination) throws WebServiceException {
        int[] pubchemCIDs = new int[pchemCompIds.length];
        for (int i = 0; i < pchemCompIds.length; i++) {
            try {
                pubchemCIDs[i] = Integer.parseInt(pchemCompIds[i]);
            } catch(NumberFormatException e) {
                LOGGER.warn("One of the pchemCompIds was not an integer as required: "+pchemCompIds[i]);
                pubchemCIDs[i] = 0;
            }
        }
        return downloadMolsToIndividualMolFiles(pubchemCIDs, destination);
    }
    
    /**
     * Downloads the specified list of PubChem ids (of the specified type of id) as SDF to the destination defined. This
     * method only applies to compounds and substances.
     * 
     * @param pchemIds
     * @param idType
     * @param destination
     * @return
     * @throws WebServiceException 
     */
    public Integer downloadMolsToIndividualMolFiles(int[] pchemIds, PCIDType idType, String destination) throws WebServiceException {
        if(!(idType.equals(PCIDType.eID_CID) || idType.equals(PCIDType.eID_SID)))
            throw new WebServiceException("Only Substance or Compound can be accepted as the id type");
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.downloadFile(pchemIds, FormatType.eFormat_SDF, idType)));
        String line;
        StringBuffer buffer = new StringBuffer();
        String pchemID = null;
        boolean nextLineIsID = false;
        int filesWritten = 0;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.contains("$$$$") && pchemID != null) {
                    FileWriter writer = new FileWriter(new File(destination + File.separator + "CID_" + pchemID + ".mol"));
                    writer.write(buffer.toString());
                    writer.close();
                    buffer = new StringBuffer();
                    pchemID = null;
                    filesWritten++;
                } else {
                    buffer.append(line).append("\n");

                    if (nextLineIsID) {
                        pchemID = line.trim();
                        nextLineIsID = false;
                    }
                    if (idType.equals(PCIDType.eID_CID) && line.contains("> <PUBCHEM_COMPOUND_CID>")) {
                        nextLineIsID = true;
                    } else if(idType.equals(PCIDType.eID_SID) && line.contains("> <PUBCHEM_COMPOUND_SID>")) {
                        nextLineIsID = true;
                    }
                }
            }
            reader.close();
        } catch (IOException ex) {
            throw new WebServiceException("Several ids...", PubChemWebServiceConnection.class.getName(), "IOErrors: " + ex.getMessage());
        }
        return filesWritten;
    }
    
    /**
     * This is the default method, which will download the provided list of PubChem Compound IDs in the format
     * specified with FormatType enum.
     * 
     * @param pchemCompIds
     * @param destination
     * @return integer number of mols downloaded.
     * @throws WebServiceException 
     */
    public Integer downloadMolsToIndividualMolFiles(int[] pchemCompIds, String destination) throws WebServiceException {
        return this.downloadMolsToIndividualMolFiles(pchemCompIds, PCIDType.eID_CID, destination);
    }
    
    /**
     * This is the default method, which will download the provided list of PubChem Compound IDs in the format
     * specified with FormatType enum.
     * 
     * @param pchemCompIds array of strings containing the pubchem compounds to download.
     * @param type The type of download format (the enumeration contains options for ASN.1, XML, SDF, etc).
     * @return  the input stream to the download file.
     * @throws WebServiceException 
     */
    public InputStream downloadFile(String[] pchemCompIds, FormatType type) throws WebServiceException {
        return downloadFile(pchemCompIds, type, PCIDType.eID_CID);
    }

    /**
     * Downloads the specified set of ids, corresponding to the type specified of ID, in the specified format type.
     * 
     * @param pchemIds  array of ids to download.
     * @param formatType    type of format to download (SDF, XML, ASN.1, etc).
     * @param pubchemIdType  type of id (Compound CID, Substance SID, Assay AID, etc).
     * @return the input stream to the downloaded data.
     * @throws WebServiceException 
     */
    public InputStream downloadFile(String[] pchemIds, FormatType formatType, PCIDType pubchemIdType) throws WebServiceException {
        int[] pubchemCIDs = new int[pchemIds.length];
        for (int i = 0; i < pchemIds.length; i++) {
            try {
                pubchemCIDs[i] = Integer.parseInt(pchemIds[i]);
            } catch(NumberFormatException e) {
                LOGGER.warn("One of the pchemCompIds was not an integer as required: "+pchemIds[i]);
                pubchemCIDs[i] = 0;
            }
        }
        return downloadFile(pubchemCIDs, formatType, pubchemIdType);
    }
    
    public InputStream downloadFile(int[] ids, FormatType type) throws WebServiceException {
        return this.downloadFile(ids, type, PCIDType.eID_CID);
    }
    
    public InputStream downloadFile(int[] ids, FormatType type, PCIDType pubchemIdType) throws WebServiceException {
        String listKey;
        String downloadKey;
        InputStream result = null;
        try {
            listKey = soap.inputList(ids, pubchemIdType);
            downloadKey = soap.download(listKey, type,
                    CompressType.eCompress_None, false);
        } catch (RemoteException ex) {
            LOGGER.error("Problems in inputting list to PUG soap web service", ex);
            throw new WebServiceException("List of accessions...", PubChemWebServiceConnection.class.getName(), WebServiceException.CLIENT_EXCEPTION);
        }
        // Wait for the download to be prepared
        StatusType status;
        try {
            while ((status = soap.getOperationStatus(downloadKey)) == StatusType.eStatus_Running
                    || status == StatusType.eStatus_Queued) {
                System.out.println("Waiting for download to finish...");
                Thread.sleep(10000);
            }
        } catch (InterruptedException ex) {
            LOGGER.error("Problems with thread waiting for download", ex);
            throw new UnfetchableEntry("List of accessions...", PubChemWebServiceConnection.class.getName(),
                    "Problems with waiting for status " + ex.getMessage());
        } catch (RemoteException ex) {
            LOGGER.error("RemoteException while waiting for PUG soap web service", ex);
            throw new WebServiceException("List of accessions...", PubChemWebServiceConnection.class.getName(), WebServiceException.CLIENT_EXCEPTION);
        }
        try {
            if (status == StatusType.eStatus_Success) {
                URL url = new URL(soap.getDownloadUrl(downloadKey));
                LOGGER.debug("Success! Download URL = " + url.toString());

                // get input stream from URL
                URLConnection fetch = url.openConnection();
                result = fetch.getInputStream();
            }
        } catch (IOException ex) {
            LOGGER.error("Problems while downloading the URL", ex);
            throw new WebServiceException("List of accessions...", PubChemWebServiceConnection.class.getName(), WebServiceException.CLIENT_EXCEPTION);
        }

        return result;
    }

    // TODO this method should throw an exception instead of null.
    protected String downloadFile(int[] ids, FormatType type, String dest) {
        String listKey;
        String downloadKey;
        try {
            listKey = soap.inputList(ids, PCIDType.eID_CID);
            downloadKey = soap.download(listKey, type,
                    CompressType.eCompress_None, false);
            // Wait for the download to be prepared
            StatusType status;
            while ((status = soap.getOperationStatus(downloadKey)) == StatusType.eStatus_Running
                    || status == StatusType.eStatus_Queued) {
                System.out.println("Waiting for download to finish...");
                Thread.sleep(10000);
            }
            if (status == StatusType.eStatus_Success) {
                URL url = new URL(soap.getDownloadUrl(downloadKey));
                LOGGER.debug("Success! Download URL = " + url.toString());

                // get input stream from URL
                URLConnection fetch = url.openConnection();
                InputStream input = fetch.getInputStream();

                // open local file based on the URL file name
                String filename;
                if (dest == null) {
                    filename = "/tmp/"
                            + url.getFile().substring(
                            url.getFile().lastIndexOf('/'));
                } else {
                    filename = dest
                            + url.getFile().substring(
                            url.getFile().lastIndexOf('/'));
                }

                FileOutputStream output = new FileOutputStream(filename);
                LOGGER.debug("Writing data to " + filename);

                // buffered read/write
                byte[] buffer = new byte[10000];
                int n;
                while ((n = input.read(buffer)) > 0) {
                    output.write(buffer, 0, n);
                }

                output.close();
                return filename;
            }

        } catch (IOException e) {
            LOGGER.error("Problems for saving downloaded files", e);
        } catch (InterruptedException e) {
            LOGGER.error("Problems with thread waiting for download", e);
        }
        return null;
    }

    public boolean downloadSDFFile(int[] ids, String path) {
        String filename = this.downloadFile(ids, FormatType.eFormat_SDF, path);
        return filename != null;
    }

    public boolean downloadStructureFiles(int[] ids, String path) {
        //String filename = this.downloadFile(ids, FormatType.eFormat_SDF, path
        //		+ "/pubchem/");
        // TODO fix this hard reference.
        String filename = this.downloadFile(ids, FormatType.eFormat_XML, path + "/pubchem/");
        IAtomContainer[] mols = this.loadMoleculesFromFile(filename);
        //HashMap<String, String> dic = this.getInChiDictionary(ids);
        try {
            for (IAtomContainer mol : mols) {
                InChIGenerator gen = factory.getInChIGenerator(mol);
                LOGGER.debug("inChi is:" + gen.getInchi());
                LOGGER.debug("inChiKey is:" + gen.getInchiKey());
                //mol.setID(dic.get(gen.getInchi()));
                String specFileName = this.defaultPath + "/pubchem/" + mol.getID() + ".mol";
                this.writeMoleculeMolToFile(mol, specFileName);
            }
        } catch (CDKException e) {
            LOGGER.error("Problems with cdk inchi factory", e);
            e.printStackTrace();
        }
        return true;
    }

    public HashMap<String, String> getInChIs(String[] ids) {
        int[] idsInt = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idsInt[i] = Integer.parseInt(ids[i]);
        }
        return getInChIs(idsInt);
    }

    public HashMap<String, String> getInChIKeys(int[] ids) {
        HashMap<String, String> inchiKeyRes = new HashMap<String, String>();

        ArrayList<IAtomContainer> mols = this.downloadMolsToCDKObject(ids);
        for (IAtomContainer mol : mols) {
            Map<Object, Object> props = mol.getProperties();
            mol.setID((String) props.get(this.propertyCID));
            inchiKeyRes.put(mol.getID(), (String) props.get(this.propertyInchiKey));
        }

        return inchiKeyRes;
    }

    public HashMap<String, String> getInChIKeys(String[] ids) {
        int[] idsInt = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            idsInt[i] = Integer.parseInt(ids[i]);
        }
        return getInChIKeys(idsInt);
    }

    public HashMap<String, String> getInChIs(int[] ids) {
        // TODO Auto-generated method stub
        HashMap<String, String> res = new HashMap<String, String>();
        String filename = this.downloadFile(ids, FormatType.eFormat_InChI, Files.createTempDir().getAbsolutePath());
        BufferedReader inchiFile;
        try {
            inchiFile = new BufferedReader(new FileReader(filename));
            String line = inchiFile.readLine();
            while (line != null) {
                String[] tokens = line.split("\t");
                if (tokens != null && tokens.length > 1) {
                    res.put(tokens[0], tokens[1]);
                }
                line = inchiFile.readLine();
            }
            inchiFile.close();
            (new File(filename)).delete();
        } catch (IOException e) {
            LOGGER.error("Problems reading file with inchis", e);
        }


        return res;
    }

    private HashMap<String, String> getInChiDictionary(int[] ids) {
        HashMap<String, String> orig = this.getInChIs(ids);
        HashMap<String, String> dic = new HashMap<String, String>();
        for (String key : orig.keySet()) {
            dic.put(orig.get(key), key);
        }
        return dic;
    }

    protected boolean writeMoleculeMolToFile(IAtomContainer m, String filename) {
        FileWriter w = null;
        try {
            w = new FileWriter(new File(filename));
        } catch (IOException e1) {
            LOGGER.error("Could not opern file for writing mols", e1);
            return false;
        }
        try {
            MDLV2000Writer mw = new MDLV2000Writer(w);
            mw.write(m);
            mw.close();
        } catch (CDKException e) {
            LOGGER.error("Problems when writing to the MDLWriter", e);
            return false;
        } catch (IOException e) {
            LOGGER.error("Could not close file being written with mols", e);
        }
        return true;
    }

    // This method shouldn't really belong to this class
    protected IAtomContainer[] loadMoleculesFromFile(String filename) {
        Vector<IAtomContainer> v = new Vector<IAtomContainer>();
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        try {
            File input = new File(filename);
            IteratingPCCompoundXMLReader readerXML = new IteratingPCCompoundXMLReader(new FileReader(input), builder);
            while (readerXML.hasNext()) {
                //IChemObject n = ;
                //Map props = n.getProperties();//PubChem CID
                v.add((IAtomContainer) readerXML.next());
            }
        } catch (IOException e) {
            LOGGER.error("Problems opening file for reading", e);
        } catch (XmlPullParserException e) {
            LOGGER.error("XML Parsing Troubles", e);
        }

        // convert the vector to a simple array
        IAtomContainer[] retValues = new IAtomContainer[v.size()];
        for (int i = 0; i < v.size(); i++) {
            retValues[i] = (IAtomContainer) v.get(i);
            Map<Object, Object> props = retValues[i].getProperties();
            retValues[i].setID((String) props.get(this.propertyCID));
            LOGGER.debug("ID: " + retValues[i].getID());
        }
        return retValues;
    }

    public String getServiceProviderName() {
        return this.serviceProviderName;
    }

    @Override
    public HashMap<String, String> searchByInChI(String inchi) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMDLString(String id) throws UnfetchableEntry,
            MissingStructureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName(String id) throws UnfetchableEntry {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<String> getSynonyms(String accession) throws UnfetchableEntry {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, String> search(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName(Identifier identifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<String> getSynonyms(Identifier identifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Identifier getIdentifier() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMaxResults(int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<? extends Identifier> searchWithName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
