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
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author John May
 * @author Jan Czarnecki
 */
public class RExHandlerTest {

    /*
    @Test
    public void testMarshal() throws Exception
    {
        RExExtract extract = new RExExtract(new PubMedIdentifier(12345), "a sentence", Arrays.asList(new RExTag(2, 4, "substrate")));
        String xml = new RExHandler().marshal(Arrays.asList(extract));
        String exp = "<rex:extracts xmlns:rex=\"http://www.bbk.ac.uk/rex/\">\n" +
            "    <rex:extract source=\"http://identifiers.org/pubmed/12345/\">\n" +
            "        <rex:sentence>a sentence</rex:sentence>\n" +
            "        <rex:tag type=\"substrate\" start=\"2\" length=\"4\"/>\n" +
            "    </rex:extract>\n" +
            "</rex:extracts>";
        assertThat(xml, is(exp));
    }
    */

    @Test
    public void marshalTest() throws XMLStreamException, JAXBException, ParserConfigurationException, IOException, SAXException, TransformerException {
        String sbml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<sbml xmlns=\"http://www.sbml.org/sbml/level2\" level=\"2\" version=\"4\">\n" +
                "\t<model id=\"model\">\n" +
                "\t\t<listOfSpecies>\n" +
                "\t\t\t<species id=\"mol1\" name=\"arginine\" metaid=\"_000000001\">\n" +
                "\t\t\t\t<annotation>\n" +
                "\t\t\t\t\t<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:bqbiol=\"http://biomodels.net/biology-qualifiers/\" xmlns:bqmodel=\"http://biomodels.net/model-qualifiers/\">\n" +
                "\t\t\t\t\t\t<rdf:Description rdf:about=\"#_000000001\">\n" +
                "\t\t\t\t\t\t\t<bqbiol:is>\n" +
                "\t\t\t\t\t\t\t\t<rdf:Bag>\n" +
                "\t\t\t\t\t\t\t\t\t<rdf:li rdf:resource=\"http://rdf.openmolecules.net/?InChI=1S/C6H14N4O2/c7-4(5(11)12)2-1-3-10-6(8)9/h4H,1-3,7H2,(H,11,12)(H4,8,9,10)\" />\n" +
                "\t\t\t\t\t\t\t\t</rdf:Bag>\n" +
                "\t\t\t\t\t\t\t</bqbiol:is>\n" +
                "\t\t\t\t\t\t</rdf:Description>\n" +
                "\t\t\t\t\t</rdf:RDF>\n" +
                "\t\t\t\t</annotation>\n" +
                "\t\t\t</species>\n" +
                "\t\t\t<species id=\"mol2\" name=\"agmatine\" metaid=\"_000000002\">\n" +
                "\t\t\t\t<annotation>\n" +
                "\t\t\t\t\t<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:bqbiol=\"http://biomodels.net/biology-qualifiers/\" xmlns:bqmodel=\"http://biomodels.net/model-qualifiers/\">\n" +
                "\t\t\t\t\t\t<rdf:Description rdf:about=\"#_000000002\">\n" +
                "\t\t\t\t\t\t\t<bqbiol:is>\n" +
                "\t\t\t\t\t\t\t\t<rdf:Bag>\n" +
                "\t\t\t\t\t\t\t\t\t<rdf:li rdf:resource=\"http://rdf.openmolecules.net/?InChI=1S/C5H14N4/c6-3-1-2-4-9-5(7)8/h1-4,6H2,(H4,7,8,9)\" />\n" +
                "\t\t\t\t\t\t\t\t</rdf:Bag>\n" +
                "\t\t\t\t\t\t\t</bqbiol:is>\n" +
                "\t\t\t\t\t\t</rdf:Description>\n" +
                "\t\t\t\t\t</rdf:RDF>\n" +
                "\t\t\t\t</annotation>\n" +
                "\t\t\t</species>\n" +
                "\t\t</listOfSpecies>\n" +
                "\t\t<listOfReactions>\n" +
                "\t\t\t<reaction id=\"rxn1\">\n" +
                "\t\t\t\t<annotation>\n" +
                "\t\t\t\t\t<rex:rex xmlns:rex=\"http://www.bbk.ac.uk/rex/\">\n" +
                "\t\t\t\t\t\t<rex:extracts>\n" +
                "\t\t\t\t\t\t\t<rex:extract source=\"0000000\">\n" +
                "\t\t\t\t\t\t\t\t<rex:sentence>Arginine is converted to agmatine.</rex:sentence>\n" +
                "\t\t\t\t\t\t\t\t<rex:tag type=\"substrate\" start=\"0\" length=\"8\" id=\"mol1\" />\n" +
                "\t\t\t\t\t\t\t\t<rex:tag type=\"product\" start=\"25\" length=\"8\" id=\"mol2\" />\n" +
                "\t\t\t\t\t\t\t</rex:extract>\n" +
                "\t\t\t\t\t\t</rex:extracts>\n" +
                "\t\t\t\t\t\t<rex:components>\n" +
                "\t\t\t\t\t\t\t<rex:reactants>\n" +
                "\t\t\t\t\t\t\t\t<rex:compound id=\"mol1\" isInSeed=\"true\" isInBranch=\"false\" extraction=\"10.0\" relevance=\"5.0\">\n" +
                "\t\t\t\t\t\t\t\t\t<rex:alternativePathways>\n" +
                "\t\t\t\t\t\t\t\t\t\t<rex:pathway id=\"META000\" />\n" +
                "\t\t\t\t\t\t\t\t\t\t<rex:pathway id=\"META001\" />\n" +
                "\t\t\t\t\t\t\t\t\t</rex:alternativePathways>\n" +
                "\t\t\t\t\t\t\t\t\t<rex:otherPathways>\n" +
                "\t\t\t\t\t\t\t\t\t\t<rex:pathway id=\"META002\" />\n" +
                "\t\t\t\t\t\t\t\t\t</rex:otherPathways>\n" +
                "\t\t\t\t\t\t\t\t</rex:compound>\n" +
                "\t\t\t\t\t\t\t</rex:reactants>\n" +
                "\t\t\t\t\t\t\t<rex:products>\n" +
                "\t\t\t\t\t\t\t\t<rex:compound id=\"mol2\" isInSeed=\"true\" isInBranch=\"false\" extraction=\"10.0\" relevance=\"5.0\">\n" +
                "\t\t\t\t\t\t\t\t\t<rex:alternativePathways>\n" +
                "\t\t\t\t\t\t\t\t\t\t<rex:pathway id=\"META000\" />\n" +
                "\t\t\t\t\t\t\t\t\t\t<rex:pathway id=\"META001\" />\n" +
                "\t\t\t\t\t\t\t\t\t</rex:alternativePathways>\n" +
                "\t\t\t\t\t\t\t\t\t<rex:otherPathways>\n" +
                "\t\t\t\t\t\t\t\t\t\t<rex:pathway id=\"META002\" />\n" +
                "\t\t\t\t\t\t\t\t\t</rex:otherPathways>\n" +
                "\t\t\t\t\t\t\t\t</rex:compound>\n" +
                "\t\t\t\t\t\t\t</rex:products>\n" +
                "\t\t\t\t\t\t</rex:components>\n" +
                "\t\t\t\t\t</rex:rex>\n" +
                "\t\t\t\t</annotation>\n" +
                "\t\t\t\t<listOfReactants>\n" +
                "\t\t\t\t\t<speciesReference species=\"mol1\" />\n" +
                "\t\t\t\t</listOfReactants>\n" +
                "\t\t\t\t<listOfProducts>\n" +
                "\t\t\t\t\t<speciesReference species=\"mol2\" />\n" +
                "\t\t\t\t</listOfProducts>\n" +
                "\t\t\t</reaction>\n" +
                "\t\t</listOfReactions>\n" +
                "\t</model>\n" +
                "</sbml>";

        SBMLDocument sbmlDoc = new SBMLReader().readSBMLFromString(sbml);
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
