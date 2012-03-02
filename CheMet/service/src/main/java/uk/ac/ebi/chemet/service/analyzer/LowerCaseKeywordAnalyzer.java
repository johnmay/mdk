package uk.ac.ebi.chemet.service.analyzer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * LowerCaseKeywordAnalyzer - 24.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LowerCaseKeywordAnalyzer extends Analyzer {

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream stream = new KeywordTokenizer(reader);
        stream = new LowerCaseFilter(Version.LUCENE_34, stream);
        return stream;
    }

}