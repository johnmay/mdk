package uk.ac.ebi.core.tools.compare;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.chemet.resource.basic.BasicChemicalIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

/**
 * @author John May
 */
public class CrossReferenceComparatorTest {

    private static final Logger LOGGER = Logger.getLogger(CrossReferenceComparatorTest.class);

    private CrossReferenceComparator<Metabolite> comparator = new CrossReferenceComparator<Metabolite>();

    @Test
    public void testEqual_direct() throws Exception {

        Metabolite m1 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(comparator.equal(m1, m2));

        m1.addAnnotation(new CrossReference(new BasicChemicalIdentifier("test-2")));

        Assert.assertTrue(comparator.equal(m1, m2));
        Assert.assertTrue(comparator.equal(m2, m1));

    }

    @Test
    public void testEqual_indirect() throws Exception {

        Metabolite m1 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(comparator.equal(m1, m2));

        m1.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));
        m2.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));

        Assert.assertTrue(comparator.equal(m1, m2));
        Assert.assertTrue(comparator.equal(m2, m1));

    }

    @Test
    public void testEqual_indirect_subclass() throws Exception {

        Metabolite m1 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(comparator.equal(m1, m2));

        m1.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));
        m2.addAnnotation(new KEGGCrossReference(new KEGGCompoundIdentifier("C00009")));

        Assert.assertTrue(comparator.equal(m1, m2));
        Assert.assertTrue(comparator.equal(m2, m1));

    }


}
