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
package uk.ac.ebi.core.tools.hash.seeds;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.mutable.MutableInt;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Stereo;
import uk.ac.ebi.core.util.Util;


/**
 *          StereoSeed - 2011.11.11 <br>
 *          The StereoSeed allows hashing of stereo-chemical features.
 *          Exclusion of these seed will results in stereo-isomers scoring the
 *          same hash code.
 *
 *          <br>
 *          You can add the stereo seed seed to the default methods used in the
 *          hash. Note that the seed can also be added in the
 *          {@see uk.ac.ebi.core.tools.hash.MolecularHashFactory#getHash(IAtomContainer, Set)}
 * 
 *          <pre>
 *          MolecularHashFactory factory = MolecularHashFactory.getInstance();
 *          factory.addSeedMethod(SeedFactory.getInstance().getSeed(StereoSeed.class));
 *          factory.getHash(molecule); // uses StereoSeed (in addition to the default)
 *          </pre>
 *
 *
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class StereoSeed implements AtomSeed {

    private Map<Integer, MutableInt> occurrences = new HashMap();


    protected StereoSeed() {
    }


    public int seed(IAtomContainer molecule, IAtom atom) {

        occurrences.clear();

        int hash = 1543;

        for (IBond bond : molecule.getConnectedBondsList(atom)) {

            Stereo stereo = bond.getStereo();

            if (stereo != Stereo.NONE) {

                
                int code = stereo.hashCode();

                /**
                 * This keeps track of occurrences, if we xor the same values
                 * together they cancel out. Therefore we rotate the value
                 * depending on how many time we have seen it before
                 */
                {
                    MutableInt mi = occurrences.get(hash);
                    if (mi == null) {
                        occurrences.put(hash, new MutableInt());
                    } else {
                        mi.increment();
                    }
                }
                {
                    MutableInt mi = occurrences.get(code);
                    if (mi == null) {
                        occurrences.put(code, new MutableInt());
                    } else {
                        mi.increment();
                    }
                }


                hash ^= Util.rotate(code, occurrences);


            }
        }

        return hash;

    }
}
