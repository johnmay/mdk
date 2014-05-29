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

import com.google.common.collect.Sets;
import org.openscience.cdk.config.Elements;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IDoubleBondStereochemistry;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.ITetrahedralChirality;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smiles.smarts.SmartsPattern;
import org.openscience.cdk.stereo.DoubleBondStereochemistry;
import org.openscience.cdk.stereo.TetrahedralChirality;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.openscience.cdk.interfaces.IDoubleBondStereochemistry.Conformation;

/** @author John May */
public class StructureUtil {


    // note these need be kept in sync
    static final SmartsPattern  PORPHYRIN_PATTERN  = sma("[#6]~1~[#6]~[#6]~2~[#7]~[#6]~1~[#6]~[#6]~1~[#7]~[#6](~[#6]~[#6]~1)~[#6]~[#6]~1~[#7]~[#6](~[#6]~[#6]~1)~[#6]~[#6]~1~[#7]~[#6](~[#6]~[#6]~1)~[#6]~2");
    static final IAtomContainer PORPHYRIN_TEMPLATE = smi("C1=CC2=NC1=CC1=NC(C=C1)=CC1=NC(C=C1)=CC1=NC(C=C1)=C2");

    static final SmartsPattern COBALAMIN_PATTERN = sma("[#6]~1~[#6]~[#6]~2~[#7]~[#6]~1~[#6]~1~[#7]~[#6](~[#6]~[#6]~1)~[#6]~[#6]~1~[#7]~[#6](~[#6]~[#6]~1)~[#6]~[#6]~1~[#7]~[#6](~[#6]~[#6]~1)~[#6]~2");

    static final Set<Elements> NON_METALS = Sets.newHashSet(Elements.Unknown,
                                                            Elements.Hydrogen,
                                                            Elements.Helium,
                                                            Elements.Boron,
                                                            Elements.Carbon,
                                                            Elements.Nitrogen,
                                                            Elements.Oxygen,
                                                            Elements.Fluorine,
                                                            Elements.Neodymium,
                                                            Elements.Silicon,
                                                            Elements.Phosphorus,
                                                            Elements.Sulfur,
                                                            Elements.Chlorine,
                                                            Elements.Argon,
                                                            Elements.Germanium,
                                                            Elements.Arsenic,
                                                            Elements.Selenium,
                                                            Elements.Bromine,
                                                            Elements.Krypton,
                                                            Elements.Tellurium,
                                                            Elements.Iodine,
                                                            Elements.Xenon,
                                                            Elements.Astatine,
                                                            Elements.Radon);

    static SmartsPattern sma(String sma) {
        try {
            return SmartsPattern.create(sma, SilentChemObjectBuilder.getInstance());
        } catch (IOException e) {
            throw new InternalError("could not create SMARTS pattern");
        }
    }

    static IAtomContainer smi(String smi) {
        try {
            return new SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smi);
        } catch (CDKException e) {
            throw new InternalError("could not create SMILES pattern");
        }
    }

    public static int neutralise(IAtomContainer org) {
        return new Neutralise(org).correct();
    }

    /**
     * Normalises porphyrin representation, disconencting metals and adjusting
     * oxidation of nitrogens.
     *
     * @param ac structure to normalise
     */
    public static void normalisePorphyrin(IAtomContainer ac) {
        int[] mapping = PORPHYRIN_PATTERN.match(ac);

        if (mapping.length == 0) return;

        for (IBond template : PORPHYRIN_TEMPLATE.bonds()) {
            int u = PORPHYRIN_TEMPLATE.getAtomNumber(template.getAtom(0));
            int v = PORPHYRIN_TEMPLATE.getAtomNumber(template.getAtom(1));
            int x = mapping[u];
            int y = mapping[v];
            IBond bond = ac.getBond(ac.getAtom(x), ac.getAtom(y));
            bond.setOrder(template.getOrder());
        }

        IAtom[] nitrogens = new IAtom[]{ac.getAtom(mapping[3]),
                                        ac.getAtom(mapping[7]),
                                        ac.getAtom(mapping[13]),
                                        ac.getAtom(mapping[19])};

        Set<IAtom> metals = new HashSet<IAtom>();

        for (IAtom nitrogen : nitrogens) {
            nitrogen.setImplicitHydrogenCount(0);
            nitrogen.setFormalCharge(0);
            for (IBond bond : ac.getConnectedBondsList(nitrogen)) {
                IAtom neighbor = bond.getConnectedAtom(nitrogen);
                if (isMetal(neighbor)) {
                    ac.removeBond(bond);
                    metals.add(neighbor);
                }
            }
        }

        // correct valence of carbons at exocyclic groups
        IAtom[] carbons = new IAtom[]{ac.getAtom(mapping[0]),
                                      ac.getAtom(mapping[1]),
                                      ac.getAtom(mapping[9]),
                                      ac.getAtom(mapping[10]),
                                      ac.getAtom(mapping[15]),
                                      ac.getAtom(mapping[16]),
                                      ac.getAtom(mapping[21]),
                                      ac.getAtom(mapping[22])};

        for (int i = 0; i < carbons.length; i += 2) {
            int v1 = (int) (ac.getBondOrderSum(carbons[i]) + carbons[i].getImplicitHydrogenCount());
            int v2 = (int) (ac.getBondOrderSum(carbons[i + 1]) + carbons[i + 1].getImplicitHydrogenCount());
            if (v1 > 4 || v2 > 4) {
                ac.getBond(carbons[i], carbons[i + 1]).setOrder(IBond.Order.SINGLE);
            }
        }
    }

    public static void disconnectMetals(IAtomContainer ac) {
        for (IAtom a : ac.atoms()) {
            if (isMetal(a)) {
                for (IBond bond : ac.getConnectedBondsList(a)) {
                    IAtom neighbor = bond.getConnectedAtom(a);
                    neighbor.setImplicitHydrogenCount(neighbor.getImplicitHydrogenCount() + 1);
                    ac.removeBond(bond);
                }
            }
        }
    }

    public static boolean isMetal(IElement element) {
        return isMetal(Elements.ofNumber(element.getAtomicNumber()));
    }

    public static boolean isMetal(Elements element) {
        return !NON_METALS.contains(element);
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
            }
            else {
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

                IAtom focus = tc.getChiralAtom();
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
