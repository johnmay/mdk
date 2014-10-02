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
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationWriter;
import uk.ac.ebi.mdk.io.IdentifierOutput;
import uk.ac.ebi.mdk.io.ObservationOutput;

import java.io.IOException;

/**
 * CrossReferenceWriter - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class CrossReferenceWriter<X extends CrossReference> implements AnnotationWriter<X> {

    private static final Logger LOGGER = Logger.getLogger(CrossReferenceWriter.class);

    private IdentifierOutput  out;
    private ObservationOutput observationOutput;

    public CrossReferenceWriter(IdentifierOutput out,
                                ObservationOutput observationOutput){
        this.out = out;
        this.observationOutput = observationOutput;
    }

    public CrossReferenceWriter(IdentifierOutput out){
        this(out, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(X annotation) throws IOException {

         out.write(annotation.getIdentifier());

         if(observationOutput != null) {
            observationOutput.writeObservations(annotation.getObservations());
         }


    }
}
