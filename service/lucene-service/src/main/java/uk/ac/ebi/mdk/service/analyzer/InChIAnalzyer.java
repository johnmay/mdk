package uk.ac.ebi.mdk.service.analyzer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.PatternAnalyzer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

/**
 * InChIAnalzyer - 05.03.2012 <br/>
 * <p/>
 * Simple analyzer that will split up an InChI on '/'
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public final class InChIAnalzyer extends Analyzer {

    private static final Logger LOGGER = Logger.getLogger(InChIAnalzyer.class);

    private static final Pattern PATTERN = Pattern.compile("(/)");
    private static final PatternAnalyzer analyzer = new PatternAnalyzer(Version.LUCENE_34, PATTERN, true, null);


    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return analyzer.tokenStream(fieldName, reader);
    }

    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
        return analyzer.reusableTokenStream(fieldName, reader);
    }
}
