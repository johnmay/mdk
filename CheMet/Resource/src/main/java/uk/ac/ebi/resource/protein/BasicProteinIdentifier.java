/**
 * BasicProteinIdentifier.java
 *
 * 2011.09.14
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
package uk.ac.ebi.resource.protein;

import java.util.LinkedList;
import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.identifiers.MetaboliteIdentifier;
import uk.ac.ebi.interfaces.identifiers.ProteinIdentifier;
import uk.ac.ebi.caf.utility.preference.type.IncrementalPreference;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;
import uk.ac.ebi.resource.ResourcePreferences;


/**
 *          BasicProteinIdentifier – 2011.09.14 <br>
 *          A basic protein identifier
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BasicProteinIdentifier
        extends AbstractProteinIdentifier implements ProteinIdentifier, MetaboliteIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BasicProteinIdentifier.class);


    public BasicProteinIdentifier() {
    }


    public BasicProteinIdentifier(String accession) {
        super(accession);
    }


    public static Identifier nextIdentifier() {
        StringPreference format = ResourcePreferences.getInstance().getPreference("BASIC_PROT_ID_FORMAT");
        IncrementalPreference ticker = ResourcePreferences.getInstance().getPreference("BASIC_GENE_ID_TICK");

        return new BasicProteinIdentifier(String.format(format.get(), ticker.get()));
    }


    /**
     * @inheritDoc
     */
    @Override
    public BasicProteinIdentifier newInstance() {
        return new BasicProteinIdentifier();
    }


    @Override
    public LinkedList<String> resolve(LinkedList<String> tokens) {
        String database = tokens.get(1); // store?
        setAccession(tokens.get(2));
        tokens.removeFirst();
        tokens.removeFirst();
        tokens.removeFirst();
        return tokens;

    }


    @Override
    public String getHeaderCode() {
        return "gnl";
    }
}
