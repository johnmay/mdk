/**
 * GeneProduct.java
 *
 * 2011.10.07
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
package uk.ac.ebi.mdk.domain.entity;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name GeneProduct - 2011.10.07 <br> Class description
 */
public abstract class AbstractGeneProduct
        extends AbstractAnnotatedEntity implements GeneProduct {

    private static final Logger LOGGER = Logger.getLogger(AbstractGeneProduct.class);
    private Set<Gene> genes = new HashSet<Gene>(1); // 1 to 1 but can be more

    public AbstractGeneProduct() {
    }

    public AbstractGeneProduct(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    public boolean addGene(Gene gene) {
        gene.addProduct(this);
        return this.genes.add(gene);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void clearGenes() {
        this.genes.clear();
    }

    public Collection<Gene> getGenes() {
        return this.genes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AbstractGeneProduct that = (AbstractGeneProduct) o;

        if (!genes.equals(that.genes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + genes.hashCode();
        return result;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean remove(Gene gene) {
        gene.removeProduct(this);
        return this.genes.remove(gene);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new NotSerializableException();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new NotSerializableException();
    }

    public void readExternal(ObjectInput in, Genome genome) throws IOException, ClassNotFoundException {
        super.readExternal(in);

        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            int c = in.readInt();
            int g = in.readInt();
            this.genes.add(genome.getGene(c, g));
        }
    }

    public void writeExternal(ObjectOutput out, Genome genome) throws IOException {

        super.writeExternal(out);

        out.writeInt(genes.size());

        for (Gene gene : genes) {
            int[] index = genome.getIndex(gene);
            out.writeInt(index[0]);
            out.writeInt(index[1]);
        }


    }
}
