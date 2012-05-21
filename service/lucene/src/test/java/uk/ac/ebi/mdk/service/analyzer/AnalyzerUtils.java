package uk.ac.ebi.mdk.service.analyzer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * AnalyzerUtils - 02.03.2012 <br/>
 * <p/>
 * Class allows to 'look inside' a lucene analyser. Code is a direct implementation
 * from the book 'Lucene in Action, Erik Hatcher and Otis Gospodnetić'.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AnalyzerUtils {

    private static final Logger LOGGER = Logger.getLogger(AnalyzerUtils.class);

    public static AttributeSource[] tokensFromAnalysis(Analyzer analyzer,
                                                       String text) throws IOException {
        TokenStream stream = //1
                analyzer.tokenStream("contents", new StringReader(text)); //1
        ArrayList<AttributeSource> tokenList = new ArrayList<AttributeSource>();
        while (true) {
            if (!stream.incrementToken())
                break;
            tokenList.add(stream.cloneAttributes());
        }
        return (AttributeSource[]) tokenList.toArray(new AttributeSource[0]);
    }

    public static void displayTokens(Analyzer analyzer,
                                     String text) throws IOException {
        AttributeSource[] tokens = tokensFromAnalysis(analyzer, text);
        for (int i = 0; i < tokens.length; i++) {
            AttributeSource token = tokens[i];
            TermAttribute term = (TermAttribute) token.addAttribute(TermAttribute.class);
            System.out.print("[" + term.term() + "] "); //2
        }
    }

}
