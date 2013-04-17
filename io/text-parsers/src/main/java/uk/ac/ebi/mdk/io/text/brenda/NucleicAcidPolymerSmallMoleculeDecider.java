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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.io.text.brenda;

import java.util.regex.Pattern;

/**
 * Class to decide whether a name belongs to a small molecule or a protein
 * @author pmoreno
 */
public class NucleicAcidPolymerSmallMoleculeDecider {

    private static final Pattern rRNAPattern = Pattern.compile("(?)rRNA");
    private static final Pattern tRNAPattern = Pattern.compile("(?)tRNA");
    private static final Pattern sRNAPattern = Pattern.compile("(?)sRNA");
    private static final Pattern mRNAPattern = Pattern.compile("(?)mRNA");
    private static final Pattern RNAPattern = Pattern.compile("(?)[ \\)]RNA");
    private static final Pattern DNAPattern = Pattern.compile("(?)DNA");
    private static final Pattern deoxyribonucPolPattern = Pattern.compile("(?i)deoxyribonucleotide\\)[mn]");

    public static boolean isRNAName(String name) {
        if (rRNAPattern.matcher(name).find()) {
            return true;
        } else if (tRNAPattern.matcher(name).find()) {
            return true;
        } else if (sRNAPattern.matcher(name).find()) {
            return true;
        } else if (mRNAPattern.matcher(name).find()) {
            return true;
        } else if (deoxyribonucPolPattern.matcher(name).find()) {
            return true;
        }
        return false;
    }
    
    public static boolean isDNAName(String name) {
        if(DNAPattern.matcher(name).find()) {
            return true;
        }
        return false;
    }
}
