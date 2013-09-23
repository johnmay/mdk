package uk.ac.ebi.mdk.apps.tool;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.fingerprint.HybridizationFingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.Beam;
import uk.ac.ebi.mdk.domain.annotation.SMILES;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;

/** @author John May */
public class SubstructureSetTest {

    @Test public void simpleSet() throws CDKException {
        
        List<Metabolite> subs = Arrays.asList(metabolite("OC1=CC=C([*])C=C1"),
                                          metabolite("NC1=CC=C([*])C=C1"),
                                          metabolite("[*]N1C=CC=C1"));
        
        SubstructureSet ss = new SubstructureSet(new HybridizationFingerprinter(),
                                                 subs);
        
        Assert.assertThat(ss.matching(Beam.fromSMILES("OC1=CC=C(C)C=C1")),
                          hasItem(subs.get(0)));
        Assert.assertThat(ss.matching(Beam.fromSMILES("OC1=CC=C(CC=CC)C=C1")),
                          hasItem(subs.get(0)));
        Assert.assertThat(ss.matching(Beam.fromSMILES("OC1=CC=C(CC=CC)C=C1")),
                          hasItem(subs.get(0)));
        
        Assert.assertThat(ss.matching(Beam.fromSMILES("CCCCCCN1C=CC=C1")),
                          hasItem(subs.get(0)));
        Assert.assertThat(ss.matching(Beam.fromSMILES("CCCCN1C=CC=C1")),
                          hasItem(subs.get(0)));
        Assert.assertThat(ss.matching(Beam.fromSMILES("CCCCCCN1C=CC=C1")),
                          hasItem(subs.get(0)));     
        Assert.assertThat(ss.matching(Beam.fromSMILES("C1=CC=CC1C=CCCN1C=CC=C1")),
                          hasItem(subs.get(0)));   
        
        
        Assert.assertTrue(ss.matching(Beam.fromSMILES("PC1=CC=C(CC=CC)C=C1")).isEmpty());
        
    }
    
    private static Metabolite metabolite(String smi) {
        Metabolite m = DefaultEntityFactory.getInstance().metabolite();
        m.addAnnotation(new SMILES(smi));
        return m;
    }
    
    private static List<IAtomContainer> load(List<String> smis) throws InvalidSmilesException {
        List<IAtomContainer> ms = new ArrayList<IAtomContainer>();        
        for (String smi : smis) {
            ms.add(Beam.fromSMILES(smi));
        }
        return ms;
    }
}
