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

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.GibbsEnergy;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.AnnotationDataInputStream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class GibbsEnergyReaderTest {

    @Test
    public void readAnnotation_GibbsEnergy() throws IOException, ClassNotFoundException {


        DataInputStream input  = new DataInputStream(getStream("gibbs-energy-annotation"));
        AnnotationDataInputStream reader = new AnnotationDataInputStream(input, new Version("0.9"));
        GibbsEnergy gibbs = reader.read();

        input.close();

        Assert.assertEquals(50,  gibbs.getValue(), 0);
        Assert.assertEquals(0.9, gibbs.getError(), 0);

    }

    private InputStream getStream(String path){
        return getClass().getResourceAsStream(path);
    }

}
