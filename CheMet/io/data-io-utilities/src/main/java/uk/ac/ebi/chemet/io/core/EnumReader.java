package uk.ac.ebi.chemet.io.core;

import org.apache.log4j.Logger;

import java.io.DataInput;
import java.io.IOException;

/**
 * EnumReader - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class EnumReader extends AbstractDataInput {

    private static final Logger LOGGER = Logger.getLogger(EnumReader.class);

    public EnumReader(DataInput in) {
        super(in);
    }

    public Enum readEnum() throws IOException, ClassNotFoundException {
        short id = readObjectId();
        return hasObject(id) ? (Enum) get(id) : (Enum) put(id, readNewEnum());
    }

    public Enum readNewEnum() throws IOException, ClassNotFoundException {
        Class c = readClass();
        return Enum.valueOf(c, getDataInput().readUTF());
    }
}
