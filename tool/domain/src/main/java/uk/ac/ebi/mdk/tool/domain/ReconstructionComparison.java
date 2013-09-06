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
package uk.ac.ebi.mdk.tool.domain;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.hash_mdk.MoleculeHashGenerator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * ReconstructionComparisson - 2011.11.28 <br> Generates report(s) on the
 * similarities and differences between two or more reconstructions
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @deprecated show be more generic and just accept comparisson methods
 */
@Deprecated
public class ReconstructionComparison {

    private static final Logger LOGGER = Logger.getLogger(ReconstructionComparison.class);

    private Reconstruction[] recons;

    private Multimap<Reconstruction, Long> metaboliteMap = ArrayListMultimap.create();

    private boolean hydrogen; // remove hydrogen

    private final MoleculeHashGenerator generator;



    public ReconstructionComparison(MoleculeHashGenerator generator,
                                    boolean hydrogen,
                                    Reconstruction... recons) {
        if (recons.length < 1) {
            throw new InvalidParameterException("At least two reconstructons should be provided");
        }
        this.generator = generator;
        this.recons = recons;
        this.hydrogen = hydrogen;

        for (Reconstruction recon : recons) {
            for (Metabolite m : recon.metabolome()) {
                if (m.hasStructure()) {
                    IAtomContainer mol = m.getStructures().iterator().next().getStructure();
                    mol = mol.getAtomCount() > 1 && hydrogen ? mol : AtomContainerManipulator.removeHydrogens(mol);
                    if(GeometryTools.has2DCoordinates(mol)) {
                        metaboliteMap.put(recon, generator.generate(mol));
                    }

                }
            }
        }

    }


    public int getMetaboliteTotal(Reconstruction recon) {
        return new HashSet<Long>(metaboliteMap.get(recon)).size();
    }


    public Reconstruction[] getReconstructions() {
        return recons;
    }


    public Map<Metabolite, Long> getMoleculeHashMap(Reconstruction recon) {
        Map<Metabolite, Long> map = new HashMap<Metabolite, Long>();
        for (Metabolite m : recon.metabolome()) {
            if (m.hasStructure()) {
                IAtomContainer mol = m.getStructures().iterator().next().getStructure();
                mol = mol.getAtomCount() > 1 && hydrogen ? mol : AtomContainerManipulator.removeHydrogens(mol);
                if(GeometryTools.has2DCoordinates(mol)){
                    map.put(m, generator.generate(mol));
                }
            }
        }
        return map;
    }


    public int getMetaboliteInstersect(Reconstruction... recons) {

        HashSet<Long> metabolites = new HashSet<Long>();

        for (Reconstruction reconstruction : recons) {

            if (metabolites.isEmpty()) {
                metabolites.addAll(metaboliteMap.get(reconstruction));
            } else {
                metabolites.retainAll(metaboliteMap.get(reconstruction));
            }

        }

        return metabolites.size();

    }
}
