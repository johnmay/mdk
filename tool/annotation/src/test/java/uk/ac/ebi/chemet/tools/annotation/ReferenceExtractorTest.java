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

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.annotation.AuthorAnnotation;
import uk.ac.ebi.mdk.domain.annotation.Charge;
import uk.ac.ebi.mdk.domain.annotation.InChI;
import uk.ac.ebi.mdk.domain.annotation.MolecularFormula;
import uk.ac.ebi.mdk.domain.annotation.Note;
import uk.ac.ebi.mdk.domain.annotation.SMILES;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John May
 */
public class ReferenceExtractorTest {

    private ReferenceExtractor<Note> extractor = new ReferenceExtractor<Note>(Note.class);

    @Test
    public void testVisit() throws Exception {

        Note note = new Note("ChEBI: 12");
        Assert.assertEquals(ChEBIIdentifier.class, note.accept(extractor).getClass());

    }

    @Test
    public void testVisit_noId() throws Exception {

        Note note = new Note("ERROR!: 12");
        Assert.assertEquals(IdentifierFactory.EMPTY_IDENTIFIER, note.accept(extractor));

    }

    @Test
    public void testVisitMany() throws Exception {

        List<Annotation> notes = getNoteList(1000000);

        long start = System.currentTimeMillis();
        for (Annotation note : notes) {
            Identifier identifier = note.accept(extractor);
            if(identifier != IdentifierFactory.EMPTY_IDENTIFIER){
                // add to entity
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);

    }


    public static List<Annotation> getNoteList(int n) {
        List<Annotation> notes = new ArrayList<Annotation>(n);
        for (int i = 0; i < n; i++) {
            int annotation = (int)(Math.random() * 10);
            int db = (int)(Math.random() * 5);
            if(annotation < 3 )
                notes.add(new Note(db < 3 ? "ChEBI: " : "Nope: " + i));
            else if(annotation == 3)
                notes.add(new CrossReference());
            else if(annotation == 4)
                notes.add(new AuthorAnnotation());
            else if(annotation == 5)
                notes.add(new AtomContainerAnnotation());
            else if(annotation == 6)
                notes.add(new MolecularFormula());
            else if(annotation == 7)
                notes.add(new Charge());
            else if(annotation == 8)
                notes.add(new InChI());
            else if(annotation == 9)
                notes.add(new SMILES());

        }
        return notes;
    }

}
