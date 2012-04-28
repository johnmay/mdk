/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.tool.domain;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Element;
import org.openscience.cdk.Isotope;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.annotation.chemical.Charge;
import uk.ac.ebi.annotation.chemical.MolecularFormula;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator.getElementCount;
import static org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator.getMolecularFormula;


/**
 *          StructureValidator 2012.02.14
 *          Class provides checking of structure validity when compared to a
 *          specified charge and formula. The main entry points are the
 *          getValidity methods that provides the validity at different
 *          levels
 * 
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class StructuralValidity {

    private static final Logger LOGGER = Logger.getLogger(StructuralValidity.class);


    public static enum Category {

        UNKNOWN,
        ERROR,
        WARNING,
        CORRECT
    };

    private static StructuralValidity match = new StructuralValidity(Category.CORRECT, "The structure matches the specified formula and charge");

    private static StructuralValidity warn = new StructuralValidity(Category.WARNING, "The structure matches the specified formula when charge difference is considered (i.e. wrong charge)");

    private static StructuralValidity error_charge = new StructuralValidity(Category.ERROR, "The structure matches the specified formula but the charge is different");

    private static StructuralValidity error_both = new StructuralValidity(Category.ERROR, "The structure does not match the specified formula or charge");

    private static StructuralValidity unknown = new StructuralValidity(Category.UNKNOWN, "The structural validitiy could not be determined");

    private final static Map<Category, Integer> CATEGORY_RANK = new EnumMap<Category, Integer>(Category.class);


    static {
        CATEGORY_RANK.put(Category.UNKNOWN, 0);
        CATEGORY_RANK.put(Category.ERROR, 1);
        CATEGORY_RANK.put(Category.WARNING, 2);
        CATEGORY_RANK.put(Category.CORRECT, 3);
    }

    private Category category;

    private String message;


    /**
     * Private constructor used in the {@see getValidity()} methods
     * @param category
     * @param message 
     */
    private StructuralValidity(Category category, String message) {
        this.category = category;
        this.message = message;
    }


    /**
     * Access the category of the validity
     * @return ERROR, WARNING or CORRECT
     */
    public Category getCategory() {
        return category;
    }


    /**
     * Access a message providing detail of the structural validity. This
     * is useful as a tool tip
     * @return specific message
     */
    public String getMessage() {
        return message;
    }


    /**
     * Access the validity for the specified metabolite. This method
     * calculates all scores for the metabolites multiple formulas/structures.
     * If the validity differs the best scoring validity will be returned.
     * 
     * @param metabolite the metabolite to validate
     * 
     * @return an instance of {@see StructuralValidity} with defined
     *         {@see Category} (see. {@see getCategory()}) and
     *         message (see. {@see getMessage()})
     * 
     */
    public static StructuralValidity getValidity(Metabolite metabolite) {

        StructuralValidity validity = unknown;

        Charge charge = metabolite.hasAnnotation(Charge.class) ? metabolite.getAnnotations(Charge.class).iterator().next() : new Charge(0d);

        for (ChemicalStructure structure : metabolite.getStructures()) {
            StructuralValidity subvalidity = getValidity(metabolite.getAnnotations(MolecularFormula.class),
                                                         structure,
                                                         charge);
            int rank = CATEGORY_RANK.get(subvalidity.category);
            if (rank >= CATEGORY_RANK.get(validity.category)) {
                validity = subvalidity;
            }
        }

        return validity;

    }


    /**
     * Access the validity for the specified formulas, structure and charge. If 
     * validity differs the best scoring validity will be returned.
     * 
     * @param formulas   a collection formulas to check against
     * @param structure structure to validate
     * @param charge    charge to check against
     * 
     * @return an instance of {@see StructuralValidity} with defined
     *         {@see Category} (see. {@see getCategory()}) and
     *         message (see. {@see getMessage()})
     * 
     */
    public static StructuralValidity getValidity(Collection<MolecularFormula> formulas,
                                                 ChemicalStructure structure,
                                                 Charge charge) {

        StructuralValidity validity = error_both;

        for (MolecularFormula formula : formulas) {
            StructuralValidity subvalidity = getValidity(formula, structure, charge);
            int rank = CATEGORY_RANK.get(subvalidity.category);
            if (rank >= CATEGORY_RANK.get(validity.category)) {
                validity = subvalidity;
            }
        }

        return validity;

    }


    /**
     * Access the validity for the specified formula, structure and charge
     * 
     * @param formula   formula to check against
     * @param structure structure to validate
     * @param charge    charge to check against
     * 
     * @return an instance of {@see StructuralValidity} with defined
     *         {@see Category} (see. {@see getCategory()}) and
     *         message (see. {@see getMessage()})
     * 
     */
    public static StructuralValidity getValidity(MolecularFormula formula,
                                                 ChemicalStructure structure,
                                                 Charge charge) {


        IAtomContainer molecule = AtomContainerManipulator.removeHydrogens(structure.getStructure());

        // calculate the charge difference
        // negative charge diff: structure has less protons
        // positive charge diff: structure has more protons
        Double otherCharge = 0d;
        for (IAtom atom : AtomContainerManipulator.getAtomArray(molecule)) {
            otherCharge += atom.getFormalCharge();
        }
        Double chargeDifference = charge.getValue() - otherCharge;
        try {
            CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance()).addImplicitHydrogens(molecule);
        } catch (CDKException ex) {
            try {
                AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(molecule);
                CDKHydrogenAdder.getInstance(DefaultChemObjectBuilder.getInstance()).addImplicitHydrogens(molecule);
            } catch (CDKException ex1) {
                LOGGER.error("Unable to add implicit hydrogens " + ex.getMessage());
            }
        }

        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);

        // complete correctness
        IMolecularFormula query = formula.getFormula();
        IMolecularFormula subject = getMolecularFormula(molecule);

        if (compareFormula(query, subject, false)) {
            return chargeDifference == 0 ? match : error_charge;
        }

        // wrong charge
        Isotope hydrogen = new Isotope(new Element("H"));

        int queryProtonCount = getElementCount(query, hydrogen);
        int subjectProtonCount = getElementCount(subject, hydrogen);

        if (compareFormula(query, subject, true)) {
            int protonDifference = queryProtonCount - subjectProtonCount;
            if (protonDifference == chargeDifference) {
                return warn;
            }
        }

        return error_both;

    }


    /**
     * We use this instead of the CDK MolecularFormulatManipulate#compare as the CDK
     * version looks at formula charge and isotope mass. In this instance we are only
     * intrested in symbols. You can also match excluding protons.
     * @param formula1
     * @param formula2
     * @param excludeProtons
     * @return 
     */
    private static boolean compareFormula(IMolecularFormula formula1, IMolecularFormula formula2, boolean excludeProtons) {

        if (formula1.getIsotopeCount() != formula2.getIsotopeCount() && !excludeProtons) {
            return false;
        }

        Map<String, Integer> map = new HashMap<String, Integer>();

        for (IIsotope isotope : formula1.isotopes()) {
            String symbol = isotope.getSymbol();
            if (!excludeProtons || (excludeProtons && !symbol.equals("H"))) {
                map.put(symbol, formula1.getIsotopeCount(isotope));
            }
        }

        for (IIsotope isotope : formula2.isotopes()) {
            String symbol = isotope.getSymbol();
            if (!excludeProtons || (excludeProtons && !symbol.equals("H"))) {
                if (!map.containsKey(symbol)) {
                    return false;
                }
                if (!map.get(symbol).equals(formula2.getIsotopeCount(isotope))) {
                    return false;
                }
            }
        }




        return true;
    }
}
