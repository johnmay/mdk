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
    
    private static final Pattern ase_end_Pattern = Pattern.compile("(?i)ase$");
    private static final Pattern ase_sep_Pattern = Pattern.compile("(?i)ase[ \\]] ");
    private static final Pattern acpPattern = Pattern.compile("(?i)\\[acp\\]"); // acp : acyl carrier protein
    private static final Pattern acpPattern2 = Pattern.compile("(?i)\\-ACPs{0,1}$"); // acp : acyl carrier protein
    private static final Pattern acpPattern3 = Pattern.compile("(?)\\-ACP\\-"); // acp : acyl carrier protein
    private static final Pattern brcaPattern = Pattern.compile("(?i)BRCA");
    private static final Pattern eIF5aPrecPattern = Pattern.compile("(?i)eIF5A-precursor");
    private static final Pattern calmodulinPattern = Pattern.compile("(?i)calmodulin");
    private static final Pattern caseinPattern = Pattern.compile("(?i)casein");
    private static final Pattern caspasePattern = Pattern.compile("(?i)caspase");
    private static final Pattern carboxylasePattern = Pattern.compile("(?i)carboxylase");
    private static final Pattern cateninPattern = Pattern.compile("(?i)catenin");
    private static final Pattern channelPattern = Pattern.compile("(?i)channel");
    private static final Pattern claudinPattern = Pattern.compile("(?i)claudin");
    private static final Pattern coenzymeAntiPattern = Pattern.compile("(?i)coenzyme");
    private static final Pattern cofactorAntiPattern = Pattern.compile("(?i)cofactor");
    private static final Pattern collagenPattern = Pattern.compile("(?i)collagen");
    private static final Pattern cytochromePattern = Pattern.compile("(?i)cytochrome");
    private static final Pattern doxinPattern = Pattern.compile("(?i)doxin");
    private static final Pattern dehydrogenasePattern = Pattern.compile("(?i)hydrogenase");
    private static final Pattern elastinPattern = Pattern.compile("(?i)elastin");
    private static final Pattern enzymePattern = Pattern.compile("(?i)enzyme");
    private static final Pattern factorPattern = Pattern.compile("(?i)factor");
    private static final Pattern fibrinPattern = Pattern.compile("(?i)fibrin");
    private static final Pattern fibronectinPattern = Pattern.compile("(?i)fibronect");
    private static final Pattern gelsolinPattern = Pattern.compile("(?i)gelsol+in");
    private static final Pattern globinPattern = Pattern.compile("(?i)globin");
    private static final Pattern glycogeninPattern = Pattern.compile("(?i)glycogenin");
    private static final Pattern histonePattern = Pattern.compile("(?i)istone");
    private static final Pattern kinasePattern = Pattern.compile("(?i)kinase");
    private static final Pattern kininogenPattern = Pattern.compile("(?i)kininogen");
    private static final Pattern ligasePattern = Pattern.compile("(?i)ligase");
    private static final Pattern myosinPattern = Pattern.compile("(?i)myosin");
    private static final Pattern paxilinPattern = Pattern.compile("(?i)pax+il+in");
    private static final Pattern phosphorylatedPattern = Pattern.compile("(?i)phosphorylated");
    // [phosphorylase a] still goes undetected
    private static final Pattern phosphorylasePattern = Pattern.compile("(?i)phosphorylase");
    private static final Pattern plasminPattern = Pattern.compile("(?i)plasmin");
    private static final Pattern protaminePattern = Pattern.compile("(?i)protamine");
    private static final Pattern proteasPattern = Pattern.compile("(?i)proteas");
    private static final Pattern proteinPattern = Pattern.compile("(?i)protein");
    private static final Pattern proteoglycanPattern = Pattern.compile("(?i)proteglycan");
    private static final Pattern pyridoxinePattern = Pattern.compile("(?i)pyridoxine");
    private static final Pattern receptorPattern = Pattern.compile("(?i)receptor");
    private static final Pattern rhodaminPattern = Pattern.compile("(?i)rhodamin");
    private static final Pattern rhodopsinPattern = Pattern.compile("(?i)rhodopsin");
    private static final Pattern septinPattern = Pattern.compile("(?i)septin");
    private static final Pattern smadPattern = Pattern.compile("(?i)smad");
    private static final Pattern synthasePattern = Pattern.compile("(?i)synthase");
    private static final Pattern thrombinPattern = Pattern.compile("(?i)thrombin");
    private static final Pattern thrombospondinPattern = Pattern.compile("(?i)thrombospondin");
    private static final Pattern transferrinPattern = Pattern.compile("(?i)transferrin");
    private static final Pattern transferasePattern = Pattern.compile("(?i)transferase");
    private static final Pattern tubulinPattern = Pattern.compile("(?i)tubulin");
    private static final Pattern vimentinPattern = Pattern.compile("(?i)vimentin");
    private static final Pattern cohesinPattern = Pattern.compile("(?i)cohesin");
    private static final Pattern angiotensinPattern = Pattern.compile("(?i)angiotensin");
    private static final Pattern cadherinPattern = Pattern.compile("(?i)cadherin");
    private static final Pattern domainsEndingPattern = Pattern.compile("(?i)-domains{0,1}$");
    private static final Pattern transporterPattern = Pattern.compile("(?i)transporter");
    private static final Pattern lipasePattern = Pattern.compile("(?i)lipase");
    private static final Pattern gelatinasePattern = Pattern.compile("(?i)gelatinase");
    private static final Pattern polymerasePattern = Pattern.compile("(?i)polymerase");
    private static final Pattern bccpPattern = Pattern.compile("(?)BCCP-");
    private static final Pattern bccpPattern2 = Pattern.compile("(?i)-BCCP");
    private static final Pattern SMURFPattern = Pattern.compile("(?i)SMURF");
    private static final Pattern ubiquitinPattern = Pattern.compile("(?i)Ubiquitin");
    private static final Pattern rnasePattern = Pattern.compile("(?i)RNASE");
    private static final Pattern interleukinPattern = Pattern.compile("(?i)interleukin");
    private static final Pattern semenogelinPattern = Pattern.compile("(?i)semenogel+in");
    private static final Pattern synaptotagminPattern = Pattern.compile("(?i)synaptotagmin");
    private static final Pattern antigenPattern = Pattern.compile("(?i)antigen");
    private static final Pattern reninPattern = Pattern.compile("(?i)^Renin");
    private static final Pattern filaggrinPattern = Pattern.compile("(?i)fil+ag+rin");
    private static final Pattern keratinPattern = Pattern.compile("(?i)keratin");
    private static final Pattern stromelysinPattern = Pattern.compile("(?i)stromelysin");
    private static final Pattern caveolinPattern = Pattern.compile("(?i)caveolin");
    
    
    
    public static boolean isProteinName(String name) {
        if (kinasePattern.matcher(name).find()) {
            return true;
        }
        if (proteinPattern.matcher(name).find()) {
            return true;
        } else if (histonePattern.matcher(name).find()) {
            return true;
        } else if (doxinPattern.matcher(name).find()) {
            if (pyridoxinePattern.matcher(name).find()) {
                return false;
            }
            return true;
        } else if (caseinPattern.matcher(name).find()) {
            return true;
        } else if (carboxylasePattern.matcher(name).find()) {
            return true;
        } else if (dehydrogenasePattern.matcher(name).find()) {
            return true;
        } else if (collagenPattern.matcher(name).find()) {
            return true;
        } else if (elastinPattern.matcher(name).find()) {
            return true;
        } //else if(Pattern.compile("(?i)in$").matcher(name).find()) return true;
        //else if(Pattern.compile("(?i)in ").matcher(name).find()) return true;
        else if (receptorPattern.matcher(name).find()) {
            return true;
        } else if (channelPattern.matcher(name).find()) {
            return true;
        } else if (brcaPattern.matcher(name).find()) {
            return true;
        } else if (calmodulinPattern.matcher(name).find()) {
            return true;
        } else if (claudinPattern.matcher(name).find()) {
            return true;
        } else if (enzymePattern.matcher(name).find()) {
            if (coenzymeAntiPattern.matcher(name).find()) {
                return false;
            }
            return true;
        } else if (factorPattern.matcher(name).find()) {
            if (cofactorAntiPattern.matcher(name).find()) {
                return false;
            }
            return true;
        } else if (cytochromePattern.matcher(name).find()) {
            return true;
        } else if (fibrinPattern.matcher(name).find()) {
            return true;
        } else if (fibronectinPattern.matcher(name).find()) {
            return true;
        } else if (eIF5aPrecPattern.matcher(name).find()) {
            return true;
        } else if (kininogenPattern.matcher(name).find()) {
            return true;
        } else if (ligasePattern.matcher(name).find()) {
            return true;
        } else if (ase_sep_Pattern.matcher(name).find()) {
            return true;
        } else if (ase_end_Pattern.matcher(name).find()) {
            return true;
        } else if (acpPattern.matcher(name).find()) {
            return true;
        } else if (acpPattern2.matcher(name).find()) {
            return true;
        } else if (phosphorylasePattern.matcher(name).find()) {
            return true;
        } else if (domainsEndingPattern.matcher(name).find()) {
            return true;
        } else if (angiotensinPattern.matcher(name).find()) {
            return true;    
        } else if (cadherinPattern.matcher(name).find()) {
            return true;        
        } else if (cohesinPattern.matcher(name).find()) {
            return true;        
        } else if (transporterPattern.matcher(name).find()) {
            return true;
        } else if (ligasePattern.matcher(name).find()) {
            return true;    
        } else if (polymerasePattern.matcher(name).find()) {
            return true;    
        } else if (lipasePattern.matcher(name).find()) {
            return true;    
        } else if (gelatinasePattern.matcher(name).find()) {
            return true;    
        } else if (bccpPattern.matcher(name).find()) {
            return true;    
        } else if (SMURFPattern.matcher(name).find()) {
            return true;    
        } else if (acpPattern3.matcher(name).find()) {
            return true;    
        } else if (ubiquitinPattern.matcher(name).find()) {
            return true;    
        } else if (rnasePattern.matcher(name).find()) {
            return true;    
        } else if (interleukinPattern.matcher(name).find()) {
            return true;    
        } else if (semenogelinPattern.matcher(name).find()) {
            return true;    
        } else if (synaptotagminPattern.matcher(name).find()) {
            return true;    
        } else if (bccpPattern2.matcher(name).find()) {
            return true;    
        } else if (antigenPattern.matcher(name).find()) {
            return true;    
        } else if (reninPattern.matcher(name).find()) {
            return true;    
        } else if (filaggrinPattern.matcher(name).find()) {
            return true;    
        } else if (paxilinPattern.matcher(name).find()) {
            return true;    
        } else if (keratinPattern.matcher(name).find()) {
            return true;    
        } else if (stromelysinPattern.matcher(name).find()) {
            return true;    
        } else if (caveolinPattern.matcher(name).find()) {
            return true;    
        } else if (synthasePattern.matcher(name).find()) {
            return true;
        } else if (glycogeninPattern.matcher(name).find()) {
            return true;
        // repeated, present before
        //} else if (Pattern.compile("(?i)receptor").matcher(name).find()) {
        //    return true;
        } else if (globinPattern.matcher(name).find()) {
            return true;
        } else if (myosinPattern.matcher(name).find()) {
            return true;
        } else if (gelsolinPattern.matcher(name).find()) {
            return true;
        } else if (cateninPattern.matcher(name).find()) {
            return true;
        } else if (phosphorylatedPattern.matcher(name).find()) {
            return true;
        } else if (phosphorylatedPattern.matcher(name).find()) {
            return true;
        } else if (plasminPattern.matcher(name).find()) {
            return true;
        } else if (caspasePattern.matcher(name).find()) {
            return true;
        } else if (proteasPattern.matcher(name).find()) {
            return true;
        } else if (thrombinPattern.matcher(name).find()) {
            return true;
        } else if (rhodaminPattern.matcher(name).find()) {
            return true;
        } else if (smadPattern.matcher(name).find()) {
            return true;
        } else if (septinPattern.matcher(name).find()) {
            return true;
        } else if (thrombospondinPattern.matcher(name).find()) {
            return true;
        } else if (vimentinPattern.matcher(name).find()) {
            return true;
        } else if (tubulinPattern.matcher(name).find()) {
            return true;
        } else if (rhodopsinPattern.matcher(name).find()) {
            return true;
        } else if (protaminePattern.matcher(name).find()) {
            return true;
        } else if (transferrinPattern.matcher(name).find()) {
            return true;
        } else if (transferasePattern.matcher(name).find()) {
            return true;
        } else if (proteoglycanPattern.matcher(name).find()) {
            return true;
        }
        return false;
    }
}
