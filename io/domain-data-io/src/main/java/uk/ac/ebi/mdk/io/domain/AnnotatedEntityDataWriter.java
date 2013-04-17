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

package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationOutput;
import uk.ac.ebi.mdk.io.EnumWriter;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.io.ObservationOutput;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.observation.Observation;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class AnnotatedEntityDataWriter
        implements EntityWriter<AnnotatedEntity> {

    private static final Logger LOGGER = Logger.getLogger(AnnotatedEntityDataWriter.class);

    private DataOutput out;
    private EnumWriter enumOut;
    private AnnotationOutput annotationOut;
    private ObservationOutput observationOut;

    public AnnotatedEntityDataWriter(DataOutput out,
                                     AnnotationOutput annotationOut,
                                     ObservationOutput observationOut) {
        this.out = out;
        enumOut = new EnumWriter(out);
        this.annotationOut = annotationOut;
        this.observationOut = observationOut;
    }

    public void write(AnnotatedEntity entity) throws IOException {

        enumOut.writeEnum(entity.getRating());

        writeAnnotations(entity);
        writeObservations(entity);

    }

    public void writeAnnotations(AnnotatedEntity entity) throws IOException {
        Collection<Class> classes = entity.getAnnotationClasses();
        out.writeInt(classes.size());

        for (Class c : classes) {

            Collection<Annotation> annotations = entity.getAnnotations(c);
            out.writeInt(annotations.size());
            annotationOut.writeClass(c);


            for (Annotation annotation : annotations) {
                annotationOut.writeData(annotation);
            }
        }
    }

    public void writeObservations(AnnotatedEntity entity) throws IOException {
        Collection<Class<? extends Observation>> classes = entity.getObservationClasses();
        out.writeInt(classes.size());
        for (Class<? extends Observation> c : classes) {

            Collection<Observation> observations = entity.getObservations(c);
            out.writeInt(observations.size());
            observationOut.writeClass(c);

            for (Observation observation : observations) {
                observationOut.writeData(observation);
            }
        }
    }

}
