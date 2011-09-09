/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.ac.ebi.metabolomes.run;

import uk.ac.ebi.metabolomes.descriptor.observation.BlastParamType;
import uk.ac.ebi.metabolomes.core.gene.GeneProductCollection;
import uk.ac.ebi.metabolomes.core.gene.GeneProteinProduct;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;
import uk.ac.ebi.metabolomes.resource.BlastDatabase;
import uk.ac.ebi.metabolomes.resource.BlastMatrix;
import uk.ac.ebi.metabolomes.resource.BlastProgram;


/**
 * BlastHomologySearchFactory.java
 *
 *
 * @author johnmay
 * @date Apr 27, 2011
 */
public class BlastHomologySearchFactory {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      BlastHomologySearchFactory.class);
    private static Integer maxSequenceCount = 60;


    /**
     * Builds several blastp searches (number depends on maxSequenceCount)
     * @param products
     * @return
     */
    public static BlastHomologySearch[] getBlastPonSwissProtTasks(GeneProteinProduct[] products,
                                                                  BlastMatrix matrix,
                                                                  String evalue,
                                                                  Integer cpus,
                                                                  JobParameters params) {

        evalue = evalue.isEmpty() ? "1" : evalue;

        // always need one tasks
        int taskCount = 1 + (products.length / maxSequenceCount);

        BlastHomologySearch[] searches = new BlastHomologySearch[taskCount];

        int sequenceIndexStart = 0;
        int sequenceIndexEnd = 0;

        for( int i = 0 ; i < searches.length ; i++ ) {


            GeneProductCollection sequenceCollection = new GeneProductCollection();

            while( sequenceIndexEnd - sequenceIndexStart < maxSequenceCount && sequenceIndexEnd <
                                                                               products.length ) {
                GeneProteinProduct product = products[sequenceIndexEnd++];
                sequenceCollection.addProduct(product);
            }
            sequenceIndexStart = sequenceIndexEnd;


            // set the params
            params.put(BlastParamType.EXPECTED_VALUE_THRESHOLD, evalue);
            params.put(BlastParamType.MATRIX, matrix);
            params.put(BlastParamType.GENE_PRODUCT_COLLECTION, sequenceCollection);
            params.put(BlastParamType.NUMBER_CPU, cpus);
            params.put(BlastParamType.PROGRAM, BlastProgram.BLASTP);
            params.put(BlastParamType.DATABASE, BlastDatabase.SWISSPROT);

            searches[i] = new BlastHomologySearch(params);
            searches[i].prerun();
        }
        return searches;
    }


    /**
     * Set the max number of sequences
     * @param maxSequenceCount
     */
    public static void setMaxSequences(Integer maxSequenceCount) {
        BlastHomologySearchFactory.maxSequenceCount = maxSequenceCount;

    }


}

