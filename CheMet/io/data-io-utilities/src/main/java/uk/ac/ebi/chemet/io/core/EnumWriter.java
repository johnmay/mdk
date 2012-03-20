package uk.ac.ebi.chemet.io.core;

import org.apache.log4j.Logger;

import java.io.DataOutput;
import java.io.IOException;

/**
 * EnumWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class EnumWriter extends AbstractDataOutput {

    private static final Logger LOGGER = Logger.getLogger(EnumWriter.class);

    
    public EnumWriter(DataOutput out) {
        super(out);
    }
    
    public void writeEnum(Enum e) throws IOException {

        if(writeObjectId(e)){
            writeClass(e.getClass());
            getDataOutput().writeUTF(e.name());
        }

    }
    
}
