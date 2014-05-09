/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

import net.sf.jnati.FileUtils;
import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.tool.domain.StructuralValidity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author John May
 */
public class UpdateReconInformation {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        final String path  = "/Users/johnmay/analysis/iYO844.mr";
        final File   input = new File(path);
        
        Reconstruction reconstruction = ReconstructionIOHelper.read(new File(path));

        System.out.println("   id: " + reconstruction.getAccession());
        System.out.println(" name: " + reconstruction.getName());
        System.out.println(" abrv: " + reconstruction.getAbbreviation());

        System.out.print("update: id (1), name (2), abrv (3), or quit (q):");
        
        Scanner scanner = new Scanner(System.in);
        String  line = null;
        while ((line = scanner.nextLine()) != null) {
            if (line.equalsIgnoreCase("q")) 
                break;
            if (line.equals("1")) {
                System.out.print("new id: ");
                reconstruction.getIdentifier().setAccession(scanner.nextLine());
            }
            else if (line.equals("2")) {
                System.out.print("new name: ");
                reconstruction.setName(scanner.nextLine());
            }
            else if (line.equals("3")) {
                System.out.print("new abrv: ");
                reconstruction.setAbbreviation(scanner.nextLine());
            } else {
                System.out.print("update: id (1), name (2), abrv (3), or quit (q):");
                continue;
            }
            System.out.println("   id: " + reconstruction.getAccession());
            System.out.println(" name: " + reconstruction.getName());
            System.out.println(" abrv: " + reconstruction.getAbbreviation());
        }

        File backup = new File(path + ".bak");
        if (input.renameTo(backup)) {
            ReconstructionIOHelper.write(reconstruction, new File(path));
            delete(backup);
            System.out.println("[INFO] update successful.");
        } else {
            System.err.println("[ERROR] could not backup existing save.");
        }
        
    }
    
    static void delete(File f) throws IOException {
        if (f == null) return;
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }
    
}
