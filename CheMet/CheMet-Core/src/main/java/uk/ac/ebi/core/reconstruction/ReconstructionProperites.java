/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.reconstruction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;
import uk.ac.ebi.resource.organism.Kingdom;
import uk.ac.ebi.resource.organism.Taxonomy;

/**
 * ReconstructionProperites.java
 *
 *
 * @author johnmay
 * @date Apr 14, 2011
 */
public class ReconstructionProperites
    extends Properties {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( ReconstructionProperites.class );
    private String fname = "project.properties";
    private File propertiesfile;

    public ReconstructionProperites( File container ) {

        propertiesfile = new File( container + File.separator + fname );

        if ( propertiesfile.exists() ) {
            try {
                load( new FileReader( propertiesfile ) );
            } catch ( IOException ex ) {
                logger.error( "Could not load project properties" );
            }
        } else {
            // create new properties file
        }

    }

    public void setContainer( File container ) {
        this.propertiesfile = new File( container + File.separator + fname );
    }

    /**
     * Accessor for the project name
     * @return
     */
    public GenericIdentifier getProjectName() {
        String projectName = getProperty( "Project.Name" );
        if ( projectName != null ) {
            return new GenericIdentifier( projectName );
        }
        return new GenericIdentifier( "Untitled Project" );
    }

    /**
     * Set the name of the project
     * @param name
     */
    public void setProjectName( GenericIdentifier name ) {
        put( "Project.Name" , name.toString() );
    }

    public Taxonomy getOrgranismIdentifier() {
        int taxon = Integer.parseInt( ( String ) get( "Organism.Taxon" ) );
        String offname = ( String ) get( "Organism.OfficialName" );
        String code = ( String ) get( "Organism.Code" );
        String commonName = ( String ) get( "Organism.CommonName" );
        Kingdom kingdom = Kingdom.getKingdom( ( String ) get( "Organism.Kingdom" ) );
        return new Taxonomy( taxon , code , kingdom , offname , commonName );
    }

    public void setOrganismIdentifier( Taxonomy identifier ) {
        put( "Organism.Taxon" , Integer.toString( identifier.getTaxon() ) );
        put( "Organism.OfficialName" , identifier.getOfficialName() );
        put( "Organism.Code" , identifier.getCode() );
        put( "Organism.CommonName" , identifier.getCommonName() );
        put( "Organism.Kingdom" , identifier.getKingdom().toString() );
    }

    public HashSet<ReconstructionContents> getProjectContents() {
        String property =  getProperty( "Project.Contents" );
        if (property == null){
            return new HashSet<ReconstructionContents>();
        }
        return ReconstructionContents.expandList( property );
    }

    public void putProjectContents( Collection<ReconstructionContents> contents ) {
        put( "Project.Contents" , ReconstructionContents.flattenList( contents ) );
    }

    public void store() {
        try {
            logger.info( "storeing project to: " + propertiesfile );
            super.store( new FileOutputStream( propertiesfile ) ,
                         "Properties file for Metabolic Network Builder " + getProjectName() + " project" );
        } catch ( IOException ex ) {
            logger.error( "Error storing: " + propertiesfile , ex );
        }
    }
}
