/**
 * ConnectionMatrixFactory.java
 *
 * 2011.11.09
 *
 * This file is part of the Metabolic Development Kit
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
package uk.ac.ebi.mdk.prototype.hash.util;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.openscience.cdk.interfaces.IBond.Order.SINGLE;

/**
 * ConnectionMatrixFactory - 2011.11.09 <br> A factory for connection matrices
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class ConnectionMatrixFactory {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j
            .Logger.getLogger(ConnectionMatrixFactory.class);
    private static final EnumMap<IBond.Order, Byte> electronPairs = new EnumMap<IBond.Order, Byte>(IBond.Order.class);
    private static final Map<String, Integer> symbolToOuterElectronCount = new HashMap<String, Integer>();


    static {
        symbolToOuterElectronCount.put("H", 1);
        symbolToOuterElectronCount.put("C", 4);
        symbolToOuterElectronCount.put("N", 5);
        symbolToOuterElectronCount.put("O", 6);
        symbolToOuterElectronCount.put("P", 5);
        symbolToOuterElectronCount.put("S", 6);
    }

    static {
        electronPairs.put(SINGLE, new Byte("1"));
        electronPairs.put(IBond.Order.DOUBLE, new Byte("2"));
        electronPairs.put(IBond.Order.TRIPLE, new Byte("3"));
        electronPairs.put(IBond.Order.QUADRUPLE, new Byte("4"));
    }

    public static ConnectionMatrixFactory getInstance() {
        return ConnectionMatrixFactoryHolder.INSTANCE;
    }

    private static class ConnectionMatrixFactoryHolder {

        private static final ConnectionMatrixFactory INSTANCE = new ConnectionMatrixFactory();
    }

    /**
     * Returns the connection matrix for the molecule where 1 indicates two
     * atoms are connected and 0 indicates they are not. The matrix is
     * symmetrical
     *
     * @param molecule
     * @return
     */
    public byte[][] getConnectionMatrix(IAtomContainer molecule) {

        byte[][] connections = new byte[molecule.getAtomCount()][molecule
                .getAtomCount()];

        IAtom[] atoms = AtomContainerManipulator.getAtomArray(molecule);
        IBond[] bonds = AtomContainerManipulator.getBondArray(molecule);

        for (int i = 0; i < bonds.length; i++) {
            IBond bond = bonds[i];

            for (int j = 0; j < bond.getAtomCount(); j++) {

                IAtom jAtom = bond.getAtom(j);
                int jIndex = molecule.getAtomNumber(jAtom);

                for (int k = j + 1; k < bond.getAtomCount(); k++) {

                    IAtom kAtom = bond.getAtom(k);
                    int kIndex = molecule.getAtomNumber(kAtom);

                    // Argh! f***ing stupid bond types and Order.UNSET
                    if (bond.getOrder() == null)
                        break;
                    Byte pairs = electronPairs.get(bond.getOrder());
                    connections[jIndex][kIndex] = connections[kIndex][jIndex] = pairs != null ? pairs : 1;

                }
            }
        }


        return connections;
    }

    /**
     * Creates a Bond Electron Matrix (BE-Matrix) for the provided molecule.
     * These matrices are also known as UGI matrices. At the moment this is not
     * a true BE-Matrix as the lone pairs are not added
     *
     * @return
     */
    public byte[][] getBondElectronMatrix(IAtomContainer molecule) {

        byte[][] connections = new byte[molecule.getAtomCount()][molecule
                .getAtomCount()];

        IAtom[] atoms = AtomContainerManipulator.getAtomArray(molecule);
        IBond[] bonds = AtomContainerManipulator.getBondArray(molecule);

        for (int i = 0; i < bonds.length; i++) {
            IBond bond = bonds[i];

            for (int j = 0; j < bond.getAtomCount(); j++) {

                IAtom jAtom = bond.getAtom(j);
                int jIndex = molecule.getAtomNumber(jAtom);
                connections[jIndex][jIndex] = getNonBondedValenceElectronCount(molecule, jAtom)
                        .byteValue();

                for (int k = j + 1; k < bond.getAtomCount(); k++) {

                    IAtom kAtom = bond.getAtom(k);
                    int kIndex = molecule.getAtomNumber(kAtom);

                    connections[jIndex][kIndex] =
                            connections[kIndex][jIndex] = electronPairs
                                    .get(bond.getOrder());

                }
            }
        }

        return connections;

    }


    public static Integer getNonBondedValenceElectronCount(IAtomContainer molecule, IAtom atom) {

        if (atom.getProperties().containsKey("nb-valence-electrons")) {
            return (Integer) atom.getProperties().get("nb-valence-electrons");
        }

        Integer shellCount = symbolToOuterElectronCount.get(atom.getSymbol());

        if (shellCount != null) {
            int adjusted = shellCount - atom.getFormalCharge();
            int nonbondedvalence = adjusted - ((Double) AtomContainerManipulator
                    .getBondOrderSum(molecule, atom)).intValue();
            atom.getProperties().put("nb-valence-electrons", nonbondedvalence);
            return nonbondedvalence;
        } else {
            LOGGER.error("Unhandled atom symbol: " + atom.getSymbol());
        }
        return 0;
    }


}
