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

package uk.ac.ebi.mdk.prototype.hash.seed;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.prototype.hash.TestMoleculeFactory;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;

/**
 * @author John May
 */
public class AtomicNumberSeedTest {

    private static final Logger LOGGER = Logger.getLogger(AtomicNumberSeedTest.class);

    @Test
    public void testHexadecanoate() {

        IAtomContainer heptaecanone = TestMoleculeFactory.loadMol(getClass(), "CPD-7894.mol", "2-heptadecanone");
        IAtomContainer palmiticAmide = TestMoleculeFactory.loadMol(getClass(), "CPD6666-3.mol", "palmitic mide");

        MolecularHashFactory factory = MolecularHashFactory.getInstance();
        factory.setDepth(1);
        Assert.assertNotSame(factory.getHash(heptaecanone), factory.getHash(palmiticAmide));


    }

}
