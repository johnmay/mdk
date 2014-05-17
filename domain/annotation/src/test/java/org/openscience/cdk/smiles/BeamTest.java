package org.openscience.cdk.smiles;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.silent.Atom;
import org.openscience.cdk.silent.AtomContainer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/** @author John May */
public class BeamTest {

    @Test
    public void read_benzene() throws Exception {
        IAtomContainer m = Beam.fromSMILES("C1=CC=CC=C1");
        assertThat(m.getAtomCount(), is(6));
    }

    @Test
    public void read_invalid() throws Exception {
        IAtomContainer m = Beam.fromSMILES("ccc");
        assertThat(m.getAtomCount(), is(0));
    }

    @Test
    public void write_ethene() throws Exception {
        IAtomContainer m = new AtomContainer();
        m.addAtom(new Atom("C"));        
        m.addAtom(new Atom("C"));
        m.getAtom(0).setImplicitHydrogenCount(2);
        m.getAtom(1).setImplicitHydrogenCount(2);
        m.addBond(0, 1, IBond.Order.DOUBLE);
        assertThat(Beam.toSMILES(m), is("C=C"));
    }
}
