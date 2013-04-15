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

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;


/**
 * @author John May
 */
public class BooleanRadicalSeedTest {

    private final IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();
    private final BooleanRadicalSeed seed = new BooleanRadicalSeed();

    @Test
    public void testSeed() throws Exception {

        IAtomContainer container = builder.newInstance(IAtomContainer.class);

        container.addAtom(builder.newInstance(IAtom.class, "C"));
        container.addAtom(builder.newInstance(IAtom.class, "C"));
        container.addAtom(builder.newInstance(IAtom.class, "C"));

        // add a single electron to the first and third atom
        container.addSingleElectron(builder.newInstance(ISingleElectron.class,
                                                        container.getAtom(0)));
        container.addSingleElectron(builder.newInstance(ISingleElectron.class,
                                                        container.getAtom(2)));

        // asserts difference between seeds for this two atoms
        assertThat("identical values for C1 and C2",
                   seed.seed(container, container.getAtom(0)),
                   is(not(seed.seed(container, container.getAtom(1)))));
        assertThat("non-identical values for C1 and C3",
                   seed.seed(container, container.getAtom(0)),
                   is(seed.seed(container, container.getAtom(2))));


    }

    @Test
    public void testToString() throws Exception {
        assertThat("unexpected toString output",
                   seed.toString(),
                   is("Radical (boolean)"));
    }


}
