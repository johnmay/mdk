/**
 * InChIMoleculeChecker.java
 *
 * 2012.01.23
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
package uk.ac.ebi.metabolomes.util.inchi;

import org.apache.log4j.Logger;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

/**
 * @name    InChIMoleculeChecker
 * @date    2012.01.23
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class InChIMoleculeChecker {

    private static final Logger LOGGER = Logger.getLogger(InChIMoleculeChecker.class);

    private static class InChIMoleculeCheckerHolder {

        static final InChIMoleculeChecker INSTANCE = new InChIMoleculeChecker();
    }

    private InChIMoleculeChecker() {
    }

    public static InChIMoleculeChecker getInstance() {
        return InChIMoleculeCheckerHolder.INSTANCE;
    }

    /**
     * Checks whether the molecule is healthy for an InChI calculation.
     * 
     * @param mol
     * @return true is inchi can be calculated, false if the molecule is not fit for inchi. 
     */
    public boolean checkMoleculeForInChI(IAtomContainer mol) {
        // Has pseudoatoms?
        if (mol.getAtomCount() == 0) {
            return false;
        }

        if (checkForGenericAtoms(mol)) {
            return false;
        }
        if (checkForNullBonds(mol)) {
            return false;
        }
        if (checkForNullAtoms(mol)) {
            return false;
        }
        
        return true;
    }

    public InChIMoleculeCheckerResult checkMolecule(IAtomContainer mol) {
        InChIMoleculeCheckerResult res = new InChIMoleculeCheckerResult();

        res.emptyMol = (mol.getAtomCount() == 0);
        res.bondNull = checkForNullBonds(mol);
        res.atomNull = checkForNullAtoms(mol);
        res.genericAtom = checkForGenericAtoms(mol);
        
        return res;
    }

    private boolean checkForNullAtoms(IAtomContainer mol) {
        for (IAtom a : mol.atoms()) {
            if (a == null) {
                return true;
            }
            
        }
        return false;
    }
    
    private boolean checkForGenericAtoms(IAtomContainer mol) {
        for (IAtom atom : mol.atoms()) {
            if(atom instanceof PseudoAtom)
                return true;
            String className = atom.getClass().getName();
            if (className.equalsIgnoreCase("org.openscience.cdk.PseudoAtom")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForNullBonds(IAtomContainer mol) {
        for (IBond b : mol.bonds()) {
            if (b == null) {
                LOGGER.info("Null bonds for: " + mol.getID());
                return true;
            }
            if (b.getAtomCount() < 2) {
                LOGGER.info("bond with less than two atoms: "
                        + mol.getID());
                return true;
            }
            if (b.getAtom(0) == null || b.getAtom(1) == null) {
                LOGGER.info("Bond with one or two atoms null: "
                        + mol.getID());
                return true;
            }
        }
        return false;
    }

    public class InChIMoleculeCheckerResult {

        private boolean genericAtom = false;
        private boolean bondNull = false;
        private boolean atomNull = false;
        private boolean emptyMol = false;

        /**
         * @return the genericAtom
         */
        public boolean isGenericAtom() {
            return genericAtom;
        }

        /**
         * @return the bondNull
         */
        public boolean isBondNull() {
            return bondNull;
        }

        /**
         * @return the atomNull
         */
        public boolean isAtomNull() {
            return atomNull;
        }

        /**
         * @return the emptyMol
         */
        public boolean isEmptyMol() {
            return emptyMol;
        }
        
        public boolean isInChIFit() {
            return !(genericAtom || bondNull || atomNull || emptyMol);
        }
    }
}
