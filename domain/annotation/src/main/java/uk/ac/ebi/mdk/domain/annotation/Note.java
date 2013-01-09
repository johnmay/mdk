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

import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractStringAnnotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.lang.annotation.Description;

/**
 * A simple string annotation that indicate a note on a given entity. This
 * annotation is similar to {@link AuthorAnnotation} but does not store any
 * information about who created the note. Normally this annotation should be
 * used when importing annotations from external sources and no author is
 * provided.
 *
 * @author John May
 * @see AuthorAnnotation
 */
@Context
@Brief("Note")
@Description("A free text note on a metabolic entity")
public class Note extends AbstractStringAnnotation {

    /**
     * Default constructor - builds a note with empty content.
     */
    public Note() {
    }

    /**
     * Construct a node with a provided value
     * @param content the content of the note
     */
    public Note(String content) {
        super(content);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Note getInstance(String value) {
        return new Note(value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Note newInstance() {
        return new Note();
    }
}
