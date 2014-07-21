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

import org.junit.Test;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.annotation.rex.RExCompound;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.io.AnnotationDataInputStream;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class RExCompoundReaderTest {

    @Test
    public void readAnnotation() throws IOException, ClassNotFoundException {


        DataInputStream input  = new DataInputStream(getStream("rex-compound-annotation"));
        AnnotationDataInputStream reader = new AnnotationDataInputStream(input, new Version("1.4.2"));
        RExCompound ann = reader.read();

        input.close();

    }

    private InputStream getStream(String path){
        return getClass().getResourceAsStream(path);
    }

}
