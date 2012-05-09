package uk.ac.ebi.mdk.service.loader.structure;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.similarity.Tanimoto;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.service.loader.location.SystemDirectoryLocation;
import uk.ac.ebi.mdk.service.query.structure.KEGGCompoundStructureService;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.ResourceLoader;
import uk.ac.ebi.mdk.service.exception.MissingLocationException;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author John May
 */
public class KEGGCompoundStructureServiceTest {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundStructureServiceTest.class);


    public static void main(String[] args) throws Exception {
        new KEGGCompoundStructureServiceTest().substructureExample();
    }

    public void substructureExample() throws IOException, MissingLocationException, CDKException {

        ResourceLoader loader = new KEGGCompoundStructureLoader();
        File keggMol = new File("/databases/kegg/ligand/mol");
        loader.addLocation("KEGG Mol files", new SystemDirectoryLocation(keggMol));
        //        loader.backup();
        //        loader.update();


        Set<Identifier> errors = new HashSet<Identifier>();

        KEGGCompoundStructureService service = new KEGGCompoundStructureService();
        service.setMaxResults(10);


        Set<Identifier> include = new HashSet<Identifier>(Arrays.asList(
                new KEGGCompoundIdentifier("C00017"),
                new KEGGCompoundIdentifier("C00008"),
                new KEGGCompoundIdentifier("C00042"),
                new KEGGCompoundIdentifier("C00021"),
                new KEGGCompoundIdentifier("C00020")
        ));

        Map<Identifier, IAtomContainer> identifiers = new HashMap<Identifier, IAtomContainer>();
        for (int i = 1; i < 50; i++) {
            Identifier identifier = new KEGGCompoundIdentifier(String.format("C%05d", i));
            IAtomContainer structure = service.getStructure((KEGGCompoundIdentifier) identifier);
            if (structure.getAtomCount() > 0 && include.contains(identifier)) {
                identifiers.put(identifier, structure);
            }

        }

        System.out.println("Collected identifiers " + identifiers.size() + " [refeshing service file]");

        service = new KEGGCompoundStructureService();
        service.setMaxResults(10);

        long start = System.currentTimeMillis();
        for (Map.Entry<Identifier, IAtomContainer> e : identifiers.entrySet()) {

            try {

                Identifier identifier = e.getKey();
                IAtomContainer structure = e.getValue();

                Collection<? extends Identifier> results = service.structureSearch(e.getValue(), true);

                BitSet query = new Fingerprinter().getFingerprint(structure);
                Collection<Float> coefs = getCoefficients(query, results, service);

                if (!results.iterator().next().equals(identifier)) {
                    errors.add(identifier);
                }

                System.out.println(identifier);
                System.out.println(results);
                System.out.println(coefs);

            } catch (Exception ex) {

            }


        }
        long end = System.currentTimeMillis();

        System.out.println(end - start + " ms");

        System.out.println(errors.size() + ": " + errors);

    }


    public static Collection<Float> getCoefficients(BitSet query, Collection<? extends Identifier> ids, StructureService
            service) throws CDKException {
        List<Float> coefs = new ArrayList<Float>();
        Fingerprinter fp = new Fingerprinter();
        for (Identifier id : ids) {
            coefs.add(Tanimoto.calculate(query, fp.getFingerprint(service.getStructure(id))));
        }
        return coefs;
    }

}
