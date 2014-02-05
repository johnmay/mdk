/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package org.openscience.cdk.isomorphism;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.isomorphism.matchers.smarts.SmartsMatchers;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.smarts.parser.SMARTSParser;

import static org.openscience.cdk.interfaces.IBond.Order.SINGLE;

/** @author John May */
public final class RingClose {

    private static final IChemObjectBuilder bldr = SilentChemObjectBuilder.getInstance();

    private final Pattern sixMemberRing  = Pattern.findSubstructure(sma("[O!RH1]-[C!R!$(C=O)]-[C!R]-[C!R]-[C!R]-[C!RH1]=O"));
    private final Pattern fiveMemberRing = Pattern.findSubstructure(sma("[O!RH1]-[C!R!$(C=O)]-[C!R]-[C!R]-[C!RH1]=O"));

    public boolean close(IAtomContainer container) {

        SmartsMatchers.prepare(container, true);

        int[] m = sixMemberRing.match(container);
        if (m.length > 0) {
            IAtom a0 = container.getAtom(m[0]);
            IAtom a5 = container.getAtom(m[5]);
            IAtom a6 = container.getAtom(m[6]);
            container.addBond(m[0], m[5], SINGLE);
            container.getBond(a5, a6)
                     .setOrder(SINGLE);
            a6.setImplicitHydrogenCount(1);
            if (a0.getImplicitHydrogenCount() == 1) {
                a0.setImplicitHydrogenCount(0);
            }
            else {
                for (IBond bond : container.getConnectedBondsList(a0)) {
                    if (bond.getConnectedAtom(a0).getAtomicNumber() == 1)
                        container.removeBond(bond);
                }
            }
            return true;
        }

        m = fiveMemberRing.match(container);
        if (m.length > 0) {
            IAtom a0 = container.getAtom(m[0]);
            IAtom a4 = container.getAtom(m[4]);
            IAtom a5 = container.getAtom(m[5]);
            container.addBond(m[0], m[4], SINGLE);
            container.getBond(a4, a5)
                     .setOrder(SINGLE);
            a5.setImplicitHydrogenCount(1);
            if (a0.getImplicitHydrogenCount() == 1) {
                a0.setImplicitHydrogenCount(0);
            }
            else {
                for (IBond bond : container.getConnectedBondsList(a0)) {
                    if (bond.getConnectedAtom(a0).getAtomicNumber() == 1)
                        container.removeBond(bond);
                }
            }
            return true;
        }

        return false;
    }

    private static IAtomContainer sma(String sma) {
        return SMARTSParser.parse(sma, bldr);
    }
}
