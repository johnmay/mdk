/**
 * GeneOntologyTerm.java
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
package uk.ac.ebi.resource.classification;

import org.apache.log4j.Logger;
import uk.ac.ebi.resource.MIR;


/**
 *          GeneOntologyTerm – 2011.09.14 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@MIR(value = 22)
public class GeneOntologyTerm
        extends ClassificationIdentifier {

    private static final Logger LOGGER = Logger.getLogger(GeneOntologyTerm.class);


    public GeneOntologyTerm() {
    }


    public GeneOntologyTerm(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    @Override
    public GeneOntologyTerm newInstance() {
        return new GeneOntologyTerm();
    }
}
