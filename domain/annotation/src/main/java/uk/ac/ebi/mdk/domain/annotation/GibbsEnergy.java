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

package uk.ac.ebi.mdk.domain.annotation;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractDoubleAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

/**
 * GibbsEnergy - 16.03.2012 <br/>
 * <p/>
 * Stores the Gibbs energy of a reaction
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Context(MetabolicReaction.class)
@Brief("Gibbs Energy (ΔG)")
@Description("Thermodynamic potential of the reaction")
public class GibbsEnergy
        extends AbstractDoubleAnnotation {

    private static final Logger LOGGER = Logger.getLogger(GibbsEnergy.class);

    private Double error = 0d;
    
    public GibbsEnergy() {
        super();
    }

    public GibbsEnergy(Double value, Double error) {
        super(value);
        this.error = error;
    }

    @Override
    public GibbsEnergy newInstance() {
        return new GibbsEnergy();
    }

    /**
     * Access the error value
     * @return
     */
    public Double getError(){
        return this.error;
    }

    /**
     * Set the error for this annotation
     * @param error
     */
    public void setError(Double error) {
        this.error = error;
    }

    /**
     * Displays the Gibbs Energy value and it's error value in brackets
     * @inheritDoc
     */
    @Override
    public String toString(){
        return getValue() + " \u00B1 " + getError();
    }
    
}


