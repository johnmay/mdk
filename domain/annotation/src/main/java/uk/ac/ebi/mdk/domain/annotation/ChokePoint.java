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

import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.lang.annotation.Unique;

import java.util.regex.Pattern;

@Unique
@Context(MetabolicReaction.class)
@Brief("Choke Point")
@Description("The reaction uniquely consumes or produces a metabolite.")
public class ChokePoint extends AbstractAnnotation implements Flag {

    private ChokePoint() {
    }

    private static class ChokePointHolder {
        private static ChokePoint INSTANCE = new ChokePoint();
    }

    public static ChokePoint getInstance() {
        return ChokePointHolder.INSTANCE;
    }

    @Override
    public boolean matches(AnnotatedEntity entity) {
        return false;
    }

    @Override
    public String toString() {
        return getShortDescription();
    }

    @Override
    public ChokePoint newInstance() {
        return getInstance();
    }
}
