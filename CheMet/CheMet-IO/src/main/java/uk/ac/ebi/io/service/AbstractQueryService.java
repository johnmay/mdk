/**
 * AbstractQueryService.java
 *
 * 2011.10.26
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
package uk.ac.ebi.io.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import uk.ac.ebi.interfaces.services.LuceneService;

/**
 *          AbstractQueryService - 2011.10.26 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AbstractQueryService {

    private Document[] documents;
    private IndexReader reader;
    private LuceneService service;
    private int max = Preferences.userNodeForPackage(AbstractQueryService.class).getInt("default.max.results", 100);

    public AbstractQueryService(LuceneService service) {
        this.service = service;
        try {
            reader = IndexReader.open(service.getDirectory(), true);
            documents = new Document[reader.numDocs()];
        } catch (CorruptIndexException ex) {
            Logger.getLogger(AbstractQueryService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AbstractQueryService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setMaxResults(int max) {
        this.max = max;
    }

    public int getMaxResults() {
        return max;
    }

    public byte[] getBinaryValue(ScoreDoc document, String field) throws CorruptIndexException, IOException {
        int index = document.doc;

        if (documents[index] == null) {
            documents[index] = reader.document(index);
        }

        return documents[index].getBinaryValue(field);
    }

    public String getValue(ScoreDoc document, String field) throws CorruptIndexException, IOException {

        int index = document.doc;

        if (documents[index] == null) {
            documents[index] = reader.document(index);
        }

        return documents[index].get(field);

    }

    public String[] getValues(ScoreDoc document, String field) throws CorruptIndexException, IOException {

        int index = document.doc;

        if (documents[index] == null) {
            documents[index] = reader.document(index);
        }

        return documents[index].getValues(field);

    }

    public Directory getDirectory() {
        return service.getDirectory();
    }

    public Analyzer getAnalyzer() {
        return service.getAnalzyer();
    }
    Pattern escape = Pattern.compile("[\\\\+\\-\\!\\(\\)\\:\\^\\]\\[\\{\\}\\~\\*\\?]");

    public String escape(String query) {
        return escape.matcher(query).replaceAll("\\\\$0");
    }
}
