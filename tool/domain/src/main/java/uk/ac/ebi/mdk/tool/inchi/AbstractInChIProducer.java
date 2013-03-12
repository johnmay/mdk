/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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
package uk.ac.ebi.mdk.tool.inchi;

import java.util.List;
import net.sf.jniinchi.INCHI_OPTION;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 *
 * @author pmoreno
 */
public abstract class AbstractInChIProducer {

    private static final Logger LOGGER = Logger.getLogger(AbstractInChIProducer.class.getName());

    protected InChIResult result;
    private Boolean generic;
    private Boolean bondNull;
    private Boolean atomNull;

    public static void typeMoleculeForInChI(IAtomContainer mol) throws CDKException {
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
    }

    public static void addHydrogensToMolecule(IAtomContainer mol) throws CDKException {
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mol.getBuilder());
        adder.addImplicitHydrogens(mol);
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
    }

    /**
     * Checks whether the molecule is healthy for an InChI calculation.
     * 
     * @deprecated use {@link InChIMoleculeChecker} instead.
     * 
     * @param mol
     * @return true is inchi can be calculated, false if the molecule is not fit for inchi. 
     */
    @Deprecated
    public static boolean checkMoleculeForInChI(IAtomContainer mol) {
        return InChIMoleculeChecker.getInstance().checkMoleculeForInChI(mol);
    }
    
}
