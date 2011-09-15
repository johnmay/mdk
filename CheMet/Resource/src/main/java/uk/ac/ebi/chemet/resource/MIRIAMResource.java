/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.chemet.resource;

import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.metabolomes.identifier.MIRIAMEntry;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.resource.protein.UniProtIdentifier;


/**
 * MIRIAMResource.java – MetabolicDevelopmentKit – Jun 25, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public enum MIRIAMResource {

    /**
     * Chemical Entities of Biological Interest
     */
    CHEBI("chebi", ChEBIIdentifier.class),
    /**
     * KEGG: Kyoto Encyclopedia of Genes and Genomes – Compound resource
     */
    KEGG_COMPOUND("kegg compound", KEGGCompoundIdentifier.class),
    /**
     * KEGG: Kyoto Encyclopedia of Genes and Genomes – Pathway resource
     */
    KEGG_PATHWAY("kegg pathway"),
    /**
     * Enzyme Nomenclature Resource
     */
    ENZYME_NOMENCLATURE("enzyme nomenclature", ECNumber.class),
    /**
     * Enzyme Nomenclature Resource Synonym
     */
    EC("enzyme nomenclature", ECNumber.class),
    /**
     * Transport Classification Database – analogous to the Enzyme Classification system
     */
    TRANSPORT_CLASSIFICATION_DATABASE("transport classification database"),
    /**
     * Transport Classification Database – analogous to the Enzyme Classification system synonym
     */
    TCDB("transport classification database"),
    /**
     * UniProtKB/SwissProt and UniProtKB/TrEMBL identifier
     */
    UniProt("uniprot", UniProtIdentifier.class);
    // attributes
    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      MIRIAMResource.class);
    private MIRIAMEntry entry;


    private MIRIAMResource(String name, Class identifierClass) {
        entry = MIRIAMResourceLoader.getInstance().getEntry(name);
        entry.setIdentifierClass(identifierClass);
    }


    private MIRIAMResource(String name) {
        entry = MIRIAMResourceLoader.getInstance().getEntry(name);
    }


    /**
     * Returns the MIRIAM entry
     * @return
     */
    public MIRIAMEntry getEntry() {
        return entry;
    }


    /**
     * Convenience Accessor to the underlying MIRIAMEntry.getResourceName() method
     * @return Resource name
     */
    public String getResourceName() {
        return entry.getResourceName();
    }


}

