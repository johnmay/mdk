/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.io.sbml;

import uk.ac.ebi.io.xml.SBMLIOUtil;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.stream.XMLStreamException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import uk.ac.ebi.core.Compartment;
import uk.ac.ebi.core.MetabolicReaction;
import uk.ac.ebi.core.MetaboliteImplementation;
import uk.ac.ebi.core.Reconstruction;
import uk.ac.ebi.core.reaction.MetabolicParticipant;
import uk.ac.ebi.resource.ReconstructionIdentifier;
import uk.ac.ebi.resource.chemical.BasicChemicalIdentifier;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.classification.ECNumber;
import uk.ac.ebi.resource.organism.Taxonomy;

/**
 *
 * @author johnmay
 */
public class SBMLIOUtilTest {

    public SBMLIOUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetDocument() throws SBMLException, XMLStreamException,
                                         UnsupportedEncodingException {
        Reconstruction recon = new Reconstruction(new ReconstructionIdentifier("mnb-project"),
                                                  new Taxonomy());
        MetaboliteImplementation m = new MetaboliteImplementation(new BasicChemicalIdentifier("m2"), null, null);
        m.setName("molecule name");
        m.addCrossReference(new ChEBIIdentifier("CHEBI:12435"));
        recon.addMetabolite(m);

        MetaboliteImplementation m2 = new MetaboliteImplementation(new BasicChemicalIdentifier("m2"), null, null);
        m2.setName("different molecule");
        m2.addCrossReference(new ChEBIIdentifier("CHEBI:12436"));
        recon.addMetabolite(m2);

        MetabolicReaction rxn = new MetabolicReaction();
        rxn.addReactant(new MetabolicParticipant(m, 1.0, Compartment.CYTOPLASM));
        rxn.addProduct(new MetabolicParticipant(m2, 2.0, Compartment.EXTRACELLULA));
        rxn.addCrossReference(new ECNumber("1.1.1.1"));
        recon.addReaction(rxn);

        SBMLDocument doc = new SBMLIOUtil().getDocument(recon);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        SBMLWriter.write(doc, stream, "MNB", "1.0");
        String result = stream.toString("UTF-8");
        System.out.println("SBML:" + result);
        // todo assertEquals

    }
}
