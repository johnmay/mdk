/**
 * ValidateXMLSchema.java
 *
 * 2011.12.09
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
package uk.ac.ebi.io.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *          ValidateXMLSchema - 2011.12.09 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ValidateXMLSchema {

    private static final Logger LOGGER = Logger.getLogger(ValidateXMLSchema.class);
    static final String JAXP_SCHEMA_LANGUAGE =
                        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA =
                        "http://www.w3.org/2001/XMLSchema";
    static final String JAXP_SCHEMA_SOURCE =
                        "http://java.sun.com/xml/jaxp/properties/schemaSource";
    static final File schema = new File(ValidateXMLSchema.class.getResource("schema/chemet.xsd").getFile());

    @Test
    public void testSchema() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory =
                               DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        factory.setAttribute(JAXP_SCHEMA_SOURCE, schema);

        InputStream stream = getClass().getResourceAsStream("sample.xml");

        Document document = factory.newDocumentBuilder().parse(stream);

        NodeList nodes = document.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println(nodes.item(i).getNamespaceURI());
        }


    }
}
