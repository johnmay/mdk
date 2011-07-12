/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.util;

import java.util.Iterator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

/**
 *
 * @author pmoreno
 */
public class CDKAtomTyper {

    public static void typeAtoms(IAtomContainer mol) {
       CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(mol.getBuilder());
       Iterator<IAtom> atoms = mol.atoms().iterator();
       while (atoms.hasNext()) {
        IAtom atom = (IAtom) atoms.next();
        IAtomType type = null;
            try {
                type = matcher.findMatchingAtomType(mol, atom);
            } catch (CDKException ex) {
                Logger.getLogger(CDKAtomTyper.class.getName()).log(Level.FATAL, null, ex);
            }
        AtomTypeManipulator.configure(atom, type);
       }
    }
}
