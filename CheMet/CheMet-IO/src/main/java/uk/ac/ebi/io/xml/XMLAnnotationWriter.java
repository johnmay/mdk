/**
 * XMLAnnotationWriter.java
 *
 * 2011.12.08
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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.openscience.cdk.io.MDLV2000Writer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.ebi.annotation.chemical.*;
import uk.ac.ebi.annotation.crossreference.*;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.StringAnnotation;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.vistors.AnnotationVisitor;

/**
 *          XMLAnnotationWriter - 2011.12.08 <br>
 *          Writes CheMet Annotation (module) classes as XML
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class XMLAnnotationWriter implements AnnotationVisitor {

    private static final Logger LOGGER = Logger.getLogger(XMLAnnotationWriter.class);
    private final Document doc;
    private List<Exception> exceptions = new ArrayList<Exception>();

    public XMLAnnotationWriter(Document doc) {
        this.doc = doc;
    }

    public String getSBMLNotes(AnnotatedEntity entity) throws TransformerConfigurationException, TransformerException {

        Element notes = doc.createElement("notes");

        for (Annotation annotation : entity.getAnnotations()) {
           notes.appendChild(visit(annotation));
        }

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(notes);
        trans.transform(source, result);

        return sw.toString();

    }

    public Element visit(Annotation annotation) {
        if (annotation instanceof StringAnnotation) {
            return visit((StringAnnotation) annotation);
        } else if (annotation instanceof CrossReference) {
            return visit((CrossReference) annotation);
        } else if (annotation instanceof ChemicalStructure) {
            return visit((ChemicalStructure) annotation);
        }
        return null;
    }

    /**
     * Generates simple XML description for a {@see CrossReference}
     * @param annotation
     * @return
     */
    public Element visit(CrossReference annotation) {

        Element element = create(annotation);

        Element id = doc.createElement("identifier-index");
        Element accession = doc.createElement("accession");

        Identifier identifier = annotation.getIdentifier();

        id.appendChild(doc.createTextNode(identifier.getIndex().toString()));
        accession.appendChild(doc.createTextNode(identifier.getAccession()));

        element.appendChild(id);
        element.appendChild(accession);


        return element;

    }

    /**
     * Generates simple XML description for a {@see StringAnnotation}
     * @param annotation
     * @return
     */
    public Element visit(StringAnnotation annotation) {

        Element element = create(annotation);

        element.appendChild(doc.createTextNode(annotation.getValue()));

        return element;

    }

    /**
     * Generates XML description of ChemicalStructure annotation. 
     * @param structure
     * @return
     */
    public Element visit(ChemicalStructure structure) {

        Element element = create(structure);

        try {

            // write MolV2000 format to a string
            StringWriter sw = new StringWriter();
            MDLV2000Writer writer = new MDLV2000Writer(sw);
            writer.write(structure.getMolecule());
            writer.close();

            // append the Mol file as a text node
            element.appendChild(doc.createTextNode(sw.toString()));

        } catch (Exception ex) {
            exceptions.add(ex);
        }

        return element;


    }

    /**
     * Creates the base annotation element for the specified annotation
     * @param annotation The annotation to create the element for
     * @return new element to which data can be appended
     */
    private Element create(Annotation annotation) {
        Element element = doc.createElement("metingeer-annotation");
        element.setAttribute("index", annotation.getIndex().toString());
        return element;
    }
}
