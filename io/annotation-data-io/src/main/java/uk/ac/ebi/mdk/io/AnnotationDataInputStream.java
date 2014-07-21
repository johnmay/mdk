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

package uk.ac.ebi.mdk.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.annotation.*;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.primitive.*;
import uk.ac.ebi.mdk.domain.annotation.rex.RExCompound;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.task.ExecutableParameter;
import uk.ac.ebi.mdk.domain.annotation.task.FileParameter;
import uk.ac.ebi.mdk.domain.annotation.task.Parameter;
import uk.ac.ebi.mdk.io.annotation.AtomContainerAnnotationReader_0_8_5;
import uk.ac.ebi.mdk.io.annotation.AtomContainerAnnotationReader_1_3_4;
import uk.ac.ebi.mdk.io.annotation.AuthorCommentReader;
import uk.ac.ebi.mdk.io.annotation.CrossReferenceReader;
import uk.ac.ebi.mdk.io.annotation.GibbsEnergyReader;
import uk.ac.ebi.mdk.io.annotation.RExCompoundReader;
import uk.ac.ebi.mdk.io.annotation.RExExtractReader;
import uk.ac.ebi.mdk.io.annotation.RExExtractWriter;
import uk.ac.ebi.mdk.io.annotation.primitive.*;
import uk.ac.ebi.mdk.io.annotation.task.ExecutableParameterReader;
import uk.ac.ebi.mdk.io.annotation.task.FileParameterReader;
import uk.ac.ebi.mdk.io.annotation.task.ParameterReader;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * AnnotationDataInputStream - 09.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AnnotationDataInputStream
        extends AbstractDataInput<AnnotationReader>
        implements AnnotationInput {

    private static final Logger LOGGER = Logger.getLogger(AnnotationDataInputStream.class);
    private DataInput        in;
    private ObservationInput observationInput;
    private IdentifierInput  identifierInput;

    public AnnotationDataInputStream(DataInputStream in, Version v) {
        this(in, null, v);
    }

    public AnnotationDataInputStream(DataInput in,
                                     ObservationInput observationInput,
                                     Version v) {

        super(in, v);

        this.identifierInput = new IdentifierDataInputStream(in, v);


        this.in = in;
        this.observationInput = observationInput;

        // special readers
        add(AuthorAnnotation.class, new AuthorCommentReader(in));
        add(AtomContainerAnnotation.class, new AtomContainerAnnotationReader_0_8_5(in));
        add(AtomContainerAnnotation.class, new AtomContainerAnnotationReader_1_3_4(in));
        add(GibbsEnergy.class, new GibbsEnergyReader(in));
        add(RExExtract.class, new RExExtractReader(in, identifierInput));
        add(RExCompound.class, new RExCompoundReader(in));

        // special readers for tasks
        add(Parameter.class, new ParameterReader(in));
        add(FileParameter.class, new FileParameterReader(in));
        add(ExecutableParameter.class, new ExecutableParameterReader(in));


    }

    public AnnotationReader getReader(Class c) {
        if (!hasMarshaller(c, getVersion())) {
            for (AnnotationReader reader : createReaders(c)) {
                add(c, reader);
            }
        }
        return getMarshaller(c, getVersion());
    }


    @SuppressWarnings("unchecked")
    public <A extends Annotation> A read() throws IOException, ClassNotFoundException {
        return (A) read(readClass());
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> A read(Class<A> c) throws IOException, ClassNotFoundException {
        Integer id = readObjectId();

        AnnotationReader reader = getReader(c);
        if (hasObject(id)) {
            return (A) get(id);
        }
        return (A) put(id, reader.readAnnotation());
    }

    @SuppressWarnings("unchecked")
    public Collection<AnnotationReader> createReaders(Class c) {

        if (StringAnnotation.class.isAssignableFrom(c)) {
            return Arrays.<AnnotationReader>asList(new StringAnnotationReader(c, in), new StringAnnotationReader_1_4_2(c, in));
        } else if (DoubleAnnotation.class.isAssignableFrom(c)) {
            return Collections.<AnnotationReader>singleton(new DoubleAnnotationReader(c, in));
        } else if (BooleanAnnotation.class.isAssignableFrom(c)) {
            return Collections.<AnnotationReader>singleton(new BooleanAnnotationReader(c, in));
        } else if (FloatAnnotation.class.isAssignableFrom(c)) {
            return Collections.<AnnotationReader>singleton(new FloatAnnotationReader(c, in));
        } else if (IntegerAnnotation.class.isAssignableFrom(c)) {
            return Collections.<AnnotationReader>singleton(new IntegerAnnotationReader(c, in));
        } else if (CrossReference.class.isAssignableFrom(c)) {
            return Collections.<AnnotationReader>singleton(new CrossReferenceReader(c, identifierInput, observationInput));
        } else if (Flag.class.isAssignableFrom(c)) {
            return Collections.<AnnotationReader>singleton(new FlagReader(c));
        }

        throw new InvalidParameterException("Could not create writer for " + c.getName());

    }


}
