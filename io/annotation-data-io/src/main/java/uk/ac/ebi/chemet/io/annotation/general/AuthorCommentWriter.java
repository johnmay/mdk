package uk.ac.ebi.chemet.io.annotation.general;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.AuthorAnnotation;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationWriter;

import java.io.DataOutput;
import java.io.IOException;

/**
 * AuthorCommentWriter - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class AuthorCommentWriter
        implements AnnotationWriter<AuthorAnnotation> {

    private static final Logger LOGGER = Logger.getLogger(AuthorCommentWriter.class);

    private DataOutput out;

    public AuthorCommentWriter(DataOutput out){
        this.out = out;
    }

    @Override
    public void write(AuthorAnnotation annotation) throws IOException {
        out.writeUTF(annotation.getAuthor());
        out.writeUTF(annotation.getValue());
    }
}
