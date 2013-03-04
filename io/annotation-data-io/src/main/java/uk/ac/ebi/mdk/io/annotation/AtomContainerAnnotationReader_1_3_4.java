/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.io.AnnotationReader;
import uk.ac.ebi.mdk.io.cdk.AtomContainerReader;

import java.io.DataInput;
import java.io.IOException;
import java.io.StringReader;

/**
 * Read CDK atom containers using our custom serializer, from version 1.3.4.
 *
 * @author johnmay
 */
@CompatibleSince("1.3.4")
public class AtomContainerAnnotationReader_1_3_4
        implements AnnotationReader<AtomContainerAnnotation> {

    private AtomContainerReader reader;

    public AtomContainerAnnotationReader_1_3_4(DataInput in) {
        this.reader = new AtomContainerReader(in);
    }


    @Override
    public AtomContainerAnnotation readAnnotation() throws IOException {
        return new AtomContainerAnnotation(reader.read());
    }
}
