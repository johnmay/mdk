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

package uk.ac.ebi.mdk.apps.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.hash.HashGeneratorMaker;
import org.openscience.cdk.hash.MoleculeHashGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.tree.FormulaHash;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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

        long t0 = System.nanoTime();
        Multimap<Integer, IAtomContainer> lookup = HashMultimap.create();
        Map<IAtomContainer, Metabolite> metaboliteMap = new HashMap<IAtomContainer, Metabolite>();

        FormulaHash key = FormulaHash.WithHydrogens;

        for (Metabolite m : reference.metabolome()) {
            for (ChemicalStructure cs : m.getStructures()) {
                lookup.put(hash2(cs.getStructure()), cs.getStructure());
                metaboliteMap.put(cs.getStructure(), m);
            }
        }
        //MetaboliteAligner aligner = new MetaboliteAligner(reference);
        long t1 = System.nanoTime();
        System.out.println(lookup.size() + " (" + lookup.keySet().size() + " bins)" + " " + (lookup.size() / (double) lookup.keySet().size()));
        System.out.println(TimeUnit.NANOSECONDS.toMillis(t1 - t0) + " ms to index ");


        Map<Integer, Integer> freq = new TreeMap<Integer, Integer>();
        for (Map.Entry<Integer, Collection<IAtomContainer>> map : lookup.asMap().entrySet()) {
            int n = map.getValue().size();
            Integer cnt;
            freq.put(n, (cnt = freq.get(n)) == null ? 1 : 1 + cnt);
        }


        for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
            System.out.println(e.getKey() + "," + e.getValue() + "," + "ec-H");
        }

    }


    static MoleculeHashGenerator generator = new HashGeneratorMaker().depth(4)
                                                                     .suppressHydrogens()
                                                                     .elemental()
                                                                     .chiral()
                                                                     .molecular();


    static int hash2(IAtomContainer container) {
        long key = generator.generate(container);
        return (int) (key ^ key >>> 32);
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
