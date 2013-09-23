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

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.Option;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.HybridizationFingerprinter;
import org.openscience.cdk.hash.HashGeneratorMaker;
import org.openscience.cdk.hash.MoleculeHashGenerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.tool.domain.TransportReactionUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Suggest <i>possible</i> metabolites which could be exchanged.
 *
 * @author John May
 */
public final class SuggestSubstructureExchangeMetabolites extends CommandLineMain {

    private MoleculeHashGenerator generator = new HashGeneratorMaker().depth(8)
                                                                      .elemental()
                                                                      .molecular();
    private EntityFactory         entities  = DefaultEntityFactory.getInstance();

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.ERROR);
        new SuggestSubstructureExchangeMetabolites().process(args);
    }

    @Override public void setupOptions() {
        add(new Option("i", "input", true,
                       "reconstruction to suggest exchange for"));
        add(new Option("r", "reference", true,
                       "reference reconstruction to get suggestions from"));
        add(new Option("o", "output", true,
                       "output reconstruction with potential exchange reactions"));
    }

    @Override public void process() {

        System.out.print("Reading reconstructions...");
        Reconstruction input = read(getFile("input"));
        Reconstruction reference = read(getFile("reference"));
        System.out.println("done");

        System.out.println("Using " + reference.getIdentifier() + " to find transport reactions in " + input.getIdentifier());

        // find compounds which are exchange in the 'reference' and list
        // - those which are exchanged and present the input
        // - those which are terminal in the input
        Multimap<Metabolite, MetabolicReaction> exchanged = exchanged(reference);
        Multimap<Long, Metabolite> index = index(exchanged.keySet());

        SubstructureSet ss = buildSubstructureSet(index.values());

//        BiMap<UUID, Integer> metaboliteIdx = HashBiMap.create(input.metabolome()
//                                                                   .size());
//        
//
//        Reconstruction output = entities.newReconstruction();
//        output.setIdentifier(new ReconstructionIdentifier(input.getIdentifier().getAccession() + "-" + reference.getIdentifier().getAccession() + "-ex"));
//        output.setTaxonomy(input.getTaxonomy());
//
        CSVWriter csv = new CSVWriter(new OutputStreamWriter(System.out), ',', '"');
//
//        // for each metabolite from the input we check if the compound is
//        // exchanged in the reference - if it is exchanged we print the information
//        // on the exchange reactions
        for (final Metabolite metabolite : input.metabolome()) {
            for (ChemicalStructure structure : metabolite.getStructures()) {
                for (Metabolite refMetabolite : ss.matching(structure.getStructure())) {


                    for (MetabolicReaction rxn : exchanged.get(refMetabolite)) {
                        csv.writeNext(new String[]{metabolite.toString(),
                                                   refMetabolite.getAccession(),
                                                   rxn.getAccession(),
                                                   rxn.getAbbreviation(),
                                                   rxn.toString()});
//                            output.addReaction(rxn);
                    }
                }
            }
        }

        try {
            csv.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
//
//        try {
//            System.out.println("Writing reconstruction exchange reactions: " + output.getContainer());            
//            ReconstructionIOHelper.write(output, output.getContainer());
//
//        } catch (IOException e) {
//            System.err.println(e.getMessage());
//        }
    }
    
    private boolean isPseudo(Metabolite m) {
        for (ChemicalStructure cs : m.getStructures()) {
            for (IAtom a : cs.getStructure().atoms()) {
                if (a instanceof IPseudoAtom)
                    return true;
            }                    
        }
        return false;
    }

    private SubstructureSet buildSubstructureSet(Collection<Metabolite> ms) {
        try {
            Collection<Metabolite> generic = new ArrayList<Metabolite>();
            for (Metabolite m : ms) {
                if(isPseudo(m))
                    generic.add(m);
            }
            
            return new SubstructureSet(new HybridizationFingerprinter(),
                                       generic);
        } catch (CDKException e) {
            throw new IllegalArgumentException("cannot build set");
        }
    }

    private static final void perceiveAtomTypes(Reconstruction reconstruction) throws CDKException {
        for (Metabolite m : reconstruction.metabolome()) {
            for (ChemicalStructure cs : m.getStructures()) {
                AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(cs.getStructure());
            }
        }
    }

    private Multimap<Long, Metabolite> index(final Set<Metabolite> metabolites) {
        Multimap<Long, Metabolite> index = HashMultimap.create(metabolites.size(), 2);
        for (final Metabolite metabolite : metabolites) {
            for (ChemicalStructure structure : metabolite.getStructures()) {
                long hashCode = generator.generate(structure.getStructure());
                index.put(hashCode, metabolite);
            }
        }
        if (index.isEmpty()) {
            System.err
                  .println("No metabolites were indexed, do they have strucutres?");
        }
        return index;
    }

    /**
     * Map the metabolites which are exchanged in a reconstruction and the
     * reactions in which they are exchanged.
     *
     * @param reconstruction reconstruction instance
     * @return exchanged metabolites
     */
    private Multimap<Metabolite, MetabolicReaction> exchanged(final Reconstruction reconstruction) {
        final Multimap<Metabolite, MetabolicReaction> map = HashMultimap
                .create();
        for (final MetabolicReaction reaction : reconstruction.reactome()) {
            for (Metabolite metabolite : TransportReactionUtil
                    .exchanged(reaction)) {
                map.put(metabolite, reaction);
            }
        }
        return map;
    }

    /**
     * Read a reconstruction for the given file name.
     *
     * @param f a file
     * @return read reconstruction
     * @throws IllegalArgumentException thrown if there was a problem reading
     *                                  the reconstruction
     */
    private Reconstruction read(final File f) {
        try {
            return ReconstructionIOHelper.read(f);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
