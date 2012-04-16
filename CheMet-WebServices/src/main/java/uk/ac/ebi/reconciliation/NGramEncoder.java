
/**
 * FingerprintEncoder.java
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
package uk.ac.ebi.reconciliation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 *          FingerprintEncoder – 2011.09.22 <br>
 *          A basic fingerprint
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class NGramEncoder extends AbstractEncoder {

    private static final Logger LOGGER = Logger.getLogger(NGramEncoder.class);
    private int size;


    /**
     * Create a new n-gram encoder that splits the string in to fragments of specified size
     * @param size
     */
    public NGramEncoder(int size) {
        this.size = size;
    }

    /**
     * @inheritDoc
     */
    public String encode(String string) {

        String clean = string.trim();
        clean = string.toLowerCase();
        clean = addSpaceWhereDashesAre(clean);
        clean = removeControlCharacters(clean);
        clean = removeHTMLTags(clean);
        clean = removeGenericBeginning(clean);

        List<String> fragments = new ArrayList();
        int index = 0;
        while( (index + size) < clean.length() ) {
            fragments.add(clean.substring(index, index + size));
            index += size;
        }

        Collections.sort(fragments);

        return asciify(StringUtils.join(fragments, ""));

    }


}

