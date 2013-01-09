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

package uk.ac.ebi.mdk.domain.annotation;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractStringAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Reaction;

/**
 *          Subsystem – 2011.09.26 <br>
 *          Class holding the annotation of a gene locus
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context({Gene.class, GeneProduct.class, Reaction.class})
@Brief("Locus")
@Description("The gene locus/association")
public class Locus
        extends AbstractStringAnnotation {

    private static final Logger LOGGER = Logger.getLogger(Locus.class);

    public Locus() {
    }

    public Locus(String locus) {
        super(locus);
    }

    public Locus newInstance() {
        return new Locus();
    }

    public Locus getInstance(String locus) {
        return new Locus(locus);
    }

    /**
     * Determines whether the locus tag defines multiple loci (as a multimeric
     * protein)
     */
    public boolean containsMultiple() {
        return getValue().contains("+");
    }
}
