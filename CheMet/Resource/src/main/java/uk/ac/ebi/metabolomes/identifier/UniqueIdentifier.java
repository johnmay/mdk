/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.ac.ebi.metabolomes.identifier;

import uk.ac.ebi.interfaces.Identifier;


/**
 * UniqueIdentifier.java
 * A class to build a unique identifier implementing a simple 'ticker'
 * integer which increments with every new identifier creation. This is
 * generally used as a placeholder when no identifiers are found but one
 * is needed
 *
 * @author johnmay
 * @date Apr 4, 2011
 */
public class UniqueIdentifier
  extends AbstractIdentifier {

    private static int ticker = 0;
    private static String prefix = "UID";
    private static int padlength = 6;


    private UniqueIdentifier(int tick) {
        setAccession(String.format(prefix + "%0" + padlength + "d", tick));
    }


    /**
     * Create a new unique (for this run) identifier
     * @return a unique identifier
     */
    public static UniqueIdentifier createUniqueIdentifer() {
        return new UniqueIdentifier(++ticker);
    }


    /**
     * Sets the prefix for the unique identifier
     * @param prefix
     */
    public static void setPrefix(String prefix) {
        UniqueIdentifier.prefix = prefix;
    }


    /**
     * Accessor for the current prefix
     * @return the prefix of the identifier
     */
    public static String getPrefix() {
        return prefix;
    }


    /**
     * Get the length of the padding for the identifier.
     * The pad length makes all identifers the same length
     * upto a point padding with 0s
     * @return
     */
    public static int getPadlength() {
        return padlength;
    }


    /**
     * Mutator for pad length
     * @param padlength
     */
    public static void setPadlength(int padlength) {
        UniqueIdentifier.padlength = padlength;
    }


    public Identifier newInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}

