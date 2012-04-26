
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
package uk.ac.ebi.chemet.resource.classification;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.resource.Synonyms;


/**
 *          GeneOntologyTerm – 2011.09.14 <br>
 *          Identifier for http://www.ebi.ac.uk/GOA/
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Brief("UniProtKB-GOA")
@Description("The UniProtKB GO annotation program aims to provide high-quality Gene Ontology (GO) annotations to proteins in the UniProt Knowledgebase (UniProtKB)")
@Synonyms("GOA")
public class GeneOntologyAnnotation
  extends ClassificationIdentifier {

    private static final Logger LOGGER = Logger.getLogger(GeneOntologyAnnotation.class);


    public GeneOntologyAnnotation() {
    }


    public GeneOntologyAnnotation(String accession) {
        super(accession);
    }

    /**
     * @inheritDoc
     */
    @Override
    public GeneOntologyAnnotation newInstance() {
        return new GeneOntologyAnnotation();
    }


}

