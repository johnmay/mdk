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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * TaggedInputStream - 08.03.2012 <br/>
 * <p/>
 * The tagged input steam allows efficient reading of object class
 * types from a {@see TaggedOutputStream}. This is achieved by reading
 * the class name only once (the first time it is encountered) and then
 * only reading a tag (short value) on subsequent calls
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class TaggedInputStream extends DataInputStream {

    public static short NEW_TAG = Short.MIN_VALUE;

    private final HashMap<Short, Class> classMap = new HashMap<Short, Class>(500);
            
    /**
     * Create a new tagged input stream
     *
     * @param stream
     */
    public TaggedInputStream(InputStream stream) {
        super(stream);
    }

    /**
     * Read the next tag in the stream. This is used to read the
     * next tag in the stream. A short value is read and if it
     * is a 'new tag' then the tag is read. If the short value
     * matches that of a previously read tag then the previously
     * read tag (stored in a map) is returned.
     *
     * @return the class
     *
     * @throws java.io.IOException low level io error
     * @throws ClassNotFoundException thrown if a new tag can not be created as the class name is not found
     */
    public Class readTag() throws IOException, ClassNotFoundException {

        short type = super.readShort();

        // if new tag, read it
        if (type == NEW_TAG) {
            return readNewTag();
        }

        // look-up in index->class map
        if (classMap.containsKey(type)) {
            return classMap.get(type);
        }

        throw new InvalidParameterException("Unknown class tag, value was not a new tag and has not previously been read");

    }

    /**
     * Reads a new tag from the stream
     *
     * @return the new class read
     *
     * @throws java.io.IOException            low level io error
     * @throws ClassNotFoundException thrown if a read class name can not be found
     */
    private Class readNewTag() throws IOException, ClassNotFoundException {

        String name = readUTF();
        Short tag   = readShort();

        // try and get the class for the name
        Class c     = Class.forName(name);

        classMap.put(tag, c);

        return c;
    }


}
