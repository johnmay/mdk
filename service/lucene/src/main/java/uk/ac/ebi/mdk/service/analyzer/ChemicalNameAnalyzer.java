/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk.service.analyzer;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.miscellaneous.PatternAnalyzer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

/**
 * ChemicalNameAnalyzer - 01.03.2012 <br/>
 * <p/>
 * From on http://stackoverflow.com/questions/3779411/lucene-wildcard-matching-fails-on-chemical-notations/5418881#5418881
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public final class ChemicalNameAnalyzer extends Analyzer {

    private static Version version = Version.LUCENE_34;
    private static Pattern pattern = compilePattern();

    private PatternAnalyzer analyzer;

    public ChemicalNameAnalyzer(){
        analyzer = new PatternAnalyzer(Version.LUCENE_34, pattern,  true, null);
    }

    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        return analyzer.tokenStream(fieldName, reader);
    }

    @Override
    public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException {
        return analyzer.reusableTokenStream(fieldName, reader);
    }

    public static Pattern compilePattern() {
        StringBuilder sb =  new StringBuilder();
        sb.append("(\\s+)|"); // white-space
        sb.append("(-{0,1}\\(-{0,1})");//Matches an optional dash followed by an opening round bracket followed by an optional dash
        sb.append("|");//"OR" (regex alternation)
        sb.append("(-{0,1}\\)-{0,1})");
        sb.append("|");//"OR" (regex alternation)
        sb.append("((?<=([a-zA-Z]{2,}))-(?=([^a-zA-Z])))");//Matches a dash ("-") preceded by two or more letters and succeeded by a non-letter
        return Pattern.compile(sb.toString());
    }
}