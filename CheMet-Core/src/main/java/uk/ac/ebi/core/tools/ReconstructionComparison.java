/**
 * ReconstructionComparisson.java
 *
 * 2011.11.28
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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.core.ReconstructionImpl;
import uk.ac.ebi.core.tools.hash.MolecularHash;
import uk.ac.ebi.core.tools.hash.MolecularHashFactory;
import uk.ac.ebi.core.tools.hash.seeds.*;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 *          ReconstructionComparisson - 2011.11.28 <br>
 *          Generates report(s) on the similarities and differences between two or more reconstructions
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ReconstructionComparison {

    private static final Logger LOGGER = Logger.getLogger(ReconstructionComparison.class);

    private ReconstructionImpl[] recons;

    private Multimap<ReconstructionImpl, Integer> metaboliteMap = ArrayListMultimap.create();

    private Set<AtomSeed> methods = SeedFactory.getInstance().getSeeds(BondOrderSumSeed.class,
                                                                       AtomicNumberSeed.class,
                                                                       ConnectedAtomSeed.class);

    private boolean hydrogen; // remove hydrogen

    private static MolecularHashFactory HASH_FACTORY = MolecularHashFactory.getInstance();


    public ReconstructionComparison(Set<AtomSeed> methods,
                                    boolean hydrogen,
                                    ReconstructionImpl... recons) {
        if (recons.length < 1) {
            throw new InvalidParameterException("At least two reconstructons should be provided");
        }
        this.recons = recons;
        this.methods = methods;
        this.hydrogen = hydrogen;

        for (ReconstructionImpl recon : recons) {
            for (Metabolite m : recon.getMetabolome()) {
                if (m.hasStructure()) {
                    IAtomContainer mol = m.getStructures().iterator().next().getStructure();
                    mol = mol.getAtomCount() > 1 && hydrogen ? mol : AtomContainerManipulator.removeHydrogens(mol);
                    MolecularHash hash = HASH_FACTORY.getHash(mol, methods);
                    metaboliteMap.put(recon, hash.hash);
                }
            }
        }

    }


    public int getMetaboliteTotal(ReconstructionImpl recon) {
        return new HashSet(metaboliteMap.get(recon)).size();
    }


    public ReconstructionImpl[] getReconstructions() {
        return recons;
    }


    public Map<Metabolite, Integer> getMoleculeHashMap(ReconstructionImpl recon) {
        HASH_FACTORY.setSeedWithMoleculeSize(true);
        Map<Metabolite, Integer> map = new HashMap<Metabolite, Integer>();
        LOGGER.debug("Generating hash code: " + methods);
        for (Metabolite m : recon.getMetabolome()) {
            if (m.hasStructure()) {
                IAtomContainer mol = m.getStructures().iterator().next().getStructure();
                mol = mol.getAtomCount() > 1 && hydrogen ? mol : AtomContainerManipulator.removeHydrogens(mol);
                MolecularHash hash = HASH_FACTORY.getHash(mol, methods);
                map.put(m, hash.hash);
                LOGGER.debug(m + " hash = " + hash.hash);
            }
        }
        return map;
    }


    public int getMetaboliteInstersect(ReconstructionImpl... recons) {

        Set<Integer> metabolites = null;

        for (ReconstructionImpl reconstruction : recons) {

            if (metabolites == null) {
                metabolites = new HashSet(metaboliteMap.get(reconstruction));
            } else {
                metabolites.retainAll(metaboliteMap.get(reconstruction));
            }

        }

        return metabolites.size();

    }
}
