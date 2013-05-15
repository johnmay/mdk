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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.cli.Option;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openscience.cdk.hash.MoleculeHashGenerator;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.prototype.hash.HashGeneratorMaker;
import uk.ac.ebi.mdk.tool.domain.TransportReactionUtil;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Suggest <i>possible</i> metabolites which could be exchanged.
 *
 * @author John May
 */
public final class SuggestExchangeMetabolites extends CommandLineMain {

    private MoleculeHashGenerator generator = new HashGeneratorMaker()
            .withDepth(8)
            .buildNew();

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.ERROR);
        new SuggestExchangeMetabolites().process(args);
    }

    @Override public void setupOptions() {
        add(new Option("i", "input", true,
                       "reconstruction to suggest exchange for"));
        add(new Option("r", "reference", true,
                       "reference reconstruction to get suggestions from"));
    }

    @Override public void process() {

        System.out.print("Reading reconstructions...");
        Reconstruction input = read(getFile("input"));
        Reconstruction reference = read(getFile("reference"));
        System.out.println("done");

        // find compounds which are exchange in the 'reference' and list
        // - those which are exchanged and present the input
        // - those which are terminal in the input
        Multimap<Metabolite, MetabolicReaction> exchanged = exchanged(reference);
        Multimap<Long, Metabolite> index = index(exchanged.keySet());

        BiMap<UUID, Integer> metaboliteIdx = HashBiMap.create(input.metabolome()
                                                                   .size());
        int i = 0;
        for (Metabolite m : input.metabolome())
            metaboliteIdx.put(m.uuid(), i++);
        int[] occurences = new int[input.metabolome().size()];

        for (MetabolicReaction rxn : input.reactome()) {
            for (MetabolicParticipant p : rxn.getParticipants()) {
                occurences[metaboliteIdx.get(p.getMolecule().uuid())]++;
            }
        }

        for (final Metabolite metabolite : input.metabolome()) {
            for (ChemicalStructure structure : metabolite.getStructures()) {
                long hashCode = generator.generate(structure.getStructure());
                if (index.containsKey(hashCode)) {
                    for (Metabolite refMetabolite : index.get(hashCode)) {
                        for (MetabolicReaction rxn : exchanged
                                .get(refMetabolite)) {
                            System.out.println(metabolite
                                                       + "\t"
                                                       + occurences[metaboliteIdx.get(metabolite.uuid())]
                                                       + "\t"
                                                       + rxn);
                        }
                    }
                }
            }
        }
    }

    private Multimap<Long, Metabolite> index(final Set<Metabolite> metabolites) {
        Multimap<Long, Metabolite> index = HashMultimap.create(metabolites
                                                                       .size(), 2);
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
