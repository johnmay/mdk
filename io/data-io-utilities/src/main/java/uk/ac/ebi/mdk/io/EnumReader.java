package uk.ac.ebi.mdk.io;

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
        super(in, null);
    }

    public Enum readEnum() throws IOException, ClassNotFoundException {
        Integer id = readObjectId();
        return hasObject(id) ? (Enum) get(id) : (Enum) put(id, readNewEnum());
    }

    public Enum readNewEnum() throws IOException, ClassNotFoundException {
        Class c = readClass();
        String name = getDataInput().readUTF();
        return Enum.valueOf(c, name);
    }
}
