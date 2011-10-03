package uk.ac.ebi.metabolomes.webservices;

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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
import org.openscience.cdk.io.iterator.IteratingPCCompoundXMLReader;
import org.xmlpull.v1.XmlPullParserException;
import uk.ac.ebi.chemet.ws.exceptions.UnfetchableEntry;
import uk.ac.ebi.chemet.ws.exceptions.MissingStructureException;
import uk.ac.ebi.interfaces.Identifier;


public class PubChemWebServiceConnection extends ChemicalDBWebService{

	/**
	 * @param args
	 */
	private PUGLocator locator;
	private PUGSoap soap;
        // TODO remove this hard reference.
	private String defaultPath = "/Users/pmoreno/structures";
	private InChIGeneratorFactory factory;
	private final Logger logger = Logger.getLogger( PubChemWebServiceConnection.class );
	private final String propertyCID = "PubChem CID";
	private final String propertyInchiKey = "InChIKey (Standard)";
	private final String serviceProviderName = "PubChem";

	public static void main(String[] args) {
		Properties p = System.getProperties();
//		System.out.println("os.name\t"+p.getProperty("os.name"));
//		System.out.println("os.arch\t"+p.getProperty("os.arch"));
//		p.setProperty("os.arch", "X86");
		PubChemWebServiceConnection c = new PubChemWebServiceConnection();
		int[] ids = new int[args.length];
		for (int i = 0; i < args.length; i++) {
			ids[i] = Integer.parseInt(args[i]);
		}
		HashMap<String, String> id2Inchi = c.getInChIs(ids);
		HashMap<String, String> id2InchiKey = c.getInChIKeys(ids);
		c.downloadStructureFiles(ids, c.defaultPath);
		ArrayList<IAtomContainer> mols = c.downloadMolsToCDKObject(ids);
		for(IAtomContainer m : mols) {
			System.out.println("CID of mol: "+m.getProperty("PubChem CID"));
		}
		for(String id : id2InchiKey.keySet()) {
			System.out.println(id+"\t"+id2InchiKey.get(id));
		}
	}

	public PubChemWebServiceConnection() {
		init();
	}

	private void init() {
		this.locator = new PUGLocator();
		try {
			this.soap = locator.getPUGSoap();
			// Generate factory - throws CDKException if native code does not load
			this.factory = InChIGeneratorFactory.getInstance();
			// Get InChIGenerator
		} catch (CDKException e) {
                    logger.error("Problem loading inchi generator", e);
		} catch (ServiceException e) {
                    logger.error("Error with connection to pubchem web service", e);
                }
	}

	public ArrayList<IAtomContainer> downloadMolsToCDKObject(String[] ids) {
		int[] idsInt = new int[ids.length];
		for(int i=0;i<ids.length;i++)
			idsInt[i] = Integer.parseInt(ids[i]);
		return this.downloadMolsToCDKObject(idsInt);
	}

	public ArrayList<IAtomContainer> downloadMolsToCDKObject(int[] ids) {
		String listKey;
		String downloadKey;
		try {
			listKey = soap.inputList(ids, PCIDType.eID_CID);
			downloadKey = soap.download(listKey, FormatType.eFormat_XML,
					CompressType.eCompress_None, false);
			// Wait for the download to be prepared
			StatusType status;
			while ((status = soap.getOperationStatus(downloadKey)) == StatusType.eStatus_Running
					|| status == StatusType.eStatus_Queued) {
				logger.info("Waiting for download to finish...");
				Thread.sleep(10000);
			}
			if (status == StatusType.eStatus_Success) {
				URL url = new URL(soap.getDownloadUrl(downloadKey));
				logger.info("Success! Download URL = " + url.toString());

				// get input stream from URL
				URLConnection fetch = url.openConnection();
				InputStream input = fetch.getInputStream();

			    ArrayList<IAtomContainer> v = new ArrayList<IAtomContainer>();
			    IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();

			    IteratingPCCompoundXMLReader readerXML = new IteratingPCCompoundXMLReader(input, builder);
			    while(readerXML.hasNext()) {
			            	//IChemObject n = ;
			            	//Map props = n.getProperties();//PubChem CID
			    			IAtomContainer mol = (IAtomContainer) readerXML.next();
			    			mol.setID((String) mol.getProperty(this.propertyCID));
			            	v.add(mol);
			    }

			    return v;

			}

		} catch (Exception e) {
			logger.error("Problems generating IAtomContainer arraylist from PubChem download", e);
		}
                return null;
	}

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
				logger.debug("Success! Download URL = " + url.toString());

				// get input stream from URL
				URLConnection fetch = url.openConnection();
				InputStream input = fetch.getInputStream();

				// open local file based on the URL file name
				String filename;
				if (dest == null)
					filename = "/tmp/"
							+ url.getFile().substring(
									url.getFile().lastIndexOf('/'));
				else
					filename = dest
							+ url.getFile().substring(
									url.getFile().lastIndexOf('/'));

				FileOutputStream output = new FileOutputStream(filename);
				logger.debug("Writing data to " + filename);

				// buffered read/write
				byte[] buffer = new byte[10000];
				int n;
				while ((n = input.read(buffer)) > 0)
					output.write(buffer, 0, n);

				output.close();
				return filename;
			}

		} catch (IOException e) {
                    logger.error("Problems for saving downloaded files", e);
		} catch (InterruptedException e) {
                    logger.error("Problems with thread waiting for download", e);
                }
                return null;
	}

	public boolean downloadStructureFiles(int[] ids, String path) {
		//String filename = this.downloadFile(ids, FormatType.eFormat_SDF, path
		//		+ "/pubchem/");
                // TODO fix this hard reference.
		String filename = this.downloadFile(ids, FormatType.eFormat_XML, path + "/pubchem/");
		IAtomContainer[] mols = this.loadMoleculesFromFile(filename);
		//HashMap<String, String> dic = this.getInChiDictionary(ids);
		try {
			for(IAtomContainer mol : mols) {
				InChIGenerator gen = factory.getInChIGenerator(mol);
				logger.debug("inChi is:"+gen.getInchi());
				logger.debug("inChiKey is:"+gen.getInchiKey());
				//mol.setID(dic.get(gen.getInchi()));
				String specFileName = this.defaultPath + "/pubchem/" + mol.getID() +".mol";
				this.writeMoleculeMolToFile(mol, specFileName);
			}
		} catch (CDKException e) {
			logger.error("Problems with cdk inchi factory", e);
			e.printStackTrace();
		}
		return true;
	}

	public HashMap<String, String> getInChIs(String[] ids) {
		int[] idsInt = new int[ids.length];
		for(int i=0;i<ids.length;i++)
			idsInt[i] = Integer.parseInt(ids[i]);
		return getInChIs(idsInt);
	}

	public HashMap<String, String> getInChIKeys(int[] ids) {
		HashMap<String, String> inchiKeyRes = new HashMap<String, String>();

		ArrayList<IAtomContainer> mols = this.downloadMolsToCDKObject(ids);
		for(IAtomContainer mol : mols) {
		    Map<Object, Object> props = mol.getProperties();
		    mol.setID((String) props.get(this.propertyCID));
		    inchiKeyRes.put(mol.getID(), (String) props.get(this.propertyInchiKey));
		}

		return inchiKeyRes;
	}

	public HashMap<String, String> getInChIKeys(String[] ids) {
		int[] idsInt = new int[ids.length];
		for(int i=0;i<ids.length;i++)
			idsInt[i] = Integer.parseInt(ids[i]);
		return getInChIKeys(idsInt);
	}

	public HashMap<String, String> getInChIs(int[] ids) {
		// TODO Auto-generated method stub
		HashMap<String, String> res = new HashMap<String, String>();
		String filename = this.downloadFile(ids, FormatType.eFormat_InChI, null);
		BufferedReader inchiFile;
		try {
			inchiFile = new BufferedReader(new FileReader(filename));
			String line = inchiFile.readLine();
			while (line != null) {
				String[] tokens = line.split("\t");
				if(tokens != null && tokens.length > 1)
					res.put(tokens[0], tokens[1]);
				line = inchiFile.readLine();
			}
			inchiFile.close();
			(new File(filename)).delete();
		} catch (IOException e) {
			logger.error("Problems reading file with inchis", e);
		}


		return res;
	}

	private HashMap<String, String> getInChiDictionary(int[] ids) {
		HashMap<String, String> orig = this.getInChIs(ids);
		HashMap<String, String> dic = new HashMap<String, String>();
		for(String key : orig.keySet()) {
			dic.put(orig.get(key),key);
		}
		return dic;
	}

	protected boolean writeMoleculeMolToFile(IAtomContainer m, String filename ) {
		FileWriter w = null;
		try {
			w = new FileWriter(new File(filename));
		} catch (IOException e1) {
                        logger.error("Could not opern file for writing mols", e1);
			return false;
		}
		try {
		    MDLV2000Writer mw = new MDLV2000Writer(w);
		    mw.write(m);
		    mw.close();
		} catch (CDKException e) {
		    logger.error("Problems when writing to the MDLWriter", e);
		    return false;
		} catch (IOException e) {
                    logger.error("Could not close file being written with mols", e);
                }
		return true;
	}

	// This method shouldn't really belong to this class
	protected IAtomContainer[] loadMoleculesFromFile(String filename) {
	    Vector<IAtomContainer> v = new Vector<IAtomContainer>();
	    IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
	    try {
	            File input = new File(filename);
	            //ReaderFactory readerFactory = new ReaderFactory();
	            //IChemObjectReader reader = readerFactory.createReader(new FileReader(input));
	            //MDLReader reader = (MDLReader) readerFactory.createReader(new FileReader(input));
	            //MDLReader reader = new MDLReader(new FileReader(input));
	            IteratingPCCompoundXMLReader readerXML = new IteratingPCCompoundXMLReader(new FileReader(input), builder);
	            while(readerXML.hasNext()) {
	            	//IChemObject n = ;
	            	//Map props = n.getProperties();//PubChem CID
	            	v.add((IAtomContainer) readerXML.next());
	            }

	            //IChemFile content = (IChemFile) reader.read(builder.newChemFile());
	            //IMoleculeSet content = (IMoleculeSet) readerXML.read(builder.newMoleculeSet());
//	            List c;
//
//	            if (content != null) {
//	            	//c = ChemFileManipulator.getAllAtomContainers(content);
//	            	c = (List) content.molecules();
//	            	for (j = 0; j < c.size(); j++) v.add((IAtomContainer) c.get(j));
//	            }
//	            System.out.println("hola");


	    } catch (IOException e) {
	        logger.error("Problems opening file for reading", e);
	    } catch (XmlPullParserException e) {
                logger.error("XML Parsing Troubles", e);
            }

	    // convert the vector to a simple array
	    IAtomContainer[] retValues = new IAtomContainer[v.size()];
	    for (int i = 0; i < v.size(); i++) {
	        retValues[i] = (IAtomContainer) v.get(i);
	        Map<Object, Object> props = retValues[i].getProperties();
	        retValues[i].setID((String) props.get(this.propertyCID));
	        logger.debug("ID: "+retValues[i].getID());
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
    public String getMDLString( String id ) throws UnfetchableEntry ,
                                                   MissingStructureException {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public String getName( String id ) throws UnfetchableEntry {
        throw new UnsupportedOperationException( "Not supported yet." );
    }


    @Override
    public Collection<String> getSynonyms(String accession) throws UnfetchableEntry {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public Set<Identifier> searchWithName(String name) {
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


}
