package uk.ac.ebi.chemet.apps.service;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.chemet.resource.chemical.ChemSpiderIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.resource.chemical.PubChemCompoundIdentifier;
import uk.ac.ebi.chemet.service.DefaultServiceManager;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import java.util.Arrays;
import java.util.List;

/**
 * @author John May
 */
public class ServiceManagerExample {

    private static final Logger LOGGER = Logger.getLogger(ServiceManagerExample.class);

    public static void main(String[] args) {

        ServiceManager manager = DefaultServiceManager.getInstance();

        List<? extends Identifier> identifiers = Arrays.asList(new ChEBIIdentifier("CHEBI:15422"),
                                                               new KEGGCompoundIdentifier("C00009"),
                                                               new KEGGCompoundIdentifier("C00010"),
                                                               new ChEBIIdentifier("CHEBI:57299"),
                                                               new PubChemCompoundIdentifier("5957"),
                                                               new ChemSpiderIdentifier("5742"),
                                                               new PubChemCompoundIdentifier("5957"),
                                                               new ChemSpiderIdentifier("5742"));


        for (Identifier identifier : identifiers) {
            if (manager.hasService(identifier, StructureService.class)) {
                StructureService service = manager.getService(identifier, StructureService.class);
                System.out.println(service.getServiceType());

                long start = System.currentTimeMillis();
                IAtomContainer structure = service.getStructure(identifier);
                long end = System.currentTimeMillis();

                System.out.println(identifier.getSummary() + " - AtomCount = " + structure.getAtomCount() + " time: " + (end - start));

            }
        }

    }

}
