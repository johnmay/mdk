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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.deprecated;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XMLHelper.java
 *
 *
 * @author johnmay
 * @date Mar 21, 2011
 */
public class XMLHelper {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( XMLHelper.class );

    /**
     * Builds and XML document hiding all the try/catches of xml dom
     * @param document
     * @return
     */
    public static Document buildDocument( File document ) {
        try {
            return buildDocument( new BufferedInputStream( new FileInputStream( document ) ) );
        } catch ( FileNotFoundException ex ) {
            logger.error( "There was a problem reading the document: " + ex.getMessage() );
        }
        return null;
    }

    public static Document buildDocument( InputStream stream ) {

        FileInputStream xmlStream = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse( stream );
            doc.getDocumentElement().normalize();
            return doc;
        } catch ( ParserConfigurationException ex ) {
            logger.error( "could not load XML document: " + ex );
        } catch ( FileNotFoundException ex ) {
            logger.error( "could not load XML document: " + ex );
        } catch ( IOException ex ) {
            logger.error( "could not load XML document: " + ex );
        } catch ( SAXException ex ) {
            logger.error( "could not load XML document: " + ex );
        } finally {
            if ( xmlStream != null ) {
                try {
                    xmlStream.close();
                } catch ( IOException ex ) {
                    logger.error( "could not close XML document: " + ex.getMessage() );
                }
            }
        }

        // return null if we couldn't create the document
        return null;
    }
}
