/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core.tools;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.annotation.chemical.Charge;
import uk.ac.ebi.annotation.chemical.MolecularFormula;
import uk.ac.ebi.chemet.TestMoleculeFactory;
import uk.ac.ebi.core.DefaultEntityFactory;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *
 * @author johnmay
 */
public class StructureValidatorTest {

    public StructureValidatorTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testGetValidity_metabolite() {

        Metabolite m = DefaultEntityFactory.getInstance().newInstance(Metabolite.class);

        m.addAnnotation(new MolecularFormula("C10H12N5O13P3"));
        m.addAnnotation(new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_3()));
        m.setCharge(-4d);

        {
            StructuralValidity validity = StructuralValidity.getValidity(m);
            Assert.assertEquals(StructuralValidity.Category.WARNING, validity.getCategory());
        }

        m.addAnnotation(new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_4()));

        {
            StructuralValidity validity = StructuralValidity.getValidity(m);
            Assert.assertEquals(StructuralValidity.Category.CORRECT, validity.getCategory());
        }

    }


    @Test
    public void testGetValidity_singular() {

        {
            StructuralValidity validtor = StructuralValidity.getValidity(new MolecularFormula("C10H12N5O13P3"),
                                                                         new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_3()),
                                                                         new Charge(-4d));
            Assert.assertEquals(StructuralValidity.Category.WARNING, validtor.getCategory());
        }

        {
            StructuralValidity validtor =
                               StructuralValidity.getValidity(new MolecularFormula("C10H13N5O13P3"),
                                                              new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_3()),
                                                              new Charge(-3d));
            Assert.assertEquals(StructuralValidity.Category.CORRECT, validtor.getCategory());
        }
        {
            StructuralValidity validtor =
                               StructuralValidity.getValidity(new MolecularFormula("C10H13N5O13P3"),
                                                              new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_3()),
                                                              new Charge(-6d));
            Assert.assertEquals(StructuralValidity.Category.ERROR, validtor.getCategory());
        }
        {
            StructuralValidity validtor =
                               StructuralValidity.getValidity(new MolecularFormula("C10H16N5O13P3"),
                                                              new AtomContainerAnnotation(TestMoleculeFactory.atp_minus_3()),
                                                              new Charge(0d));
            Assert.assertEquals(StructuralValidity.Category.WARNING, validtor.getCategory());
        }

    }
}
