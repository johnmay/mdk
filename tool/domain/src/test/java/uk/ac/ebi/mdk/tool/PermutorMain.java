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

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.AtomContainerAtomPermutor;
import org.openscience.cdk.graph.AtomContainerPermutor;
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
 * Simple class to generate test input by permutation atoms around chiral
 * centres.
 *
 * @author John May
 */
public class PermutorMain {

    public static void main(String[] args) throws IOException, CDKException {

        if (args.length != 1)
            throw new IllegalArgumentException("Not enough arguments");

        File f = new File(args[0]);

        if (!f.exists() || f.isDirectory())
            throw new IllegalArgumentException("File does not exists or is a directory");

        MDLV2000Reader reader = new MDLV2000Reader(new FileReader(f));

        IAtomContainer container = reader.read(DefaultChemObjectBuilder.getInstance().newInstance(IAtomContainer.class));

        AtomContainerPermutor permutor = new AtomContainerAtomPermutor(container);

        SDFWriter writer = new SDFWriter(new FileOutputStream(args[0] + "-rotated.sdf"));

        while (permutor.hasNext()) {
            writer.write(permutor.next());
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
