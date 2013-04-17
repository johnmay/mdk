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

package uk.ac.ebi.mdk.domain.identifier.basic;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.IncrementalPreference;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.ResourcePreferences;
import uk.ac.ebi.mdk.domain.identifier.AbstractIdentifier;

/**
 *          BasicReactionIdentifier – 2011.09.26 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Brief("Reaction")
@Description("A basic auto-incrementing identifier for reactions")
public class BasicReactionIdentifier extends AbstractIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BasicReactionIdentifier.class);

    public BasicReactionIdentifier() {
    }

    public BasicReactionIdentifier(String accession) {
        super(accession);
    }

    @Override
    public void setAccession(String accession) {
        super.setAccession(accession);
    }

    public BasicReactionIdentifier newInstance() {
        return new BasicReactionIdentifier();
    }

    private static String nextAccession(){
        StringPreference format = ResourcePreferences.getInstance().getPreference("BASIC_RXN_ID_FORMAT");
        IncrementalPreference ticker = ResourcePreferences.getInstance().getPreference("BASIC_RXN_ID_TICK");
        return String.format(format.get(), ticker.get());
    }
    
    public static BasicReactionIdentifier nextIdentifier() {
        return new BasicReactionIdentifier(nextAccession());
    }

}
