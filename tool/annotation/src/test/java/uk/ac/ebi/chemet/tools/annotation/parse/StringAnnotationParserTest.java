package uk.ac.ebi.chemet.tools.annotation.parse;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.annotation.Note;
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

/** @author John May */
public class StringAnnotationParserTest {

    @Test
    public void testParse_Null() throws Exception {
        assertNull(new StringAnnotationParser(new Note()).parse(null));
    }

    @Test
    public void testParse_Empty() throws Exception {
        assertNull(new StringAnnotationParser(new Note()).parse(""));
    }

    @Test
    public void testParse() throws Exception {
        assertThat(new StringAnnotationParser(new Note()).parse("a note"),
                   is((StringAnnotation) new Note("a note")));
    }

    @Test
    public void testParse_multiple() throws Exception {
        AnnotationParser<StringAnnotation> parser = new StringAnnotationParser(new Note());
        StringAnnotation a = parser.parse("a note");
        StringAnnotation b = parser.parse("a note");
        assertThat(a,
                   is(not(sameInstance(b))));
    }
}
