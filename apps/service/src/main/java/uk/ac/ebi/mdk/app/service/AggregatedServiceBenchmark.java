/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.app.service;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;
import uk.ac.ebi.mdk.service.query.AbstractSoapService;
import uk.ac.ebi.mdk.service.query.ChEBIAdapter;
import uk.ac.ebi.mdk.service.query.name.ChEBINameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.structure.ChEBIStructureService;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.instrument.Instrumentation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a simple benchmark of service call times
 *
 * @author John May
 */
public class AggregatedServiceBenchmark {

    interface MyChEBIService extends StructureService<ChEBIIdentifier>,
                                     PreferredNameService<ChEBIIdentifier> {
    }

    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }


    public static void main(String[] args) throws MalformedURLException, IOException {

        ServiceManager manager = DefaultServiceManager.getInstance();
        MyChEBIService service = manager.createService(ChEBIIdentifier.class,
                                                       MyChEBIService.class);


        // read identifiers
        List<ChEBIIdentifier> identifiers = new ArrayList<ChEBIIdentifier>();

        URL url = new URL("ftp://ftp.ebi.ac.uk/pub/databases/chebi/Flat_file_tab_delimited/compounds.tsv");
        CSVReader tsv = new CSVReader(new InputStreamReader(url.openStream()), '\t', '\0');
        String[] row = tsv.readNext();
        while ((row = tsv.readNext()) != null) {
            if (row[4].equals("null") && row[1].equals("C") && !row[3].equals("Chemical Ontology")) {
                identifiers.add(new ChEBIIdentifier(row[2]));
            }
        }

        int binSize = 25;
        int nrepeat = 1;

        CSVWriter writer = new CSVWriter(new FileWriter(new File("/Users/johnmay/Desktop/benchmark.tsv")), '\t', '\0');

        writer.writeNext(new String[]{"entries", "separate.cold", "separate.warm", "proxy.cold", "proxy.warm"});


        System.out.println("warmup...");
        {
            PreferredNameService pns = new ChEBINameService();
            StructureService ss = new ChEBIStructureService();
            pns.startup();
            ss.startup();
            for (ChEBIIdentifier identifier : identifiers.subList(0, 500)) {
                printNameAndAtomCount(ss, pns, identifier);
            }
        }


        System.out.println(identifiers.size());
        for (int i = binSize; i + binSize < 1001; i += binSize) {

            List<ChEBIIdentifier> subset = identifiers.subList(0, i);
            List<String> result = new ArrayList<String>();


            PreferredNameService pns = new ChEBINameService();
            StructureService ss = new ChEBIStructureService();

            pns.startup();
            ss.startup();

            Logger.getLogger(AbstractLuceneService.class).setLevel(Level.OFF);
            Logger.getLogger(AbstractSoapService.class).setLevel(Level.OFF);

            ChEBIAdapter adapter = new ChEBIAdapter();
            adapter.startup();

            service.renew();

            result.add(Integer.toString(subset.size()));



            System.out.println("separate.cold");
            {
                long start = System.currentTimeMillis();
                int notnull = 0;

                // avg out quick times
                for (int j = 0; j < nrepeat; j++) {

                    pns.renew();
                    ss.renew();
                    for (ChEBIIdentifier identifier : subset) {
                        if (printNameAndAtomCount(ss, pns, identifier)) {
                            notnull++;
                        }
                    }

                }

                long end = System.currentTimeMillis();
                result.add(Long.toString((end - start) / nrepeat));
                //                result.add(Long.toString(instrumentation.getObjectSize(ss) + instrumentation.getObjectSize(pns)));

            }


            System.out.println("separate.warm");
            {
                long start = System.currentTimeMillis();
                int notnull = 0;

                // avg out quick times
                for (int j = 0; j < nrepeat; j++) {
                    for (ChEBIIdentifier identifier : subset) {
                        if (printNameAndAtomCount(ss, pns, identifier)) {
                            notnull++;
                        }
                    }
                }

                long end = System.currentTimeMillis();
                result.add(Long.toString((end - start) / nrepeat));
                //                result.add(Long.toString(instrumentation.getObjectSize(ss) + instrumentation.getObjectSize(pns)));

            }



            System.out.println("proxy.cold");
            {
                long start = System.currentTimeMillis();
                int notnull = 0;
                for (int j = 0; j < nrepeat; j++) {
                    service.renew();
                    for (ChEBIIdentifier identifier : subset) {
                        if (printNameAndAtomCount(service, identifier)) {
                            notnull++;
                        }
                    }
                }

                long end = System.currentTimeMillis();
                result.add(Long.toString((end - start) / nrepeat));
                //                result.add(Long.toString(instrumentation.getObjectSize(service)));

            }

            System.out.println("proxy.warm");
            {
                long start = System.currentTimeMillis();
                int notnull = 0;
                for (int j = 0; j < nrepeat; j++) {
                    for (ChEBIIdentifier identifier : subset) {
                        if (printNameAndAtomCount(service, identifier)) {
                            notnull++;
                        }
                    }

                }


                long end = System.currentTimeMillis();
                result.add(Long.toString((end - start) / nrepeat));
                //                result.add(Long.toString(instrumentation.getObjectSize(service)));

            }

            System.out.println("SOAP");
            {
                long start = System.currentTimeMillis();
                int notnull = 0;
                for (ChEBIIdentifier identifier : subset) {
                    if (printNameAndAtomCount(adapter, identifier)) {
                        notnull++;
                    }
                }
                long end = System.currentTimeMillis();
                result.add(Long.toString(end - start));
                //              result.add(Long.toString(instrumentation.getObjectSize(adapter)));

            }

            writer.writeNext(result.toArray(new String[0]));
            System.out.println(result);


        }

        writer.close();


    }

    @SuppressWarnings("unchecked")
    public static <S extends PreferredNameService & StructureService> boolean printNameAndAtomCount(S service, Identifier identifier) {
        service.getPreferredName(identifier);
        return service.getStructure(identifier).getAtomCount() != 0;
    }

    @SuppressWarnings("unchecked")
    public static boolean printNameAndAtomCount(StructureService ss, PreferredNameService pns, Identifier identifier) {
        pns.getPreferredName(identifier);
        return ss.getStructure(identifier).getAtomCount() != 0;
    }


}
