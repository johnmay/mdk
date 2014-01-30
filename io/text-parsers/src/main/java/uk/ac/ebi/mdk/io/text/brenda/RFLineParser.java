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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.mdk.io.text.brenda;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pmoreno
 */
public class RFLineParser extends LineParser{

    private String citationKey;
    private Pattern citationKeyPattern;
    private Pattern pubmedFieldPattern;
    private String pubmedId;
    private String title;

    public RFLineParser(BufferedReader reader) {
        this.reader = reader;
        this.citationKeyPattern = Pattern.compile("^RF\\t<(\\d+)>");
        this.pubmedFieldPattern = Pattern.compile("\\{Pubmed:(.*)\\}");
    }

    public String getCitationNumber(String line) {
        Matcher citationKeyMatcher = citationKeyPattern.matcher(line);
        if(citationKeyMatcher.find()) {
            return citationKeyMatcher.group(1);
        }
        return null;
    }

    @Override
    public String parse(String line) throws IOException {
        boolean pubmedFound=false;
        boolean titleFound=false;
        boolean keepParsing=true;

        boolean titleMarkerSeen=false;

        String titleBuffer="";
        while(keepParsing) {
            if(line.startsWith("RF\t")) {
                if(line.contains(".:")) {
                    titleMarkerSeen=true;
                    if(line.indexOf(".:")+2 < line.length())
                        titleBuffer+=line.substring(line.indexOf(".:"));
                }
            } else if(line.startsWith("\t")) {
                if(titleMarkerSeen) {
                    if(line.contains("{Pubmed:"))
                        titleBuffer+=line.substring(1,line.indexOf("{Pubmed"));
                    else
                        titleBuffer+=line;
                }


            }

			// brenda_download.txt has this.
			//  771166         Chem. Soc. (1979) 101, 2774-2776. {Pubmed:}
			// This makes pubmedId to be null and then uk.ac.ebi.mdk.io.text.brenda.Citations.equals()
            Matcher pubmedFieldMatcher = pubmedFieldPattern.matcher(line);
            if(!pubmedFound && pubmedFieldMatcher.find()) {
                this.pubmedId = pubmedFieldMatcher.group(1);
                this.title = titleBuffer;
                pubmedFound=true;
                titleFound=true;
            }

            line = reader.readLine();

            if(line.startsWith("RF\t") && pubmedFound && titleFound)
                return line;

            if(line.length()<2)
                return reader.readLine();
        }
        return null;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getTitle() {
        return title;
    }

    /**
     * @return the pubmedId
     */
    public String getPubmedId() {
        return pubmedId;
    }

}
