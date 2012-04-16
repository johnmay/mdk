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
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import static javax.xml.stream.events.XMLEvent.*;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.chemet.resource.classification.ECNumber;
import uk.ac.ebi.chemet.resource.protein.SwissProtIdentifier;
import uk.ac.ebi.chemet.resource.protein.UniProtIdentifier;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.metabolomes.resource.DatabaseProperties;
import uk.ac.ebi.resource.IdentifierFactory;

/**
 * @name    UniprotAnnotations - 2011.10.13 <br>
 *          Currently includes EC, KO (KEGG orthology annotations) and Taxonomy ID.
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class UniProtAnnoationLoader implements Externalizable {

    private static final Logger LOGGER = Logger.getLogger(UniProtAnnoationLoader.class);
    private File xml;
    private SwissProtIdentifier id = new SwissProtIdentifier();
    private Multimap<UniProtIdentifier, Identifier> map = HashMultimap.create();
    private static String location = "ftp://ftp.ebi.ac.uk/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_trembl.xml.gz";

    public Multimap<UniProtIdentifier, Identifier> getMap() {
        return map;
    }

    public static void main(String[] args) throws MalformedURLException, IOException {
        URL url = new URL(location);
        URLConnection connection = url.openConnection();
        int size = connection.getContentLength();
        url.openStream().close(); // atempt to free up resource
        System.out.println(size);
    }

    public void load() {
        try {
            File root = DatabaseProperties.getInstance().getFile("uniprot.root");
            File mapFile = new File(root, "uniprot_ref.ser");
            ObjectInput in = new ObjectInputStream(new FileInputStream(mapFile));
            this.readExternal(in);
            in.close();
        } catch (Exception ex) {
            LOGGER.error("Unable to load mapping file");
        }
    }

    /**
     * @inheritDoc
     */
    public void writeExternal(ObjectOutput out) throws IOException {

        List<Identifier> references = new ArrayList(new HashSet(map.values()));
        System.out.println(references.size());
        out.writeInt(references.size());
        for (Identifier reference : references) {
            throw new UnsupportedOperationException("Refractor to use IdentifierOutputStream");
            //IdentifierFactory.getInstance().write(out, reference);
        }

        out.writeInt(map.keySet().size());
        for (UniProtIdentifier key : map.keySet()) {
            key.writeExternal(out);
            Collection<Identifier> refs = map.get(key);
            out.writeInt(refs.size());
            for (Identifier value : refs) {
                out.writeInt(references.indexOf(value));
            }
        }
    }

    /**
     * @inheritDoc
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        IdentifierFactory factory = IdentifierFactory.getInstance();
        int size = in.readInt();
        Identifier[] references = new Identifier[size];
        for (int i = 0; i < size; i++) {
            // references[i] = factory.read(in);
            // throw new UnsupportedOperationException("Refractor to use IdentifierOutputStream");
        }

        int keySize = in.readInt();
        for (int i = 0; i < keySize; i++) {
            UniProtIdentifier identifier = id.newInstance();
            identifier.readExternal(in);
            int refSize = in.readInt();
            for (int j = 0; j < refSize; j++) {
                map.put(identifier, references[in.readInt()]);
            }
        }

    }

    /**
     * Set the FTP address
     */
    public static void setFTP(String locaiton) {
        UniProtAnnoationLoader.location = locaiton;
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
        xmlif.configureForLowMemUsage();

        XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(stream);

        map = createMap(xmlr);

        stream.close();

    }

    private Multimap<UniProtIdentifier, Identifier> createMap(XMLStreamReader2 xmlr) throws XMLStreamException {

        Multimap<UniProtIdentifier, Identifier> references = HashMultimap.create();

        while (xmlr.hasNext()) {

            int event = xmlr.next();
            switch (event) {
                case END_DOCUMENT:
                    return references;
                case START_ELEMENT:
                    String name = xmlr.getName().getLocalPart();
                    if (name.equals("entry")) {
                        references.putAll(parseEntry(xmlr));
                    }
                    break;
            }
        }

        return references;
    }

    private Multimap<UniProtIdentifier, Identifier> parseEntry(XMLStreamReader2 xmlr) throws XMLStreamException {

        List<UniProtIdentifier> keys = new ArrayList();
        List<Identifier> values = new ArrayList();

        Multimap<UniProtIdentifier, Identifier> map = HashMultimap.create();

        while (xmlr.hasNext()) {
            int event = xmlr.next();
            switch (event) {
                case START_ELEMENT:
                    String name = xmlr.getName().getLocalPart();
                    if (name.equals("accession")) {
                        xmlr.next();
                        UniProtIdentifier identifier = id.newInstance();
                        identifier.setAccession(xmlr.getText());
                        keys.add(identifier);

                    } else if (name.equals("dbReference")) {
                        Identifier idFound = gerIdentifier(xmlr);
                        if (idFound != null) {
                            values.add(idFound);
                        }
                    }
                    break;
                case END_ELEMENT:
                    if (xmlr.getName().getLocalPart().equals("entry")) {
                        for (UniProtIdentifier identifier : keys) {
                            map.putAll(identifier, values);
                        }
                        return map;
                    }
                    break;
            }
        }

        LOGGER.error("Never reached end element");

        return map;
    }

    public Identifier gerIdentifier(XMLStreamReader2 xmlr) throws XMLStreamException {

        String accession = "";
        int count = xmlr.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String name = xmlr.getAttributeName(i).getLocalPart();
            if (name.equals("id")) {
                accession = xmlr.getAttributeValue(i);
            } else if (name.equals("type")) {
                String type = xmlr.getAttributeValue(i);
                if (type.equals("EC")) {
                    return new ECNumber(accession);
                } /*else if(type.equals("KO")) {
                    // <dbReference type="KO" id="K11440" key="35"/>
                    return new KEGGOrthology(accession);
                } else if(type.equals("NCBI Taxonomy")) {
                    Taxonomy tax = new Taxonomy();
                    tax.setAccession(accession);
                    return tax;
                }*/
            }
        }

        return null;
    }
}
