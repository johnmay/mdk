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

/**
 * ChiralSeed.java
 *
 * 2011.11.11
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
package uk.ac.ebi.mdk.prototype.hash.seed;

import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.prototype.hash.cip.ChiralityCalculator;


/**
 * StereoSeed - 2011.11.11 <br>
 * The StereoSeed allows hashing of stereo-chemical features.
 * Exclusion of these seed will results in stereo-isomers scoring the
 * same hash code.
 * <p/>
 * <br>
 * You can add the stereo seed seed to the default methods used in the
 * hash. Note that the seed can also be added in the
 * {@see uk.ac.ebi.core.tools.hash.MolecularHashFactory#getHash(IAtomContainer, Set)}
 * <p/>
 * <pre>
 *          MolecularHashFactory factory = MolecularHashFactory.getInstance();
 *          factory.addSeedMethod(SeedFactory.getInstance().getSeed(StereoSeed.class));
 *          factory.getHash(molecule); // uses StereoSeed (in addition to the default)
 *          </pre>
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @deprecated this method use the CIP rules which are very slow - when hashing we can take a short cut
 *             which is build into the hash code factory
 */
@Deprecated
public class StereoSeed implements AtomSeed {

    private static final Logger LOGGER = Logger.getLogger(StereoSeed.class);


    private static final String STEREO_CALCULATED = "Stereo.Calculated";
    private static final String STEREO = "IStereoValue";

    protected StereoSeed() {
    }


    public int seed(IAtomContainer molecule, IAtom atom) {

        // only calculate the stereo elements once
        if (molecule.getProperty(STEREO_CALCULATED) == null) {
            calculateStereo(molecule);
        }

        Object value = atom.getProperty(STEREO);

        if (value != null) {
            return ((Enum) value).ordinal();
        }

        return 1543;

    }


    public void calculateStereo(IAtomContainer structure) {

        try {
            AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(structure);
        } catch (CDKException e) {
            LOGGER.debug("CDK Exception", e);
        }

        try {
            for (IAtom atom : structure.atoms()) {

                ChiralityCalculator.Chirality chirality = ChiralityCalculator.getChirality(structure, atom);

                if (chirality != ChiralityCalculator.Chirality.NONE)
                    atom.setProperty(STEREO, chirality);
            }
        } catch (NullPointerException ex) {
            LOGGER.debug("Null pointer exception whilst calculating stereo centre", ex);
        }


        structure.setProperty(STEREO_CALCULATED, Boolean.TRUE);

    }

    @Override
    public String toString() {
        return "Chirality";
    }
}
