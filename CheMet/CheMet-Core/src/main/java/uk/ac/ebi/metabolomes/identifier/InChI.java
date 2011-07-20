/**
 * WriteReactionsMain.java
 *
 * 2011.05.06
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
package uk.ac.ebi.metabolomes.identifier;

import java.io.Serializable;

/**
 * @brief   Wrapper object for an InChI storing the InChI, InChIKey and AuxInfo
 *          strings. Allows storage and comparison.
 *          TODO: Name is for convenience only might be better to move this somewhere else
 *
 * @author  Pablo Moreno
 * @author  John May
 * @author  $Author$ (this version)
 * @version $Rev$ Last Changed $Date$
 * @date    2011.05.06
 */
public class InChI
        extends AbstractIdentifier
        implements Comparable<InChI> , Serializable {

    private static final long serialVersionUID = 8312829501093553787L;
    private boolean isStandardInChI = false; // field to specify standard inchi
    private String name = "";
    private String inchi = "";
    private String inchiKey = "";
    private String auxInfo = "";


    /**
     * @brief   Construtor for instantiating with just the InChI string
     * @param   inchi
     */
    public InChI( String inchi ) {
        this( "" , inchi , "" , "" );
    }

    /**
     * @brief Constructor for setting the InChi, InChIKey and AuxInfo
     * @param inchi
     * @param inchiKey
     * @param auxInfo
     */
    public InChI(
            String inchi ,
            String inchiKey ,
            String auxInfo ) {
        this( "" , inchi , inchiKey , auxInfo );
    }

    /**
     * @brief Constructor for setting the Molecule name, InChI, InChIKey and AuxInfo
     * @param name
     * @param inchi
     * @param inchiKey
     * @param auxInfo
     */
    public InChI( String name ,
                  String inchi ,
                  String inchiKey ,
                  String auxInfo ) {

        // make sure there are no null values and only empty strings
        this.name = name != null ? name : "";
        this.inchi = inchi != null ? inchi : "";
        this.inchiKey = inchiKey != null ? inchiKey : "";
        this.auxInfo = auxInfo != null ? auxInfo : "";

        // use the main inchi as the identifier
        setIdentifierString( inchi );
    }

    /**
     * @brief  Accessor for whether the InChI is a Standard InChI
     * @return true/false depending on if the InChI is standard
     */
    public Boolean isStandardInChI() {
        return isStandardInChI;
    }

    /**
     * @brief Sets that this is a standard InChI
     * @param isStandardInChI – true/false
     */
    public void setStandardInChI( final Boolean isStandardInChI ) {
        this.isStandardInChI = isStandardInChI;
    }

    /**
     * @brief  Accessor for the stored name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @brief  Accessor for the InChI string
     * @return InChI string
     */
    public String getInchi() {
        return inchi;
    }

    /**
     * @brief Mutator for the InChI string
     * @param inchi
     */
    public void setInchi( String inchi ) {
        this.inchi = inchi;
    }

    /**
     * @brief  Accessor for the hashed InChI Key
     * @return 27 character InChIKey
     */
    public String getInchiKey() {
        return inchiKey;
    }

    /**
     * @brief Mutator for the InChI Key
     * @param inchiKey
     */
    public void setInchiKey( String inchiKey ) {
        this.inchiKey = inchiKey;
    }

    /**
     * @brief  Accessor for the AuxInfo string
     * @return AuxInfo string
     */
    public String getAuxInfo() {
        return auxInfo;
    }

    /**
     * @brief Mutator for the AuxInfo string
     * @param auxInfo new AuxInfo value
     */
    public void setAuxInfo( String auxInfo ) {
        this.auxInfo = auxInfo;
    }

    @Override
    public final String parse( String identifier ) {
        throw new UnsupportedOperationException( "unsupported" );
    }

    /**
     * @brief  Compares the this InChI with another. The method checks the inheritance of the
     *         object and then the InChI, InChIKey and AuxInfo. If the InChIKey or AuxInfo is
     *         absent from either object these values are not checked.
     * @param  obj Object to compare if equals
     * @return Whether the objects are equal
     */
    @Override
    public boolean equals( Object obj ) {

        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final InChI other = ( InChI ) obj;
        // check the InChI regardless
        if ( this.inchi.isEmpty() ? other.inchi.isEmpty() : !this.inchi.equals( other.inchi ) ) {
            return false;
        }
        // if either of the InChIKeys are empty don't check these
        if ( this.inchiKey.isEmpty() || other.inchiKey.isEmpty() ? false : !this.inchiKey.equals( other.inchiKey ) ) {
            return false;
        }
        // if either of the AuxInfos are empty don't check these
        if ( this.auxInfo.isEmpty() || other.auxInfo.isEmpty() ? false : !this.auxInfo.equals( other.auxInfo ) ) {
            return false;
        }
        return true;
    }

    /**
     * @brief Generates a hash code on the InChI string
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + ( this.inchi != null ? this.inchi.hashCode() : 0 );
        return hash;
    }

    /**
     * @brief   Returns the InChI string and ignores InChIKey and AuxInfo
     * @return
     */
    @Override
    public String toString() {
        return inchi;
    }

    /**
     * @brief   Comparator method compares on the InChI string alone
     * @param   o
     * @return  Either -1, 0 or 1 depending on where to order
     */
    public int compareTo( InChI o ) {
        return inchi.compareTo( o.getInchi() );
    }

    /**
     * @brief   Clone method returns a clone of the InChI object
     *          and it's underlying inchi, inchiKey and AuxInfo fiels
     * @return Clone of the object
     */
    @Override
    public InChI clone() {
        return new InChI( name , inchi , inchiKey , auxInfo );
    }
}
