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

package uk.ac.ebi.chemet.tools.annotation.parse;

import uk.ac.ebi.mdk.domain.annotation.primitive.DoubleAnnotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple function for creating double annotations.
 *
 * @author John May
 */
final class DoubleAnnotationParser
        extends AnnotationParser<DoubleAnnotation> {

    private final DoubleAnnotation template;

    private final Pattern DOUBLE_PATTERN = Pattern
            .compile("([-+]?[0-9]*\\\\.?[0-9]+([eE][-+]?[0-9]+)?)");

    /**
     * Parse for the given annotation.
     *
     * @param annotation an annotation
     */
    DoubleAnnotationParser(DoubleAnnotation annotation) {
        template = annotation;
    }

    /** @inheritDoc */
    @Override public DoubleAnnotation parse(String str) {
        if (str == null || str.isEmpty())
            return null;
        Matcher matcher = DOUBLE_PATTERN.matcher(str);
        if (matcher.find())
            return template.forValue(Double.parseDouble(matcher.group(1)));
        return null;
    }
}
