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

package uk.ac.ebi.chemet.tools.annotation;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.Note;

import java.util.regex.Pattern;

/**
 * @author John May
 */
public class BasicPatternMatcherTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullPattern() {

        BasicPatternMatcher<Note> visitor = new BasicPatternMatcher<Note>(Note.class,
                                                                      null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern_no_parentheses() {
        BasicPatternMatcher<Note> visitor = new BasicPatternMatcher<Note>(Note.class,
                                                                      Pattern.compile(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern_two_parentheses() {
        BasicPatternMatcher<Note> visitor = new BasicPatternMatcher<Note>(Note.class,
                                                                      Pattern.compile("(first) (second)"));
    }

    @Test
    public void testMatch_FullMatch() {

        BasicPatternMatcher<Note> visitor = new BasicPatternMatcher<Note>(Note.class,
                                                                      Pattern.compile("(a test)"));

        Note note = new Note("a test");
        Assert.assertEquals("a test", note.accept(visitor));

    }

    @Test
    public void testMatch_PartialMatch() {

        BasicPatternMatcher<Note> visitor = new BasicPatternMatcher<Note>(Note.class,
                                                                      Pattern.compile("a (test)"));

        Note note = new Note("a test");
        Assert.assertEquals("test", note.accept(visitor));

    }

    @Test
    public void testMatch_IncompleteMatch() {

        // this doesn't match the entire string so should not match
        BasicPatternMatcher<Note> visitor = new BasicPatternMatcher<Note>(Note.class,
                                                                      Pattern.compile("a (test)"));

        Note note = new Note("a test that should fail");
        Assert.assertEquals("", note.accept(visitor));

    }

    @Test(expected = NullPointerException.class)
    public void testNullAnnotation() {


        // so we override to force return of null
        Note node = new Note() {
            @Override
            public String getValue() {
                return null;
            }
        };
        BasicPatternMatcher<Note> visitor = new BasicPatternMatcher<Note>(node.getClass(),
                                                                      Pattern.compile("this (doesn't) matter"));


        node.accept(visitor);

    }

}
