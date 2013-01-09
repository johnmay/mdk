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

package uk.ac.ebi.mdk.db;

import java.io.File;

/**
 * IntEnzDatabaseProperties.java
 * Wrapper class for access to IntEnz paths in databases.properties
 *
 * @author johnmay
 * @date Mar 21, 2011
 */
public class IntEnzDatabaseProperties {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( IntEnzDatabaseProperties.class );
    private static final DatabaseProperties dbproperties = DatabaseProperties.getInstance();
    private static IntEnzDatabaseProperties instance;

    // Statically set keys in the properties file
    private static final String INTENZ_ROOT_KEY = "intenz.root";
    private static final String INTENZ_ASCII_XML_KEY = "intenz.ascii.xml";
    private static final String INTENZ_XCHARS_XML_KEY = "intenz.xchars.xml";

    // Storage of file objects
    private File intEnzASCIIXML;
    private File intEnzXCharsXML;
    private File intEnzRoot;

    private IntEnzDatabaseProperties() {

        // load the specific filenames
        intEnzRoot = dbproperties.getFile( INTENZ_ROOT_KEY );
        intEnzASCIIXML = dbproperties.getFile( INTENZ_ASCII_XML_KEY );
        intEnzXCharsXML = dbproperties.getFile( INTENZ_XCHARS_XML_KEY );

        logger.debug( "loaded intenz locations: '" + intEnzRoot + "', '" + intEnzASCIIXML + "' and '" + intEnzXCharsXML + "'" );
    }

    public static IntEnzDatabaseProperties getInstance() {
        if ( instance == null ) {
            instance = new IntEnzDatabaseProperties();
        }
        return instance;
    }

    public File getIntEnzRoot() {
        return intEnzRoot;
    }

    /**
     * Returns the path to the ASCII flavor of IntEnz XML, for most purposes this is what is needed
     * @return File object (IntEnz.xml)
     */
    public File getIntEnzASCIIXML() {
        return intEnzASCIIXML;
    }

    /**
     * Returns the path to the XCHARS flavor of IntEnz XML
     * @return
     */
    public File getIntEnzXCharsXML() {
        return intEnzASCIIXML;
    }

}
