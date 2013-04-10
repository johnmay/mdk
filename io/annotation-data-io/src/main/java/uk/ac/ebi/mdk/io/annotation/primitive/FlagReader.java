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

package uk.ac.ebi.mdk.io.annotation.primitive;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.annotation.AnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.Flag;
import uk.ac.ebi.mdk.io.AnnotationReader;

import java.io.IOException;

/**
 * @author John May
 */
@CompatibleSince("0.9")
public class FlagReader implements AnnotationReader<Flag> {

    private static final Logger LOGGER = Logger.getLogger(FlagReader.class);

    private static final AnnotationFactory ANNOTATION_FACTORY = DefaultAnnotationFactory.getInstance();

    private Class<? extends Flag> c;

    public FlagReader(Class<? extends Flag> c) {
        this.c = c;
    }

    @Override
    public Flag readAnnotation() throws IOException, ClassNotFoundException {
        return ANNOTATION_FACTORY.ofClass(c);
    }
}
