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
import org.biojava3.core.sequence.Strand;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * GeneImpl - 2011.10.17 <br> An implementation of the Gene interface
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class GeneImpl extends AbstractAnnotatedEntity implements Gene {

    private static final Logger LOGGER = Logger.getLogger(GeneImpl.class);

    private int start;

    private int end;

    private Strand strand = Strand.UNDEFINED;

    private Sequence sequence;

    private Chromosome chromosome;

    private Set<GeneProduct> products = new HashSet<GeneProduct>(1);


    public GeneImpl() {
    }


    public GeneImpl(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    public GeneImpl(Identifier identifier, String abbreviation, String name, int start, int end, Strand strand) {
        super(identifier, abbreviation, name);
        this.start = start;
        this.end = end;
        this.strand = strand;
    }


    public Chromosome getChromosome() {
        return chromosome;
    }


    public void setChromosome(Chromosome chromosome) {
        this.chromosome = chromosome;
    }


    public Strand getStrand() {
        return strand;
    }


    public void setStrand(Strand strand) {
        this.strand = strand;
    }


    public int getStart() {
        return start;
    }


    public void setStart(int start) {
        this.start = start;
    }


    public int getEnd() {
        return end;
    }


    public void setEnd(int end) {
        this.end = end;
    }


    public int getLength() {
        return sequence.getLength();
    }


    public Sequence getSequence() {
        return sequence;
    }


    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean removeProduct(GeneProduct product) {
        return this.products.remove(product);
    }

    @Override
    public boolean addProduct(GeneProduct product) {
        return this.products.add(product);
    }

    @Override
    public Collection<GeneProduct> getProducts() {
        return Collections.unmodifiableSet(products);
    }

    public Gene newInstance() {
        return new GeneImpl();
    }

    @Override
    public String toString() {
        return super.toString() + start + ".." + end;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        start = in.readInt();
        end = in.readInt();
        strand = (Strand) in.readObject();
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(start);
        out.writeInt(end);
        out.writeObject(strand);
    }
}
