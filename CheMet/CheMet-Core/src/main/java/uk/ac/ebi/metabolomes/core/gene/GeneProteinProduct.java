/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.ac.ebi.metabolomes.core.gene;

import java.io.Externalizable;
import java.io.Serializable;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.UniqueIdentifier;


/**
 * GeneProteinProduct.java
 * A class to represent a monomeric protein product from a genome
 *
 * @author johnmay
 * @date Apr 4, 2011
 */
public class GeneProteinProduct
  extends GeneProduct
  implements Externalizable {

    private transient static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.
      getLogger(GeneProteinProduct.class);


    /**
     * Creates and empty gene protein product with a new random identifier and an empty sequence
     */
    public GeneProteinProduct() {
        this(UniqueIdentifier.createUniqueIdentifer(), "");
    }


    /**
     * Creates a gene protein product with the specified identifier but an empty sequence
     * @param identifier AbstractIdentifier for the sequence
     */
    public GeneProteinProduct(AbstractIdentifier identifier) {
        this(identifier, "");
    }


    /**
     * //</editor-fold>
     * Creates a protein product with specified id and sequecne
     * @param identifier AbstractIdentifier for the product
     * @param sequence ProteinSequence for the product
     */
    public GeneProteinProduct(AbstractIdentifier identifier, String sequence) {
        this(identifier, sequence, "");
    }


    public GeneProteinProduct(AbstractIdentifier identifier,
                              String sequence,
                              String description) {
        setIdentifier(identifier);
        setType(ProductType.PROTEIN);
        setSequence(sequence);
        setSequenceLength(sequence.length());
        // TODO put description in the details setDescription( description );
    }


}

