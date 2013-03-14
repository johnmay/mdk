/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.io.xml.hmdb;

import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A mutable value store the entries read in with {@link HMDBParser}.
 *
 * @author John May
 */
public final class HMDBMetabolite {

    /* primary accession of the entry */
    private String accession = "";

    /* secondary accession (normally from merged entries) */
    private List<String> secondaryAccessions = new ArrayList<String>(6);

    /* common name */
    private String name = "";

    /* iupac name (systematic) */
    private String iupac = "";

    /* synonyms (alternate names) */
    private List<String> synonyms = new ArrayList<String>(8);

    /* formal charge */
    private String charge = "";

    /* molecular formula */
    private String formula = "";

    /* inchi line notation */
    private String inchi = "";

    /* smiles line notation */
    private String smiles = "";

    /* database cross-references */
    private final List<Identifier> xrefs = new ArrayList<Identifier>();
    
    /* body fluids where the metabolite is found */
    private final List<String> bodyFluids = new ArrayList<String>();
    
    /* tissues where the metabolite is found */
    private final List<String> tissues = new ArrayList<String>();
    
    /* Cellular locations where the metabolite has been found */
    private final List<String> cellularLocation = new ArrayList<String>();

    public Collection<String> getBodyFluids() {
        return bodyFluids;
    }
    
    public Collection<String> getTissues() {
        return tissues;
    }
    
    public void addTissue(String tissue) {
        this.tissues.add(tissue);
    }
    
    public void addBodyFluid(String bodyFluid) {
        this.bodyFluids.add(bodyFluid);
    }
    
    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public Collection<String> getSecondaryAccessions() {
        return secondaryAccessions;
    }

    public void addSecondaryAccession(String accession) {
        this.secondaryAccessions.add(accession);
    }

    public String getCommonName() {
        return name;
    }

    public void setCommonName(String name) {
        this.name = name;
    }

    public String getIUPACName() {
        return iupac;
    }

    public void setIUPACName(String iupac) {
        this.iupac = iupac;
    }

    public Collection<String> getSynonyms() {
        return synonyms;
    }

    public void addSynonym(String synonym) {
        this.synonyms.add(synonym);
    }

    public String getFormalCharge() {
        return charge;
    }

    public void setFormalCharge(String charge) {
        this.charge = charge;
    }

    public String getMolecularFormula() {
        return formula;
    }

    public void setMolecularFormula(String formula) {
        this.formula = formula;
    }

    public Collection<Identifier> getCrossReferences() {
        return xrefs;
    }

    public void addCrossReference(Identifier identifier) {
        this.xrefs.add(identifier);
    }

    public String getInChI() {
        return inchi;
    }

    public void setInChI(String inchi) {
        this.inchi = inchi;
    }

    public String getSMILES() {
        return smiles;
    }

    public void setSMILES(String smiles) {
        this.smiles = smiles;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("Name: ").append(name).append(", ");
        sb.append("Accession: ").append(accession).append(", ");
        sb.append("IUPAC: ").append(iupac).append(", ");
        sb.append("InChI: ").append(inchi).append(", ");
        sb.append("SMILES: ").append(smiles).append(", ");
        sb.append("Synonyms: ").append(synonyms).append(", ");
        sb.append("Secondary Accessions: ").append(secondaryAccessions).append(", ");
        sb.append("Formal Charge: ").append(charge).append(", ");
        sb.append("Molecular Formula: ").append(formula).append(", ");
        sb.append("Database Cross-references: ").append(xrefs);
        return sb.toString();
    }

    public void addCellularLocation(String cellularLocation) {
        this.cellularLocation.add(cellularLocation);
    }

    public Collection<String> getCellularLocations() {
        return this.cellularLocation;
    }
}
