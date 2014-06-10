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

import uk.ac.ebi.mdk.domain.DefaultLoader;
import uk.ac.ebi.mdk.domain.MetaInfo;
import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractDoubleAnnotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.DoubleAnnotation;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Description;

@Context(Metabolite.class)
@Brief("Natural molecular weight")
@Description("The average mass of a compound, assuming natural abundance of isotopes.")
public class NaturalMass extends AbstractDoubleAnnotation {

    private static final MetaInfo metaInfo = DefaultLoader.getInstance().getMetaInfo(NaturalMass.class);

    public NaturalMass() {
    }

    public NaturalMass(double exactMass) {
        super.setValue(exactMass);
    }

    public NaturalMass newInstance() {
        return new NaturalMass();
    }

    @Override public DoubleAnnotation forValue(double v) {
        return getInstance(v);
    }

    public NaturalMass getInstance(double exactMass) {
        return new NaturalMass(exactMass);
    }


    @Override
    public String getShortDescription() {
        return metaInfo.brief;
    }


    @Override
    public String getLongDescription() {
        return metaInfo.description;
    }
}
