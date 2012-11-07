package uk.ac.ebi.mdk.prototype.hash.cip;

import Jama.Matrix;
import org.apache.log4j.Logger;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.geometry.cip.ILigand;
import org.openscience.cdk.geometry.cip.Ligand;
import org.openscience.cdk.geometry.cip.VisitedAtoms;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import uk.ac.ebi.mdk.prototype.hash.cip.rules.CahnIngoldPrelogRule;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Uses the CDK CIP tool to get the curability of a carbon
 *
 * @author John May
 */
public class ChiralityCalculator {

    private static final Logger LOGGER = Logger.getLogger(ChiralityCalculator.class);

    private static final CahnIngoldPrelogRule CIP_LIGAND_RULE = new CahnIngoldPrelogRule();

    public static enum Chirality {
        R(1),
        S(2),
        NONE(0),
        UNKNOWN(3);

        private int parity;

        private Chirality(int parity) {
            this.parity = parity;
        }

        public int getParity() {
            return parity;
        }
    }


    /**
     * Determine the sign of space make by the 'central' atom's ligands. This method uses the determinant
     * of the coordinates of the connected atoms (in CIP priority) to provide a positive (clockwise) or
     * negative (anti-clockwise) number. A value of 0 (none/unknown chirality) is returned by default
     * or if the space does not have a sign.
     *
     * @param container the container for the provided central atom
     * @param central   central atom (currently restricted to atoms that have 4 formal neighbours)
     *
     * @return the sign of the space
     *
     * @link tinyurl.com/stereo-parity
     */
    public static double getSpatialSign(IAtomContainer container, IAtom central) {

        ILigand centralLigand = new Ligand(container, new VisitedAtoms(), central, central);

        List<ILigand> ligands = new ArrayList<ILigand>(getConnected(centralLigand));

        if (!central.getFormalNeighbourCount().equals(4)) {
            return 0;
        }

        if (ligands.size() < 3 || ligands.size() > 4) {
            return 0;
        }

        // add the central atom as the 4th (lowest priority) this will maintain the
        // correct sign of the triangle
        if (ligands.size() == 3) {
            ligands.add(0, centralLigand);
        }

        Matrix matrix = new Matrix(new double[4][4]);

        // fill in the matrix
        for (int j = 0; j < ligands.size(); j++) {

            ILigand query = ligands.get(3 - j);

            double[] xyz = getParityColumn(query);

            // fill in the matrix
            for (int i = 0; i < xyz.length; i++) {
                matrix.set(i, j, xyz[i]);
            }

            // do z coordinate adjustment
            // this is doing linear search for connected atoms, some improvement might be possible with a map
            for (IBond bond : container.getConnectedBondsList(query.getLigandAtom())) {
                if (bond.getConnectedAtom(query.getLigandAtom()).equals(central)) {
                    matrix.set(3, j, getAdjustment(bond));
                }
            }

        }

        return matrix.det();

    }

    public static Chirality getChirality(IAtomContainer container, IAtom atom) {
        double determinant = getSpatialSign(container, atom);
        return determinant > 0 ? Chirality.R :
               determinant < 0 ? Chirality.S
                               : Chirality.NONE; // could be unknown (i.e. chiral centre but unknown rotation)
    }

    private static double getAdjustment(IBond bond) {
        // Object.equals() handles null correctly
        return IBond.Stereo.UP.equals(bond.getStereo()) ? +1d :
               IBond.Stereo.DOWN.equals(bond.getStereo()) ? -1d :
               0d;
    }

    private static double[] getParityColumn(ILigand ligand) {

        IAtom atom = ligand.getLigandAtom();

        if (atom.getPoint2d() != null) {
            Point2d p = atom.getPoint2d();
            return new double[]{1d,
                                p.x,
                                p.y,
                                0d};
        } else if (atom.getPoint3d() != null) {
            Point3d p = atom.getPoint3d();
            return new double[]{1d,
                                p.x,
                                p.y,
                                p.z};
        }

        LOGGER.error("Atom did not have coordinates, chirality calculation is likely wrong. Please ensure" +
                             "all atoms have coordinates before invoking");

        return new double[]{
                1d,
                0d,
                0d,
                0d
        };
    }

    // for debuging
    public static String getConnectedString(IAtomContainer container, IAtom atom) {
        StringBuilder sb = new StringBuilder();
        Set<ILigand> ligands = getConnected(new Ligand(container, new VisitedAtoms(), atom, atom));
        for (ILigand ligand : ligands) {
            for (int i = 0; i < container.getAtomCount(); i++) {
                if (container.getAtom(i).equals(ligand.getLigandAtom())) {
                    sb.append(container.getAtom(i).getSymbol()).append(i + 1).append(" ");
                }
            }
        }
        return sb.toString();
    }

    public static Set<ILigand> getConnected(ILigand ligand) {
        IAtomContainer container = ligand.getAtomContainer();
        IAtom centralAtom = ligand.getCentralAtom();
        List<IBond> bonds = container.getConnectedBondsList(centralAtom);
        // ligands are kept sorted
        Set<ILigand> ligands = new TreeSet<ILigand>(CIP_LIGAND_RULE);

        for (IBond bond : bonds) {
            int duplication = getDuplication(bond);
            IAtom connectedAtom = bond.getConnectedAtom(centralAtom);

            ligands.add(new Ligand(
                    container, new VisitedAtoms(), centralAtom, connectedAtom
            ));

            for (int i = 2; i <= duplication; i++) {
                ligands.add(new TerminalLigand(
                        container, new VisitedAtoms(), centralAtom, connectedAtom
                ));
            }
        }
        return ligands;
    }

    // duplication from CDK
    private static int getDuplication(IBond bond) {

        if (bond.getFlag(CDKConstants.ISAROMATIC)) {
            return 1;
        }

        IBond.Order order = bond.getOrder();
        if (order == IBond.Order.SINGLE) return 1;
        if (order == IBond.Order.DOUBLE) return 2;
        if (order == IBond.Order.TRIPLE) return 3;
        if (order == IBond.Order.QUADRUPLE) return 4;
        return 0;
    }


    // duplication from CDK with change to bond order (aromatic) test

    public static ILigand[] getLigandLigands(ILigand ligand) {
        if (ligand instanceof TerminalLigand) return new ILigand[0];

        IAtomContainer container = ligand.getAtomContainer();
        IAtom ligandAtom = ligand.getLigandAtom();
        IAtom centralAtom = ligand.getCentralAtom();
        VisitedAtoms visitedAtoms = ligand.getVisitedAtoms();
        List<IBond> bonds = container.getConnectedBondsList(ligandAtom);
        // duplicate ligands according to bond order, following the CIP rules
        List<ILigand> ligands = new ArrayList<ILigand>();
        for (IBond bond : bonds) {
            if (bond.contains(centralAtom)) {
                int duplication = getDuplication(bond) - 1;
                if (duplication > 0) {
                    for (int i = 1; i <= duplication; i++) {
                        ligands.add(new TerminalLigand(
                                container, visitedAtoms, ligandAtom, centralAtom
                        ));
                    }
                }
            } else {
                int duplication = getDuplication(bond);
                IAtom connectedAtom = bond.getConnectedAtom(ligandAtom);
                if (visitedAtoms.isVisited(connectedAtom)) {
                    ligands.add(new TerminalLigand(
                            container, visitedAtoms, ligandAtom, connectedAtom
                    ));
                } else {
                    ligands.add(new Ligand(
                            container, visitedAtoms, ligandAtom, connectedAtom
                    ));
                }
                for (int i = 2; i <= duplication; i++) {
                    ligands.add(new TerminalLigand(
                            container, visitedAtoms, ligandAtom, connectedAtom
                    ));
                }
            }
        }
        return ligands.toArray(new ILigand[0]);
    }

    // duplication from CDK
    private static class TerminalLigand extends Ligand {

        public TerminalLigand(IAtomContainer container, VisitedAtoms visitedAtoms,
                              IAtom centralAtom, IAtom ligandAtom) {
            super(container, visitedAtoms, centralAtom, ligandAtom);
        }

    }


}
