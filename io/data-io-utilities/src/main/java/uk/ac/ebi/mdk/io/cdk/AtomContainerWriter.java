/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.silent.Atom;

import javax.vecmath.Point2d;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author John May
 */
public class AtomContainerWriter {

    private final DataOutput out;


    public AtomContainerWriter(DataOutput out) {
        this.out = out;
    }

    public void write(IAtomContainer mol) throws IOException {

        out.writeInt(mol.getAtomCount());
        out.writeInt(mol.getBondCount());

        for(IAtom atom : mol.atoms()) {

            boolean pseudo = atom instanceof IPseudoAtom;
            out.writeBoolean(pseudo);

            out.writeInt(atom.getAtomicNumber());
            out.writeInt(atom.getMassNumber() != null ? atom.getMassNumber() : 0);

            Point2d p = atom.getPoint2d();
            if(p != null) {
                out.writeInt((int) (p.x * 10000));
                out.writeInt((int) (p.y * 10000));
            } else {
                out.writeInt(0);
                out.writeInt(0);
            }

            out.writeUTF(atom.getSymbol());
            if(pseudo){
                String label = ((IPseudoAtom) atom).getLabel();
                out.writeUTF(label != null ? label : "");
            }
            out.writeByte(atom.getFormalCharge() != null
                          ? atom.getFormalCharge() : Byte.MIN_VALUE);
            out.writeByte(atom.getImplicitHydrogenCount() != null
                          ? atom.getImplicitHydrogenCount() : Byte.MIN_VALUE);
            out.writeUTF(atom.getAtomTypeName() != null
                         ? atom.getAtomTypeName() :
                         "");
        }

        for(IBond bond : mol.bonds()){

            IAtom a1 = bond.getAtom(0);
            IAtom a2 = bond.getAtom(1);

            out.writeInt(mol.getAtomNumber(a1));
            out.writeInt(mol.getAtomNumber(a2));

            // XXX: ordinal may change
            out.writeByte(bond.getOrder().ordinal());
            out.writeByte(bond.getStereo().ordinal());
        }

        out.writeInt(mol.getSingleElectronCount());
        for(int i = 0; i < mol.getSingleElectronCount(); i++){
            ISingleElectron se = mol.getSingleElectron(i);
            out.writeInt(mol.getAtomNumber(se.getAtom()));
            out.writeInt(se.getElectronCount());
        }
    }

}
