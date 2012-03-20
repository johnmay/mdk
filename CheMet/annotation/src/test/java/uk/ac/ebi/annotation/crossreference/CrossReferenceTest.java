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

package uk.ac.ebi.annotation.crossreference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.annotation.util.AnnotationFactory;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;


/**
 *
 * @author johnmay
 */
public class CrossReferenceTest {

    public CrossReferenceTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testExternalization() {
//        try {
//            File tmp = File.createTempFile("test", ".externalized");
//
//            ChEBICrossReference chebiXRef = new ChEBICrossReference(new ChEBIIdentifier("ChEBI:13242"));
//
//
//
//            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(tmp));
//            out.writeByte(chebiXRef.getIndex());
//            chebiXRef.writeExternal(out);
//            out.close();
//
//            System.out.println("file size: " + tmp.length());
//
//            ObjectInput in = new ObjectInputStream(new FileInputStream(tmp));
//            Annotation annotation = AnnotationFactory.getInstance().readExternal(in.readByte(), in);
//            out.close();
//
//            System.out.println(annotation.getClass().getSimpleName() + " content: " + annotation);
//
//        } catch( Exception ex ) {
//            ex.printStackTrace();
//        }

    }


}

