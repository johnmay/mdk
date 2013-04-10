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

package uk.ac.ebi.mdk.service.index.name;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import uk.ac.ebi.mdk.service.BasicServiceLocation;
import uk.ac.ebi.mdk.service.analyzer.ChemicalNameAnalyzer;
import uk.ac.ebi.mdk.service.analyzer.LowerCaseKeywordAnalyzer;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.io.File;
import java.io.IOException;

/**
 * DefaultNameIndex - 29.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultNameIndex extends BasicServiceLocation implements LuceneIndex {

    private static final Logger LOGGER = Logger.getLogger(DefaultNameIndex.class);

    private Directory directory;
    private PerFieldAnalyzerWrapper analyzer;

    /**
     * @param name
     * @param path from service root
     */
    public DefaultNameIndex(String name, String path) {
        super(name, path);

        // by default we use the ChemicalNameAnalyzer
        analyzer = new PerFieldAnalyzerWrapper(new ChemicalNameAnalyzer());

        analyzer.addAnalyzer(QueryService.IDENTIFIER.field(), new LowerCaseKeywordAnalyzer());



    }

    /**
     * @param name
     * @param f location of the index
     */
    public DefaultNameIndex(String name, File f) {
        super(name, f);

        // by default we use the ChemicalNameAnalyzer
        analyzer = new PerFieldAnalyzerWrapper(new ChemicalNameAnalyzer());

        analyzer.addAnalyzer(QueryService.IDENTIFIER.field(), new LowerCaseKeywordAnalyzer());

    }



    @Override
    public PerFieldAnalyzerWrapper getAnalyzer(){
        return analyzer;
    }

    @Override
    public Directory getDirectory() throws IOException {
        if (directory == null)
            directory = new NIOFSDirectory(getLocation());
        return directory;
    }
}
