/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.parser.brenda;

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
