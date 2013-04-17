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

import org.junit.Assert;
import org.junit.Test;

/**
 * @author John May
 */
public class NoteTest {

    @Test
    public void testGetInstance() throws Exception {

        Note instance = new Note().getInstance("this is a note");

        Assert.assertNotNull("new instance was null",
                instance);
        Assert.assertTrue(   "value was not passed",
                             instance.getValue().equals("this is a note"));
    }

    @Test
    public void testNewInstance() throws Exception {

        Assert.assertNotNull("new instance was null",
                new Note().newInstance());

    }

    @Test
    public void testSetGetValue() throws Exception {

        Note note = new Note();

        Assert.assertNotNull("null value", note.getValue());
        Assert.assertTrue(   "expected empty note", note.getValue().isEmpty());

        note.setValue("new value");
        Assert.assertEquals("new value", note.getValue());


    }


    @Test
    public void testEquals() throws Exception {

        Note n1 = new Note("one");
        Note n2 = new Note("two");
        Note n3 = new Note("one");

        Assert.assertNotSame("notes 1 and 2 should not be equal", n1, n2);
        Assert.assertEquals("notes 1 and 3 should be equal", n1, n3);



    }

    @Test
    public void testHashCode() throws Exception {

        Note n1 = new Note("one");
        Note n2 = new Note("two");
        Note n3 = new Note("one");

        Assert.assertNotSame("notes 1 and 2 hashCode should not be equal", n1.hashCode(), n2.hashCode());
        Assert.assertEquals("notes 1 and 3 hashCode should be equal", n1.hashCode(), n3.hashCode());

    }

    @Test
    public void testToString() throws Exception {
        Note note = new Note("I note, therefore I am");
        Assert.assertEquals("I note, therefore I am", note.toString());
    }

    @Test
    public void testGetShortDescription() throws Exception {
        Assert.assertEquals("Note", new Note().getShortDescription());
    }

    @Test
    public void testGetBrief() throws Exception {
        Assert.assertEquals("Note", new Note().getBrief());
    }

    @Test
    public void testGetLongDescription() throws Exception {
        Assert.assertEquals("A free text note on a metabolic entity", new Note().getLongDescription());
    }

    @Test
    public void testGetDescription() throws Exception {
        Assert.assertEquals("A free text note on a metabolic entity", new Note().getDescription());
    }
}
