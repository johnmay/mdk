/**
 * SmilesBugAmender.java
 *
 * 2011.11.12
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
package uk.ac.ebi.metabolomes.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * @name    SmilesBugAmender
 * @date    2011.11.12
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class SmilesBugAmender {
    
    private static final Logger LOGGER = Logger.getLogger(SmilesBugAmender.class);
    // OP(O)(=O) for OP(=O)(O)
    // (OP(O)(O)=O) for (OP(=O)(O)O)
    private final List<Pattern> patterns = new ArrayList<Pattern>();
    private final List<String> corrections = new ArrayList<String>();
    
    public SmilesBugAmender() {
        patterns.add(Pattern.compile("P\\(O\\)\\(=O\\)"));
        corrections.add("P(=O)(O)");
        
        patterns.add(Pattern.compile("P\\(O\\)\\(O\\)=O"));
        corrections.add("P(=O)(O)O");
        
        patterns.add(Pattern.compile("P\\(O\\)\\(=O\\)O"));
        corrections.add("P(=O)(O)O");
        
        patterns.add(Pattern.compile("\\(O\\)\\(\\[O-\\]\\)\\[O-\\]"));
        corrections.add("([O-])([O-])O");
        
        patterns.add(Pattern.compile("\\(=\\[N+\\]\\)N"));
        corrections.add("(N)=[N+]");
        
        patterns.add(Pattern.compile("C\\(C\\)=O"));
        corrections.add("C(=O)C");
    }
    
    public String amendSmiles(String smiles) {
        String toAmend = smiles;
        for (int i = 0; i < patterns.size(); i++) {
            Pattern pattern = patterns.get(i);
            toAmend = pattern.matcher(toAmend).replaceAll(corrections.get(i));
        }
        return toAmend;
    }
    
    
}
