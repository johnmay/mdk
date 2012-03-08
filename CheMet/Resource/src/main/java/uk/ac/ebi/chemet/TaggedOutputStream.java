package uk.ac.ebi.chemet;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang.mutable.MutableShort;
import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TaggedInputStream - 08.03.2012 <br/>
 * <p/>
 * The tagged output steam allows efficient writing of object class
 * types to a stream. This is achieved by writing the class name only
 * once and then storing a number value 'currently a short' which is written
 * on future calls to the write method.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class TaggedOutputStream extends DataOutputStream {

    private static final Logger LOGGER = Logger.getLogger(TaggedOutputStream.class);

    private final MutableShort TICKER = new MutableShort(Short.MIN_VALUE + 10);

    // synchronised maps (adding values to one also adds to other)
    private final BiMap<Class, Short> tagMap   = HashBiMap.create(500);
    private final BiMap<Short, Class> classMap = tagMap.inverse();

    /**
     * Create a new tagged output stream.
     *
     * @param stream stream to write to
     */
    public TaggedOutputStream(OutputStream stream) {
        super(stream);
    }

    /**
     * Writes a the class to the stream. If the class has not already
     * been stored in the stream a new tag is written. If the tag has
     * already been stored then just the tag value 'short' it written
     *
     * @param c the class to write to the stream
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException low-level io error
     */
    public void write(Class c) throws IOException, ClassNotFoundException {

        if (tagMap.containsKey(c)) {
            writeShort(tagMap.get(c));
        } else {
            writeNew(c);
        }

    }

    /**
     * Writes a new tag as a short value to the output stream passed in
     * the constructor
     *
     * @param c the class to write to the stream
     *
     * @throws IOException            low-level io error
     * @throws ClassNotFoundException low-level io error
     */
    private void writeNew(Class c) throws IOException, ClassNotFoundException {

        TICKER.increment();

        short tag = TICKER.shortValue();

        writeShort(TaggedInputStream.NEW_TAG);
        writeUTF(c.getName());
        writeShort(tag);

        tagMap.put(c, tag);

    }


}
