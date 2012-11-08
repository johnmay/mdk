package uk.ac.ebi.mdk.tool.match;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.MetaboliteImpl;
import uk.ac.ebi.mdk.prototype.hash.TestMoleculeFactory;

/**
 * @author John May
 */
public class SubstructureMatcherTest {

    private static final Logger LOGGER = Logger.getLogger(SubstructureMatcherTest.class);


    @Test
    public void testMatches_menaquinol_strict() throws Exception {

        Metabolite menaquinol = new MetaboliteImpl();
        Metabolite menaquinol8 = new MetaboliteImpl();

        IAtomContainer mstructure_markush = TestMoleculeFactory.loadMol(getClass(), "menaquinol-markush.mol", "menaquinol", Boolean.FALSE);
        IAtomContainer mstructure = TestMoleculeFactory.loadMol(getClass(), "ChEBI_18151.mol", "menaquinol", Boolean.FALSE);
        IAtomContainer m8structure = TestMoleculeFactory.loadMol(getClass(), "ChEBI_61684.mol", "menaquinol-8 (CHEBI)", Boolean.FALSE);


        Annotation annotation = new AtomContainerAnnotation(mstructure_markush);

        menaquinol.addAnnotation(annotation);
        menaquinol8.addAnnotation(new AtomContainerAnnotation(m8structure));

        EntityMatcher<Metabolite, ?> matcher = new SubstructureMatcher(new Fingerprinter(), Boolean.TRUE);

        // menaquinol (reference) should be a substructure of menaquinol8 (query)
        Assert.assertTrue(matcher.matches(menaquinol8, menaquinol));

        // menaquinol8 (reference) should NOT be a substructure of menaquinol (query)
        Assert.assertFalse(matcher.matches(menaquinol, menaquinol8));


        // remove and try with non-markush
        menaquinol.removeAnnotation(annotation);

        menaquinol.addAnnotation(new AtomContainerAnnotation(mstructure));

        // menaquinol (reference) should be a substructure of menaquinol8 (query)
        // but allow non markush flag is false!
        Assert.assertFalse(matcher.matches(menaquinol8, menaquinol));

    }


    @Test
    public void testMatches_menaquinol() throws Exception {

        Metabolite menaquinol = new MetaboliteImpl();
        Metabolite menaquinol8 = new MetaboliteImpl();

        IAtomContainer mstructure = TestMoleculeFactory.loadMol(getClass(), "ChEBI_18151.mol", "menaquinol", Boolean.FALSE);
        IAtomContainer m8structure = TestMoleculeFactory.loadMol(getClass(), "ChEBI_61684.mol", "menaquinol-8 (CHEBI)", Boolean.FALSE);


        menaquinol.addAnnotation(new AtomContainerAnnotation(mstructure));
        menaquinol8.addAnnotation(new AtomContainerAnnotation(m8structure));

        EntityMatcher<Metabolite, ?> matcher = new SubstructureMatcher(new Fingerprinter(), Boolean.FALSE);

        // menaquinol (reference) should be a substructure of menaquinol8 (query)
        Assert.assertTrue(matcher.matches(menaquinol8, menaquinol));

        // menaquinol8 (reference) should NOT be a substructure of menaquinol (query)
        Assert.assertFalse(matcher.matches(menaquinol, menaquinol8));

    }


}
