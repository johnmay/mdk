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

package uk.ac.ebi.mdk.io.annotation.primitive;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.Charge;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationDataInputStream;
import uk.ac.ebi.mdk.domain.annotation.primitive.DoubleAnnotation;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * DoubleAnnotationReaderTest - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DoubleAnnotationReaderTest {

    private static final Logger LOGGER = Logger.getLogger(DoubleAnnotationReaderTest.class);

    @Test public void readAnnotation_Charge() throws IOException, ClassNotFoundException {


        DataInputStream        input  = new DataInputStream(getStream("charge-annotation"));
        AnnotationDataInputStream reader = new AnnotationDataInputStream(input, new Version("0.9"));
        Charge                 charge = reader.read();

        input.close();

        Assert.assertEquals(-2d, (double) charge.getValue(), 0);

    }

    @Test public void readAnnotation_FluxUpperBound() throws IOException, ClassNotFoundException {


        DataInputStream        input  = new DataInputStream(getStream("flux-ub-annotation"));
        AnnotationDataInputStream reader = new AnnotationDataInputStream(input, new Version("0.9"));
        DoubleAnnotation       fluxUB = reader.read();

        input.close();

        Assert.assertEquals(1004.23, (double) fluxUB.getValue(), 0);

    }

    @Test public void readAnnotation_FluxLowerBound() throws IOException, ClassNotFoundException {


        DataInputStream        input  = new DataInputStream(getStream("flux-lb-annotation"));
        AnnotationDataInputStream reader = new AnnotationDataInputStream(input, new Version("0.9"));
        DoubleAnnotation       fluxUB = reader.read();

        input.close();

        Assert.assertEquals(0.0005, (double) fluxUB.getValue(), 0);

    }

    @Test public void readAnnotation_GibbsEnergy() throws IOException, ClassNotFoundException {


        DataInputStream           input  = new DataInputStream(getStream("flux-lb-annotation"));
        AnnotationDataInputStream reader = new AnnotationDataInputStream(input, new Version("0.9"));
        DoubleAnnotation          fluxUB = reader.read();

        input.close();

        Assert.assertEquals(0.0005, (double) fluxUB.getValue(), 0);

    }

    private InputStream getStream(String path){
        return getClass().getResourceAsStream(path);
    }
    
}
