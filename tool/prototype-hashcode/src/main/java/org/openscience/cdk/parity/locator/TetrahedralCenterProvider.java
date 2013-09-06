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

package org.openscience.cdk.parity.locator;

import org.openscience.cdk.hash_mdk.graph.Graph;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.parity.ParityCalculator;
import org.openscience.cdk.parity.SP3Parity2DCalculator;
import org.openscience.cdk.parity.component.StereoComponent;
import org.openscience.cdk.parity.component.StereoIndicator;
import org.openscience.cdk.parity.component.TetrahedralComponent;
import org.openscience.cdk.parity.ncomponent.Tetrahedral2DComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link StereoComponentProvider} that locates candidate tetrahedral atoms
 * with a chemical graph.
 *
 * Note: Currently only 2D is supported, the current candidates are the atom
 * must have Sp3 hybridization, at least 2 explicit atoms, no query bonds and at
 * least one stereo (UP/DOWN) bond.
 *
 * @author John May
 */
public class TetrahedralCenterProvider
        implements StereoComponentProvider<Long> {

    private final StereoIndicator<Long> indicator;

    // inject.. when we add 3D support
    private final ParityCalculator parityCalculator = new SP3Parity2DCalculator();

    public TetrahedralCenterProvider(StereoIndicator<Long> indicator
                                    ) {
        this.indicator = indicator;
    }

    @Override
    public List<StereoComponent<Long>> getComponents(IAtomContainer container) {
        List<StereoComponent<Long>> components = new ArrayList<StereoComponent<Long>>();
        for (IAtom atom : container.atoms()) {
            if (isCandidateTetrahedralAtom(atom, container)) {
                StereoComponent<Long> component = new Tetrahedral2DComponent(atom, container.getConnectedBondsList(atom), container);
                components.add(component);
            }
        }
        return components;
    }

    @Override
    public List<StereoComponent<Long>> getComponents(Graph graph) {

        List<StereoComponent<Long>> components = new ArrayList<StereoComponent<Long>>();

        for (int i = 0; i < graph.n(); i++) {
            if (isCandidateTetrahedralAtom(i, graph)) {
                components.add(new TetrahedralComponent<Long>(i,
                                                           graph.neighbors(i),
                                                           parity(i, graph),
                                                           indicator));
            }
        }

        return components;

    }

    private int parity(int x, Graph g) {

        // get the actual atom values and the planes that the atoms are on
        IAtom[] atoms = new IAtom[4];
        int[] planes = new int[4];

        int[] neighbours = g.neighbors(x);

        for (int j = 0; j < neighbours.length; j++) {
            atoms[j] = g.getVertexValue(neighbours[j]);
            planes[j] = g.getEdgeAtIndex(x, j).plane();
        }

        // if this tetrahedral centre is missing a ligand
        // we add the central atom (x) as the lowest priority
        if (neighbours.length == 3)
            atoms[3] = g.getVertexValue(x);

        return parityCalculator.parity(atoms, planes);

    }

    private boolean isCandidateTetrahedralAtom(int i, Graph graph) {

        IAtom atom = graph.getVertexValue(i);

        // tests that atom has SP3 hybridization and has at least two
        // neighbours
        if (IAtom.Hybridization.SP3.equals(atom.getHybridization())
                && graph.neighbors(i).length > 2) {

            int[] neighbours = graph.neighbors(i);

            // count the number of bonds that are not on the plane
            int nBondsOffPlane = 0;

            for (int j = 0; j < neighbours.length; j++) {

                // query bond means we can't have a tetrahedral centre
                if (graph.getEdgeAtIndex(i, j).isQuery())
                    return false;
                else if (graph.getEdgeAtIndex(i, j).plane() != 0)
                    nBondsOffPlane++;

            }

            // as we're in 2D we need at least one bond which is not on the plane
            return nBondsOffPlane > 0;

        }

        return false;

    }

    private boolean isCandidateTetrahedralAtom(IAtom atom, IAtomContainer container) {

        List<IBond> neighbours = container.getConnectedBondsList(atom);

        // tests that atom has SP3 hybridization and has at least two
        // neighbours
        if (IAtom.Hybridization.SP3.equals(atom.getHybridization())
                && neighbours.size() > 2) {

            // count the number of bonds that are not on the plane
            int nBondsOffPlane = 0;

            for (IBond bond : neighbours) {

                // query bond means we can't have a tetrahedral centre
                if (isQuery(bond))
                    return false;
                else if (isUpOrDown(bond))
                    nBondsOffPlane++;

            }

            // as we're in 2D we need at least one bond which is not on the plane
            return nBondsOffPlane > 0;

        }

        return false;

    }

    private boolean isQuery(IBond bond) {
        return IBond.Stereo.UP_OR_DOWN.equals(bond.getStereo())
                || IBond.Stereo.UP_OR_DOWN_INVERTED.equals(bond.getStereo())
                || IBond.Stereo.E_OR_Z.equals(bond.getStereo());
    }

    private boolean isUpOrDown(IBond bond) {
        return IBond.Stereo.UP.equals(bond.getStereo())
                || IBond.Stereo.DOWN.equals(bond.getStereo())
                || IBond.Stereo.UP_INVERTED.equals(bond.getStereo())
                || IBond.Stereo.DOWN_INVERTED.equals(bond.getStereo());
    }


}
