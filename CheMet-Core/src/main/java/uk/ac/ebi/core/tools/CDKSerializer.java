/**
 * CDKSerializer.java
 *
 * 2011.10.28
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.core.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *          CDKSerializer - 2011.10.28 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class CDKSerializer {

    public static byte[] serialize(IAtomContainer molecule) {

        List<Byte> bytes = new ArrayList();

        if (molecule.getAtomCount() > 128) {
            throw new UnsupportedOperationException("Molecule is too large for this method (currently)");
        }

        bytes.add((byte) molecule.getAtomCount());

        Map<IAtom, Byte> atomMap = new HashMap();

        for (int i = 0; i < bytes.get(0); i++) {
            IAtom atom = molecule.getAtom(i);
            byte[] symbol = atom.getSymbol().getBytes();
            bytes.add((byte) symbol.length);
            for (byte c : symbol) {
                bytes.add(c);
            }
            
            // xy data
            byte[] xb = toByta(atom.getPoint2d().x);
            byte[] yb = toByta(atom.getPoint2d().y);

            for (int k = 0; k < 8; k++) {
                bytes.add(xb[k]);
                bytes.add(yb[k]);
            }


            atomMap.put(atom, (byte) i);
        }

        bytes.add((byte) molecule.getBondCount());

        for (int i = 0; i < bytes.get(1); i++) {
            IBond bond = molecule.getBond(i);
            bytes.add((byte) bond.getAtomCount());
            for (int j = 0; j < bond.getAtomCount(); j++) {
                bytes.add(atomMap.get(bond.getAtom(j)));
            }
            bytes.add((byte) (int) bond.getElectronCount());
        }

        byte[] bs = new byte[bytes.size()];
        for (int i = 0; i < bs.length; i++) {
            bs[i] = bytes.get(i);
        }

        return bs;

    }

    /**
     * from http://www.daniweb.com/software-development/java/code/216874
     *
     */
    public static byte[] toByta(double data) {
        return toByta(Double.doubleToRawLongBits(data));
    }

    public static byte[] toByta(long data) {
        return new byte[]{
                    (byte) ((data >> 56) & 0xff),
                    (byte) ((data >> 48) & 0xff),
                    (byte) ((data >> 40) & 0xff),
                    (byte) ((data >> 32) & 0xff),
                    (byte) ((data >> 24) & 0xff),
                    (byte) ((data >> 16) & 0xff),
                    (byte) ((data >> 8) & 0xff),
                    (byte) ((data >> 0) & 0xff),};
    }
}
