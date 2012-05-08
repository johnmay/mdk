package uk.ac.ebi.mdk.app.service;

import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.ChEBIAdapter;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.structure.StructureService;
import uk.ac.ebi.ws.chebi.*;

import java.util.Arrays;
import java.util.Collection;

/**
 * Provides a simple benchmark of service call times
 * @author John May
 */
public class AggregatedServiceBenchmark {

    interface MyChEBIService extends StructureService<ChEBIIdentifier>,
                                     PreferredNameService<ChEBIIdentifier> {
    }

    public static void main(String[] args) {

        ServiceManager  manager = DefaultServiceManager.getInstance();
        MyChEBIService service = manager.createService(ChEBIIdentifier.class,
                                                        MyChEBIService.class);

        Collection<ChEBIIdentifier> identifiers = Arrays.asList(new ChEBIIdentifier("CHEBI:15422"),
                                                                new ChEBIIdentifier("CHEBI:16761"),
                                                                new ChEBIIdentifier("CHEBI:15996"),
                                                                new ChEBIIdentifier("CHEBI:17552"),
                                                                new ChEBIIdentifier("CHEBI:61454"),
                                                                new ChEBIIdentifier("CHEBI:57527"),
                                                                new ChEBIIdentifier("CHEBI:62230"),
                                                                new ChEBIIdentifier("CHEBI:17009"));

        PreferredNameService pns = manager.getService(ChEBIIdentifier.class,
                                                      PreferredNameService.class);
        StructureService ss = manager.getService(ChEBIIdentifier.class,
                                                 StructureService.class);

        ChEBIAdapter adapter = new ChEBIAdapter();
        adapter.startup();

        {
            for (int i = 0; i < 10000; i++) {
                for (ChEBIIdentifier identifier : identifiers) {
                    printNameAndAtomCount(service, identifier);
                }
            }
        }

        {
            for (int i = 0; i < 10000; i++) {
                for (ChEBIIdentifier identifier : identifiers) {
                    printNameAndAtomCount(ss, pns, identifier);
                }
            }
        }
        {
            for (int i = 0; i < 1000; i++) {
                for (ChEBIIdentifier identifier : identifiers) {
                    printNameAndAtomCount(adapter, identifier);
                }
            }
        }

        // now lets time

        {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                for (ChEBIIdentifier identifier : identifiers) {
                    printNameAndAtomCount(service, identifier);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("Aggregated:" + (end - start) + "ms");

        }

        {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                for (ChEBIIdentifier identifier : identifiers) {
                    printNameAndAtomCount(ss, pns, identifier);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("Separated: " + (end - start) + "ms");
        }
        {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                for (ChEBIIdentifier identifier : identifiers) {
                    printNameAndAtomCount(adapter, identifier);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("SOAP: " + (end - start) + "ms");
        }
        {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                for (ChEBIIdentifier identifier : identifiers) {
                    printNameAndAtomCount_opt(adapter, identifier);
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("SOAP Optimised: " + (end - start) + "ms");
        }


    }


    public static <S extends PreferredNameService & StructureService> void printNameAndAtomCount(S service, Identifier identifier) {
        service.getPreferredName(identifier);
        service.getStructure(identifier);
    }

    public static void printNameAndAtomCount(StructureService ss, PreferredNameService pns, Identifier identifier) {
        pns.getPreferredName(identifier);
        ss.getStructure(identifier);
    }

    public static void printNameAndAtomCount_opt(ChEBIAdapter adapter, ChEBIIdentifier identifier) {
        Entity entity = adapter.getCompleteEntity(identifier);
        entity.getChebiAsciiName();
        for (StructureDataItem s : entity.getChemicalStructures()) {
            if (s.getType().equals("mol")) {
                adapter.mol2Structure(s.getStructure());
                return;
            }
        }
    }



}
