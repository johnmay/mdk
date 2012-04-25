package uk.ac.ebi.chemet.service.query.structure;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.service.index.structure.KEGGCompoundStructureIndex;
import uk.ac.ebi.chemet.service.loader.location.SystemDirectoryLocation;
import uk.ac.ebi.chemet.service.loader.structure.KEGGCompoundStructureLoader;
import uk.ac.ebi.service.ResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * ${Name}.java - 21.02.2012 <br/> MetaInfo...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundStructureService
        extends AbstractStructureQueryService<KEGGCompoundIdentifier> {

    public KEGGCompoundStructureService() {
        super(new KEGGCompoundStructureIndex());
    }

    public KEGGCompoundIdentifier getIdentifier() {
        return new KEGGCompoundIdentifier();
    }

    public static void main(String[] args) throws Exception {
        ResourceLoader loader = new KEGGCompoundStructureLoader();
        File molDir = new File("/databases/kegg/ligand/mol");
        loader.addLocation("KEGG Mol files", new SystemDirectoryLocation(molDir));
        loader.backup();
        loader.update();
        KEGGCompoundStructureService service = new KEGGCompoundStructureService();

        Fingerprinter fingerprinter = new Fingerprinter();

        Set<IAtomContainer> molecules = new HashSet<IAtomContainer>();

        for (File file : molDir.listFiles()) {
            MDLV2000Reader reader = new MDLV2000Reader(new FileInputStream(file));
            molecules.add(reader.read(new Molecule()));
            reader.close();
            if (molecules.size() > 10000) {
                break;
            }
        }

        System.out.println("Starting search:");
        long start = System.currentTimeMillis();
        for (IAtomContainer atomContainer : molecules) {
            service.searchStructure(fingerprinter.getFingerprint(atomContainer));
        }
        long end = System.currentTimeMillis();
        System.out.println("10000 Molecules " + (end - start) + "ms");

        System.out.println("Avg: " + (end - start) / 10000 + " ms/per molecule");


    }

}
