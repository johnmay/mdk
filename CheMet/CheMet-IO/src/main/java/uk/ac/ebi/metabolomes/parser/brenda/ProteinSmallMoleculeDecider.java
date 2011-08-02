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
public class ProteinSmallMoleculeDecider {

    public static boolean isProteinName(String name) {
        if(Pattern.compile("(?i)kinase").matcher(name).find()) return true;
        if(Pattern.compile("(?i)protein").matcher(name).find()) return true;
else if(Pattern.compile("(?i)istone").matcher(name).find()) return true;
else if(Pattern.compile("(?i)doxin").matcher(name).find()) {
    if(Pattern.compile("(?i)pyridoxine").matcher(name).find())
        return false;
    return true;
}
else if(Pattern.compile("(?i)casein").matcher(name).find()) return true;
else if(Pattern.compile("(?i)collagen").matcher(name).find()) return true;
else if(Pattern.compile("(?i)elastin").matcher(name).find()) return true;
//else if(Pattern.compile("(?i)in$").matcher(name).find()) return true;
//else if(Pattern.compile("(?i)in ").matcher(name).find()) return true;
else if(Pattern.compile("(?i)receptor").matcher(name).find()) return true;
else if(Pattern.compile("(?i)channel").matcher(name).find()) return true;
else if(Pattern.compile("(?i)BRCA").matcher(name).find()) return true;
else if(Pattern.compile("(?i)calmodulin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)claudin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)enzyme").matcher(name).find()) {
    if(Pattern.compile("(?i)coenzyme").matcher(name).find())
        return false;
    return true;
}
else if(Pattern.compile("(?i)factor").matcher(name).find()) {
 if(Pattern.compile("(?i)cofactor").matcher(name).find())
     return false;
 return true;
}
else if(Pattern.compile("(?i)cytochrome").matcher(name).find()) return true;
else if(Pattern.compile("(?i)fibrin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)fibronect").matcher(name).find()) return true;
else if(Pattern.compile("(?i)kininogen").matcher(name).find()) return true;
else if(Pattern.compile("(?i)ase[ \\]] ").matcher(name).find()) return true;
else if(Pattern.compile("(?i)ase$").matcher(name).find()) return true;
else if(Pattern.compile("(?i)synthase").matcher(name).find()) return true;
else if(Pattern.compile("(?i)glycogenin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)receptor").matcher(name).find()) return true;
else if(Pattern.compile("(?i)globin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)myosin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)paxilin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)gelsolin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)catenin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)phosphorylated").matcher(name).find()) return true;
else if(Pattern.compile("(?i)plasmin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)caspase").matcher(name).find()) return true;
else if(Pattern.compile("(?i)proteas").matcher(name).find()) return true;
else if(Pattern.compile("(?i)thrombin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)rhodamin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)smad").matcher(name).find()) return true;
else if(Pattern.compile("(?i)septin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)thrombospondin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)vimentin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)tubulin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)rhodopsin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)protamine").matcher(name).find()) return true;
else if(Pattern.compile("(?i)transferrin").matcher(name).find()) return true;
else if(Pattern.compile("(?i)proteglycan").matcher(name).find()) return true;
return false;
    }
}
