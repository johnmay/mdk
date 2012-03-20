package uk.ac.ebi.chemet.io.annotation.base;


import com.sun.tools.javac.util.Version;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.chemet.io.annotation.AnnotationWriter;
import uk.ac.ebi.interfaces.IntegerAnnotation;
import uk.ac.ebi.interfaces.StringAnnotation;

import java.io.DataOutput;
import java.io.IOException;

/**
 * StringAnnotationReader - 09.03.2012 <br/>
 * <p/>
 * Read's a string annotation from {@see DataInput}
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class StringAnnotationWriter
    implements AnnotationWriter<StringAnnotation> {

    private DataOutput out;

    public StringAnnotationWriter(DataOutput out){
        this.out = out;
    }

    public void write(StringAnnotation annotation) throws IOException {
        out.writeUTF(annotation.getValue());
    }

}
