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

package uk.ac.ebi.mdk.io.cdk;

import org.apache.log4j.Logger;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.exception.NoSuchAtomTypeException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.silent.Atom;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.Bond;
import org.openscience.cdk.silent.PseudoAtom;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.silent.SingleElectron;
import org.openscience.cdk.stereo.StereoElementFactory;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import javax.vecmath.Point2d;
import java.io.DataInput;
import java.io.IOException;

import static org.openscience.cdk.interfaces.IBond.Order;
import static org.openscience.cdk.interfaces.IBond.Stereo;

/**
 * @author John May
 */
public class AtomContainerReader {

    private final DataInput in;

    private final AtomTypeFactory atomTyper = AtomTypeFactory
            .getInstance("org/openscience/cdk/dict/data/cdk-atom-types.owl",
                         SilentChemObjectBuilder.getInstance());

    private static Order[] orders = new Order[Order.values().length];
    private static Stereo[] stereos = new Stereo[Stereo.values().length];

    static {
        for (Order order : Order.values()) {
            orders[order.ordinal()] = order;
        }
        for (Stereo stereo : Stereo.values()) {
            stereos[stereo.ordinal()] = stereo;
        }
    }

    public AtomContainerReader(DataInput in) {
        this.in = in;
    }

    public IAtomContainer read() throws IOException {

        int n = in.readInt();
        int m = in.readInt();

        IAtom[] atoms = new IAtom[n];
        IBond[] bonds = new IBond[m];

        for (int i = 0; i < n; i++) {
            boolean pseudo = in.readBoolean();
            atoms[i] = pseudo ? new PseudoAtom() : new Atom();
            atoms[i].setAtomicNumber(in.readInt());

            int mass = in.readInt();
            atoms[i].setMassNumber(mass > 0 ? mass : null);

            atoms[i].setPoint2d(new Point2d(in.readInt() / 10000d,
                                            in.readInt() / 10000d));

            atoms[i].setSymbol(in.readUTF());
            if (pseudo) {
                ((IPseudoAtom) atoms[i]).setLabel(in.readUTF());
            }
            byte fc = in.readByte();
            byte ih = in.readByte();
            String type = in.readUTF();
            atoms[i].setFormalCharge(fc != Byte.MIN_VALUE ? (int) fc : null);
            atoms[i].setImplicitHydrogenCount(
                    ih != Byte.MIN_VALUE ? (int) ih : null);

            if (!type.isEmpty()) {
                atoms[i].setAtomTypeName(type);
                try {
                    IAtomType atomType = atomTyper
                            .getAtomType(atoms[i].getAtomTypeName());
                    AtomTypeManipulator
                            .configureUnsetProperties(atoms[i], atomType);
                } catch (NoSuchAtomTypeException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        for (int i = 0; i < m; i++) {
            int u = in.readInt();
            int v = in.readInt();
            bonds[i] = new Bond(atoms[u], atoms[v],
                                orders[in.readByte()],
                                stereos[in.readByte()]);
        }

        int nSingleElectrons = in.readInt();

        AtomContainer container = new AtomContainer(0, 0, 0, nSingleElectrons);

        container.setAtoms(atoms);
        container.setBonds(bonds);
        if (nSingleElectrons > 0) {
            ISingleElectron[] singleElectrons = new ISingleElectron[nSingleElectrons];
            for (int i = 0; i < nSingleElectrons; i++) {
                singleElectrons[i] = new SingleElectron(atoms[in.readInt()]);
                singleElectrons[i].setElectronCount(in.readInt());
                container.addSingleElectron(singleElectrons[i]);
            }
        }

        // sanitise
        for (IAtom a : container.atoms()) {
            if (a.getImplicitHydrogenCount() == null)
                a.setImplicitHydrogenCount(0);
            if (a.getAtomicNumber() == null)
                a.setAtomicNumber(0);
            if (a.getFormalCharge() == null)
                a.setFormalCharge(0);
        }
        
        try {
            container.setStereoElements(StereoElementFactory.using2DCoordinates(container).createAll());
        } catch (Exception e) {
            Logger.getLogger(getClass()).warn("Could not define stereo centers for molecule");
        }

        return container;
    }

}
