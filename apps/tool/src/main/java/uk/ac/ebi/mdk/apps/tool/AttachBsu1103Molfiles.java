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

import uk.ac.ebi.mdk.apps.io.ReconstructionIOHelper;
import uk.ac.ebi.mdk.domain.annotation.Molfile;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John May
 */
public final class AttachBsu1103Molfiles {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final String path = "/Users/johnmay/repo/iBsubtilis1437.mr";
        Reconstruction reconstruction = ReconstructionIOHelper.read(new File(path));      

        File moldir = new File("/Users/johnmay/EBI/Project/models/published/iBsu1103-molfiles");
        File[] mols = moldir.listFiles(new FileFilter() {
            @Override public boolean accept(File pathname) {
                return pathname.getName().endsWith(".mol");
            }
        });

        Map<String,Metabolite> abrvMap = new HashMap<String, Metabolite>();
        for (Metabolite m : reconstruction.metabolome()) {
            if (abrvMap.put(m.getAbbreviation(), m) != null)
                System.err.println(m.getAbbreviation() + " already added");
        }

        for (File f : mols) {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            br.close();
            String abrv = f.getName().substring(0, f.getName().length() - 4);
            Metabolite m = abrvMap.get(abrv);
            if (m == null) {
                System.err.println(abrv + " not found");
                continue;
            }
            m.addAnnotation(new Molfile(sb.toString()));
        }
        
        ReconstructionIOHelper.write(reconstruction, new File("/Users/johnmay/repo/iBsubtilis1437+molfiles.mr"));
        System.out.println("//");
        ReconstructionIOHelper.read(new File("/Users/johnmay/repo/iBsubtilis1437+molfiles.mr"));

    }

}
