/**
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.annotation.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import org.junit.Test;
import uk.ac.ebi.annotation.chemical.MolecularFormula;
import uk.ac.ebi.annotation.util.AnnotationFactory;
import uk.ac.ebi.interfaces.Annotation;


/**
 *
 * @author johnmay
 */
public class MolecularFormulaTest extends TestCase {

    public MolecularFormulaTest(String testName) {
        super(testName);
    }


    @Test
    public void testConstruction() {
        MolecularFormula formula = new MolecularFormula("C2H16O2");
        assertEquals("C2H16O2", formula.toString());
    }


    @Test
    public void testExternalization() {
//        try {
//            MolecularFormula formula = new MolecularFormula("C2H16O2");
//            File tmp = File.createTempFile("test", ".externalized");
//
//            ObjectOutput oo = new ObjectOutputStream(new FileOutputStream(tmp));
//            formula.writeExternal(oo);
//            oo.close();
//
//            ObjectInput oi = new ObjectInputStream(new FileInputStream(tmp));
//            Annotation annotation = AnnotationFactory.getInstance().readExternal(formula.getIndex(),
//                                                                                 oi);
//            assertTrue(annotation instanceof MolecularFormula);
//            assertEquals("C2H16O2", formula.toString());
//            oi.close();
//
//        } catch( Exception ex ) {
//            ex.printStackTrace();
//        }
    }


}

