package uk.ac.ebi.chemet.io.annotation.base;


import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationWriter;
import uk.ac.ebi.mdk.domain.annotation.primitive.BooleanAnnotation;

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
public class BooleanAnnotationWriter
        implements AnnotationWriter<BooleanAnnotation> {

    private DataOutput out;

    public BooleanAnnotationWriter(DataOutput out){
        this.out = out;
    }

    public void write(BooleanAnnotation annotation) throws IOException {
        out.writeBoolean(annotation.getValue());
    }

}
