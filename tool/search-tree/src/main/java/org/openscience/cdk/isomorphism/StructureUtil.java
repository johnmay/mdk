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

package org.openscience.cdk.isomorphism;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IDoubleBondStereochemistry;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.stereo.DoubleBondStereochemistry;
import org.openscience.cdk.stereo.TetrahedralChirality;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.openscience.cdk.interfaces.IDoubleBondStereochemistry.Conformation;

/** @author John May */
public class StructureUtil {

    public static IAtomContainer neutralise(IAtomContainer org) {
        new Neutralise(org);
        return org;
    }
    
    public static IAtomContainer createSubstructure(IAtomContainer org) {

        int nOrgAtoms = org.getAtomCount();
        int nOrgBonds = org.getBondCount();

        IAtom[] atoms = new IAtom[nOrgAtoms];
        IBond[] bonds = new IBond[nOrgBonds];

        int nCpyAtoms = 0;
        int nCpyBonds = 0;

        Set<IAtom> included = new HashSet<IAtom>(nOrgAtoms * 2);

        for (int i = 0; i < org.getAtomCount(); i++) {
            IAtom atom = org.getAtom(i);
            if (include(org, atom)) {
                atoms[nCpyAtoms++] = atom;
                included.add(atom);
            } else {
                for (IAtom neighbor : org.getConnectedAtomsList(atom)) {
                    neighbor.setImplicitHydrogenCount(neighbor.getImplicitHydrogenCount() + 1);
                }
            }
        }
        
        for (final IBond bond : org.bonds()) {
            if (included.contains(bond.getAtom(0)) && included.contains(bond.getAtom(1)))
                bonds[nCpyBonds++] = bond;
        }

        IChemObjectBuilder bldr = org.getBuilder();

        IAtomContainer cpy = bldr.newInstance(IAtomContainer.class, 0, 0, 0, 0);
        cpy.setAtoms(Arrays.copyOf(atoms, nCpyAtoms));
        cpy.setBonds(Arrays.copyOf(bonds, nCpyBonds));

        for (IStereoElement se : org.stereoElements()) {
            if (se instanceof ITetrahedralChirality) {
                ITetrahedralChirality tc = (ITetrahedralChirality) se;

                IAtom   focus = tc.getChiralAtom();
                IAtom[] neighbors = tc.getLigands();

                if (!included.contains(focus))
                    continue;

                int idx = -1;

                for (int i = 0; i < neighbors.length; i++) {
                    if (!included.contains(neighbors[i])) {
                        if (idx < 0)
                            idx = i;
                        else
                            idx = neighbors.length;
                    }
                }

                // all present
                if (idx < 0) {
                    cpy.addStereoElement(se);
                    continue;
                }

                // more than one neighbor was not included
                if (idx == neighbors.length)
                    continue;

                // non hydrogen
                if (neighbors[idx].getAtomicNumber() != 1)
                    continue;

                neighbors = Arrays.copyOf(neighbors, neighbors.length);
                neighbors[idx] = tc.getChiralAtom();

                cpy.addStereoElement(new TetrahedralChirality(focus,
                                                              neighbors,
                                                              tc.getStereo()));
            }
            else if (se instanceof IDoubleBondStereochemistry) {
                IDoubleBondStereochemistry db = (IDoubleBondStereochemistry) se;
                Conformation conformation = db.getStereo();

                IBond orgStereo = db.getStereoBond();
                IBond orgLeft = db.getBonds()[0];
                IBond orgRight = db.getBonds()[1];

                // we use the following variable names to refer to the
                // double bond atoms and substituents
                // x       y
                //  \     /
                //   u = v 

                IAtom u = orgStereo.getAtom(0);
                IAtom v = orgStereo.getAtom(1);
                IAtom x = orgLeft.getConnectedAtom(u);
                IAtom y = orgRight.getConnectedAtom(v);

                if (!included.contains(u) || !included.contains(v))
                    continue;

                boolean modified = false;

                if (!included.contains(x)) {
                    conformation = invert(conformation);
                    orgLeft = findOther(org, u, v, x);
                    x = orgLeft.getConnectedAtom(u);
                    if (!included.contains(x))
                        continue;
                    modified = true;
                }

                if (!included.contains(y)) {
                    conformation = invert(conformation);
                    orgRight = findOther(org, v, u, y);
                    y = orgRight.getConnectedAtom(v);
                    if (!included.contains(y))
                        continue;
                    modified = true;
                }

                // no other atoms connected, invalid double-bond configuration?
                if (x == null || y == null)
                    continue;

                if (!modified) {
                    cpy.addStereoElement(db);
                    continue;
                }
                cpy.addStereoElement(new DoubleBondStereochemistry(orgStereo,
                                                                   new IBond[]{orgLeft, orgRight},
                                                                   conformation));
            }
        }

        return cpy;
    }

    private static Conformation invert(Conformation conformation) {
        if (conformation == Conformation.TOGETHER)
            return Conformation.OPPOSITE;
        if (conformation == Conformation.OPPOSITE)
            return Conformation.TOGETHER;
        throw new IllegalArgumentException();
    }

    private static IBond findOther(IAtomContainer container, IAtom atom, IAtom exclude1, IAtom exclude2) {
        for (IBond bond : container.getConnectedBondsList(atom)) {
            IAtom neighbor = bond.getConnectedAtom(atom);
            if (neighbor != exclude1 && neighbor != exclude2)
                return bond;
        }
        return null;
    }

    public static IAtomContainer suppressHydrogens(IAtomContainer org) {
        try {
            return createSubstructure(org.clone());
        } catch (CloneNotSupportedException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static boolean include(IAtomContainer container, IAtom a) {
        if (a.getAtomicNumber() != 1)
            return true;
        int mass = mass(a);
        if (mass == 2 || mass == 3)
            return true;
        int q = charge(a);
        if (q != 0)
            return true;
        if (a.getImplicitHydrogenCount() > 0)
            return true;
        if (container.getConnectedBondsCount(a) > 1)
            return true;
        return false;
    }

    private static int mass(IAtom a) {
        Integer mass = a.getMassNumber();
        if (mass != null)
            return mass;
        return -1;
    }

    private static int charge(IAtom a) {
        Integer q = a.getFormalCharge();
        if (q != null)
            return q;
        return 0;
    }

}
