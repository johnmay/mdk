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
import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractStringAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.lang.annotation.Unique;


/**
 * The primary name for an annotation.
 *
 * @author johnmay
 */
@Context
@Brief("Primary Name")
@Description("An alternative name for this entity")
@Unique
public class PrimaryName extends AbstractStringAnnotation implements Name {

    public PrimaryName() {
    }

    public PrimaryName(String name) {
        super(name);
    }

    @Override public String name() {
        return getValue();
    }

    public PrimaryName newInstance() {
        return new PrimaryName();
    }


    public PrimaryName getInstance(String synonym) {
        return new PrimaryName(synonym);
    }
}
