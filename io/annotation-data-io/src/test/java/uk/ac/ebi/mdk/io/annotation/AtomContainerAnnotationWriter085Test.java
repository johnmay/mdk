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

package uk.ac.ebi.mdk.io.annotation;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.templates.TestMoleculeFactory;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationDataOutputStream;
import uk.ac.ebi.mdk.io.AnnotationOutput;

import java.io.*;

/**
 * AtomContainerAnnotationWriter085Test - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AtomContainerAnnotationWriter085Test {

    private static final Logger LOGGER = Logger.getLogger(AtomContainerAnnotationWriter085Test.class);

    /**
     * Can not test as the Mol file has a time stamp :-(...
     * @throws IOException
     */
    public void testWrite() throws IOException{

        AtomContainerAnnotation annotation = new AtomContainerAnnotation(TestMoleculeFactory.makeAdenine());
        ByteArrayOutputStream   bytestream = new ByteArrayOutputStream();
        AnnotationOutput out         = new AnnotationDataOutputStream(new DataOutputStream(bytestream), new Version("0.8.5"));
        out.write(annotation);
        bytestream.close();

        byte[] actual   = bytestream.toByteArray();
        byte[] expected = new byte[actual.length];

        InputStream stream = getStream("atomcontainer-annotation");
        stream.read(expected);
        stream.close();

        Assert.assertArrayEquals(expected, actual);

    }

    public void rewrite() throws IOException {
        System.err.println("Rewriting test files");

        IAtomContainer molecule  = TestMoleculeFactory.makeAdenine();
        molecule.setID("Adenine");
        AtomContainerAnnotation    annotation = new AtomContainerAnnotation(molecule);
        FileOutputStream           fos        = new FileOutputStream(getWritePath("atomcontainer-annotation"));
        AnnotationDataOutputStream out        = new AnnotationDataOutputStream(new DataOutputStream(fos), new Version("0.8.5"));
        out.write(annotation);
        fos.close();
    }

    private InputStream getStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    private String getWritePath(String path) {
        String absolute = getClass().getResource(path).getPath();
        return absolute.replace("target/test-classes/uk/ac/ebi", "src/test/resources/uk/ac/ebi");
    }


}
