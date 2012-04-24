/**
 * PubChemSubstanceESummaryResult.java
 *
 * 2011.10.30
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
package uk.ac.ebi.metabolomes.webservices.eutils;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.DefaultIdentifierFactory;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KEGGDrugIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KeggGlycanIdentifier;

/**
 * @name    PubChemSubstanceESummaryResult
 * @date    2011.10.30
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class PubChemSubstanceESummaryResult extends ESummaryResult {

    private static final Logger LOGGER = Logger.getLogger(PubChemSubstanceESummaryResult.class);
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    public enum PubChemSubstanceESummaryListFields {

        SourceNameList, SynonymList;
    }

    public enum PubChemSubstanceESummarySingleFields {

        SID, SourceID, DBUrl;
    }
    private String id;
    private List<String> sourceNames = new ArrayList<String>();
    private List<String> sourceIdentifiers = new ArrayList<String>();
    private String DBUrl;
    private final List<String> synonyms = new ArrayList<String>();
    private final List<CrossReference> crossReferences = new ArrayList<CrossReference>();

    public List<PubChemSubstanceESummarySingleFields> getScalarKeywords() {
        return Arrays.asList(PubChemSubstanceESummarySingleFields.values());
    }

    public List<PubChemSubstanceESummaryListFields> getListKeywords() {
        return Arrays.asList(PubChemSubstanceESummaryListFields.values());
    }

    public void addListForKeyword(Enum keyword, List<String> parseList) {
        if (keyword instanceof PubChemSubstanceESummaryListFields) {
            switch ((PubChemSubstanceESummaryListFields) keyword) {
                case SourceNameList:
                    this.setSourceNames(parseList);
                    break;
                case SynonymList:
                    this.setSynonyms(parseList);
                    break;
            }
        }
    }

    public void addScalarForKeyword(Enum keyword, String followingCharacters) {
        if (keyword instanceof PubChemSubstanceESummarySingleFields) {
            switch ((PubChemSubstanceESummarySingleFields) keyword) {
                case SID:
                    this.setId(followingCharacters);
                    break;
                case DBUrl:
                    this.setDBUrl(followingCharacters);
                    break;
                case SourceID:
                    this.addSourceID(followingCharacters);
                    break;
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

    @Override
    public void wrap() {
        super.wrap();
        String extDbRef = "";
        try {
            if (sourceIdentifiers.size() > 0) {
                if (sourceNames.size() > 0) {
                    //addCrossReference(potentialExtDB, potentialExtId);
                    for (int i = 0; i < Math.min(sourceIdentifiers.size(), sourceNames.size()); i++) {
                        addCrossReference(resolveAmbiguous(sourceNames.get(i), sourceIdentifiers.get(i)), sourceIdentifiers.get(i));
                    }
                } else if (DBUrl != null) {
                    //addCrossReference(potentialExtDBURL, potentialExtId);
                    addCrossReference(resolveAmbiguous(DBUrl, sourceIdentifiers.get(0)),sourceIdentifiers.get(0));
                }
            }
        } catch (InvalidParameterException e) {
            LOGGER.warn("Could not recognize db: " + extDbRef + " skipping.");
        }
        /*if (getSynonyms().size() > 0 && getName() == null) {
            setName(getSynonyms().get(0));
        } else {
            setName("");
        }*/

        // Process synonyms
        // In the case of pubchem substances, some database links are stored as synonyms
        // for instance:
        // EINECS 202-551-4
        // EPA Pesticide Chemical Code 055102
        // HSDB 5306
        Matcher matcher = null;
        Set<String> syns = new HashSet<String>();
        syns.addAll(getSynonyms());
        for (String syn : syns) {
            matcher = einecDBPat.matcher(syn);
            if (matcher.matches()) {
                addCrossReference("EINECS", matcher.group(1));
                continue;
            }
            /*matcher = zincDBPat.matcher(syn);
            if(matcher.matches()) {
            addCrossReference("ZINC", matcher.group(1));
            continue;
            }*/
            matcher = HSDBPat.matcher(syn);
            if (matcher.matches()) {
                addCrossReference("HSDB", matcher.group(1));
                continue;
            }
            matcher = epaPesticidePat.matcher(syn);
            if (matcher.matches()) {
                addCrossReference("EPA_PESTICIDE", matcher.group(1));
                continue;
            }
            matcher = BrnRegNumPat.matcher(syn);
            if (matcher.matches()) {
                addCrossReference("BRN", matcher.group(1));
                continue;
            }
        }
    }
    
    private void addCrossReference(String extDbName, String extIdentifier) {
        try {
            Identifier ident = FACTORY.ofSynonym(extDbName);
            ident.setAccession(extIdentifier);
            CrossReference cr = new CrossReference(ident);
            this.crossReferences.add(cr);
        } catch (InvalidParameterException e) {
            //LOGGER.warn("Could not recognize db: " + extDbName + " skipping.");
        }
    }
    
    public List<CrossReference> getCrossReferences() {
        return this.crossReferences;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the DBUrl
     */
    public String getDBUrl() {
        return DBUrl;
    }

    /**
     * @param DBUrl the DBUrl to set
     */
    public void setDBUrl(String DBUrl) {
        this.DBUrl = DBUrl;
    }

    /**
     * @return the synonyms
     */
    public List<String> getSynonyms() {
        return synonyms;
    }

    /**
     * @param synonyms the synonyms to set
     */
    public void setSynonyms(List<String> synonyms) {
        this.synonyms.addAll(synonyms);
    }

    public void addSynonym(String synonym) {
        this.synonyms.add(synonym);
    }

    public void addSourceNameAndID(String sourceName, String sourceIdentifier) {
        this.getSourceNames().add(sourceName);
        this.sourceIdentifiers.add(sourceIdentifier);
    }

    /**
     * @return the sourceNames
     */
    public List<String> getSourceNames() {
        return sourceNames;
    }

    /**
     * @param sourceNames the sourceNames to set
     */
    public void setSourceNames(List<String> sourceNames) {
        this.sourceNames.addAll(sourceNames);
    }

    /**
     * @param sourceIdentifiers the sourceIdentifiers to set
     */
    public void setSourceIdentifiers(List<String> sourceIdentifiers) {
        this.sourceIdentifiers.addAll(sourceIdentifiers);
    }

    void addSourceID(String sourceIdentifier) {
        this.sourceIdentifiers.add(sourceIdentifier);
    }

    /**
     * There are some cases in which a the name of the resource is ambiguous since it could be referring to compounds,
     * proteins or drugs for instance (like KEGG, which could be for KEGG Compound or KEGG Drug, or Glycan, etc.).
     * @param extDbRef
     * @param extID
     * @return 
     */
    private String resolveAmbiguous(String extDbRef, String extID) {
        if (extDbRef.equalsIgnoreCase("KEGG")) {
            LOGGER.info("Kegg id: " + extID);
            if (extID != null && keggCompoundIdPattern.matcher(extID).matches()) {
                return KEGGCompoundIdentifier.IDENTIFIER_LOADER.getShortDescription(KEGGCompoundIdentifier.class);
            } else if (extID != null && keggGlycanIdPattern.matcher(extID).matches()) {
                return KeggGlycanIdentifier.IDENTIFIER_LOADER.getShortDescription(KeggGlycanIdentifier.class);
            } else if (extID != null && keggDrugIdPattern.matcher(extID).matches()) {
                return KEGGDrugIdentifier.IDENTIFIER_LOADER.getShortDescription(KEGGDrugIdentifier.class);
            }
        }
        return extDbRef;
    }
}
