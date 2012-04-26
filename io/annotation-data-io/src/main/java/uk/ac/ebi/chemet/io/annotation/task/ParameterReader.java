package uk.ac.ebi.chemet.io.annotation.task;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.task.Parameter;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.AnnotationReader;

import java.io.DataInput;
import java.io.IOException;

/**
 * ParameterWriter - 10.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class ParameterReader implements AnnotationReader<Parameter> {

    private static final Logger LOGGER = Logger.getLogger(ParameterReader.class);

    DataInput in;

    public ParameterReader(DataInput in) {
        this.in = in;
    }

    @Override
    public Parameter readAnnotation() throws IOException {

        return new Parameter(in.readUTF(),
                             in.readUTF(),
                             in.readUTF(),
                             in.readUTF());


    }
}
