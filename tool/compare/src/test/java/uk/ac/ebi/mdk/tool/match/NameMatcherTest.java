/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool.match;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 *
 * @author johnmay
 */
public class NameMatcherTest {


    private EntityFactory factory;

    private Metabolite m1;

    private Metabolite m2;


    public NameMatcherTest() {

        factory = DefaultEntityFactory.getInstance();
    }


    @Before
    public void createNewMeatbolites() {
        m1 = factory.newInstance(Metabolite.class);
        m2 = factory.newInstance(Metabolite.class);
    }


    @Test
    public void testNameEquality() {

        EntityMatcher matcher = new NameMatcher();

        m1.setName("ATP");
        m2.setName("Adenosine TP");

        Assert.assertFalse(matcher.matches(m1, m2));

        m2.setName("ATP");

        Assert.assertTrue(matcher.matches(m1, m2));

    }

    @Test
    public void testNameEquality_inequalCase() {

        EntityMatcher matcher = new NameMatcher();

        m1.setName("ATP");
        m2.setName("Adenosine TP");

        Assert.assertFalse(matcher.matches(m1, m2));

        m2.setName("atp");

        Assert.assertTrue(matcher.matches(m1, m2));

    }


    @Test
         public void testSynonymEquality() {

        EntityMatcher matcher = new NameMatcher(false, true);

        m1.setName("ATP");
        m2.setName("Adenosine TP");

        Assert.assertFalse(matcher.matches(m1, m2));

        m2.addAnnotation(new Synonym("atp"));

        Assert.assertTrue(matcher.matches(m1, m2));

    }

    @Test
    public void testNormalisedEquality() {

        EntityMatcher matcher = new NameMatcher(false, false);

        m1.setName("Adenosine triphosphate");
        m2.setName("Adenosine-triphosphate");

        Assert.assertFalse(matcher.matches(m1, m2));

        matcher = new NameMatcher(true, false);

        Assert.assertTrue(matcher.matches(m1, m2));

    }

}
