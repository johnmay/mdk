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
package uk.ac.ebi.metabolomes.resource;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;
import uk.ac.ebi.resource.protein.UniProtIdentifier;

/**
 * An Enumeration class to store description and links resources
 * @author johnmay
 * @date   Apr 2011
 */
public enum Resource {

    // todo load from properties file
    UNIPROT( "UniProt protein databse" , "" , UniProtIdentifier.class , "http://www.uniprot.org/uniprot" ),
    SWISSPORT( "UniProtKB/SwissProt – Currated Protein Sequence Datbase" , "SP" , UniProtIdentifier.class , "" ),
    TREMBL( "UniProtKB/TrEMBL – Currated Protein Sequence Datbase" , "TR" , UniProtIdentifier.class , "" ),
    ENA( "European Nucleotide Archive" , "ENA" , null , "" ),
    PROSITE( "Active Site Database" , "" , null , "" ),
    RHEA( "Reaction Enzyme Database" , "" , null , "" ),
    INTENZ( "Enzyme database" , "EC" , ECNumber.class , "" ),
    ENZYME( "Enzyme database" , "EC" , ECNumber.class , "" ),
    TCDB( "Transport Commission Database" , "" , null , "" ),
    PDB( "Brookaven Protein Structural Database" , "PDB" , null , "http://www.rcsb.org" ),
    PDBE( "European Protein Structural Database" , "" , null , "http://www.ebi.ac.uk/pdbe/#id" ),
    CAS( "Chemical Abstracts" , "CAS" , null , "" ),
    GO( "Gene Ontology" , "GO" , null , "" ),
    MEROPS( "MEROPS the Peptidase Database" , "" , null , "" ),
    DDBJ( "DNA Databank of Japan" , "DBJ" , null , "" ),
    EMBL( "EMBL Nucleotide Sequence Database" , "EMB" , null , "" ),
    GENBANK( "NCBI GenBank" , "GB" , null , "" ),
    GENINFO( "NCBI GenInfo" , "GI" , null , "" ),
    REFSEQ( "NCBI Reference Sequence" , "REF" , null , "" ),
    PIR( "Protein Information Resource" , "PIR" , null , "" ),
    PRF( "Protein Research Foundation" , "PRF" , null , "" ),
    PATENTS( "" , "PAT" , null , "" ),
    GBID( "Gene Backbone ID" , "BBS" , null , "" ),
    LOCAL( "" , "LCL" , null , "" ),
    GENERAL( "General" , "GNL" , GenericIdentifier.class , "" ),
    UNKNOWN( "Unknown Resource (add to Resource.java)" , "" , null , "" );


    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( Resource.class );
    private Class identifierClass;
    private Constructor identifierConstructor;
    private String desc;
    private String dbid;
    private String link;

    private Resource() {
        this.dbid = "";
        this.desc = "";
        this.link = "";
        identifierClass = null;
        identifierConstructor = null;
    }

    private Resource( String desc , String dbid , Class<? extends AbstractIdentifier> idClass , String link ) {
        this.dbid = dbid;
        this.desc = desc;
        this.link = link;
        identifierClass = idClass;
        if ( identifierClass != null ) {
            try {
                identifierConstructor = identifierClass.getDeclaredConstructor( String.class , Boolean.class );
            } catch ( Exception ex ) {
                //logger.error( "could not build id constructor for resource: " + dbid );
            }
        }
    }

    public URL getURL() throws MalformedURLException {
        return new URL( link );
    }

    public String getDescription() {
        return desc;
    }

    public Class getIdentifierClass() {
        return identifierClass;
    }

    public Constructor getIdentifierConstructor() {
        return identifierConstructor;
    }

    public String getDbid() {
        return dbid;
    }

    /**
     * Gets the resource object given the resource name
     * @param resouceName
     * @return
     */
    public static Resource getResource( String resouceName ) {

        String lcResouceName = resouceName.toUpperCase();

        for ( Resource r : values() ) {
            if ( lcResouceName.equals( r.toString() )
                 || lcResouceName.equals( r.getDbid() ) ) {
                return r;
            }
        }
        return UNKNOWN;
    }
}
