package uk.ac.ebi.mdk.io;

import org.apache.log4j.Logger;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    private Map<String,String> names = new HashMap<String,String>();

    public EnumReader(DataInput in) {
        super(in, null);
    }

    /**
     * Allows alternate names to be specified for reading older files
     * @param name the orginal name
     * @param value the new name (current)
     */
    public void put(String name, String value) {
        this.names.put(name, value);
    }

    public Enum readEnum() throws IOException, ClassNotFoundException {
        Integer id = readObjectId();
        return hasObject(id) ? (Enum) get(id) : (Enum) put(id, readNewEnum());
    }

    public Enum readNewEnum() throws IOException, ClassNotFoundException {
        Class c = readClass();
        String name = getDataInput().readUTF();
        return Enum.valueOf(c,
                            names.containsKey(name) ? names.get(name) : name);
    }
}
