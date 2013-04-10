/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.io;

import org.apache.commons.lang.mutable.MutableShort;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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

    private final MutableShort TICKER = new MutableShort(Short.MIN_VALUE + 10);

    // synchronised maps (adding values to one also adds to other)   
    private final Map<Class, Short> classIds = new HashMap<Class,Short>();

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
     * @throws java.io.IOException            low-level io error
     */
    public void write(Class c) throws IOException {

        if (classIds.containsKey(c)) {
            writeShort(classIds.get(c));
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
     * @throws java.io.IOException            low-level io error
     */
    private void writeNew(Class c) throws IOException {

        TICKER.increment();

        short tag = TICKER.shortValue();

        writeShort(TaggedInputStream.NEW_TAG);
        writeUTF(c.getName());
        writeShort(tag);

        classIds.put(c, tag);

    }


}
