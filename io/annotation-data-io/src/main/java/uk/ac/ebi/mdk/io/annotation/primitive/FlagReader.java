package uk.ac.ebi.mdk.io.annotation.primitive;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.annotation.AnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.Flag;
import uk.ac.ebi.mdk.io.AnnotationReader;

import java.io.IOException;

/**
 * @author John May
 */
@CompatibleSince("0.9")
public class FlagReader implements AnnotationReader<Flag> {

    private static final Logger LOGGER = Logger.getLogger(FlagReader.class);

    private static final AnnotationFactory ANNOTATION_FACTORY = DefaultAnnotationFactory.getInstance();

    private Class<? extends Flag> c;

    public FlagReader(Class<? extends Flag> c) {
        this.c = c;
    }

    @Override
    public Flag readAnnotation() throws IOException, ClassNotFoundException {
        return ANNOTATION_FACTORY.ofClass(c);
    }
}
