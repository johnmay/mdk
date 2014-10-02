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

    @SuppressWarnings("unchecked")
    public Enum readEnum() throws IOException, ClassNotFoundException {
        Integer id = readObjectId();
        return hasObject(id) ? (Enum) get(id) : (Enum) put(id, readNewEnum());
    }

    @SuppressWarnings("unchecked")
    public Enum readNewEnum() throws IOException, ClassNotFoundException {
        Class c = readClass();
        String name = getDataInput().readUTF();
        return Enum.valueOf(c,
                            names.containsKey(name) ? names.get(name) : name);
    }
}
