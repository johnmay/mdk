/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
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

package uk.ac.ebi.apps.benchmark;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.Option;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.name.*;
import uk.ac.ebi.mdk.tool.resolve.ChemicalFingerprintEncoder;
import uk.ac.ebi.mdk.tool.resolve.FingerprintEncoder;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Benchmarks the searching of chemical names measuring
 * <p/>
 * approximate=true/false
 * method=preferred name, all names
 * <p/>
 * string=fingerprint
 * Time
 * Recal
 * Precission
 */
public class ChemicalNameSearch extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger("[BENCHMARK] ChemicalNameSearch");

    private List<String> names;
    private int found = 0;
    private int hits  = 0;

    public static void main(String[] args) {
        BasicConfigurator.configure();
        new ChemicalNameSearch().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("i", "input", true, "List of names to search (resource)"));
        add(new Option("f", "input-file", true, "List of names to search (file)"));
        add(new Option("a", "approximate", false, "Search using approximate match"));
        add(new Option("s", "service", true, "Name service to use (chebi, kegg, metacyc, lipidmaps, hmdb, all)"));
        add(new Option("g", "greedy", false, "Greedy Service searching (multiple services only)"));
    }


    public void testMultiple() {

        System.out.println("Testing multiple");

        Map<Class, NameService> services = new LinkedHashMap<Class, NameService>();

        StringBuilder sb = new StringBuilder("Combined: ");
        for (String serviceName : getCommandLine().getOptionValues("service")) {
            sb.append(serviceName).append(", ");
            NameService service = getNameService(serviceName);
            service.startup();
            services.put(service.getIdentifier().getClass(), service);
        }

        boolean greedy = has("g");

        Multimap<String, Identifier> results = ArrayListMultimap.create();

        long searchStart = System.currentTimeMillis();
        for (String name : names) {
            boolean foundHit = false;
            for (NameService service : services.values()) {

                Collection hits = service.searchName(name, has("a"));
                results.putAll(name, hits);
                if (!hits.isEmpty()) {
                    if (!foundHit) {
                        found++;
                        foundHit = true;
                    }
                    if (!greedy) break;
                }

            }
        }
        long searchEnd = System.currentTimeMillis();

        Long searchTime = (searchEnd - searchStart);

        Multimap<String, Set<String>> nameResults = ArrayListMultimap.create();

        long resolveStart = System.currentTimeMillis();
        for (Map.Entry<String, Identifier> e : results.entries()) {
            Identifier id = e.getValue();
            nameResults.put(e.getKey(), new HashSet<String>(services.get(id.getClass()).getNames(id)));
        }
        long resolveEnd = System.currentTimeMillis();

        Long resolveTime = resolveEnd - resolveStart;

        int trueFound = getRealScore(nameResults, new ChemicalFingerprintEncoder(), null);

        SummaryStatistics statistics = getHitIndices(nameResults, new ChemicalFingerprintEncoder());

        String[] row = new String[]{
                sb.toString(),
                searchTime.toString(),
                resolveTime.toString(),
                Integer.toString(found),
                Integer.toString(trueFound),
                Double.toString(statistics.getMax()),
                Double.toString(statistics.getMean()),
                Double.toString(statistics.getStandardDeviation())
        };

        System.out.println(Joiner.on("\t").join(row));

    }

    @Override
    public void process() {

        loadNames();

        if (getCommandLine().getOptionValues("s").length > 1) {
            testMultiple();
            return;
        }


        final NameService<Identifier> service = (NameService<Identifier>) getNameService(get("service", "none"));
        if (!service.startup()) {
            LOGGER.error("Unable to start service");
        }
        service.setMaxResults(100);

        Multimap<String, Identifier> results = ArrayListMultimap.create();

        // doing the search
        long searchStart = System.currentTimeMillis();
        for (String name : names) {
            Collection<? extends Identifier> hits = service.searchName(name, has("a"));
            results.putAll(name, hits);
            found += hits.isEmpty() ? 0 : 1;
        }
        long searchEnd = System.currentTimeMillis();

        Long searchTime = (searchEnd - searchStart);

        Multimap<String, Set<String>> nameResults = ArrayListMultimap.create();

        // resolving

        System.out.println("Transforming for performance test");

        long resolveStart = System.currentTimeMillis();
        for (Map.Entry<String, Identifier> e : results.entries()) {
            Identifier id = e.getValue();
            nameResults.put(e.getKey(), new HashSet<String>(service.getNames(id)));
        }
        long resolveEnd = System.currentTimeMillis();

        Long resolveTime = (resolveEnd - resolveStart);


        int trueFound = getRealScore(nameResults, new ChemicalFingerprintEncoder(), null);

        SummaryStatistics statistics = getHitIndices(nameResults, new ChemicalFingerprintEncoder());

        String[] row = new String[]{
                get("service", "unknown"),
                searchTime.toString(),
                resolveTime.toString(),
                Integer.toString(found),
                Integer.toString(trueFound),
                Double.toString(statistics.getMax()),
                Double.toString(statistics.getMean()),
                Double.toString(statistics.getStandardDeviation())
        };

        System.out.println(Joiner.on("\t").join(row));


    }

    public NameService<? extends Identifier> getNameService(String name) {

        if (name.equals("kegg")) {
            return new KEGGCompoundNameService();
        } else if (name.equals("chebi")) {
            return new ChEBINameService();
        } else if (name.equals("metacyc")) {
            return new MetaCycNameService();
        } else if (name.equals("lipidmaps")) {
            return new LipidMapsNameService();
        } else if (name.equals("hmdb")) {
            return new HMDBNameService();
        }

        System.out.println("Invalid service name: valid=[kegg, chebi, metacyc, lipidmaps or hmdb]");

        super.showHelp();
        System.exit(0);

        return null;

    }


    private SummaryStatistics getHitIndices(Multimap<String, Set<String>> results, FingerprintEncoder encoder) {

        SummaryStatistics summaryStatistics = new SummaryStatistics();

        QUERY:
        for (String name : results.keySet()) {

            String normName = encoder.encode(name);

            StringBuffer buffer = new StringBuffer();

            List<Set<String>> hits = new ArrayList<Set<String>>(results.get(name));

            for (int i = 0; i < hits.size(); i++) {

                Set<String> hitNames = hits.get(i);

                for (String hit : hitNames) {

                    String normHit = encoder.encode(hit);

                    buffer.append("\t").append(hit);
                    buffer.append("\t").append(normHit);
                    buffer.append("\t").append(StringUtils.getLevenshteinDistance(normName, normHit));
                    buffer.append("\n");

                    if (normName.equals(normHit)) {
                        summaryStatistics.addValue(i + 1);
                        continue QUERY;
                    }

                }

            }


        }

        return summaryStatistics;

    }

    private int getRealScore(Multimap<String, Set<String>> results, FingerprintEncoder encoder, File file) {

        int hitCount = 0;

        FileWriter writer = null;

        try {
            if (file != null)
                writer = new FileWriter(file);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        QUERY:
        for (String name : results.keySet()) {

            String normName = encoder.encode(name);

            StringBuffer buffer = new StringBuffer();

            for (Set<String> hitNames : results.get(name)) {

                for (String hit : hitNames) {

                    String normHit = encoder.encode(hit);

                    buffer.append("\t").append(hit);
                    buffer.append("\t").append(normHit);
                    buffer.append("\t").append(StringUtils.getLevenshteinDistance(normName, normHit));
                    buffer.append("\n");

                    if (normName.equals(normHit)) {
                        hitCount++;
                        continue QUERY;
                    }

                }

            }

            try {
                if (writer != null) {
                    writer.write(name + "\n");
                    writer.write(buffer.toString() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }


        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("Written unmatched results to :" + file);

        return hitCount;

    }


    private void loadNames() {
        names = getNames();
        LOGGER.info("Loaded " + names.size() + " metabolite names");
    }

    private Callable<Integer> create(final List<String> nameSublist, final NameService service, final Boolean approximate) {

        System.out.println("Creating query: " + nameSublist.size());

        service.startup();

        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int value = 0;
                long start = System.currentTimeMillis();
                for (String name : nameSublist) {
                    if (!service.searchName(name, approximate).isEmpty()) {
                        value++;
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println("Completed " + (end - start) + " ms");
                return value;
            }
        };
    }

    private List<String> getNames() {

        Set<String> names = new HashSet<String>();

        // load resource
        if (has("i")) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(get("i", ""))));

            try {

                String line;

                while ((line = reader.readLine()) != null) {
                    names.add(line);
                }

            } catch (IOException e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }

        }

        // load file
        if (has("f")) {

            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new FileReader(getFile("f")));
                String line;

                while ((line = reader.readLine()) != null) {
                    names.add(line);
                }
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }

        }


        System.out.println("Loaded " + names.size());


        return new ArrayList<String>(names);


    }


}
