package uk.ac.ebi.io.service.loader.structure;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.AbstractResourceLoader;
import uk.ac.ebi.io.service.loader.LocationDescription;
import uk.ac.ebi.io.service.loader.location.ResourceDirectoryLocation;
import uk.ac.ebi.io.service.index.structure.KEGGCompoundStructureIndex;

import java.io.*;

/**
 * ${Name}.java - 20.02.2012 <br/>
 * <p/>
 * Load the KEGG mol directory into a lucene index.
 * The KEGG Compound identifier is provided by the name of the file
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundStructureLoader extends AbstractResourceLoader {


    public KEGGCompoundStructureLoader() throws IOException {
        super(new KEGGCompoundStructureIndex());

        addRequiredResource(new LocationDescription("Mol Directory",
                                                    " directory containing '.mol' files named with KEGG Compound Id (i.e. kegg/ligand/mol/C00009.mol)",
                                                    ResourceDirectoryLocation.class));

    }

    @Override
    public void load() throws MissingLocationException, IOException {


       backup();

       ResourceDirectoryLocation location = getLocation("Mol Directory");

       StructureIndexWriter writer =  StructureIndexWriter.create(getIndex());
        
       for(File file : location.list()){

            MDLV2000Reader mdlReader = new MDLV2000Reader(new FileInputStream(file));
            try{
                IAtomContainer molecule = new AtomContainer(mdlReader.read(new Molecule()));

                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteStream);
                out.writeObject(molecule);

                writer.add(file.getName().substring(0, 6),
                                byteStream.toByteArray());


            } catch (Exception ex){
                ex.printStackTrace();
            }


        }

        writer.close();

    }



}
