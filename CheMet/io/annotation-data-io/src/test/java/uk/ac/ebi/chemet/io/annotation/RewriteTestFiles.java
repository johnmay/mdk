package uk.ac.ebi.chemet.io.annotation;

import uk.ac.ebi.chemet.io.annotation.base.DoubleAnnotationWriterTest;
import uk.ac.ebi.chemet.io.annotation.base.StringAnnotationWriterTest;
import uk.ac.ebi.chemet.io.annotation.chemical.AtomContainerAnnotationWriterTest;
import uk.ac.ebi.chemet.io.annotation.crossreference.CrossReferenceWriterTest;

import java.io.IOException;

/**
 * RewriteTestFiles - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class RewriteTestFiles {


    public static void main(String[] args) throws IOException {

        new StringAnnotationWriterTest().rewrite();
        new DoubleAnnotationWriterTest().rewrite();
        new AtomContainerAnnotationWriterTest().rewrite();
        new CrossReferenceWriterTest().rewrite();

    }

}
