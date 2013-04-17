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

package uk.ac.ebi.mdk.db;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * DatabaseProperties.java
 * A class to handle the location of various database flat-files on a file systems
 *
 * @author johnmay
 * @date Mar 21, 2011
 */
public class DatabaseProperties extends XProperties {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( DatabaseProperties.class );
    private static DatabaseProperties instance;
    private static URL dbPropertiesURL = ClassLoader.getSystemResource( "config/databases.properties" );

    /**
     * private constructor - use getInstance()
     */
    private DatabaseProperties() {
        super();
        loadPropertiesFile();
    }

    /**
     * reads the properties file and loads the properties
     */
    private void loadPropertiesFile() {

        try {
            load( dbPropertiesURL.openStream() );
        } catch ( IOException ex ) {
            logger.error( "could not load properties file '" + dbPropertiesURL + "' :" + ex );
        } catch ( NullPointerException ex ) {
            logger.error( "could not load properties file '" + dbPropertiesURL + "' :" + ex );
        }

    }

    /**
     * Method to retrieve the singleton instance of DatabaseProperties
     * @return The instance to act on
     */
    public static DatabaseProperties getInstance() {
        if ( instance == null ) {
            instance = new DatabaseProperties();
        }
        return instance;
    }

    /**
     * Accessor method for a database path, method checks the file exists and warns (via logger) if not
     * @param key The key for the property in the file the key is automatically transformed to lower case
     * @return
     */
    public File getFile( String key ) {
        if ( containsKey( key ) ) {
            String path = getProperty( key.toLowerCase() );
            return accessFile( key , path );
        }
        logger.warn( "Could not find key '" + key + "' in properties file '" + dbPropertiesURL + "' returning empty path" );
        return null;
    }

    /**
     * Get a File object to the specified key,path
     * @param key the key as it appears in the properties file
     * @param path the path to the file
     * @return a new File object for the path
     */
    private File accessFile( String key , String path ) {
        File file = new File( path );
        if ( !file.exists() ) {
            logger.warn( "Database file/folder '"
                         + file
                         + "' for '"
                         + key
                         + "' does not exist" );
        }
        return file;
    }



}
