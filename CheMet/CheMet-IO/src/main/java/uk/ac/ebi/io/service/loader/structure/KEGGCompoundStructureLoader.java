package uk.ac.ebi.io.service.loader.structure;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.io.service.exception.MissingLocationException;
import uk.ac.ebi.io.service.loader.AbstractResourceLoader;
import uk.ac.ebi.io.service.loader.location.ResourceLocationKey;
import uk.ac.ebi.io.service.loader.location.SystemDirectoryLocation;
import uk.ac.ebi.io.service.index.structure.KEGGCompoundStructureIndex;

import java.io.*;
import java.util.Arrays;
import java.util.prefs.Preferences;

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

    @ResourceLocationKey(name="Mol Direcotry",
                         desc="A directory containing only '.mol' files named with KEGG Compound Id")
    public static final String KEGG_MOL_DIRECTORY = "kegg.mol.dir";

    public KEGGCompoundStructureLoader() throws IOException {
        super(new KEGGCompoundStructureIndex());
    }

    @Override
    public void load() throws MissingLocationException, IOException {

        SystemDirectoryLocation location = (SystemDirectoryLocation) getLocation(KEGG_MOL_DIRECTORY);

        BufferedReader in = new BufferedReader(new InputStreamReader(location.open()));
        clean();
        StructureIndexWriter indexwriter = StructureIndexWriter.create(getIndex());

        byte[] b = new byte[0];
        String line = null;
        while ((line = in.readLine()) != null) {

            File molfile = new File(line);

            MDLV2000Reader mdlReader = new MDLV2000Reader(new FileInputStream(molfile));
            try{
                IAtomContainer molecule = new AtomContainer(mdlReader.read(new Molecule()));

                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteStream);
                out.writeObject(molecule);

                indexwriter.add(molfile.getName().substring(0, 6),
                                byteStream.toByteArray());


            } catch (Exception ex){
                ex.printStackTrace();
            }


        }

        indexwriter.close();
        location.close();

    }

    @Override
    public void clean() {
        delete(getIndex().getLocation());
    }

}
