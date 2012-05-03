package uk.ac.ebi.mdk.io.annotation;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.AuthorAnnotation;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationReader;

import java.io.DataInput;
import java.io.IOException;

/**
 * AuthorCommentWriter - 10.03.2012 <br/>
 * <p/>
 * Reads an author annotation from a {@see DataInput}
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class AuthorCommentReader
        implements AnnotationReader<AuthorAnnotation> {

    private static final Logger LOGGER = Logger.getLogger(AuthorCommentReader.class);

    private DataInput in;

    public AuthorCommentReader(DataInput in){
        this.in = in;
    }

    @Override
    public AuthorAnnotation readAnnotation() throws IOException {
        return new AuthorAnnotation(in.readUTF(), in.readUTF());
    }
}
