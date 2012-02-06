/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.xml;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.templates.MoleculeFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.annotation.chemical.MolecularFormula;
import uk.ac.ebi.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

/**
 *
 * @author johnmay
 */
public class XMLAnnotationWriterTest {

    public XMLAnnotationWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testVisit_Annotation() {
    }

    @Test
    public void testVisit_CrossReference() throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        ChEBICrossReference xref = new ChEBICrossReference(new ChEBIIdentifier(12));

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        XMLAnnotationWriter vistor = new XMLAnnotationWriter(doc);
        doc.appendChild(vistor.visit(xref));

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "no");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        String actual = sw.toString().trim();

        String expected =
               "<metingeer-annotation index=\"7\"><identifier-index>5</identifier-index><accession>CHEBI:12</accession></metingeer-annotation>";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testVisit_StringAnnotation() throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        MolecularFormula mf = new MolecularFormula("C22H36N16");
        Synonym synonym = new Synonym("Random name");

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        XMLAnnotationWriter vistor = new XMLAnnotationWriter(doc);
        Element root = doc.createElement("annotations");
        doc.appendChild(root);
        root.appendChild(vistor.visit(mf));
        root.appendChild(vistor.visit(synonym));

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "no");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        String actual = sw.toString().trim();

        String expected = "<annotations>"
                          + "<metingeer-annotation index=\"6\">C22H36N16</metingeer-annotation>"
                          + "<metingeer-annotation index=\"14\">Random name</metingeer-annotation>"
                          + "</annotations>";

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testVisit_ChemicalStructure() throws ParserConfigurationException, TransformerConfigurationException, TransformerException {

        IAtomContainer molecule = MoleculeFactory.makeAdenine();
        AtomContainerAnnotation structure = new AtomContainerAnnotation(molecule);

        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element element = new XMLAnnotationWriter(doc).visit(structure);

        doc.appendChild(element);

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        String actual = sw.toString();

        //TODO this is hard to test as the MOL file can lose it's formating. Perhaps better to write in CML?
        Assert.assertEquals("<metingeer-annotation index=\"1\">", actual.substring(0, 32));

    }
}
