/**
 * PubChemSubstanceSDFRecord.java
 *
 * 2011.10.27
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.io;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.resource.chemical.KeggGlycanIdentifier;
import uk.ac.ebi.resource.chemical.KEGGDrugIdentifier;

/**
 * @name    PubChemSubstanceSDFRecord
 * @date    2011.10.27
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class PubChemSubstanceSDFRecord extends SDFRecord {

    private static final Logger LOGGER = Logger.getLogger(PubChemSubstanceSDFRecord.class);
    private String potentialExtId;
    private String potentialExtDB;
    private String potentialExtDBURL;

    void setPotentialExternalID(String line) {
        this.potentialExtId = line;
    }

    void setPotentialExternalDB(String line) {
        this.potentialExtDB = line;
    }

    void setPotentialExternalDBURL(String line) {
        this.potentialExtDBURL = line;
    }

    @Override
    public void computeWhenArchiving() {
        super.computeWhenArchiving();
        String extDbRef="";
        try {
        if (potentialExtId != null) {
            if (potentialExtDB != null) {
                //addCrossReference(potentialExtDB, potentialExtId);
                extDbRef = potentialExtDB;
            } else if (potentialExtDBURL != null) {
                //addCrossReference(potentialExtDBURL, potentialExtId);
                extDbRef = potentialExtDBURL;
            }
            extDbRef = resolveAmbiguous(extDbRef,potentialExtId);
            addCrossReference(extDbRef, potentialExtId);
        }
        } catch(InvalidParameterException e) {
                LOGGER.warn("Could not recognize db: "+extDbRef+" skipping.");
        }
        if(getSynonyms().size()>0 && getName()==null)
            setName(getSynonyms().get(0));
        else
            setName("");
        
        // Process synonyms
        // In the case of pubchem substances, some database links are stored as synonyms
        // for instance:
        // EINECS 202-551-4
        // EPA Pesticide Chemical Code 055102
        // HSDB 5306
        Matcher matcher=null;
        Set<String> syns = new HashSet<String>();
        syns.addAll(getSynonyms());
        for (String syn : syns) {
            matcher = einecDBPat.matcher(syn);
            if(matcher.matches()) {
                addCrossReference("EINECS", matcher.group(1));
                continue;
            }
            /*matcher = zincDBPat.matcher(syn);
            if(matcher.matches()) {
                addCrossReference("ZINC", matcher.group(1));
                continue;
            }*/
            matcher = HSDBPat.matcher(syn);
            if(matcher.matches()) {
                addCrossReference("HSDB", matcher.group(1));
                continue;
            }
            matcher = epaPesticidePat.matcher(syn);
            if(matcher.matches()) {
                addCrossReference("EPA_PESTICIDE", matcher.group(1));
                continue;
            }
            matcher = BrnRegNumPat.matcher(syn);
            if(matcher.matches()) {
                addCrossReference("BRN", matcher.group(1));
                continue;
            }
        }
    }

    private final Pattern keggCompoundIdPattern = Pattern.compile("[Cc]\\d{5}"); 
    private final Pattern keggGlycanIdPattern = Pattern.compile("[Gg]\\d{5}"); 
    private final Pattern keggDrugIdPattern = Pattern.compile("[Dd]\\d{5}"); 
    private final Pattern casRegNumPat = Pattern.compile("\\d+-\\d+-\\d+");
    // BRN : I think is the Belstein Registry Number
    private final Pattern BrnRegNumPat = Pattern.compile("BRN\\s{0,1}(\\d+)");
    // The EPA database of pesticide chemicals
    private final Pattern epaPesticidePat = Pattern.compile("EPA Pesticide Chemical Code (\\d+)");
    // Zinc is a database of comercially available compounds for virtual screening.
    private final Pattern zincDBPat = Pattern.compile("ZINC(\\d+)");
    // Einecs is a european database of comercialy available compounds.
    private final Pattern einecDBPat = Pattern.compile("EINECS\\s*(\\d+-\\d+-\\d+)");
    // The HSDB Database is the Hazardous substance database.
    private final Pattern HSDBPat = Pattern.compile("HSDB\\s{0,1}(\\d+)");
    /**
     * There are some cases in which a the name of the resource is ambiguous since it could be referring to compounds,
     * proteins or drugs for instance (like KEGG, which could be for KEGG Compound or KEGG Drug, or Glycan, etc.).
     * @param extDbRef
     * @param extID
     * @return 
     */
    private String resolveAmbiguous(String extDbRef, String extID) {
        if(extDbRef.equalsIgnoreCase("KEGG")) {
            LOGGER.info("Kegg id: "+extID);
            if(extID!=null && keggCompoundIdPattern.matcher(extID).matches())
                return KEGGCompoundIdentifier.IDENTIFIER_LOADER.getShortDescription(KEGGCompoundIdentifier.class);
            else if(extID!=null && keggGlycanIdPattern.matcher(extID).matches())
                return KeggGlycanIdentifier.IDENTIFIER_LOADER.getShortDescription(KeggGlycanIdentifier.class);
            else if(extID!=null && keggDrugIdPattern.matcher(extID).matches())
                return KEGGDrugIdentifier.IDENTIFIER_LOADER.getShortDescription(KEGGDrugIdentifier.class);
        }
        return extDbRef;
    }
}
