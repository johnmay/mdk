/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.core;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;
import org.openscience.cdk.templates.MoleculeFactory;
import uk.ac.ebi.annotation.chemical.AtomContainerAnnotation;
import uk.ac.ebi.annotation.chemical.Charge;
import uk.ac.ebi.annotation.chemical.InChI;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 *
 * @author johnmay
 */
public class MetaboliteImplementationTest {

    public MetaboliteImplementationTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testUniqueAnnotation() {

        Metabolite m = DefaultEntityFactory.getInstance().newInstance(Metabolite.class);

        m.addAnnotation(new Charge(1d));
        m.addAnnotation(new Charge(2d));

        System.out.println(m.getAnnotations());

    }


    @Test
    public void testHasStructure() {

        Metabolite m = DefaultEntityFactory.getInstance().newInstance(Metabolite.class);

        Assert.assertFalse(m.hasStructure());

        Annotation annotation = new AtomContainerAnnotation();

        m.addAnnotation(annotation);

        Assert.assertTrue(m.hasStructure());

        m.removeAnnotation(annotation);

        Assert.assertFalse(m.hasStructure());

        m.addAnnotation(new InChI());

        Assert.assertTrue(m.hasStructure());

    }


    @Test
    public void testGetStructures() {

        Metabolite m = DefaultEntityFactory.getInstance().newInstance(Metabolite.class);

        m.addAnnotation(new AtomContainerAnnotation(MoleculeFactory.make123Triazole()));
        m.addAnnotation(new InChI("InChI=1S/C2H5N3/c1-2-4-5-3-1/h1-2H2,(H,3,4)"));

        if (m.hasStructure()) {
            for (ChemicalStructure structure : m.getStructures()) {
                Assert.assertEquals(5, structure.getStructure().getAtomCount());
            }
        }


    }
}
