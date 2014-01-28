/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.io.cdk;

import org.junit.Test;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author John May
 */
public class AtomContainerWriterTest {

    @Test public void pseudoatoms_roundtrip() throws IOException, CDKException {
        IAtomContainer benzeneOut = MoleculeFactory.makeBenzene();

        IAtom a = benzeneOut.getAtom(0);
        IAtom r = new PseudoAtom("ARR!");
        for (IBond bond : benzeneOut.getConnectedBondsList(a)) {
            if (bond.getAtom(0) == a) {
                bond.setAtom(r, 0);
            } else if (bond.getAtom(1) == a) {
                bond.setAtom(r, 1);
            }
        }
        benzeneOut.setAtom(0, r);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(benzeneOut);
        CDKHydrogenAdder.getInstance(SilentChemObjectBuilder.getInstance()).addImplicitHydrogens(benzeneOut);
        r.setImplicitHydrogenCount(0);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ByteArrayOutputStream bao2 = new ByteArrayOutputStream();

        DataOutputStream out = new DataOutputStream(bao);
        new AtomContainerWriter(out).write(benzeneOut);
        out.close();

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bao.toByteArray()));
        IAtomContainer benzeneIn = new AtomContainerReader(in).read();
        in.close();
        
        assertAtomContainerEquals(benzeneOut, benzeneIn);
    }

    @Test public void adenine_roundtrip() throws IOException, CDKException {

        IAtomContainer benzeneOut = MoleculeFactory.makeAdenine();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(benzeneOut);
        CDKHydrogenAdder.getInstance(SilentChemObjectBuilder.getInstance())
                        .addImplicitHydrogens(benzeneOut);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ByteArrayOutputStream bao2 = new ByteArrayOutputStream();

        DataOutputStream out = new DataOutputStream(bao);
        new AtomContainerWriter(out).write(benzeneOut);
        out.close();

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bao.toByteArray()));
        IAtomContainer benzeneIn = new AtomContainerReader(in).read();
        in.close();

        assertAtomContainerEquals(benzeneOut, benzeneIn);

    }

    @Test public void adenine_radical_roundtrip() throws IOException,
                                                         CDKException {

        IAtomContainer benzeneOut = MoleculeFactory.makeAdenine();
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(benzeneOut);
        CDKHydrogenAdder.getInstance(SilentChemObjectBuilder.getInstance())
                        .addImplicitHydrogens(benzeneOut);
        benzeneOut.addSingleElectron(0);
        benzeneOut.addSingleElectron(4);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ByteArrayOutputStream bao2 = new ByteArrayOutputStream();

        DataOutputStream out = new DataOutputStream(bao);
        new AtomContainerWriter(out).write(benzeneOut);
        out.close();

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bao.toByteArray()));
        IAtomContainer benzeneIn = new AtomContainerReader(in).read();
        in.close();


        assertAtomContainerEquals(benzeneOut, benzeneIn);

    }

    private static void assertAtomContainerEquals(IAtomContainer out, IAtomContainer in) {
        for (int i = 0; i < out.getAtomCount(); i++) {
            IAtom aOut = out.getAtom(i);
            IAtom aIn = in.getAtom(i);
            assertEquals(aOut.getSymbol(), aIn.getSymbol());
            assertEquals(aOut.getAtomicNumber(), aIn.getAtomicNumber());
            assertEquals(aOut.getImplicitHydrogenCount(), aIn.getImplicitHydrogenCount());
            assertEquals(aOut.getAtomTypeName(), aIn.getAtomTypeName());
            assertEquals(aOut.getMassNumber(), aIn.getMassNumber());
            assertEquals(aOut.getFormalCharge(), aIn.getFormalCharge());
            if(aOut.getPoint2d() != null) {
                assertEquals(aOut.getPoint2d().x, aIn.getPoint2d().x, 0.1);
                assertEquals(aOut.getPoint2d().y, aIn.getPoint2d().y, 0.1);
            }
            if (aOut instanceof IPseudoAtom) {
                assertTrue(aIn instanceof IPseudoAtom);
                assertEquals(((IPseudoAtom) aOut)
                                     .getLabel(), ((IPseudoAtom) aIn)
                                     .getLabel());
            }
        }

        for (int i = 0; i < out.getAtomCount(); i++) {
            IBond bOut = out.getBond(i);
            IBond bIn = in.getBond(i);
            assertEquals(out.getAtomNumber(bOut.getAtom(0)),
                         in.getAtomNumber(bIn.getAtom(0)));
            assertEquals(out.getAtomNumber(bOut.getAtom(1)),
                         in.getAtomNumber(bIn.getAtom(1)));
            assertEquals(bOut.getStereo(), bIn.getStereo());
            assertEquals(bOut.getOrder(), bIn.getOrder());
        }

        for (int i = 0; i < out.getSingleElectronCount(); i++) {
            ISingleElectron seOut = out.getSingleElectron(i);
            ISingleElectron seIn = in.getSingleElectron(i);
            assertEquals(out.getAtomNumber(seOut.getAtom()),
                         in.getAtomNumber(seIn.getAtom()));
            assertEquals(seOut.getElectronCount(),
                         seIn.getElectronCount());
        }
    }

}
