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
 * UniProtIdentifier.java
 *
 *
 * @author johnmay
 * @date Mar 21, 2011
 */
public class UniProtIdentifier
        extends AbstractIdentifier {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( UniProtIdentifier.class );
    private static final String UNIPROT_ACCESSION_SCHEMA = "[A-Z][A-Z0-9]{5}";
    private static final String UNIPROT_WITH_SPECIES_ACCESSION_SCHEMA = UNIPROT_ACCESSION_SCHEMA + "_[A-Z]{5}";

    public UniProtIdentifier() {
      //  setResource( Resource.UNIPROT );
    }

    public UniProtIdentifier( String identifier , Boolean check ) {
        this();

        if ( check ) {
            setIdentifierString( parse( identifier ) );
        } else {
            setIdentifierString( identifier );
        }
    }

    public UniProtIdentifier( String identifier ) {
        this( identifier , Boolean.TRUE );
    }

    public final String parse( String identifier ) {
        String parsedIdentifier = identifier;

        // first trim spaces
        parsedIdentifier.trim();

        // remove the version
        if ( parsedIdentifier.contains( "." ) ) {
            // could store if needed
            parsedIdentifier = parsedIdentifier.substring( 0 , parsedIdentifier.indexOf( "." ) );
        }

        // if not matching schema warn
        if ( parsedIdentifier.matches( UNIPROT_ACCESSION_SCHEMA ) ) {
            return parsedIdentifier;
        }
        if ( parsedIdentifier.matches( UNIPROT_WITH_SPECIES_ACCESSION_SCHEMA ) ) {
            return parsedIdentifier;
        }

        logger.warn( getResource() + " accession '" + identifier + "' doesn't look like a valid uniprot identifier" );

        return parsedIdentifier;

    }

    @Override
    public int hashCode() {
        return getIdentifierString().hashCode();
    }

    @Override
    public boolean equals( Object o ) {
        if ( o != null && o.getClass().equals( UniProtIdentifier.class ) ) {
            return equals( ( UniProtIdentifier ) o );
        }
        return false;
    }

    public boolean equals( UniProtIdentifier queryAccession ) {
        return getIdentifierString().equals( queryAccession.toString() );
    }

    public boolean equals( String queryAccession ) {
        return getIdentifierString().equals( queryAccession );
    }

    @Override
    public String toString() {
        return getIdentifierString();
    }
}
