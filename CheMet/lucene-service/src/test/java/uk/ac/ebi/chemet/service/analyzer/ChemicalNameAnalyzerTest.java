package uk.ac.ebi.chemet.service.analyzer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

/**
 * ChemicalNameAnalyzerTest - 02.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChemicalNameAnalyzerTest {

    private static final Logger LOGGER = Logger.getLogger(ChemicalNameAnalyzerTest.class);

    private ChemicalNameAnalyzer analyzer = new ChemicalNameAnalyzer();

    @Test
    public void testAnalyzer() throws Exception {
        String[] names = new String[]{"(R)-Acetoin",
                "Coenzyme A",
                "D-glucose 6-phosphate",
                "phenyl lactate"};
        for (String name : names) {
            System.out.print(name + ": ");
            AnalyzerUtils.displayTokens(analyzer, name);
            System.out.println();
        }
    }


}
