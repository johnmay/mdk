/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package org.openscience.cdk.hash.stereo;


import com.google.common.collect.Maps;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IDoubleBondStereochemistry;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.ringsearch.RingSearch;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author John May
 */
public class MyStereoEncoder implements StereoEncoderFactory {

    private static final boolean         ENCODE_UNDEFINED_STEREO = true;
    private static final GeometricParity NO_PARITY               = GeometricParity.valueOf(0);

    @Override public StereoEncoder create(IAtomContainer ac, int[][] graph) {

        Map<IAtom, Integer> atom2Idx = Maps.newHashMapWithExpectedSize(ac.getAtomCount());

        for (IAtom atom : ac.atoms())
            atom2Idx.put(atom, atom2Idx.size());

        BondIndex bondIndex = new BondIndex(ac, atom2Idx);

        List<StereoEncoder> encoders = new ArrayList<StereoEncoder>();

        boolean[] included = new boolean[ac.getAtomCount()];

        // index existing elements
        for (IStereoElement se : ac.stereoElements()) {
            if (se instanceof ITetrahedralChirality) {
                ITetrahedralChirality tc = (ITetrahedralChirality) se;
                included[atom2Idx.get(tc.getChiralAtom())] = true;
                encoders.add(encoder(tc, atom2Idx));
            }
            else if (se instanceof IDoubleBondStereochemistry) {
                IDoubleBondStereochemistry dbs = (IDoubleBondStereochemistry) se;
                IBond stereoBond = dbs.getStereoBond();
                included[atom2Idx.get(stereoBond.getAtom(0))] =
                        included[atom2Idx.get(stereoBond.getAtom(1))] = true;
                encoders.add(encoder(dbs, atom2Idx, graph));
            }
        }

        // for those that don't have a definition but are of a given type, 
        // create a new encoding

        AtomStereoType ast = new AtomStereoType(ac, graph, bondIndex);
        RingSearch     rs  = new RingSearch(ac, graph);

        for (int i = 0; i < ac.getAtomCount(); i++) {
            if (included[i])
                continue;
            switch (ast.typeOf(i)) {

                // tetrahedral atoms
                case Tetrahedral:
                    GeometricParity geometricParity = createGeometricParity(ac, i, graph[i], bondIndex);
                    
                    if (geometricParity == null)
                        continue;
                    
                    // note below is for 'none'
                    encoders.add(new MyGeometryEncoder(i,
                                                       new BasicPermutationParity(graph[i]),
                                                       geometricParity));

                    break;

                // double bonds
                case Tricoordinate:

                    int j = findDoubleBonded(graph, i, bondIndex);
                    
                    if (ast.typeOf(j) != AtomStereoType.Type.Tricoordinate)
                        continue;
                    
                    included[i] = included[j] = true;

                    // don't encode ring double-bond stereochemistry
                    if (rs.cyclic(i, j))
                        continue;
                    
                    StereoEncoder encoder = newDoubleBondEncoder(ac, i, j, j, i, bondIndex, graph);
                    
                    if (encoder != null)
                        encoders.add(encoder);
                    
                    break;
            }

        }


        return encoders.isEmpty() ? StereoEncoder.EMPTY
                                  : new MultiStereoEncoder(encoders);
    }
    static StereoEncoder newDoubleBondEncoder(IAtomContainer container,
                                              int left, int leftParent,
                                              int right, int rightParent,
                                              BondIndex bonds,
                                              int[][] graph) {

        IBond[] leftBonds  = bonds.getAll(left, graph[left]);
        IBond[] rightBonds = bonds.getAll(right, graph[right]);

        // check the left and right bonds are acceptable


        // neighbors of u/v with the bonded atoms (left,right) moved
        // to the back of each array. this is important as we can
        // drop it when we build the permutation parity
        int[] leftNeighbors = moveToBack(graph[left], leftParent);
        int[] rightNeighbors = moveToBack(graph[right], rightParent);

        int l1 = leftNeighbors[0];
        int l2 = leftNeighbors[1] == leftParent ? left
                                                : leftNeighbors[1];
        int r1 = rightNeighbors[0];
        int r2 = rightNeighbors[1] == rightParent ? right
                                                  : rightNeighbors[1];

        boolean undefined = bonds.get(left, right).getStereo() == IBond.Stereo.E_OR_Z
                || !hasNoWavyBond(leftBonds)
                || !hasNoWavyBond(rightBonds);
        
        // not the coordinates may also indicate undefined
        GeometricParity geometric = undefined 
                                    ? ENCODE_UNDEFINED_STEREO ? NO_PARITY : null 
                                    : geometric(container, left, right, l1, l2, r1, r2);
        
        // geometric is null if there were no coordinates
        if (geometric != null) {
            return new MyGeometryEncoder(new int[]{left, right},
                                         new CombinedPermutationParity(permutation(leftNeighbors),
                                                                       permutation(rightNeighbors)),
                                         geometric);
        }


        return null;
    }

    /**
     * Generate a new geometric parity (2D or 3D) for the given molecule and
     * atom indices. This method ensure that 2D and 3D coordinates are available
     * on the specified atoms and returns null if the 2D or 3D coordinates are
     * not fully available.
     *
     * @param mol a molecule
     * @param l   left double bonded atom
     * @param r   right double bonded atom
     * @param l1  first substituent atom of <i>l</i>
     * @param l2  second substituent atom of <i>l</i> or <i>l</i> if there is
     *            none
     * @param r1  first substituent atom of <i>r</i>
     * @param r2  second substituent atom of <i>r</i> or <i>r</i> if there is
     *            none
     * @return geometric parity or null
     */
    static GeometricParity geometric(IAtomContainer mol, int l, int r, int l1, int l2, int r1, int r2) {

        // we need all points for 2D as they may be skewed, i.e.
        //
        // \
        //  C=C
        //    |\
        //    C H
        Point2d l2d = mol.getAtom(l).getPoint2d();
        Point2d r2d = mol.getAtom(r).getPoint2d();
        Point2d l12d = mol.getAtom(l1).getPoint2d();
        Point2d l22d = mol.getAtom(l2).getPoint2d();
        Point2d r12d = mol.getAtom(r1).getPoint2d();
        Point2d r22d = mol.getAtom(r2).getPoint2d();

        if (l2d != null && r2d != null && l12d != null && l22d != null && r12d != null && r22d != null) {
            return new DoubleBond2DParity(l2d, r2d, l12d, l22d, r12d, r22d);
        }

        // we only need the first point, we presume the 3D angles are all correct
        Point3d l3d = mol.getAtom(l).getPoint3d();
        Point3d r3d = mol.getAtom(r).getPoint3d();
        Point3d l13d = mol.getAtom(l1).getPoint3d();
        Point3d r13d = mol.getAtom(r1).getPoint3d();
        if (l3d != null && r3d != null && l13d != null && r13d != null)
            return new DoubleBond3DParity(l3d, r3d, l13d, r13d);

        return null;

    }

    /**
     * Create a permutation parity for the given neighbors. The neighbor list
     * should include the other double bonded atom but in the last index.
     *
     * <pre>
     * c3
     *  \
     *   c2 = c1  = [c3,c4,c1]
     *  /
     * c4
     * </pre>
     *
     * @param neighbors neighbors of a double bonded atom specified by index
     * @return a new permutation parity
     */
    static PermutationParity permutation(int[] neighbors) {
        return neighbors.length == 2
               ? PermutationParity.IDENTITY
               : new BasicPermutationParity(Arrays.copyOf(neighbors, neighbors.length - 1));
    }

    /**
     * Utility method for shifting a specified value in an index to the back
     * (see {@link #permutation(int[])}).
     *
     * @param neighbors list of neighbors
     * @param v         the value to shift to the back
     * @return <i>neighbors</i> array
     */
    static int[] moveToBack(int[] neighbors, int v) {
        int j = 0;
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i] != v) {
                neighbors[j++] = neighbors[i];
            }
        }
        neighbors[neighbors.length - 1] = v;
        return neighbors;
    }

    static boolean hasNoWavyBond(IBond[] bonds) {
        for (IBond bond : bonds) {
            // up/down bonds sometimes used to indicate E/Z
            IBond.Stereo stereo = bond.getStereo();
            if (IBond.Stereo.UP_OR_DOWN.equals(stereo) || IBond.Stereo
                    .UP_OR_DOWN_INVERTED.equals(stereo))
                return false;
        }

        return true;
    }

    private static GeometryEncoder encoder(ITetrahedralChirality tc,
                                           Map<IAtom, Integer> atomToIndex) {

        IAtom[] ligands = tc.getLigands();

        int centre = atomToIndex.get(tc.getChiralAtom());
        int[] indices = new int[4];

        int offset = -1;

        for (int i = 0; i < ligands.length; i++) {
            indices[i] = atomToIndex.get(ligands[i]);
            if (indices[i] == centre)
                offset = i;
        }

        // convert clockwise/anticlockwise to -1/+1
        int parity = tc.getStereo() == ITetrahedralChirality.Stereo.CLOCKWISE ? -1 : 1;

        // now if any atom is the centre (indicating an implicit
        // hydrogen) we need to adjust the indicies and the parity 
        if (offset >= 0) {

            // remove the 'implicit' central from the first 3 vertices 
            for (int i = offset; i < indices.length - 1; i++) {
                indices[i] = indices[i + 1];
            }

            // we now take how many vertices we moved which is
            // 3 (last index) minus the index where we started. if the
            // value is odd we invert the parity (odd number of
            // inversions)
            if (Integer.lowestOneBit(3 - offset) == 0x1)
                parity *= -1;

            // trim the array to size we don't include the last (implicit)
            // vertex when checking the invariants                    
            indices = Arrays.copyOf(indices, indices.length - 1);
        }

        return new GeometryEncoder(centre,
                                   new BasicPermutationParity(indices),
                                   GeometricParity.valueOf(parity));
    }

    private static GeometricParity createGeometricParity(IAtomContainer container,
                                                         int v,
                                                         int[] vs,
                                                         BondIndex bondIndex) {
        IAtom focus = container.getAtom(v);

        IBond[] bonds = new IBond[vs.length];
        int[] elevation = new int[4];
        Point2d[] point2d = new Point2d[4];
        Point3d[] point3d = new Point3d[4];

        boolean hasNonPlanarBond = false;
        boolean has2D = true;
        boolean has3D = true;

        int n = 0;
        for (int w : vs) {
            IBond bond = bondIndex.get(v, w);
            bonds[n] = bond;
            if (bond.getStereo() == IBond.Stereo.UP_OR_DOWN)
                return ENCODE_UNDEFINED_STEREO ? NO_PARITY : null;
            elevation[n] = elevation(focus, bond);
            point2d[n] = container.getAtom(w).getPoint2d();
            point3d[n] = container.getAtom(w).getPoint3d();

            if (point2d[n] == null)
                has2D = false;
            if (point3d[n] == null)
                has3D = false;
            if (elevation[n] != 0)
                hasNonPlanarBond = true;

            n++;

        }

        if (!has2D && !has3D)
            return ENCODE_UNDEFINED_STEREO ? NO_PARITY : null;
        if (!has3D && !hasNonPlanarBond)
            return ENCODE_UNDEFINED_STEREO ? NO_PARITY : null;

        if (n < 4) {
            point2d[n] = focus.getPoint2d();
            point3d[n] = focus.getPoint3d();
            n++;
        }

        if (n < 4)
            return ENCODE_UNDEFINED_STEREO ? NO_PARITY : null;

        return has2D ? new Tetrahedral2DParity(point2d, elevation)
                     : new Tetrahedral3DParity(point3d);
    }

    private static GeometryEncoder encoder(IDoubleBondStereochemistry dbs,
                                           Map<IAtom, Integer> atomToIndex,
                                           int[][] graph) {

        IBond db = dbs.getStereoBond();
        int u = atomToIndex.get(db.getAtom(0));
        int v = atomToIndex.get(db.getAtom(1));

        // we now need to expand our view of the environment - the vertex arrays
        // 'us' and 'vs' hold the neighbors of each end point of the double bond
        // ('u' or 'v'). The first neighbor is always the one stored in the
        // stereo element. The second is the other non-double bonded vertex 
        // which we must find from the neighbors list (findOther). If there is
        // no additional atom attached (or perhaps it is an implicit Hydrogen) 
        // we use either double bond end point.  
        IBond[] bs = dbs.getBonds();
        int[] us = new int[2];
        int[] vs = new int[2];

        us[0] = atomToIndex.get(bs[0].getConnectedAtom(db.getAtom(0)));
        us[1] = graph[u].length == 2 ? u : findOther(graph[u], v, us[0]);

        vs[0] = atomToIndex.get(bs[1].getConnectedAtom(db.getAtom(1)));
        vs[1] = graph[v].length == 2 ? v : findOther(graph[v], u, vs[0]);

        int parity = dbs.getStereo() == IDoubleBondStereochemistry.Conformation.OPPOSITE ? +1 : -1;

        GeometricParity geomParity = GeometricParity.valueOf(parity);

        // the permutation parity is combined - but note we only use this if we
        // haven't used 'u' or 'v' as place holders (i.e. implicit hydrogens)
        // otherwise there is only '1' and the parity is just '1' (identity)
        PermutationParity permParity = new CombinedPermutationParity(us[1] == u ? BasicPermutationParity.IDENTITY
                                                                                : new BasicPermutationParity(us),
                                                                     vs[1] == v ? BasicPermutationParity.IDENTITY
                                                                                : new BasicPermutationParity(vs));
        return new GeometryEncoder(new int[]{u, v},
                                   permParity,
                                   geomParity);
    }

    /**
     * Finds a vertex in 'vs' which is not 'u' or 'x'. .
     *
     * @param vs fixed size array of 3 elements
     * @param u  a vertex in 'vs'
     * @param x  another vertex in 'vs'
     * @return the other vertex
     */
    private static int findOther(int[] vs, int u, int x) {
        for (int v : vs) {
            if (v != u && v != x)
                return v;
        }
        throw new IllegalArgumentException("vs[] did not contain another vertex");
    }

    static int findDoubleBonded(int[][] g, int v, BondIndex bonds) {
        for (int w : g[v])
            if (bonds.get(v, w).getOrder() == IBond.Order.DOUBLE)
                return w;
        return -1;
    }

    static int elevation(IAtom focus, IBond bond) {
        switch (bond.getStereo()) {
            case UP:
                return bond.getAtom(0).equals(focus) ? +1 : 0;
            case DOWN:
                return bond.getAtom(0).equals(focus) ? -1 : 0;
        }
        return 0;
    }
}
