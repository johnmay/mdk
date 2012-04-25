package uk.ac.ebi.core.tools.compare;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.chemet.resource.basic.BasicChemicalIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.core.MetaboliteImplementation;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.entities.Metabolite;

/**
 * @author John May
 */
public class CrossReferenceComparatorTest {

    private static final Logger LOGGER = Logger.getLogger(CrossReferenceComparatorTest.class);

    private EntityComparator<Metabolite> comparator = new CrossReferenceComparator<Metabolite>();

    @Test
    public void testDirectReference() throws Exception {

        Metabolite m1 = new MetaboliteImplementation(new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = new MetaboliteImplementation(new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(comparator.equal(m1, m2));
        Assert.assertFalse(comparator.equal(m2, m1));

        Annotation xref = new CrossReference(new BasicChemicalIdentifier("test-2"));

        m1.addAnnotation(xref);

        Assert.assertTrue(comparator.equal(m1, m2));
        Assert.assertTrue(comparator.equal(m2, m1)); // simulate transpose


    }


    @Test
    public void testIndirectReference() throws Exception {

        Metabolite m1 = new MetaboliteImplementation(new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = new MetaboliteImplementation(new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(comparator.equal(m1, m2));
        Assert.assertFalse(comparator.equal(m2, m1));

        m1.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));
        m2.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));

        Assert.assertTrue(comparator.equal(m1, m2));
        Assert.assertTrue(comparator.equal(m2, m1)); // simulate transpose


    }

    /**
     * Tests that sub-classes get picked up correctly
     * @throws Exception
     */
    @Test
    public void testIndirectReference_subclass() throws Exception {

        Metabolite m1 = new MetaboliteImplementation(new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = new MetaboliteImplementation(new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(comparator.equal(m1, m2));
        Assert.assertFalse(comparator.equal(m2, m1));

        m1.addAnnotation(new KEGGCrossReference(new KEGGCompoundIdentifier("C00009")));
        m2.addAnnotation(new KEGGCrossReference(new KEGGCompoundIdentifier("C00009")));

        Assert.assertTrue(comparator.equal(m1, m2));
        Assert.assertTrue(comparator.equal(m2, m1)); // simulate transpose


    }

}
