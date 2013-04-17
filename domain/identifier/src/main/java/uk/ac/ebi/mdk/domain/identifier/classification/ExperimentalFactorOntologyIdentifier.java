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

package uk.ac.ebi.mdk.domain.identifier.classification;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.deprecated.Synonyms;

/**
 * @name    ExperimentalFactorOntologyIdentifier
 * @date    2012.02.15
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
@Brief("Experimental Factor Ontology")
@Synonyms("EFO")
public class ExperimentalFactorOntologyIdentifier extends ClassificationIdentifier {
    
    private static final Logger LOGGER = Logger.getLogger(ExperimentalFactorOntologyIdentifier.class);
    
    public Identifier newInstance() {
        return new ExperimentalFactorOntologyIdentifier();
    }
}
