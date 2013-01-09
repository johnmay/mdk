/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.domain.entity;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.template.AbstractCompound;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicProteinIdentifier;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 *          Multimer - 2011.10.24 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MultimerImpl extends AbstractGeneProduct implements Multimer {

    private static final Logger LOGGER = Logger.getLogger(MultimerImpl.class);

    public static final String BASE_TYPE = "Multimer";

    private List<GeneProduct> subunits = new ArrayList();


    public MultimerImpl() {
    }


    public MultimerImpl(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    /**
     * Creates a multimeric gene product from existing products. Note the copies
     * are shallow thus changing the names of the product within the multimer
     * will change the names of the products and vise-versa.
     * @param subunits
     */
    public MultimerImpl(GeneProduct... subunits) {

        super(new BasicProteinIdentifier(), null, null);


        // make an aggregated name

        StringBuilder idBuilder = new StringBuilder();
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder abbrBuilder = new StringBuilder();

        for (int i = 0; i < subunits.length; i++) {
            GeneProduct subunit = subunits[i];
            addSubunit(subunit);

            idBuilder.append(subunit.getAccession()).append(i + 1 < subunits.length
                                                            ? " + " : "");
            nameBuilder.append(subunit.getName()).append(i + 1 < subunits.length
                                                         ? " + " : "");
            abbrBuilder.append(subunit.getAbbreviation()).append(i + 1 < subunits.length
                                                                 ? " + " : "");

        }

        setName(nameBuilder.toString());
        setAbbreviation(abbrBuilder.toString());

    }


    public final boolean addSubunit(GeneProduct subunit) {
        return subunits.add(subunit);
    }


    /**
     * Returns a collection (list) of all genes of the subunits
     * @return
     */
    public Collection<Gene> getGenes() {
        List<Gene> genes = new ArrayList();
        for (GeneProduct product : subunits) {
            genes.addAll(product.getGenes());
        }
        return genes;
    }


    /**
     * Throws an UnsupportedOperationException as the call to add a gene to a
     * multimeric product is ambiguous
     */
    public boolean addGene(Gene gene) {
        throw new UnsupportedOperationException("Unable to add a gene to a multimer");
    }


    public List<? extends Sequence> getSequences() {
        List<Sequence> sequences = new ArrayList();
        for (GeneProduct product : subunits) {
            sequences.addAll(product.getSequences());
        }
        return sequences;
    }


    public List<GeneProduct> getSubunits() {
        return subunits;
    }


    public boolean removeSubunit(GeneProduct product) {
        return subunits.remove(product);
    }


    public boolean addSequence(Sequence<? extends AbstractCompound> sequence) {
        throw new UnsupportedOperationException("Can't add sequence to multimer");
    }


    public void readExternal(ObjectInput in, Genome genome) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void writeExternal(ObjectOutput out, Genome genome) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public GeneProduct newInstance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
