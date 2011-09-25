
package uk.ac.ebi.metabolomes.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.ReaderFactory;
import uk.ac.ebi.chemet.ws.exceptions.UnfetchableEntry;
import uk.ac.ebi.chemet.ws.exceptions.MissingStructureException;


public class BioCycFolderService extends ChemicalDBWebService {

    /**
     * @param args
     */
    private final String serviceProviderName = "BioCyc";
    private String pathToFiles;
    private String suffix = ".mol";
    private HashMap<String, String> dbid2num;


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String path = "/Users/pmoreno/structures/biocyc";
        ArrayList<String> bioCycNames = new ArrayList<String>();
        for( String arg : args ) {
            bioCycNames.add(arg);
        }
        ChemicalDBWebService bioCycServ = new BioCycFolderService(path);
        HashMap<String, String> inchi = new HashMap<String, String>();
        HashMap<String, String> inchiKey = new HashMap<String, String>();
        try {
            inchi = bioCycServ.getInChIs(bioCycNames);
            inchiKey = bioCycServ.getInChIKeys(bioCycNames);
        } catch( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for( String key : inchi.keySet() ) {
            System.out.println(key + "\t" + inchi.get(key));
            System.out.println(key + "\t" + inchiKey.get(key));
        }

    }


    public BioCycFolderService(String folderPath) {
        this.setPathToFile(folderPath);
        this.setDbId2NumForMols();
        this.logger = Logger.getLogger(BioCycFolderService.class);
    }


    private void setDbId2NumForMols() {
        try {
            BufferedReader aliases = new BufferedReader(new FileReader(this.pathToFiles +
                                                                       "compName2Id.txt"));
            this.dbid2num = new HashMap<String, String>();
            String line = aliases.readLine();
            while( line != null ) {
                String tokens[] = line.split(" ");
                this.dbid2num.put(tokens[0], tokens[1]);
                line = aliases.readLine();
            }
        } catch( FileNotFoundException ex ) {
            logger.error("Could not load file with dbid 2 numbers", ex);
        } catch( IOException ex ) {
            logger.error("Could not read file for dbid 2 numbers", ex);
        }
    }


    public void setPathToFile(String path) {
        this.pathToFiles = path;
    }


    @Override
    public ArrayList<IAtomContainer> downloadMolsToCDKObject(String[] ids) {
        // TODO Auto-generated method stub
        ArrayList<IAtomContainer> mols = new ArrayList<IAtomContainer>();
        ReaderFactory readerFactory = new ReaderFactory();
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        for( String id : ids ) {
            File input = new File(this.pathToFiles + System.getProperty("file.separator") +
                                  this.dbid2num.get(id) + this.suffix);
            //IChemObjectReader reader = readerFactory.createReader(new FileReader(input));
            try {
                MDLV2000Reader reader = new MDLV2000Reader(new FileReader(input));//(MDLV2000Reader) readerFactory.createReader(new FileReader(input));
                IAtomContainer aux = (IAtomContainer) reader.read(builder.newInstance(
                  IMolecule.class));
                aux.setID(id);
                mols.add(aux);
            } catch( FileNotFoundException e ) {
                // TODO Auto-generated catch block
                System.out.println("Could not find file for Biocyc ID: " + id);
            } catch( IOException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch( CDKException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mols;
    }


    @Override
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
    public Set<String> searchWithName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public Map<String, String> search(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}

