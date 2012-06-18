package uk.ac.ebi.mdk.io.annotation.primitive;


import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationWriter;
import uk.ac.ebi.mdk.domain.annotation.primitive.FloatAnnotation;

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
public class FloatAnnotationWriter
        implements AnnotationWriter<FloatAnnotation>  {

    private DataOutput out;

    public FloatAnnotationWriter(DataOutput out){
        this.out = out;
    }

    public void write(FloatAnnotation annotation) throws IOException {
        out.writeFloat(annotation.getValue());
    }

}
