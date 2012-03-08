package uk.ac.ebi.chemet;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TaggedStreamTest - 08.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class TaggedStreamTest {

    private static final Logger LOGGER = Logger.getLogger(TaggedStreamTest.class);

    @Test
    public void testStream() throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TaggedOutputStream tagOut = new TaggedOutputStream(out);

        Class[] expected = new Class[]{Integer.class,
                                       Integer.class,
                                       Identifier.class,
                                       ChEBIIdentifier.class,
                                       ChEBIIdentifier.class};


        for (Class c : expected)
            tagOut.write(c);

        tagOut.close();

        TaggedInputStream in = new TaggedInputStream(new ByteArrayInputStream(out.toByteArray()));

        Class[] actual = new Class[expected.length];
        for (int i = 0; i < actual.length; i++)
            actual[i] = in.readTag();

        in.close();

        Assert.assertArrayEquals(expected, actual);

    }
}
