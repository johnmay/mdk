package uk.ac.ebi.mdk.io;

import uk.ac.ebi.mdk.io.annotation.primitive.DoubleAnnotationWriterTest;
import uk.ac.ebi.mdk.io.annotation.primitive.StringAnnotationWriterTest;
import uk.ac.ebi.mdk.io.annotation.AtomContainerAnnotationWriter085Test;
import uk.ac.ebi.mdk.io.annotation.CrossReferenceWriterTest;
import uk.ac.ebi.mdk.io.annotation.GibbsEnergyWriterTest;

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
        new AtomContainerAnnotationWriter085Test().rewrite();
        new CrossReferenceWriterTest().rewrite();
        new GibbsEnergyWriterTest().rewrite();

    }

}
