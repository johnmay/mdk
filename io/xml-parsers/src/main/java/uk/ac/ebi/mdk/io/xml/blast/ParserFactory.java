/**
 * AlignmentFactory.java
 *
 * 2011.10.06
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
package uk.ac.ebi.mdk.io.xml.blast;

import org.apache.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * @name    AlignmentFactory - 2011.10.06 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ParserFactory {

    private static final Logger LOGGER = Logger.getLogger(ParserFactory.class);
    Map<String, BLASTRowParser> blastParsers = new HashMap();

    private ParserFactory() {
        blastParsers.put("2.2.24", new BLASTRowParser_V2_2_24());
        blastParsers.put("2.2.25", new BLASTRowParser_V2_2_24());
    }

    private  static class AlignmentFactoryHolder {

        private static ParserFactory INSTANCE = new ParserFactory();
    }

    /**
     * Returns an instance of the factory
     * @return
     */
    public static ParserFactory getInstance() {
        return AlignmentFactoryHolder.INSTANCE;
    }

    /**
     * Creates a LocalAlignment from a blast
     * @param version
     */
    public BLASTRowParser getBLASTRowParser(String version) {
        if (blastParsers.containsKey(version)) {
            return blastParsers.get(version);
        } else {
            throw new InvalidParameterException("Version " + version + " of blase is not supported yet");
        }
    }
}
