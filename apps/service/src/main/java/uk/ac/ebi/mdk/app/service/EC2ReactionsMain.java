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
import net.sf.jnati.FileUtils;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.entity.ReconstructionImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/** @author John May */
public class EC2ReactionsMain {

    public static void main(String[] args) throws IOException {

        EC2Reactions ec2rxns = EC2Reactions.usingKegg();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter reconstruction id:");
        String id = scanner.nextLine();
        System.out.print("Taxon id:");
        int taxon = Integer.parseInt(scanner.nextLine());
        System.out.print("Organism name:");
        String name = scanner.nextLine();

        ReconstructionImpl recon = new ReconstructionImpl(new ReconstructionIdentifier(id),
                                                          "",
                                                          name);

        recon.setTaxonomy(new Taxonomy(taxon, "", Taxonomy.Kingdom.BACTERIA, name, name));

        CSVReader csv = new CSVReader(new FileReader(args[0]),
                                      ',', '\0');
        long t0 = System.nanoTime();
        List<MetabolicReaction> rxns = ec2rxns.fromTable(csv, 1);
        long t1 = System.nanoTime();
        for (MetabolicReaction rxn : rxns) {
            recon.addReaction(rxn);
        }
        System.out.println(TimeUnit.NANOSECONDS.toMillis(t1 - t0) + " ms");
        File parent = new File(new File(args[0]).getParent());
        File dest = new File(parent, recon.getAccession() + ".mr");
        if(dest.exists()) {
            System.out.print("overwirte " + dest + "? [y/n]");
            if(scanner.nextLine().equalsIgnoreCase("y")){
                FileUtils.delTree(dest);
            }
        }
        ReconstructionIOHelper.write(recon, dest);
        System.out.println("reconstruction saved to: " + dest);
    }
}
