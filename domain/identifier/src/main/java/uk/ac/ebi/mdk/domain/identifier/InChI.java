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

package uk.ac.ebi.mdk.domain.identifier;

import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;

import java.io.Serializable;
import java.util.regex.Pattern;


/**
 * @author Pablo Moreno
 * @author John May
 * @author $Author$ (this version)
 * @version $Rev$ Last Changed $Date$
 * @brief Wrapper object for an InChI storing the InChI, InChIKey and AuxInfo
 * strings. Allows storage and comparison.
 * TODO: Name is for convenience only might be better to move this somewhere else
 * @date 2011.05.06
 */
@Brief("InChI")
@Description("The IUPAC International Chemical Identifier ")
public class InChI
        extends AbstractChemicalIdentifier
        implements Serializable {

    private static final long serialVersionUID = 8312829501093553787L;
    private String name = "";
    private String inchi = "";
    private String key = "";
    private String auxInfo = "";
    transient private boolean standard = false;
    // main layer
    //    transient private MolecularFormula formula;
    transient private String connectivity;
    transient private String hydrogens;
    // charge layer
    transient private String charges;
    // matchers
    transient private Pattern standardInChIMatcher = Pattern.compile("InChI=1S");
    private static final IdentifierMetaInfo DESCRIPTION =
            IDENTIFIER_LOADER.getMetaInfo(InChI.class);


    public InChI() {
    }


    /**
     * @param inchi
     *
     * @brief Construtor for instantiating with just the InChI string
     */
    public InChI(String accession) {
        this("", accession, "", "");
    }


    /**
     * @param inchi
     * @param key
     * @param auxInfo
     *
     * @brief Constructor for setting the InChi, InChIKey and AuxInfo
     */
    public InChI(
            String inchi,
            String key,
            String auxInfo) {
        this("", inchi, key, auxInfo);
    }


    /**
     * @param name
     * @param inchi
     * @param key
     * @param auxInfo
     *
     * @brief Constructor for setting the Molecule name, InChI, InChI-Key and AuxInfo.
     * the InChI layers are split and stored
     */
    public InChI(String name,
                 String inchi,
                 String key,
                 String auxInfo) {

        // make sure there are no null values and only empty strings
        this.name = name != null ? name : "";
        this.inchi = inchi != null ? inchi : "";
        this.key = key != null ? key : "";
        this.auxInfo = auxInfo != null ? auxInfo : "";

        // use the main inchi as the identifier
        setAccession(inchi);

        String[] layers = this.inchi.split("/");
        if (standardInChIMatcher.matcher(layers[0]).find()) {
            standard = true;
        }
        //
        //
        //        for( String inchiLayer : layers ) {
        //        }

        // no to test if the part exists
        //        this.formula = new MolecularFormula();
        //        this.connectivity = layers[2];
        //        this.hydrogens = layers[3];
        //
        //        if ( layers.length > 4 ) {
        //            this.charges = layers[4];
        //        }

    }


    /**
     * @return
     *
     * @brief Accessor for the stored name
     */
    public String getName() {
        return name;
    }


    /**
     * @return InChI string
     *
     * @brief Accessor for the InChI string
     */
    public String getInchi() {
        return inchi;
    }


    /**
     * @param inchi
     *
     * @brief Mutator for the InChI string
     */
    public void setInchi(String inchi) {
        this.inchi = inchi;
    }


    /**
     * @return 27 character InChIKey
     *
     * @brief Accessor for the hashed InChI Key
     */
    public String getInchiKey() {
        return key;
    }


    /**
     * @param inchiKey
     *
     * @brief Mutator for the InChI Key
     */
    public void setInchiKey(String inchiKey) {
        this.key = inchiKey;
    }


    /**
     * @return AuxInfo string
     *
     * @brief Accessor for the AuxInfo string
     */
    public String getAuxInfo() {
        return auxInfo;
    }


    /**
     * @param auxInfo new AuxInfo value
     *
     * @brief Mutator for the AuxInfo string
     */
    public void setAuxInfo(String auxInfo) {
        this.auxInfo = auxInfo;
    }


    public boolean isStandard() {
        return standard;
    }

    //    public MolecularFormula getFormula() {
    //        return formula;
    //    }

    public String getConnectivity() {
        return connectivity;
    }


    public String getHydrogens() {
        return hydrogens;
    }


    /**
     * @param obj Object to compare if equals
     *
     * @return Whether the objects are matches
     *
     * @brief Compares the this InChI with another. The method checks the inheritance of the
     * object and then the InChI, InChIKey and AuxInfo. If the InChIKey or AuxInfo is
     * absent from either object these values are not checked.
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InChI other = (InChI) obj;
        // check the InChI regardless
        if (this.inchi.isEmpty() ? other.inchi.isEmpty() : !this.inchi.equals(other.inchi)) {
            return false;
        }
        // if either of the InChIKeys are empty don't check these
        if (this.key.isEmpty() || other.key.isEmpty() ? false : !this.key.equals(other.key)) {
            return false;
        }
        // if either of the AuxInfos are empty don't check these
        if (this.auxInfo.isEmpty() || other.auxInfo.isEmpty() ? false :
                !this.auxInfo.equals(other.auxInfo)) {
            return false;
        }
        return true;
    }


    /**
     * @return
     *
     * @brief Generates a hash code on the InChI string
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.inchi != null ? this.inchi.hashCode() : 0);
        return hash;
    }


    /**
     * @return
     *
     * @brief Returns the InChI string and ignores InChIKey and AuxInfo
     */
    @Override
    public String toString() {
        return inchi;
    }


    /**
     * @return Clone of the object
     *
     * @brief Clone method returns a clone of the InChI object
     * and it's underlying inchi, inchiKey and AuxInfo fiels
     */

    public InChI copy() {
        return new InChI(name, inchi, key, auxInfo);
    }


    /**
     * @inheritDoc
     */
    @Override
    public InChI newInstance() {
        return new InChI();
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return DESCRIPTION.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return DESCRIPTION.description;
    }





    /**
     * @inheritDoc
     */
    @Override
    public MIRIAMEntry getResource() {
        return DESCRIPTION.resource;
    }


}

