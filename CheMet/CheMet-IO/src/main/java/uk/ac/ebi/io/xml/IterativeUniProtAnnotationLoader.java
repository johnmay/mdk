/**
 * UniprotAnnotations.java
 *
 * 2011.10.13
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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import static javax.xml.stream.events.XMLEvent.*;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.resource.classification.KEGGOrthology;
import uk.ac.ebi.resource.organism.Taxonomy;
import uk.ac.ebi.resource.protein.SwissProtIdentifier;
import uk.ac.ebi.resource.protein.UniProtIdentifier;

/**
 * @name    UniprotAnnotations - 2011.10.13 <br>
 *          Currently includes EC, KO (KEGG orthology annotations) and Taxonomy ID.
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class IterativeUniProtAnnotationLoader {

    private static final Logger LOGGER = Logger.getLogger(IterativeUniProtAnnotationLoader.class);
    private File xml;
    private SwissProtIdentifier id = new SwissProtIdentifier();
    private XMLStreamReader2 xmlr;
    private static String location = "ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_trembl.xml.gz";

    public static void main(String[] args) throws MalformedURLException, IOException {
        URL url = new URL(location);
        URLConnection connection = url.openConnection();
        int size = connection.getContentLength();
        url.openStream().close(); // atempt to free up resource
        System.out.println(size);
    }

    /**
     * Set the FTP address
     */
    public static void setFTP(String locaiton) {
        IterativeUniProtAnnotationLoader.location = locaiton;
    }

    /**
     * Updates the data using FTP connection
     * @throws IOException
     */
    public void update() throws IOException, XMLStreamException {
        update(new GZIPInputStream(new URL(location).openStream()));
    }

    /**
     * Updates using a provided file
     * @param file
     */
    public void update(File file) throws IOException, XMLStreamException {
        update(new FileInputStream(file));
    }

    public void update(InputStream stream) throws IOException, XMLStreamException {

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(stream);

        startParse(xmlr);

        //stream.close();

    }
    private UniProtEntry current;

    private void startParse(XMLStreamReader2 xmlr) throws XMLStreamException {
        loop1:
        while (xmlr.hasNext()) {
            int event = xmlr.next();
            switch (event) {
                case END_DOCUMENT:
                    break;
                case START_ELEMENT:
                    String name = xmlr.getName().getLocalPart();
                    if (name.equals("entry")) {
                        current = parseEntry(xmlr);
                        break loop1;
                    }
                    break;
            }
        }


    }

    public UniProtEntry nextEntry() {
        UniProtEntry toRet = current;
        try {
            current = parseEntry(xmlr);
        } catch (XMLStreamException ex) {
            LOGGER.warn("Problems with StAx parser:", ex);
        }
        return toRet;
    }

    private UniProtEntry parseEntry(XMLStreamReader2 xmlr) throws XMLStreamException {
        UniProtEntry toReturn = null;

        while (xmlr.hasNext()) {
            int event = xmlr.next();
            switch (event) {
                case START_ELEMENT:
                    String name = xmlr.getName().getLocalPart();
                    if (name.equals("accession")) {
                        xmlr.next();
                        UniProtIdentifier identifier = id.newInstance();
                        identifier.setAccession(xmlr.getText());
                        toReturn = new UniProtEntry(identifier);

                    } else if (name.equals("dbReference")) {
                        Identifier idFound = getIdentifier(xmlr);
                        if (idFound != null && toReturn != null) {
                            toReturn.addExtIdentifier(idFound);
                        }
                    }
                    break;
                case END_ELEMENT:
                    if (xmlr.getName().getLocalPart().equals("entry")) {
                        return toReturn;
                    }
                    break;
            }
        }

        return toReturn;
    }

    public Identifier getIdentifier(XMLStreamReader2 xmlr) throws XMLStreamException {
        String accession = null;
        Identifier ident = null;
        int count = xmlr.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String name = xmlr.getAttributeName(i).getLocalPart();
            if (name.equals("id")) {
                accession = xmlr.getAttributeValue(i);
            } else if (name.equals("type")) {
                String type = xmlr.getAttributeValue(i);
                if (type.equals("EC")) {
                    ident = new ECNumber();
                } else if (type.equals("KO")) {
                    // <dbReference type="KO" id="K11440" key="35"/>
                    ident = new KEGGOrthology();
                } else if (type.equals("NCBI Taxonomy")) {
                    ident = new Taxonomy();
                }
            }
        }
        if(ident!=null && accession!=null)
            ident.setAccession(accession);

        return ident;
    }

    public void close() {
        try {
            xmlr.closeCompletely();
        } catch(XMLStreamException e) {
            LOGGER.error("Could not close xmlr completely", e);
        }
    }

    public class UniProtEntry {

        private UniProtIdentifier identifier;
        private List<Identifier> extIdentifiers;

        public UniProtEntry(String accession) {
            this.identifier = id.newInstance();
            this.identifier.setAccession(accession);
            init();
        }

        public UniProtEntry(UniProtIdentifier uniProtIdentifier) {
            this.identifier = uniProtIdentifier;
            init();
        }

        private void init() {
            this.extIdentifiers = new ArrayList<Identifier>();
        }

        public void addExtIdentifier(Identifier ident) {
            this.extIdentifiers.add(ident);
        }

        public List<Identifier> getIdentifiers() {
            return extIdentifiers;
        }
        
        public UniProtIdentifier getUniProtIdentifier() {
            return this.identifier;
        }
    }
}
