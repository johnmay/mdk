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
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.io.AnnotationWriter;

import java.io.DataOutput;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UTFDataFormatException;

/**
 * AtomContainerAnnotationWriter_0_8_5 - 09.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.8.5")
public class AtomContainerAnnotationWriter_0_8_5 implements AnnotationWriter<AtomContainerAnnotation> {

    private static final Logger LOGGER = Logger.getLogger(AtomContainerAnnotationWriter_0_8_5.class);

    private MDLV2000Writer mdl;
    private DataOutput out;

    public AtomContainerAnnotationWriter_0_8_5(DataOutput out) {
        this.out = out;
        this.mdl = new MDLV2000Writer();
    }

    @Override
    public void write(AtomContainerAnnotation annotation) throws IOException {

        IAtomContainer molecule = annotation.getStructure();

        StringWriter sw = new StringWriter(1000);

        try {
            mdl.setWriter(sw);
            mdl.write(molecule);
        } catch (CDKException ex) {
            out.writeBoolean(false);
            LOGGER.error("Could not write chemical structure an empty structure will be stored");
            return;
        }

        out.writeBoolean(true);
        try {
            out.writeUTF(sw.toString());
        } catch (UTFDataFormatException ex) {
            out.writeUTF("");
        }

    }
}
