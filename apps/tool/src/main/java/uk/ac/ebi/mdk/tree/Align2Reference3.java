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

package uk.ac.ebi.mdk.tree;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.hash.MyHashMaker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IDoubleBondStereochemistry;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.isomorphism.StructureUtil;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tautomer.TautomerStream;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.annotation.Lumped;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;
import uk.ac.ebi.mdk.prototype.hash.util.MutableInt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/** @author John May */
public class Align2Reference3 extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(Align2Reference3.class);

    public static void main(String[] args) {
        new Align2Reference3().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("q", "query", true, "Query reconstruction ('.mr' folder)"));
        add(new Option("r", "reference", true, "Reference reconstruction ('.mr' folder)"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process() {

        System.out.print("[INFO] Reading query...");
        final Reconstruction query = getReconstruction("query");
        System.out.println("done");
        System.out.print("[INFO] Reading reference...");
        final Reconstruction reference = getReconstruction("reference");
        System.out.println("done");

        System.out.printf("[INFO]     Query reconstruction %20s %6s,%6s\n", query
                .getAccession(), query.metabolome().size(), query.reactome()
                                                                 .size());
        System.out.printf("[INFO] Reference reconstruction %20s %6s,%6s\n", reference
                .getAccession(), reference.metabolome().size(), reference
                                  .reactome().size());

        boolean metacyc = reference.getAccession().contains("MetaCyc");

        List<IAtomContainer> containers = new ArrayList<IAtomContainer>(reference.metabolome().size());
        final List<Metabolite> metabolites = new ArrayList<Metabolite>(reference.metabolome().size());
        List<Integer> ids = new ArrayList<Integer>(reference.metabolome().size());
        {
            long t0 = System.nanoTime();
            for (Metabolite target : reference.metabolome()) {
                // Set the MetaCyc Id as abbreviation
                if (metacyc)
                    target.setAbbreviation(target.getAccession().substring(5));
                for (ChemicalStructure cs : target.getStructures()) {
                    containers.add(cs.getStructure());
                    ids.add(ids.size());
                    metabolites.add(target);
                }
            }
            long t1 = System.nanoTime();
            System.out.printf("[INFO] flattened structure annotations in: %.2f s\n", (t1 - t0) / 1e9);
        }
        Structures structures = Structures.ofList(containers);


        Encoder encoder = new Encoder(FormulaHash.WithoutHydrogens,
                                      new MyHashMaker(4).elements()
                                                        .build(),
                                      new MyHashMaker(8).elements()
                                                        .stereochemistry()
                                                        .build()
//                ,
//                                      new MyHashMaker(8).elements()
//                                                        .stereochemistry()
//                                                        .formalCharges()
//                                                        .build()
        );

        Bin bin = new Bin(structures, encoder, 0, ids);
        int unique = 0;
        int missed = 0;
        int tuple = 0;
        int total = 0;

        CSVWriter csv = null;
        try {
            csv = new CSVWriter(new FileWriter("/Users/johnmay/Desktop/matched.tsv"), '\t', '\0');
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        Metabolite[] queryMetabolites = Iterables.toArray(query.metabolome(), Metabolite.class);
        Arrays.sort(queryMetabolites, new Comparator<Metabolite>() {
            @Override public int compare(Metabolite a, Metabolite b) {
                return a.getAbbreviation().compareTo(b.getAbbreviation());
            }
        });

        List<ResultSet> resultSets = new ArrayList<ResultSet>();

        long t0 = System.nanoTime();
        for (final Metabolite queryM : queryMetabolites) {

            // if (queryM.hasAnnotation(Lumped.class)) continue;
            // if (!queryM.getStructures().isEmpty())
            //    total++;

            ResultSet rs = new ResultSet(queryM);

            for (final ChemicalStructure cs : queryM.getStructures()) {

                IAtomContainer queryStructure = cs.getStructure();
                Collection<Integer> candidates = bin.find(queryStructure);

                
                queryStructure = StructureUtil.suppressHydrogens(queryStructure);
                StructureUtil.neutralise(queryStructure);
                new TautomerStream(queryStructure).next();
                fixStereo(queryStructure);
                StructureUtil.disconnectMetals(queryStructure);
                
                if (candidates.size() == metabolites.size()) {
                    missed++;
                    continue;
                }

                for (int candidate : candidates) {

                    Metabolite targetM = metabolites.get(candidate);
                    IAtomContainer targetStructure = structures.get(candidate);
                    
                    targetStructure = StructureUtil.suppressHydrogens(targetStructure);
                    StructureUtil.neutralise(targetStructure);
                    StructureUtil.disconnectMetals(targetStructure);
                    new TautomerStream(targetStructure).next();
                    fixStereo(targetStructure);

                    long   rt0 = System.nanoTime();
                    Result result = new Result(queryM, targetM, queryStructure, targetStructure);
                    long   rt1   = System.nanoTime();
                    long   rt_ms = TimeUnit.NANOSECONDS.toMillis(rt1-rt0);
                    if (rt_ms > 2)
                        System.out.println(queryM.getAbbreviation() + "-" + targetM.getAbbreviation() + ": " + rt_ms + " ms");

                    rs.add(result);
                }

            }

            if (rs.size() > 1) rs.refine();

            resultSets.add(rs);
        }
        long t1 = System.nanoTime();

        Collections.sort(resultSets);

        Map<String, MutableInt> types = new TreeMap<String, MutableInt>();

        ResultSet.writeHeader(csv);
        for (ResultSet rs : resultSets) {
            rs.write(csv);

            String key = aggregateType(rs);
            MutableInt counter = types.get(key);
            if (counter == null) types.put(key, counter = new MutableInt(0));
            counter.increment();
        }

        System.out.println(Joiner.on('\n').join(types.entrySet()));

        System.out.printf("[INFO] matched in %.2f s\n", (t1 - t0) / 1e9);
        System.out.printf("[INFO] %d/%d resolved uniquely\n", unique, total);
        System.out.printf("[INFO] %d/%d resolved tuple\n", tuple, total);
        System.out.printf("[INFO] %d/%d not resolved\n", missed, total);

        try {
            csv.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
//        Bin bin = new Bin(structures, encoder, 0, ids);
//
//        int ident = 0, aprx = 0, aprx_name = 0, aprx_name_unspec = 0, aprx_xv_stereo = 0, aprx_xv_stereo_unspec = 0, aprx_xv = 0, fp = 0, none = 0;
//
//        Set<Integer> sizes = new TreeSet<Integer>();
//
//        File outRoot = new File("/Users/johnmay/Desktop/");
//
//        String prefix = query.getIdentifier() + "-" + reference.getIdentifier() + (false ? "-neu" : "");
//
//        File output = new File(outRoot, query.getIdentifier() + "-" + reference.getIdentifier() + (neutralise ? "-neu" : "") + ".csv");
//
//        MetaboliteNameIndex nameIndex = new MetaboliteNameIndex(reference, true);
//
//        Map<Results.Classificiation, MutableInt> counter = new HashMap<Results.Classificiation, MutableInt>();
//        for (Results.Classificiation classificiation : Results.Classificiation.values()) {
//            counter.put(classificiation, new MutableInt(0));
//        }
//        
//        try {
//            CSVWriter csv = new CSVWriter(new FileWriter(output), ',');
//
//
//            String[] header = new String[]{
//                    "query-id",
//                    "query-name",
//                    "classification",
//                    "score",
//                    "score.components",
//                    "target-id",
//                    "target-name",
//                    "name-matches",
//                    "syn-matches",
//                    "query-smi",
//                    "target-smi"
//            };
//
//            csv.writeNext(header);
//       
//
//            for (Metabolite queryM : query.metabolome()) {
//
//                Results results = new Results();
//
//                for (ChemicalStructure cs : queryM.getStructures()) {
//                    Collection<Integer> matched = bin.find(cs.getStructure());
//
//                    if (matched.size() < containers.size()) {
//
//                        IAtomContainer queryStructure = StructureUtil.suppressHydrogens(cs.getStructure());
//                        if (neutralise)
//                            StructureUtil.neutralise(queryStructure);
////                        Map<Score, Integer> scores = new TreeMap<Score, Integer>();
//                        for (Integer id : matched) {
//                            IAtomContainer structure = StructureUtil.suppressHydrogens(structures.get(id));
//                            if (neutralise)
//                                StructureUtil.neutralise(structure);
//
//                            try {
//                                results.add(new Result(queryM, metabolites.get(id), queryStructure, structure));
//                            } catch (Exception e) {
//                                System.out.println("could not score: " + smi(queryStructure) + " with " + smi(structure));
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    else {
//                        results.add(new Result(queryM, null, cs.getStructure(), null));
//                    }
//                }
//                
//                // counter.get(results.classify()).increment();
//                
//                results = results.filterAndClasify();
//                if (results.classificiation() != Results.Classificiation.None) {
//                    for (Result result : results.results()) {
//                        result.query().addObservation(new MatchedEntity(reference.getIdentifier(), 
//                                                                        result.target().getIdentifier()));
//                    }
//                }
//                
//
////                if (results.hasExactMatch()) {
////                    results = results.unique().ident();
////                    if (results.size() > 1) {
////                        results.write(new CSVWriter(new PrintWriter(System.out), '\t'));
////                    }
////                    ident++;
////                }
////                else if (results.hasAprxMatch()) {
////                    if (results.hasNameMatch()) {
////                        if (results.hasSingleUnspecCenter()) {
////                            results = results.singleUnspecCenter().unique();
////                            if(results.size() > 1) {
////                                CSVWriter csv = new CSVWriter(new PrintWriter(System.out), '\t');
////                                results.write(csv);
////                                csv.flush();
////                            }
////                            results.write(aprxCSV_name_unspec);
////                            aprx_name_unspec++;
////                        }
////                        else {
////                            results.nameMatches().unique().write(aprxCSV_name);
////                            aprx_name++;
////                        }
////                    }
////                    else {
////                        if (results.hasValidConn()) {
////                            results = results.validConnectMatches();
////                            if (results.hasMissingStereo()) {
////                                if (results.hasSingleUnspecCenter()) {
////                                    results.singleUnspecCenter().unique().write(aprxCSV_vx_stereo_unspec);
////                                    aprx_xv_stereo_unspec++;
////                                }
////                                else {
////                                    aprx_xv_stereo++;
////                                    results.validStereoMatches().unique().write(aprxCSV_vx_stereo);
////                                }
////                            }
////                            else {
////                                aprx_xv++;
////                                results.unique().write(aprxCSV_vx);
////                            }
////                        }
////                        else {
////                            aprx++;
////                            results.unique().write(aprxCSV_other);
////                        }
////                    }
////                } else {
////                    for (Metabolite target : nameIndex.ofName(queryM.getName())) {
////                        results.add(new Result(queryM, target, null, null));
////                    }
////                    if (results.isEmpty())
////                        results.add(new Result(queryM, null, null, null));
////                    results.unique().write(noneCSV);
////                    none++;
////                }
//            }
//
//            String name = query.getContainer().getPath();
//            String base = name.substring(0, name.lastIndexOf('.'));
//            System.out.println("Writing... " + base + "-matched.mr");
//            ReconstructionIOHelper.write(query, new File(base + "-matched.mr"));
//            
//            System.out.println(counter);
//
//            System.out.println("ident: " + ident + ", aprx:" + aprx + ", aprx (name):" + aprx_name + ", aprx (name+1unspec):" + aprx_name_unspec + ", aprx(vxs+unspec) " + aprx_xv_stereo_unspec + ", aprx (xvs):" + aprx_xv_stereo + ", aprx (xv):" + aprx_xv + ", fp:" + fp + ", none:" + none);
//
//            csv.close();
//
//        } catch (IOException e) {
//            System.err.println(e);
//        }
//
//        long t2 = System.nanoTime();
//
//        System.out.println(sizes);
//
//        System.out.println(TimeUnit.NANOSECONDS.toMillis(t1 - t0) + " ms");
//        System.out.println(TimeUnit.NANOSECONDS.toMillis(t2 - t1) + " ms");

    }

    static String aggregateType(ResultSet rs) {
        if (rs.results.size() == 1) {
            return rs.results.get(0).type().toString();
        }
        else if (rs.results.size() == 2) {
            return "Tuple";
        }
        else if (rs.results.size() > 1) {
            return "Multiple";
        }
        else {
            return "None";
        }
    }

    static void fixStereo(IAtomContainer ac) {
        List<IStereoElement> ses = new ArrayList<IStereoElement>();
        for (IStereoElement se : ac.stereoElements()) {
            if (se instanceof IDoubleBondStereochemistry) {
                if (((IDoubleBondStereochemistry) se).getStereoBond().getOrder() == IBond.Order.DOUBLE)
                    ses.add(se);
            }
            else {
                ses.add(se);
            }
        }
        ac.setStereoElements(ses);
    }

    static CSVWriter createWriter(File root, String path) throws IOException {
        return new CSVWriter(new FileWriter(new File(root, path)), ',');
    }

    static boolean nameMatch(String a, String b) {
        a = a.toLowerCase().replaceAll("[- ,(){}\\[\\]]", "");
        b = b.toLowerCase().replaceAll("[- ,(){}\\[\\]]", "");
        return a.equals(b);
    }

    static String smi(IAtomContainer m) {
        try {
            return SmilesGenerator.isomeric().create(m);
        } catch (CDKException e) {
            return " n/a";
        }
    }


    static <M> int adjustedCount(Reaction<? extends Participant<M, ?>> rxn, Set<M> currency) {
        int count = 0;
        for (Participant<M, ?> p : rxn.getParticipants()) {
            count += currency.contains(p.getMolecule()) ? 0 : 1;
        }
        return count;
    }

    static <M, P extends Participant<M, ?>> Collection<P> adjustedParticipants(Reaction<P> rxn, Set<M> currency) {
        Collection<P> participants = new ArrayList<P>();
        for (P p : rxn.getParticipants()) {
            if (!currency.contains(p.getMolecule())) {
                participants.add(p);
            }
        }
        return participants;
    }


    static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res
                                        : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        for (Map.Entry<K, V> e : map.entrySet()) {
            sortedEntries.add(e);
        }
        return sortedEntries;
    }


    public Reconstruction getReconstruction(String option) {

        File file = getFile(option);

        try {
            return ReconstructionIOHelper.read(file);
        } catch (IOException e) {
            LOGGER.fatal("Could not read reconstruction '" + option + "': " + e
                    .getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.fatal("Could not read reconstruction '" + option + "': " + e
                    .getMessage());
        }

        System.exit(1);

        return null;

    }

}
