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

package uk.ac.ebi.mdk.tool;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.openscience.cdk.interfaces.IAtomType.Hybridization.SP3;

/**
 * Simple class to generate test input by permutation atoms
 * around chiral centres.
 * @author John May
 */
public class Permutate {

    public static void main(String[] args) throws IOException, CDKException {

        if (args.length != 1)
            throw new IllegalArgumentException("Not enough arguments");

        File f = new File(args[0]);

        if (!f.exists() || f.isDirectory())
            throw new IllegalArgumentException("File does not exists or is a directory");

        MDLV2000Reader reader = new MDLV2000Reader(new FileReader(f));

        IAtomContainer container = reader.read(SilentChemObjectBuilder.getInstance().newInstance(IAtomContainer.class));

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(container);

        List<List<Integer>> permutations = new ArrayList<List<Integer>>();

        for (int i = 0; i < container.getAtomCount(); i++) {

            IAtom atom = container.getAtom(i);

            if (SP3.equals(atom.getHybridization())
                    && atom.getFormalNeighbourCount() > 2
                    && hasStereoBonds(atom, container)) {
                List<Integer> permutation = new ArrayList<Integer>();
                permutation.add(i);
                for (IAtom neighbour : container.getConnectedAtomsList(atom)) {
                    permutation.add(container.getAtomNumber(neighbour));
                }
                permutations.add(permutation);

            }


        }

        SDFWriter writer = new SDFWriter(new FileOutputStream(args[0] + "-rotated.sdf"));

        for(List<Integer> permutation : permutations) {

            List<Integer> identity = new ArrayList<Integer>(permutation);

            int n = permutation.size();
            for(int i = 0; i < n; i++){

                permutation.add(permutation.remove(0));

                IAtomContainer copy = new AtomContainer(container);

                int j = identity.get(i);
                int k = permutation.get(i);

                IAtom tmp = copy.getAtom(j);
                copy.setAtom(j, copy.getAtom(k));
                copy.setAtom(k, tmp);
                writer.write(copy);

            }

        }

        writer.close();
        reader.close();

    }


    private static boolean hasStereoBonds(IAtom atom, IAtomContainer container) {

        for (IBond bond : container.getConnectedBondsList(atom)) {
            IBond.Stereo stereo = bond.getStereo();
            if (stereo.equals(IBond.Stereo.DOWN) || stereo.equals(IBond.Stereo.UP))
                return true;
        }

        return false;

    }

}
