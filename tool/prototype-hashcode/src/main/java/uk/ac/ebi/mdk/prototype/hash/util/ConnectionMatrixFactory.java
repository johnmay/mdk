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

/**
 *          ConnectionMatrixFactory - 2011.11.09 <br>
 *          A factory for connection matrices
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ConnectionMatrixFactory {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ConnectionMatrixFactory.class);
    private HashMap<IAtom, Integer> atomIndexMap = new HashMap(50);
    private EnumMap<IBond.Order, Byte> electronPairs = new EnumMap(IBond.Order.class);
    private static final Map<String, Integer> symbolToOuterElectronCount = new HashMap();


    static {
        symbolToOuterElectronCount.put("H", 1);
        symbolToOuterElectronCount.put("C", 4);
        symbolToOuterElectronCount.put("N", 5);
        symbolToOuterElectronCount.put("O", 6);
        symbolToOuterElectronCount.put("P", 5);
        symbolToOuterElectronCount.put("S", 6);
    }

    private ConnectionMatrixFactory() {

        electronPairs.put(IBond.Order.SINGLE, new Byte("1"));
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
     *
     * Returns the connection matrix for the molecule where 1 indicates two
     * atoms are connected and 0 indicates they are not. The matrix is
     * symmetrical
     *
     * @param molecule
     * @return
     * 
     */
    public byte[][] getConnectionMatrix(IAtomContainer molecule) {
        byte[][] connections = new byte[molecule.getAtomCount()][molecule.getAtomCount()];

        IAtom[] atoms = AtomContainerManipulator.getAtomArray(molecule);
        IBond[] bonds = AtomContainerManipulator.getBondArray(molecule);

        atomIndexMap.clear(); // reuse atom index map

        for (int i = 0; i < bonds.length; i++) {
            IBond bond = bonds[i];

            for (int j = 0; j < bond.getAtomCount(); j++) {

                IAtom jAtom = bond.getAtom(j);
                int jIndex = getIndex(jAtom, atoms);

                for (int k = j + 1; k < bond.getAtomCount(); k++) {

                    IAtom kAtom = bond.getAtom(k);
                    int kIndex = getIndex(kAtom, atoms);

                    connections[jIndex][kIndex] = connections[kIndex][jIndex] = 1;

                }
            }
        }



        return connections;
    }

    /**
     *
     * Creates a Bond Electron Matrix (BE-Matrix) for the provided molecule. These
     * matrices are also known as UGI matrices. At the moment this is not a true
     * BE-Matrix as the lone pairs are not added
     *
     * @return
     * 
     */
    public byte[][] getBondElectronMatrix(IAtomContainer molecule)  {

        byte[][] connections = new byte[molecule.getAtomCount()][molecule.getAtomCount()];

        IAtom[] atoms = AtomContainerManipulator.getAtomArray(molecule);
        IBond[] bonds = AtomContainerManipulator.getBondArray(molecule);

        atomIndexMap.clear(); // reuse atom index map

        for (int i = 0; i < bonds.length; i++) {
            IBond bond = bonds[i];

            for (int j = 0; j < bond.getAtomCount(); j++) {

                IAtom jAtom = bond.getAtom(j);
                int jIndex = getIndex(jAtom, atoms);
                connections[jIndex][jIndex] = getNonBondedValenceElectronCount(molecule, jAtom).byteValue();

                for (int k = j + 1; k < bond.getAtomCount(); k++) {

                    IAtom kAtom = bond.getAtom(k);
                    int kIndex = getIndex(kAtom, atoms);

                    connections[jIndex][kIndex] =
                    connections[kIndex][jIndex] = electronPairs.get(bond.getOrder());

                }
            }
        }

        return connections;

    }

    /**
     *
     * Access the index of the atom array. A map is used to speed up access
     * prevent re-searching of the array. Subsequently the field atomIndexMap
     * should be cleared for each new molecule
     *
     * @param atom  the atom to find the index of
     * @param atoms array of atoms
     * 
     * @return
     *
     */
    private int getIndex(IAtom atom, IAtom[] atoms) {

        if (atomIndexMap.containsKey(atom)) {
            return atomIndexMap.get(atom);
        }

        for (int i = 0; i < atoms.length; i++) {
            // we don't want content comparisson here (not that CDK does that)
            if (atom == atoms[i]) {
                atomIndexMap.put(atom, i);
                return i;
            }
        }

        throw new UnsupportedOperationException("Could not find index of atom in array!");
    }

    public static Integer getNonBondedValenceElectronCount(IAtomContainer molecule, IAtom atom) {

        if (atom.getProperties().containsKey("nb-valence-electrons")) {
            return (Integer) atom.getProperties().get("nb-valence-electrons");
        }

        Integer shellCount = symbolToOuterElectronCount.get(atom.getSymbol());

        if (shellCount != null) {
            int adjusted = shellCount - atom.getFormalCharge();
            int nonbondedvalence = adjusted - ((Double) AtomContainerManipulator.getBondOrderSum(molecule, atom)).intValue();
            atom.getProperties().put("nb-valence-electrons", nonbondedvalence);
            return nonbondedvalence;
        } else {
            LOGGER.error("Unhandled atom symbol: " + atom.getSymbol());
        }
        return 0;
    }


}
