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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.hash.HashGeneratorMaker;
import org.openscience.cdk.hash.MoleculeHashGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.annotation.SMILES;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/** @author John May */
public class MetaboliteKeyDistribution extends CommandLineMain {

    private static final Logger LOGGER = Logger
            .getLogger(MetaboliteKeyDistribution.class);

    public static void main(String[] args) {
        new MetaboliteKeyDistribution().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("r", "recon", true, "Reference reconstruction ('.mr' folder)"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process() {

        System.out.print("Reading recon...");
        final Reconstruction reference = getReconstruction("recon");
        System.out.println("done");


        System.out.printf("Reference reconstruction %20s %6s,%6s\n", reference
                .getAccession(), reference.metabolome().size(), reference
                                  .reactome().size());


        {
            long t0 = System.nanoTime();
            for (Metabolite m : reference.metabolome()) {
                for (ChemicalStructure cs : m.getStructures()) {
                    try {
                        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(cs.getStructure());
                        CDKHydrogenAdder.getInstance(cs.getStructure().getBuilder()).addImplicitHydrogens(cs.getStructure());
                    } catch (CDKException e) {
                        System.err.println(e.getMessage());
                        for (IAtom a : cs.getStructure().atoms())
                            if (a.getImplicitHydrogenCount() == null)
                                a.setImplicitHydrogenCount(0);
                    }
                }
            }

            long t1 = System.nanoTime();
            System.out.println(TimeUnit.NANOSECONDS.toMillis(t1 - t0) + " ms to atom type");
        }


        List<IAtomContainer> containers = new ArrayList<IAtomContainer>();

        for (Metabolite m : reference.metabolome()) {
            for (ChemicalStructure cs : m.getStructures()) {
                // if (cs instanceof SMILES)
                    containers.add(cs.getStructure());
            }
        }
        
        String id = reference.getIdentifier().toString();
        
        // id += "-smi";

        try {
            CSVWriter csv = new CSVWriter(new FileWriter("/Users/johnmay/desktop/key-generation-" + id + ".csv"), ',', '\0');

            csv.writeNext(new String[]{
                    "bin_size", "freq", "gen_name", "t_ms", "dataset", "n_struct", "n_struct_indexed", "n_bins", "avg_per_bin"
            });

            for (InvGen generator : Arrays.asList(new AtomCount(),
                                                  new BondCount(),
                                                  new AtomAndBondCount(),
                                                  new FormulaWithH(),
                                                  new FormulaNoH(),
                                                  new ExtendConHash1(),
                                                  new ExtendConHash2(),
                                                  new ExtendConHash3(),
                                                  new ExtendConHash4(),
                                                  new ExtendConHash5(),
                                                  new ExtendConHash6(),
                                                  new InChI())) {

                int err = 0;
                List<Long> invs = new ArrayList<Long>();
                Set<Integer> skip = new HashSet<Integer>();
                long t0 = System.nanoTime();
                for (IAtomContainer container : containers) {
                    try {
                        invs.add(generator.compute(container));
                    } catch (Exception e) {
                        skip.add(invs.size());
                        invs.add(0L);
                        err++;
                    }
                }
                long t1 = System.nanoTime();

                // now build lookup
                Multimap<Long, IAtomContainer> lookup = HashMultimap.create();

                for (int i = 0; i < containers.size(); i++) {
                    if (skip.contains(i))
                        continue;
                    IAtomContainer container = containers.get(i);
                    Long inv = invs.get(i);
                    lookup.put(inv, container);
                }

                System.out.println(lookup.size() + " (" + lookup.keySet().size() + " bins)" + " " + (lookup.size() / (double) lookup.keySet().size()) + " " + err + " errors");
                System.out.println(TimeUnit.NANOSECONDS.toMillis(t1 - t0) + " ms to index ");

                Map<Integer, Integer> freq = new TreeMap<Integer, Integer>();
                for (Map.Entry<Long, Collection<IAtomContainer>> map : lookup.asMap().entrySet()) {
                    int n = map.getValue().size();
                    Integer cnt;
                    freq.put(n, (cnt = freq.get(n)) == null ? 1 : 1 + cnt);
                }

                for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
                    csv.writeNext(new String[]{
                            e.getKey().toString(),
                            e.getValue().toString(),
                            generator.name(),
                            Long.toString(TimeUnit.NANOSECONDS.toMillis(t1 - t0)),
                            id,
                            Integer.toString(containers.size()),
                            Integer.toString(lookup.size()),                            
                            Integer.toString(lookup.keySet().size()),
                            Double.toString((lookup.size() / (double) lookup.keySet().size()))
                    });
                }

            }

            csv.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private interface InvGen {
        public String name();

        public long compute(IAtomContainer container);
    }

    private final class AtomCount implements InvGen {
        @Override public String name() {
            return "|V|";
        }

        @Override public long compute(IAtomContainer container) {
            return container.getAtomCount();
        }
    }

    private final class BondCount implements InvGen {
        @Override public String name() {
            return "|E|";
        }

        @Override public long compute(IAtomContainer container) {
            return container.getAtomCount();
        }
    }

    private final class AtomAndBondCount implements InvGen {
        @Override public String name() {
            return "|V|+|E|";
        }

        @Override public long compute(IAtomContainer container) {
            return (container.getAtomCount() << 16) | container.getBondCount();
        }
    }

    private final class FormulaNoH implements InvGen {
        @Override public String name() {
            return "formula-h";
        }

        @Override public long compute(IAtomContainer container) {
            return FormulaHash.WithoutHydrogens.generate(container);
        }
    }

    private final class FormulaWithH implements InvGen {
        @Override public String name() {
            return "formula+h";
        }

        @Override public long compute(IAtomContainer container) {
            return FormulaHash.WithHydrogens.generate(container);
        }
    }

    private final class ExtendConHash1 implements InvGen {

        private final MoleculeHashGenerator generator = new HashGeneratorMaker().depth(4)
                                                                                .elemental()
                                                                                .molecular();

        @Override

        public String name() {
            return "ec-4EsHp";
        }

        @Override public long compute(IAtomContainer container) {
            return generator.generate(container);
        }
    }

    private final class ExtendConHash2 implements InvGen {

        private final MoleculeHashGenerator generator = new HashGeneratorMaker().depth(4)
                                                                                .elemental()
                                                                                .suppressHydrogens()
                                                                                .molecular();

        @Override

        public String name() {
            return "ec-4Eshp";
        }

        @Override public long compute(IAtomContainer container) {
            return generator.generate(container);
        }
    }

    private final class ExtendConHash3 implements InvGen {

        private final MoleculeHashGenerator generator = new HashGeneratorMaker().depth(4)
                                                                                .elemental()
                                                                                .chiral()
                                                                                .suppressHydrogens()
                                                                                .molecular();

        @Override

        public String name() {
            return "ec-4EShp";
        }

        @Override public long compute(IAtomContainer container) {
            return generator.generate(container);
        }
    }

    private final class ExtendConHash4 implements InvGen {

        private final MoleculeHashGenerator generator = new HashGeneratorMaker().depth(16)
                                                                                .elemental()
                                                                                .chiral()
                                                                                .suppressHydrogens()
                                                                                .molecular();

        @Override

        public String name() {
            return "ec-16EShp";
        }

        @Override public long compute(IAtomContainer container) {
            return generator.generate(container);
        }
    }

    private final class ExtendConHash5 implements InvGen {

        private final MoleculeHashGenerator generator = new HashGeneratorMaker().depth(32)
                                                                                .elemental()
                                                                                .chiral()
                                                                                .perturbed()
                                                                                .suppressHydrogens()
                                                                                .molecular();

        @Override

        public String name() {
            return "ec-32-all";
        }

        @Override public long compute(IAtomContainer container) {
            return generator.generate(container);
        }
    }

    private final class ExtendConHash6 implements InvGen {

        private final MoleculeHashGenerator generator = new HashGeneratorMaker().depth(16)
                                                                                .elemental()
                                                                                .chiral()
                                                                                .charged()
                                                                                .radical()
                                                                                .isotopic()
                                                                                .orbital()
                                                                                .perturbed()
                                                                                .suppressHydrogens()
                                                                                .molecular();

        @Override
        public String name() {
            return "ec-16EShP";
        }

        @Override public long compute(IAtomContainer container) {
            return generator.generate(container);
        }
    }

    private final class InChI implements InvGen {

        InChIGeneratorFactory igf;

        private InChI() {
            try {
                igf = InChIGeneratorFactory.getInstance();
                igf.setIgnoreAromaticBonds(true);
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }

        @Override
        public String name() {
            return "inchi-key";
        }

        @Override public long compute(IAtomContainer container) {
            try {
                return igf.getInChIGenerator(container).getInchiKey().hashCode();
            } catch (Exception e) {
                throw new RuntimeException("catch me");  // quick hack
            }
        }
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
