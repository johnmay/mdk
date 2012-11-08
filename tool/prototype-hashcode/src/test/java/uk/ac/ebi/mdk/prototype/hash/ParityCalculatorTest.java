/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
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

package uk.ac.ebi.mdk.prototype.hash;

import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.util.ParitiyCalculator;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author John May
 */
public class ParityCalculatorTest {

    @Test
    public void testGetTetrahedralParity_R() throws Exception {

        List<IAtomContainer> containers = MolecularHashFactoryTest.readSDF(getClass(),
                                                                           "r-structures.sdf",
                                                                           -1);

        for(IAtomContainer container : containers){

            IAtom chiral = container.getAtom(0);

            int p = ParitiyCalculator.getMDLTetrahedralParity(chiral, container);

            if(p == -1)
                p = 2;

            assertEquals(p,
                         chiral.getStereoParity().intValue());

        }

    }

    @Test
    public void testGetTetrahedralParity_R_explicit() throws Exception {

        List<IAtomContainer> containers = MolecularHashFactoryTest.readSDF(getClass(),
                                                                           "r-structures-explicit.sdf",
                                                                           -1);

        for(IAtomContainer container : containers){

            IAtom chiral = container.getAtom(0);

            int p = ParitiyCalculator.getMDLTetrahedralParity(chiral, container);

            if(p == -1)
                p = 2;

            assertEquals(p,
                         chiral.getStereoParity().intValue());

        }

    }

    @Test
    public void testGetTetrahedralParity_S() throws Exception {

        List<IAtomContainer> containers = MolecularHashFactoryTest.readSDF(getClass(),
                                                                           "s-structures.sdf",
                                                                           -1);

        for(IAtomContainer container : containers){

            IAtom chiral = container.getAtom(0);

            int p = ParitiyCalculator.getMDLTetrahedralParity(chiral, container);

            if(p == -1)
                p = 2;

            assertEquals(p,
                         chiral.getStereoParity().intValue());

        }

    }


    @Test
    public void testGetTetrahedralParity_S_explicit() throws Exception {

        List<IAtomContainer> containers = MolecularHashFactoryTest.readSDF(getClass(),
                                                                           "s-structures-explicit.sdf",
                                                                           -1);

        for(IAtomContainer container : containers){

            IAtom chiral = container.getAtom(0);

            int p = ParitiyCalculator.getMDLTetrahedralParity(chiral, container);

            if(p == -1)
                p = 2;

            assertEquals(p,
                         chiral.getStereoParity().intValue());

        }

    }


    @Test
    public void testDithianediols() throws IOException, CDKException {

        MolecularHashFactory factory = MolecularHashFactory.getInstance();
        factory.setDepth(4);

        List<IAtomContainer> dithianediols = MolecularHashFactoryTest.readSDF(getClass(), "dithianediols.sdf", -1);

        IAtomContainer cisdithianediol   = dithianediols.get(0);
        IAtomContainer transdithianediol = dithianediols.get(1);

        Assert.assertEquals(-1, ParitiyCalculator.getMDLTetrahedralParity(cisdithianediol.getAtom(1), cisdithianediol));
        Assert.assertEquals(-1, ParitiyCalculator.getMDLTetrahedralParity(cisdithianediol.getAtom(5), cisdithianediol));

        Assert.assertEquals(1, ParitiyCalculator.getMDLTetrahedralParity(transdithianediol.getAtom(3), transdithianediol));
        Assert.assertEquals(-1,  ParitiyCalculator.getMDLTetrahedralParity(transdithianediol.getAtom(4), transdithianediol));

    }

    @Test
    public void testSP2() throws IOException, CDKException {

        IAtomContainer container = MolecularHashFactoryTest.readSDF(getClass(), "dutenediol.mol", 1).iterator().next();

        System.out.println(ParitiyCalculator.getSP2Parity(container.getAtom(0), container));
        System.out.println(ParitiyCalculator.getSP2Parity(container.getAtom(2), container));

    }
}
