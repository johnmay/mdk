package uk.ac.ebi.mdk.apps.tool;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.fingerprint.HybridizationFingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.Beam;
import org.openscience.cdk.smiles.SmilesParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** @author John May */
public class SubstructureSetTest {

    @Test public void simpleSet() throws CDKException {
        
        List<String> subs = Arrays.asList("OC1=CC=C([*])C=C1",
                                          "NC1=CC=C([*])C=C1",
                                          "[*]N1C=CC=C1");
        
        SubstructureSet ss = new SubstructureSet(new HybridizationFingerprinter(),
                                                 load(subs));
        
        Assert.assertTrue(ss.contains(Beam.fromSMILES("OC1=CC=C(C)C=C1")));
        Assert.assertTrue(ss.contains(Beam.fromSMILES("OC1=CC=C(CC=CC)C=C1")));
        Assert.assertTrue(ss.contains(Beam.fromSMILES("OC1=CC=C(CC=CC)C=C1")));
        
        Assert.assertTrue(ss.contains(Beam.fromSMILES("CCCCCCN1C=CC=C1")));
        Assert.assertTrue(ss.contains(Beam.fromSMILES("CCCCN1C=CC=C1")));
        Assert.assertTrue(ss.contains(Beam.fromSMILES("CCCCCCN1C=CC=C1")));     
        Assert.assertTrue(ss.contains(Beam.fromSMILES("C1=CC=CC1C=CCCN1C=CC=C1")));    
        
        
        Assert.assertFalse(ss.contains(Beam.fromSMILES("PC1=CC=C(CC=CC)C=C1")));
        
    }
    
    private static List<IAtomContainer> load(List<String> smis) throws InvalidSmilesException {
        List<IAtomContainer> ms = new ArrayList<IAtomContainer>();        
        for (String smi : smis) {
            ms.add(Beam.fromSMILES(smi));
        }
        return ms;
    }
}
