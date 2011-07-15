/**
 * FileHashMapper.java
 *
 * 2011.07.13
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.chemet.io.mapping;

import au.com.bytecode.opencsv.CSVReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * @name    FileHashMapper
 * @date    2011.07.13
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Generates a HashMap from a delimited file with specified Key and Value classes
 *
 */
public abstract class FileHashMapper<K , V> {

    private static final Logger logger = Logger.getLogger( FileHashMapper.class );
    private CSVReader reader;
    private Integer keyIndex;
    private Integer valueIndex;
    private Class<?> keyClass;
    private Class<?> valuClass;

    /**
     * @brief Constructor specifying the input reader and the column index of the key
     *        and the column
     * @param reader     Instance of CSVReader object (part of OpenCSV library)
     * @param keyIndex   Index of the column to use as a key
     * @param valueIndex Index of the column to use as a value
     */
    public FileHashMapper( CSVReader reader , Integer keyIndex , Integer valueIndex ) {
        this.reader = reader;
        this.keyIndex = keyIndex;
        this.valueIndex = valueIndex;
    }

    /**
     * Builds a hash map by reading a file and using the specified columns for mapping
     * keys and values
     * @param  header skip the first line of the file
     * @return constructed hash map
     * @throw  IOException
     */
    public Map<K , V> getHashMap( boolean header ) throws IOException {

        // if the header is specified skip the first line
        if ( header ) {
            String[] headers = reader.readNext();
        }

        Map<K , V> map = new HashMap<K , V>();

        // reader the file/stream in
        String[] row = reader.readNext();
        do {

            // get the key and value objects from the abstract methods
            K key = parseKey( row[keyIndex] );
            V value = parseValue( row[valueIndex] );

            if ( map.containsKey( key ) ) {
                map.put( key , clash( map.get( key ) , value ) );
            } else {
                // add to the map
                map.put( key , value );
            }

            row = reader.readNext();
        } while ( row != null );

        return map;
    }

    /**
     * Parses a string in to the defined object type
     * @param keyString
     * @return
     */
    public abstract K parseKey( String keyString );

    /**
     * Parses a string in to the defined object type
     * @param keyString
     * @return
     */
    public abstract V parseValue( String keyString );

    /**
     * Handles the clashing of keys. Given the old and new values to be stored
     * the method should decided what todo e.g. discard old, new or merger
     * @param oldValue
     * @param newValue
     * @return
     */
    public abstract V clash( V oldValue , V newValue );
}
