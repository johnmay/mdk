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

package uk.ac.ebi.mdk.io.xml.rex;

import com.google.common.io.CharStreams;
import org.junit.Test;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import uk.ac.ebi.mdk.domain.annotation.rex.RExAnnotation;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.rex.RExTag;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.PubMedIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.io.xml.sbml.SBMLReactionReader;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author John May
 * @author Jan Czarnecki
 */
public class RExHandlerTest {

    @Test
    public void marshalUnmarshalTest() throws XMLStreamException, JAXBException, ParserConfigurationException, IOException, SAXException, TransformerException {
        List<String> sbmlFile = CharStreams.readLines(new InputStreamReader(
                this.getClass().getResourceAsStream("/uk/ac/ebi/mdk/io/xml/sbml/rex.xml")));
        StringBuilder sbml = new StringBuilder();
        for(String line : sbmlFile)
        {
            sbml.append(line);
        }

        SBMLDocument sbmlDoc = new SBMLReader().readSBMLFromString(sbml.toString());
        Model model = sbmlDoc.getModel();
        Reaction sbmlReaction = model.getReaction(0);

        MetabolicReaction reaction = DefaultEntityFactory.getInstance().ofClass(MetabolicReaction.class,
                                                                                new BasicReactionIdentifier(
                                                                                        sbmlReaction.getId()),
                                                                                sbmlReaction.getName(),
                                                                                sbmlReaction.getMetaId());

        RExHandler handler = new RExHandler();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(sbmlReaction.getAnnotationString())));

        NodeList rexElements = doc.getElementsByTagName("rex:rex");
        Node rexElement = rexElements.item(0);

        StringWriter insw = new StringWriter();
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.transform(new DOMSource(rexElement), new StreamResult(insw));

        RExAnnotation rexAnnotation = handler.unmarshal(insw.toString());

        String marshalled = handler.marshal(rexAnnotation.getExtracts(), rexAnnotation.getCompounds());

        doc = dBuilder.parse(new InputSource(new StringReader(marshalled)));
        StringWriter outsw = new StringWriter();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.transform(new DOMSource(rexElement), new StreamResult(outsw));

        assertTrue(insw.toString().equals(outsw.toString()));
    }
}
