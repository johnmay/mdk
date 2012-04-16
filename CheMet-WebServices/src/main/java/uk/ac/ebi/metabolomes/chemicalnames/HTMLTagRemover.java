/**
 * HTMLTagRemover.java
 *
 * 2011.09.09
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
package uk.ac.ebi.metabolomes.chemicalnames;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * @name    HTMLTagRemover
 * @date    2011.09.09
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class HTMLTagRemover {
    
    private static final Logger LOGGER = Logger.getLogger(HTMLTagRemover.class);
    //private static final Pattern definedHtmlTags = Pattern.compile("</{0,1}(i|sub|sup)>");
    //private static final Pattern greekLetters = Pattern.compile("&(alpha|beta|gamma|epsilon);");
    private static final List<Pattern> htmlPatternsWithReplacement = Arrays.asList(
            Pattern.compile("&(alpha);",Pattern.CASE_INSENSITIVE),
            Pattern.compile("&(delta);",Pattern.CASE_INSENSITIVE),
            Pattern.compile("&(beta);",Pattern.CASE_INSENSITIVE),
            Pattern.compile("&(gamma);",Pattern.CASE_INSENSITIVE),
            Pattern.compile("&(epsilon);",Pattern.CASE_INSENSITIVE)
            );
    private static final List<Pattern> htmlPatternsNoReplace = Arrays.asList(
            Pattern.compile("</{0,1}(i|sub|sup)>",Pattern.CASE_INSENSITIVE)
            );
    
    
    public static String cleanHTMLTags(String toClean) {
        for (Pattern pattern : htmlPatternsNoReplace) {
            Matcher matcher = pattern.matcher(toClean);
            if(matcher.find())
                toClean = matcher.replaceAll("");
        }
        for (Pattern pattern : htmlPatternsWithReplacement) {
            Matcher matcher = pattern.matcher(toClean);
            if(matcher.find())
                toClean = matcher.replaceAll(matcher.group(1));
        }
        /*Matcher greekLettersMatcher = greekLetters.matcher(toClean);
         * This loop wasn't working correctly
        while(greekLettersMatcher.find()) {
            String letter = greekLettersMatcher.group(1);
            toClean = greekLettersMatcher.replaceFirst(letter);
            greekLettersMatcher.
            greekLettersMatcher = greekLetters.matcher(toClean);
        }*/
        return toClean;
    }
}
