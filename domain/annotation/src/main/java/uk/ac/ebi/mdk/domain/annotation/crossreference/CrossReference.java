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
import uk.ac.ebi.mdk.domain.DefaultLoader;
import uk.ac.ebi.mdk.domain.MetaInfo;
import uk.ac.ebi.mdk.domain.annotation.ObservationBasedAnnotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractValueAnnotation;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Description;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 *          CrossReference – 2011.09.14 <br>
 *          Base class for cross references annotations
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@Context
@Brief("Cross-reference")
@Description("A crossreference to an alternative identifier")
public class CrossReference<E extends Identifier, O extends Observation>
        extends AbstractValueAnnotation<E>
        implements ObservationBasedAnnotation<O> {

    private static final Logger LOGGER = Logger.getLogger(CrossReference.class);


    private static MetaInfo metaInfo = DefaultLoader.getInstance().getMetaInfo(
            CrossReference.class);

    private List<O> observations = new ArrayList<O>();


    public CrossReference() {
    }


    public CrossReference(E identifier) {
        super(identifier);
    }

    public static <I extends Identifier> CrossReference<I,Observation> create(I identifier){
        return new CrossReference<I, Observation>(identifier);
    }


    public E getIdentifier() {
        return getValue();
    }


    public void setIdentifier(E identifier) {
        setValue(identifier);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return getValue() != null ? getValue().getShortDescription() : metaInfo.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return getValue() != null ? getValue().getLongDescription() : metaInfo.description;
    }


    /**
     * @inheritDoc
     */
    public CrossReference newInstance() {
        return new CrossReference();
    }


    /**
     * @inheritDoc
     */
    public Collection<O> getObservations() {
        return Collections.unmodifiableList(observations);
    }


    /**
     * @inheritDoc
     */
    public boolean addObservation(O observation) {
        return observations.add(observation);
    }


    /**
     * @inheritDoc
     */
    public boolean addObservations(Collection<O> observations) {
        return this.observations.addAll(observations);
    }


    /**
     * @inheritDoc
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        throw new UnsupportedOperationException();
    }


    /**
     * @inheritDoc
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        throw new UnsupportedOperationException();
    }



}
