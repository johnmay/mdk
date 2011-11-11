/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.core.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.mutable.MutableInt;

/**
 * Util.java
 *
 *
 * @author johnmay
 * @date May 11, 2011
 */
public class Util {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Util.class);

    public static String encodeURN(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 127 || c == '"' || c == '<' || c == '>') {
                sb.append("&#").append((int) c);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String unescapeHTML(String s) {
        Pattern pattern = Pattern.compile("&#(\\d+);");
        Matcher m = pattern.matcher(s);
        while (m.find()) {
            s = m.replaceAll(new String(new char[]{(char) Integer.parseInt(m.group(1))}));
            m = pattern.matcher(s);
        }
        return s;
    }

    /**
     *
     * Performs pseudo random number generation on the provided seed
     *
     * @param seed
     * @return
     */
    public static int xorShift(int seed) {
        seed ^= seed << 6;
        seed ^= seed >>> 21;
        seed ^= (seed << 7);
        return seed;
    }

    /**
     *
     * Rotates the seed using xor shift (pseudo random number generation) the
     * specified number of times.
     *
     * @param seed the starting seed
     * @param rotation Number of xor rotations to perform
     * @return The starting seed rotated the specified number of times
     */
    public static int rotate(int seed, int rotation) {
        for (int j = 0; j < rotation; j++) {
            seed = xorShift(seed);
        }
        return seed;
    }

    /**
     *
     * Rotates the seed if the seed has already been seen in the provided
     * occurrences map
     *
     * @param seed
     * @param occurences
     * @return
     */
    public static int rotate(int seed, Map<Integer, MutableInt> occurences) {
        if (occurences.get(seed) == null) {
            occurences.put(seed, new MutableInt());
        } else {
            occurences.get(seed).increment();
        }
        // System.out.printf("%10s", occMap.get(seed).value);
        return rotate(seed, occurences.get(seed).intValue());
    }
}
