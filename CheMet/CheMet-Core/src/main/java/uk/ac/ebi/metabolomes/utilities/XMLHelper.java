/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
     * Builds and XML document hiding all the try/catches from xml dom
     * @param xmlDocumentFile
     * @return
     */
    public static Document buildDocument( File xmlDocumentFile ) {

        FileInputStream xmlStream = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            xmlStream = new FileInputStream( xmlDocumentFile );
            Document doc = builder.parse( xmlStream );
            doc.getDocumentElement().normalize();
            return doc;
        } catch ( ParserConfigurationException ex ) {
            logger.error( "could not load XML document: " + xmlDocumentFile , ex );
        } catch ( FileNotFoundException ex ) {
            logger.error( "could not load XML document: " + xmlDocumentFile , ex );
        } catch ( IOException ex ) {
            logger.error( "could not load XML document: " + xmlDocumentFile , ex );
        } catch ( SAXException ex ) {
            logger.error( "could not load XML document: " + xmlDocumentFile , ex );
        } finally {
            if ( xmlStream != null ) {
                try {
                    xmlStream.close();
                } catch ( IOException ex ) {
                    logger.error( "could not close XML document: " + xmlDocumentFile , ex );
                }
            }
        }

        // return null if we couldn't create the document
        return null;
    }
}
