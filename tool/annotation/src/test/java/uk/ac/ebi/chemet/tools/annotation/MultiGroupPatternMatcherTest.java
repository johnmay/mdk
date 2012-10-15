/*
 * Copyright (c) 2012. John May <jwmay@sf.net>
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

package uk.ac.ebi.chemet.tools.annotation;

import junit.framework.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.AnnotationVisitor;
import uk.ac.ebi.mdk.domain.annotation.Note;

import java.util.List;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John May
 */
public class MultiGroupPatternMatcherTest {


    @Test(expected = IllegalArgumentException.class)
    public void testConstruction_nullClass() {
        new MultiGroupPatternMatcher(null, Pattern.compile(""), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruction_nullPattern() {
        new MultiGroupPatternMatcher<Note>(Note.class, null, 0);
    }

    @Test
    public void testConstruction_success() {
        new MultiGroupPatternMatcher<Note>(Note.class,
                                           Pattern.compile("(first)(second)"),
                                           2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruction_strangePattern() {
        new MultiGroupPatternMatcher<Note>(Note.class,
                                           Pattern.compile("(first)(second)|(third)"),
                                           2);
    }

    @Test
    public void testDefaultValue() {
        List<String> value = new MultiGroupPatternMatcher<Note>(Note.class,
                                                                Pattern.compile("(first)(second)"),
                                                                2).getValue();

        assertEquals(2, value.size());
        assertEquals("", value.get(0));
        assertEquals("", value.get(1));
    }

    @Test
    public void testVisit_NoMatch() {

        AnnotationVisitor<List<String>> visitor = new MultiGroupPatternMatcher<Note>(Note.class,
                                                                                     Pattern.compile("(a) simple (test)"),
                                                                                     2);

        Note note = new Note("not a simple test");
        List<String> value = note.accept(visitor);

        assertNotNull(value);
        assertEquals(2, value.size());
        assertEquals("", value.get(0));
        assertEquals("", value.get(1));

    }




}
