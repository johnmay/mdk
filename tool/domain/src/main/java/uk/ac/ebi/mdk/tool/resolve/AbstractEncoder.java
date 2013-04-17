
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

/**
 * AbstractEncoder.java
 *
 * 2011.09.22
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
package uk.ac.ebi.mdk.tool.resolve;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.TreeSet;
import java.util.regex.Pattern;


/**
 *          AbstractEncoder – 2011.09.22 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractEncoder
  implements StringEncoder {

    private static final Logger LOGGER = Logger.getLogger(AbstractEncoder.class);
    private static final Pattern ALPHA_NUMERIC_PATTERN = Pattern.compile("\\p{Punct}|\\p{Cntrl}");
    private static final Pattern PLURAL = Pattern.compile("s$");
    private static final Pattern DEFINED_HTML_TAGS = Pattern.compile("</{0,1}(?:i|sub|sup)>");
    private static final Pattern DIGIT = Pattern.compile("\\d");
    private static final Pattern GENERIC_BEGINNING = Pattern.compile("\\Aan{0,1}\\s+");
    private static final Pattern WHITE_SPACE = Pattern.compile("\\s");
    private static final Pattern DASHES = Pattern.compile("-|\u2012|\u2013|\u2014|\u2015");
    private static final Pattern BRACKETED_PHRASE = Pattern.compile("\\(\\d\\-\\)");

    
    public String removeChargeBrace(String string) {
        return BRACKETED_PHRASE.matcher(string).replaceAll("");
    }


    public String removeWhiteSpace(String string) {
        return WHITE_SPACE.matcher(string).replaceAll("");
    }


    /**
     * Removes all types of dashes (e.g. different lengths)
     *
     * @param string
     * @return
     */
    public String removeDashes(String string) {
        return DASHES.matcher(string).replaceAll("");
    }


    public String addSpaceWhereDashesAre(String string) {
        return DASHES.matcher(string).replaceAll(" ");
    }


    /**
     * splits on white-space and sorts fragments alphabetically (fragments should be unique). Numbers
     * are maintained in order
     */
    public String reorderFragments(String string) {

        String[] fragments = string.split("\\s+");

        TreeSet textFragments = new TreeSet();
        StringBuilder digits = new StringBuilder();

        for( String fragment : fragments ) {
            if( fragment.matches("\\d+") ) {
                digits.append(fragment);
            } else {
                textFragments.add(fragment);
            }
        }

        return digits.toString() + StringUtils.join(textFragments, "");

    }


    /**
     * removes 'an' and 'a' from the start of a chemical name. For example 'an alcohol' would
     * be trimmed to 'alochol'
     */
    public String removeGenericBeginning(String string) {
        return GENERIC_BEGINNING.matcher(string).replaceAll("");
    }


    public String removeControlCharacters(String string) {
        return ALPHA_NUMERIC_PATTERN.matcher(string).replaceAll("");
    }


    /**
     * Removes {@code <i> <sub> <sup> html tags}
     * @param string
     * @return
     */
    public String removeHTMLTags(String string) {
        return DEFINED_HTML_TAGS.matcher(string).replaceAll("");
    }


    /**
     *
     * Converts a unicode string into an appropriate ascii representation
     *
     * @param s
     * @return
     * 
     */
    protected static String asciify(final String s) {

        char[] characters = s.toCharArray();

        for( int i = 0 ; i < characters.length ; i++ ) {
            characters[i] = translate(characters[i]);
        }

        return new String(characters);

    }


//    private static void f(){
//
//    }
    /**
     * Translate the given unicode char in the closest ASCII representation
     * NOTE: this function deals only with latin-1 supplement and latin-1 extended code charts
     */
    private static char translate(final char c) {
        switch( c ) {
            case '\u00C0':
            case '\u00C1':
            case '\u00C2':
            case '\u00C3':
            case '\u00C4':
            case '\u00C5':
            case '\u00E0':
            case '\u00E1':
            case '\u00E2':
            case '\u00E3':
            case '\u00E4':
            case '\u00E5':
            case '\u0100':
            case '\u0101':
            case '\u0102':
            case '\u0103':
            case '\u0104':
            case '\u0105':
                return 'a';
            case '\u00C7':
            case '\u00E7':
            case '\u0106':
            case '\u0107':
            case '\u0108':
            case '\u0109':
            case '\u010A':
            case '\u010B':
            case '\u010C':
            case '\u010D':
                return 'c';
            case '\u00D0':
            case '\u00F0':
            case '\u010E':
            case '\u010F':
            case '\u0110':
            case '\u0111':
                return 'd';
            case '\u00C8':
            case '\u00C9':
            case '\u00CA':
            case '\u00CB':
            case '\u00E8':
            case '\u00E9':
            case '\u00EA':
            case '\u00EB':
            case '\u0112':
            case '\u0113':
            case '\u0114':
            case '\u0115':
            case '\u0116':
            case '\u0117':
            case '\u0118':
            case '\u0119':
            case '\u011A':
            case '\u011B':
                return 'e';
            case '\u011C':
            case '\u011D':
            case '\u011E':
            case '\u011F':
            case '\u0120':
            case '\u0121':
            case '\u0122':
            case '\u0123':
                return 'g';
            case '\u0124':
            case '\u0125':
            case '\u0126':
            case '\u0127':
                return 'h';
            case '\u00CC':
            case '\u00CD':
            case '\u00CE':
            case '\u00CF':
            case '\u00EC':
            case '\u00ED':
            case '\u00EE':
            case '\u00EF':
            case '\u0128':
            case '\u0129':
            case '\u012A':
            case '\u012B':
            case '\u012C':
            case '\u012D':
            case '\u012E':
            case '\u012F':
            case '\u0130':
            case '\u0131':
                return 'i';
            case '\u0134':
            case '\u0135':
                return 'j';
            case '\u0136':
            case '\u0137':
            case '\u0138':
                return 'k';
            case '\u0139':
            case '\u013A':
            case '\u013B':
            case '\u013C':
            case '\u013D':
            case '\u013E':
            case '\u013F':
            case '\u0140':
            case '\u0141':
            case '\u0142':
                return 'l';
            case '\u00D1':
            case '\u00F1':
            case '\u0143':
            case '\u0144':
            case '\u0145':
            case '\u0146':
            case '\u0147':
            case '\u0148':
            case '\u0149':
            case '\u014A':
            case '\u014B':
                return 'n';
            case '\u00D2':
            case '\u00D3':
            case '\u00D4':
            case '\u00D5':
            case '\u00D6':
            case '\u00D8':
            case '\u00F2':
            case '\u00F3':
            case '\u00F4':
            case '\u00F5':
            case '\u00F6':
            case '\u00F8':
            case '\u014C':
            case '\u014D':
            case '\u014E':
            case '\u014F':
            case '\u0150':
            case '\u0151':
                return 'o';
            case '\u0154':
            case '\u0155':
            case '\u0156':
            case '\u0157':
            case '\u0158':
            case '\u0159':
                return 'r';
            case '\u015A':
            case '\u015B':
            case '\u015C':
            case '\u015D':
            case '\u015E':
            case '\u015F':
            case '\u0160':
            case '\u0161':
            case '\u017F':
                return 's';
            case '\u0162':
            case '\u0163':
            case '\u0164':
            case '\u0165':
            case '\u0166':
            case '\u0167':
                return 't';
            case '\u00D9':
            case '\u00DA':
            case '\u00DB':
            case '\u00DC':
            case '\u00F9':
            case '\u00FA':
            case '\u00FB':
            case '\u00FC':
            case '\u0168':
            case '\u0169':
            case '\u016A':
            case '\u016B':
            case '\u016C':
            case '\u016D':
            case '\u016E':
            case '\u016F':
            case '\u0170':
            case '\u0171':
            case '\u0172':
            case '\u0173':
                return 'u';
            case '\u0174':
            case '\u0175':
                return 'w';
            case '\u00DD':
            case '\u00FD':
            case '\u00FF':
            case '\u0176':
            case '\u0177':
            case '\u0178':
                return 'y';
            case '\u0179':
            case '\u017A':
            case '\u017B':
            case '\u017C':
            case '\u017D':
            case '\u017E':
                return 'z';
        }
        return c;
    }


}

