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

package uk.ac.ebi.mdk.apps.tool;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BondOrderSumSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ConnectedAtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.SeedFactory;
import uk.ac.ebi.mdk.prototype.hash.seed.StereoSeed;
import uk.ac.ebi.mdk.prototype.hash.cip.ChiralityCalculator;
import uk.ac.ebi.mdk.prototype.hash.MolecularHashFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author John May
 */
public class StructureDifference {

    private static final Logger LOGGER = Logger.getLogger(StructureDifference.class);

    public static void main(String[] args) throws CDKException, IOException {

        IAtomContainer left = load(new File(args[0]));
        IAtomContainer right = load(new File(args[1]));

//        left = AtomContainerManipulator.removeHydrogens(left);
//        right = AtomContainerManipulator.removeHydrogens(right);

        System.out.println("left\t\t\tright");
        System.out.println(left.getAtomCount() + "\t\t\t" + right.getAtomCount());

        System.out.println(getChiralities(left) + "\t\t\t" + getChiralities(right));
        System.out.println(getFullHash(left) + "\t\t\t" + getFullHash(right));
        System.out.println(getNonChargeHash(left) + "\t\t\t" + getNonChargeHash(right));
        System.out.println(getNonStereoHash(left) + "\t\t\t" + getNonStereoHash(right));

    }

    @SuppressWarnings("unchecked")
    public static Integer getNonStereoHash(IAtomContainer container) {
        return MolecularHashFactory.getInstance().getHash(container, SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                                        ConnectedAtomSeed.class,
                                                                                                        BondOrderSumSeed.class
                                                                                                       )).hash;
    }
    
    @SuppressWarnings("unchecked")
    public static Integer getNonChargeHash(IAtomContainer container) {


        return MolecularHashFactory.getInstance().getHash(container, SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                                        ConnectedAtomSeed.class,
                                                                                                        BondOrderSumSeed.class
                                                                                                       )).hash;
    }

    @SuppressWarnings("unchecked")
    public static Integer getFullHash(IAtomContainer container) {
        return MolecularHashFactory.getInstance().getHash(container, SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class,
                                                                                                        BondOrderSumSeed.class,
                                                                                                        ConnectedAtomSeed.class,
                                                                                                        ChargeSeed.class)).hash;

    }

    public static List<ChiralityCalculator.Chirality> getChiralities(IAtomContainer container) throws CDKException {

        List<ChiralityCalculator.Chirality> chiralities = new ArrayList<ChiralityCalculator.Chirality>();

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(container);
        for (IAtom atom : container.atoms()) {
            ChiralityCalculator.Chirality chirality = ChiralityCalculator.getChirality(container, atom);
            if (chirality != ChiralityCalculator.Chirality.NONE) {
                chiralities.add(chirality);
            }
        }

        return chiralities;

    }

    public static IAtomContainer load(File f) throws IOException, CDKException {
        IAtomContainer container;
        MDLV2000Reader in = new MDLV2000Reader(new FileInputStream(f));
        container = in.read(SilentChemObjectBuilder.getInstance().newInstance(IAtomContainer.class));
        in.close();
        return container;
    }

}
