/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package uk.ac.ebi.metabolomes.identifier;

import uk.ac.ebi.metabolomes.resource.Resource;

/**
 * Wrapper object for an InChI storing the InChI, InChIKey and AuxInfo
 *
 *
 * @author Pablo Moreno
 * @author John May
 * @date   6 Mar 2011
 */
public class InChI extends AbstractIdentifier {

    private String name;
    private String inchi;
    private String inchiKey;
    private String auxInfo;

    public InChI() {
        // set resource to general as there isn't a InChI db
 //       setResource( Resource.GENERAL );
    }

    public InChI( String inchi , Boolean check ) {
        this.inchi = inchi;
        inchiKey = null;
        auxInfo = null;
    }

    public InChI( String inchi ) {
        this( inchi , true );
    }

    public InChI( String name ,
                  String inchi ,
                  String inchiKey ,
                  String auxInfo ) {
        this.name = name;
        this.inchi = inchi;
        this.inchiKey = inchiKey;
        this.auxInfo = auxInfo;

        // use the main inchi as the identifier
        setIdentifierString( inchi );
    }

    public String getName() {
        return name;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi( String inchi ) {
        this.inchi = inchi;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public void setInchiKey( String inchiKey ) {
        this.inchiKey = inchiKey;
    }

    public String getAuxInfo() {
        return auxInfo;
    }

    public void setAuxInfo( String auxInfo ) {
        this.auxInfo = auxInfo;
    }

    @Override
    public final String parse( String identifier ) {
        throw new UnsupportedOperationException( "unsupported" );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final InChI other = ( InChI ) obj;
        if ( ( this.inchi == null ) ? ( other.inchi != null ) : !this.inchi.equals( other.inchi ) ) {
            return false;
        }

//        if ( ( this.inchiKey == null ) ? ( other.inchiKey != null ) : !this.inchiKey.equals( other.inchiKey ) ) {
//            return false;
//        }
//        if ( ( this.auxInfo == null ) ? ( other.auxInfo != null ) : !this.auxInfo.equals( other.auxInfo ) ) {
//            return false;
//        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + ( this.inchi != null ? this.inchi.hashCode() : 0 );
        return hash;
    }

    @Override
    public String toString() {
        return inchi;
    }
}
