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
import org.apache.commons.cli.Option;
import org.apache.log4j.Logger;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BondOrderSumSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ConnectedAtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.StereoSeed;
import uk.ac.ebi.mdk.tool.MappedEntityAligner;
import uk.ac.ebi.mdk.prototype.hash.MolecularHash;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;
import uk.ac.ebi.mdk.tool.match.EntityAligner;
import uk.ac.ebi.mdk.tool.match.MetaboliteHashCodeMatcher;
import uk.ac.ebi.mdk.tool.match.NameMatcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tool finds cases where molecules match on structure but not on name
 * <p/>
 * Rubbish code for quick checks on hash coding problems
 *
 * @author John May
 */
public class FindMismatches extends CommandLineMain {

    private static final Logger LOGGER = Logger.getLogger(FindMismatches.class);

    public static void main(String[] args) {
        new FindMismatches().process(args);
    }

    @Override
    public void setupOptions() {
        add(new Option("q", "query", true, "Query reconstruction"));
        add(new Option("r", "reference", true, "Reference reconstruction"));
        add(new Option("s", "synonyms", false, "Include synonyms?"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process() {


        Reconstruction query = getReconstruction(getFile("q"));
        Reconstruction reference = getReconstruction(getFile("r"));

        EntityAligner<Metabolite> aligner = new MappedEntityAligner<Metabolite>(reference.metabolome().toList(),
                                                                                true,
                                                                                true);

        MetaboliteHashCodeMatcher hashCodeMatcher = new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                                                                  BondOrderSumSeed.class,
                                                                                  ConnectedAtomSeed.class,
                                                                                  ChargeSeed.class,
                                                                                  StereoSeed.class);

        MolecularHashFactory.getInstance().setDepth(1);
        aligner.push(hashCodeMatcher);
//        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
//                                                   BondOrderSumSeed.class,
//                                                   ConnectedAtomSeed.class,
//                                                   StereoSeed.class));
//        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
//                                                   BondOrderSumSeed.class,
//                                                   ConnectedAtomSeed.class,
//                                                   ChargeSeed.class));
//        aligner.push(new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
//                                                   BondOrderSumSeed.class,
//                                                   ConnectedAtomSeed.class));

        NameMatcher matcher = new NameMatcher<Metabolite>(true, has("s"));

        int matched = 0;
        int unmatched = 0;

        File molRoot = new File("/Users/johnmay/Desktop/hash-evaluation/mdl");
        molRoot.mkdirs();
        CSVWriter summaryWriter = null;
        try {
            summaryWriter = new CSVWriter(new FileWriter("/Users/johnmay/Desktop/hash-evaluation/mismatches.tsv"), '\t', '\0');
        } catch (IOException e) {
        }

        for (Metabolite metabolite : query.metabolome()) {

            List<Metabolite> matches = aligner.getMatches(metabolite);

            matched += matches.isEmpty() ? 0 : 1;
            unmatched += matches.isEmpty() ? 1 : 0;

            System.out.println(metabolite.getName() + ": ");
            for (Metabolite match : matches) {
                boolean nameMatch = matcher.matches(metabolite, match);
                System.out.println("\t" + match.getIdentifier() + " [" + nameMatch + "]");
                if (!nameMatch) {

                    MolecularHash ref = hashCodeMatcher.getHash(match.getStructures().iterator().next().getStructure());

                    for (ChemicalStructure structure : metabolite.getStructures()) {


                    }


                    List<ChemicalStructure> structures = new ArrayList<ChemicalStructure>(metabolite.getStructures());
                    for (int i = 0; i < structures.size(); i++) {
                        try {
                            ChemicalStructure cs = structures.get(i);

                            MolecularHash que = hashCodeMatcher.getHash(cs.getStructure());


                            summaryWriter.writeNext(new String[]{
                                    metabolite.getAccession() + "-" + (i + 1),
                                    match.getAccession(),
                                    metabolite.getName(),
                                    match.getName(),
                                    Integer.toString(que.hash),
                                    Integer.toString(ref.hash),
                                    que.toString(),
                                    ref.toString()
                            });
                            writeMDL(cs.getStructure(), metabolite.getAccession() + "-" + (i + 1), molRoot);
                        } catch (IOException e) {
                        } catch (CDKException e) {
                        }
                    }

                    try {
                        writeMDL(match.getStructures().iterator().next().getStructure(),
                                 match.getAccession(),
                                 molRoot);
                    } catch (IOException e) {
                    } catch (CDKException e) {
                    }


                }


            }
        }

        try {
            summaryWriter.close();
        } catch (IOException e) {
        }

        System.out.println(matched + " matched");
        System.out.println(unmatched + " unmatched");

    }


    private static void writeMDL(IAtomContainer container, String id, File root) throws IOException,
            CDKException {

        id = id.replaceAll("/", "-");

        container.setProperty(CDKConstants.TITLE, id);

        File f = new File(root, id + ".mol");

        MDLV2000Writer mdlWriter = new MDLV2000Writer(new FileWriter(f));
        mdlWriter.write(container);
        mdlWriter.close();

    }


    public Reconstruction getReconstruction(File file) {
        try {
            return ReconstructionIOHelper.read(file);
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
        throw new InvalidParameterException("Reconstruction " + file + " was not read!");
    }
}
