/**
 * ChEBISDFFieldExtractor.java
 *
 * 2011.10.11
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * @name    ChEBISDFFieldExtractor
 * @date    2011.10.11
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class ChEBISDFFieldExtractor extends AbstractSDFFieldExtractor{
    
    private static final Logger LOGGER = Logger.getLogger(ChEBISDFFieldExtractor.class);    
    
    private boolean nextLineID=false;
    private boolean nextLineSecID=false;
    private boolean nextLineName=false;
    private boolean nextLineSyn=false;
    private boolean nextLineDBLink=false;

    public void feedLine(String line) {
        if(nextLineID) { 
            this.currentRecord.setId(line.replaceFirst("CHEBI:", ""));
            nextLineID=false;
        } else if(nextLineName) {
            this.currentRecord.setName(line);
            nextLineName=false;
        } else if(nextLineSecID) { // this is a multiline field
            if(line.startsWith("CHEBI:")) 
                this.currentRecord.addSecondaryID(line.replaceFirst("CHEBI:", ""));
            else
                nextLineSecID=false;
        } else if(nextLineSyn) {
            if(line.length()>1)
                this.currentRecord.addSynonym(line);
            else
                nextLineSyn=false;
        } else if(nextLineDBLink) {
            nextLineDBLink = processDBLinkLine(line);
        } else {
            
            if(line.startsWith("> <ChEBI ID>"))
                nextLineID=true;
            else if(line.startsWith("> <ChEBI Name>"))
                nextLineName=true;
            else if(line.startsWith("> <Secondary ChEBI ID>"))
                nextLineSecID=true;
            else if(line.startsWith("> <Synonyms>"))
                nextLineSyn=true;
            else if(dbLinkPattern.matcher(line).find())
                nextLineDBLink=true;
        }
    }

    public boolean proposeIdentifier() {
        return proposesID;
    }

    public String getProposedIdentifier() {
        proposesID = false;
        return proposedIdentifier;
    }

    private final Pattern dbLinkPattern = Pattern.compile("^> <(.+) Database Links>");
    private String nextDB;
    
    private boolean processDBLinkLine(String line) {
        Matcher dbLinkMatcher = dbLinkPattern.matcher(line);
        if(dbLinkMatcher.find()) {
            nextDB=transformDBName(dbLinkMatcher.group(1));
            return true;
        } else if(nextDB!=null) {
            this.currentRecord.addCrossReference(nextDB, line);
        }
        return false;
    }

    private String transformDBName(String dbName) {
        if(dbName.equals("KEGG COMPOUND"))
            return "LIGAND-CPD";
        return dbName;
    }
    
    
}
