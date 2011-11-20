/**
 * KEGGBriteReader.java
 *
 * 2011.11.19
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
package uk.ac.ebi.io.plain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * @name    KEGGBriteReader
 * @date    2011.11.19
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class KEGGBriteReader {
    
    private static final Logger LOGGER = Logger.getLogger(KEGGBriteReader.class);
    
    private BufferedReader reader;
    private String currentBriteEntry; // file header category
    private String currentBriteName; // file header category
    private String currentBriteDefinition; // file header category
    
    private Pattern entryPattern;
    private Pattern namePattern;
    private Pattern defPattern;
    private Pattern regLinePattern;
    private Pattern keggCompPattern;
    
    private KEGGBriteEntry currentEntry;
    
    public KEGGBriteReader(InputStream in) throws IOException {
        entryPattern = Pattern.compile("#ENTRY\\s{7}(.+)");
        namePattern = Pattern.compile("#NAME\\s{8}(.+)");
        defPattern = Pattern.compile("#DEFINITION\\s{2}(.+)");
        
        regLinePattern = Pattern.compile("^([A-Z])\\s+(.+)$");
        keggCompPattern = Pattern.compile("(C\\d{5})\\s+(.+)$");
        
        reader = new BufferedReader(new InputStreamReader(in));
        currentBriteEntry = readBriteHeader(entryPattern);
        currentBriteName = readBriteHeader(namePattern);
        currentBriteDefinition = readBriteHeader(defPattern);
        readUntilNextExclamationLine();
        currentEntry = new KEGGBriteEntry();
    }

    private String readBriteHeader(Pattern pattern) throws IOException {
        String line=reader.readLine();
        Matcher matcher = pattern.matcher(line);
        while(!matcher.find() && line!=null) {
            line = reader.readLine();
            matcher = entryPattern.matcher(line);
        }
        return matcher.group(1);
    }
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    public KEGGBriteEntry readEntry() throws IOException {
        String line = reader.readLine();
        while(!line.equals("!")) {
            Matcher matcher = regLinePattern.matcher(replaceHTMLTags(line));
            if(matcher.find()) {
                String categoryDesc = matcher.group(1);
                String value = matcher.group(2);
                Matcher keggCompMatcher = keggCompPattern.matcher(value);
                if(keggCompMatcher.find()) {
                    // We have found a KEGG Compound
                    currentEntry.setKEGGCompound(keggCompMatcher.group(1));
                    currentEntry.setCpdName(keggCompMatcher.group(2));
                    KEGGBriteEntry toRet = currentEntry;
                    currentEntry = new KEGGBriteEntry(toRet);
                    return toRet;
                } else {
                    currentEntry.setCategory(categoryDesc, value);
                }
            }
            line = reader.readLine();
        }
        return null;
    }

    /**
     * @return the currentBriteEntry
     */
    public String getBriteEntryHeader() {
        return currentBriteEntry;
    }

    /**
     * @return the currentBriteName
     */
    public String getBriteNameHeader() {
        return currentBriteName;
    }

    /**
     * @return the currentBriteDefinition
     */
    public String getBriteDefinitionHeader() {
        return currentBriteDefinition;
    }

    private void readUntilNextExclamationLine() throws IOException {
        String line = reader.readLine();
        while(line!=null && !line.equals("!")) {
            line = reader.readLine();
        }
    }

    private CharSequence replaceHTMLTags(String line) {
        return line.replace("<b>", "  ").replace("</b>", "");
    }
    
    public void close() throws IOException {
        reader.close();
    }
    
    
}
