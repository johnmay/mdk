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

package uk.ac.ebi.mdk.domain.annotation.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.MetaInfo;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Reaction;
import uk.ac.ebi.mdk.domain.DefaultLoader;


/**
 *          EnzymeClassification – 2011.09.14 <br>
 *          Basic implementation of the {@see Classification} annotation for EC classification
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context({GeneProduct.class, Reaction.class})
@Brief("Enzyme Classification")
@Description("A cross-reference to the Enzyme Classification (E.C.) number")
public class EnzymeClassification<O extends Observation>
        extends Classification<ECNumber, O> {

    private static final Logger LOGGER = Logger.getLogger(EnzymeClassification.class);

    private static MetaInfo metaInfo = DefaultLoader.getInstance().getMetaInfo(
            EnzymeClassification.class);


    public EnzymeClassification() {
    }


    public EnzymeClassification(ECNumber ec) {
        super(ec);
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return metaInfo.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return metaInfo.description;
    }





    /**
     * @inheritDoc
     */
    @Override
    public EnzymeClassification newInstance() {
        return new EnzymeClassification();
    }
}
