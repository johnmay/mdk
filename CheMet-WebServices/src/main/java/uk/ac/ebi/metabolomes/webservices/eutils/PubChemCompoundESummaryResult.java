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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;

/**
 * @name    PubChemCompoundESummaryResult
 * @date    2011.10.30
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Eutils ESummary request result for PubChem Compounds
 *
 */
public class PubChemCompoundESummaryResult extends ESummaryResult {

    private static final Logger LOGGER = Logger.getLogger(PubChemCompoundESummaryResult.class);
    private static final DefaultIdentifierFactory FACTORY = DefaultIdentifierFactory.getInstance();

    /**
     * @return the preferredName
     */
    public String getPreferredName() {
        return preferredName;
    }

    /**
     * @param preferredName the preferredName to set
     */
    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }
    private final Pattern inchiKeyPat = Pattern.compile("^[A-Z]{14}\\-[A-Z]{10}\\-[A-Z]{1}");
    private final Pattern casNumberPat = Pattern.compile("^\\d+\\-\\d+\\-\\d+$");
    private final Pattern pchemCidPat = Pattern.compile("^CID\\d+$");
    private final Pattern keggDrugPat = Pattern.compile("^D\\d+$");
    private final Pattern keggCompPat = Pattern.compile("^C\\d+$");
    private final Pattern strangePubchemCodePat = Pattern.compile("^[A-Z0-9]{32}$");
    private final Pattern strangeEPCodePat = Pattern.compile("\\d+-EP\\d+A\\d");
    private final Pattern flukaPat = Pattern.compile("\\d+_FLUKA");
    private final Pattern molPortPat = Pattern.compile("^MolPort");
    private final Pattern aldrichPat = Pattern.compile("_ALDRICH");
    private final Pattern sialPat = Pattern.compile("_SIAL");
    private final Pattern ncgcPat = Pattern.compile("^NCGC\\d+-\\d+");
    private final Pattern ccCrisPat = Pattern.compile("^CCRIS");
    private final Pattern akosPat = Pattern.compile("^AKOS");


    private void cleanUpSynonyms() {
        
        List<Pattern> patterns = new ArrayList<Pattern>(15);
        patterns.add(inchiKeyPat);
        patterns.add(casNumberPat);
        patterns.add(pchemCidPat);
        patterns.add(keggCompPat);
        patterns.add(keggDrugPat);
        patterns.add(strangeEPCodePat);
        patterns.add(strangePubchemCodePat);
        patterns.add(flukaPat);
        patterns.add(molPortPat);
        patterns.add(aldrichPat);
        patterns.add(sialPat);
        patterns.add(ncgcPat);
        patterns.add(ccCrisPat);
        patterns.add(akosPat);
        
        loop1:
        for (int index = 0; index < synonyms.size(); index++) {
            if (this.synonyms.get(index).startsWith("BRN ")) {
                synonyms.remove(index);
                index--;
                continue;
            }
            for (Pattern pattern : patterns) {
                if(pattern.matcher(synonyms.get(index)).find()) {
                    synonyms.remove(index);
                    index--;
                    continue loop1;
                }
            }
            if (this.synonyms.get(index).startsWith("AC1")) {
                synonyms.remove(index);
                index--;
                continue;
            }
            if (this.synonyms.get(index).startsWith("CHEBI:")) {
                synonyms.remove(index);
                index--;
                continue;
            }
            if (this.synonyms.get(index).startsWith("CHEMBL")) {
                synonyms.remove(index);
                index--;
                continue;
            }
            if (this.synonyms.get(index).startsWith("EINECS")) {
                synonyms.remove(index);
                index--;
                continue;
            }
            if (this.synonyms.get(index).startsWith("ZINC")) {
                synonyms.remove(index);
                index--;
                continue;
            }
            if (this.synonyms.get(index).startsWith("HMDB")) {
                synonyms.remove(index);
                index--;
                continue;
            }
            if (this.synonyms.get(index).startsWith("HSDB")) {
                synonyms.remove(index);
                index--;
                continue;
            }
            if (this.synonyms.get(index).startsWith("AIDS")) {
                synonyms.remove(index);
                index--;
                continue;
            }
            if (this.synonyms.get(index).startsWith("InChI")) {
                synonyms.remove(index);
                index--;
                continue;
            }
        }
    }

    private void choosePreferredName() {
        //this.setPreferredName(this.synonyms.get(0));
        if (this.synonyms.size() == 1) {
            this.setPreferredName(this.synonyms.get(0));
        } else {
            int index = 0;

            if (index < this.synonyms.size()) {
                this.setPreferredName(this.synonyms.remove(index));
            } else {
                this.setPreferredName(iupacName);
            }
        }
    }

    /**
     * This enum holds the fields that are lists (more than one value)
     * that are currently parsed by the XML parser. If more list fields are desired for parsing (that are present in the
     * response, they should be added here).
     */
    public enum PubChemCompoundESummaryListFields {

        SynonymList;
    }

    /**
     * This enum holds the fields that are scalars (just one value) that are currently parsed by the XML parser. If more
     * scalar fields are desired for parsing, they should be added here.
     */
    public enum PubChemCompoundESummarySingleFields {

        CID, IUPACName;
    }
    /**
     * This is all internal representation.
     */
    private String id;
    private String iupacName;
    private String preferredName; // normally the first synonym.
    private final List<String> synonyms = new ArrayList<String>();

    public List<PubChemCompoundESummarySingleFields> getScalarKeywords() {
        return Arrays.asList(PubChemCompoundESummarySingleFields.values());
    }

    public List<PubChemCompoundESummaryListFields> getListKeywords() {
        return Arrays.asList(PubChemCompoundESummaryListFields.values());
    }

    public void addListForKeyword(Enum keyword, List<String> parseList) {
        if (keyword instanceof PubChemCompoundESummaryListFields) {
            switch ((PubChemCompoundESummaryListFields) keyword) {
                case SynonymList:
                    this.setSynonyms(parseList);
                    break;
            }
        }
    }

    public void addScalarForKeyword(Enum keyword, String followingCharacters) {
        if (keyword instanceof PubChemCompoundESummarySingleFields) {
            switch ((PubChemCompoundESummarySingleFields) keyword) {
                case CID:
                    this.setId(followingCharacters);
                    break;
                case IUPACName:
                    this.setIupacName(followingCharacters);
                    break;
            }
        }
    }

    @Override
    public void wrap() {
        super.wrap();
        cleanUpSynonyms();
        // here we could set the preferred name to either the first synonym or the iupac name.
        if (this.synonyms.size() > 0) {
            choosePreferredName();
        } else {
            this.setPreferredName(this.iupacName);
        }
    }

    private void setIupacName(String iupacName) {
        this.iupacName = iupacName;
    }

    public String getIUPACName() {
        return iupacName;
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
}
