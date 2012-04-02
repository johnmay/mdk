package uk.ac.ebi.chemet.io.annotation;

import org.junit.Test;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.caf.utility.version.VersionMap;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;

import java.io.*;

import static org.junit.Assert.assertNotNull;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AnnotationDataInputStreamTest {

    @Test
    public void testVersionTagsExist() throws IOException {
        AnnotationDataInputStream manager = new AnnotationDataInputStream(new DataInputStream(new ByteArrayInputStream(new byte[200])),
                                                                            new Version("1.0"));
        for(VersionMap map : manager.getMarshals().values()){
            for(Object o : map.values()){
                assertNotNull(o.getClass().getAnnotation(CompatibleSince.class));
            }
        }

    }

}
