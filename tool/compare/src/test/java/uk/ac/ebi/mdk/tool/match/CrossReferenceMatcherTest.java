package uk.ac.ebi.mdk.tool.match;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

/**
 * @author John May
 */
public class CrossReferenceMatcherTest {

    private static final Logger LOGGER = Logger.getLogger(CrossReferenceMatcherTest.class);

    private CrossReferenceMatcher<Metabolite> matcher = new CrossReferenceMatcher<Metabolite>();

    @Test
    public void testEqual_direct() throws Exception {

        Metabolite m1 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(matcher.matches(m1, m2));

        m1.addAnnotation(new CrossReference(new BasicChemicalIdentifier("test-2")));

        Assert.assertTrue(matcher.matches(m1, m2));
        Assert.assertTrue(matcher.matches(m2, m1));

    }

    @Test
    public void testEqual_indirect() throws Exception {

        Metabolite m1 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(matcher.matches(m1, m2));

        m1.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));
        m2.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));

        Assert.assertTrue(matcher.matches(m1, m2));
        Assert.assertTrue(matcher.matches(m2, m1));

    }

    @Test
    public void testEqual_indirect_subclass() throws Exception {

        Metabolite m1 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-1"), "", "");
        Metabolite m2 = DefaultEntityFactory.getInstance().ofClass(Metabolite.class, new BasicChemicalIdentifier("test-2"), "", "");

        Assert.assertFalse(matcher.matches(m1, m2));

        m1.addAnnotation(new CrossReference(new KEGGCompoundIdentifier("C00009")));
        m2.addAnnotation(new KEGGCrossReference(new KEGGCompoundIdentifier("C00009")));

        Assert.assertTrue(matcher.matches(m1, m2));
        Assert.assertTrue(matcher.matches(m2, m1));

    }


}
