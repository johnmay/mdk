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
package uk.ac.ebi.chemet.resource.basic;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.IncrementalPreference;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.chemet.resource.ResourcePreferences;
import uk.ac.ebi.chemet.resource.base.AbstractIdentifier;
import uk.ac.ebi.mdk.domain.identifier.type.ProteinIdentifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


/**
 *          BasicProteinIdentifier – 2011.09.14 <br>
 *          A basic protein identifier
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Brief("Protein")
@Description("A basic auto-incrementing identifier for proteins")
public class BasicProteinIdentifier
        extends AbstractIdentifier implements ProteinIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BasicProteinIdentifier.class);


    public BasicProteinIdentifier() {
        super(nextAccession());
    }


    public BasicProteinIdentifier(String accession) {
        super(accession);
    }
    
    private static String nextAccession(){
        StringPreference format = ResourcePreferences.getInstance().getPreference("BASIC_PROT_ID_FORMAT");
        IncrementalPreference ticker = ResourcePreferences.getInstance().getPreference("BASIC_GENE_ID_TICK");
        return String.format(format.get(), ticker.get());
    }

    /**
     * @inheritDoc
     */
    @Override
    public BasicProteinIdentifier newInstance() {
        return new BasicProteinIdentifier();
    }


    @Override
    public BasicProteinIdentifier ofHeader(Iterator<String> token) {
        return new BasicProteinIdentifier(token.next());  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public Collection<String> getHeaderCodes() {
        return Arrays.asList("lcl");
    }
}
