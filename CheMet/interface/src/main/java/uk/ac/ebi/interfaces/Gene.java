
/**
 * GeneProduct.java
 *
 * 2011.09.12
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
package uk.ac.ebi.interfaces;


/**
 *          Gene – 2011.09.12 <br>
 *          Interface describing a gene
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface Gene {

    public String getGeneName();


    /**
     *
     * Access the chromosome this gene is present on
     *
     * @return
     *
     */
    public Chromosome getChromosome();


    /**
     *
     * Access the strand the gene is sense (+)/3'-5'/watson or anti-sense (-)/5'-3'/crick
     *
     * @return
     *
     */
    public Integer getPolarity();

    /**
     *
     * Access the start location of the gene
     *
     * @return
     *
     */
    public Integer getStart();

    /**
     *
     * Access the end location of the gene
     *
     * @return
     */
    public Integer getEnd();

    /**
     *
     * Access the length of the gene
     *
     * @return
     */
    public Integer getLength();


}

