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
package uk.ac.ebi.mdk.domain.entity;

import org.biojava3.core.sequence.Strand;
import org.biojava3.core.sequence.template.Sequence;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;

import java.util.Collection;

/**
 * Gene – 2011.09.12 <br> Interface describing a gene
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public interface Gene extends AnnotatedEntity {

    /**
     * Access the chromosome this gene is present on
     *
     * @return
     */
    public Chromosome chromosome();

    public void setChromosome(Chromosome chromosome);

    /**
     * Access the sense the gene is sense (+)/3'-5'/watson or anti-sense
     * (-)/5'-3'/crick
     *
     * @return
     */
    public Strand getStrand();

    public void setStrand(Strand strand);

    /**
     * Access the start location of the gene
     *
     * @return
     */
    public int getStart();

    public void setStart(int end);

    /**
     * Access the end location of the gene
     *
     * @return
     */
    public int getEnd();

    public void setEnd(int end);

    /**
     * Access the length of the gene
     *
     * @return
     */
    public int getLength();

    /**
     * Access the sequence of the gene
     *
     * @return
     */
    public Sequence getSequence();

    /**
     * Set the sequence of the gene (normally derived from the start and end
     * with the entire genome sequence
     *
     * @param sequence
     */
    public void setSequence(Sequence sequence);
}
