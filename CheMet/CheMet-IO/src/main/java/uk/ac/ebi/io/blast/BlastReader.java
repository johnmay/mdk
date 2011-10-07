/**
 * BlastReader.java
 *
 * 2011.09.19
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
package uk.ac.ebi.io.blast;

import au.com.bytecode.opencsv.CSVReader;
import com.hp.hpl.jena.reasoner.IllegalParameterException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.core.gene.GeneProductCollection;
import uk.ac.ebi.observation.sequence.LocalAlignment;

/**
 *          BlastReader – 2011.09.19 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BlastReader {

    private static final Logger LOGGER = Logger.getLogger(BlastReader.class);
    public Collection<Integer> supportedFormats = new HashSet();
    private Map<String, Integer> columnMap = new HashMap();

    public BlastReader() {
        supportedFormats.add(7); // xml
        supportedFormats.add(8); // tsv

        int index = 0;
        for (String name : Arrays.asList("Query.ID", "Subject.ID", "Perc.Ident", "Aln.Len",
                "Mismatches", "Gap.Openings", "Q.start", "Q.end", "S.start",
                "S.end", "Expected", "Bit.Score")) {
            columnMap.put(name, index++);
        }
    }

    public void parse(File outputFile, Integer format) {

        if (supportedFormats.contains(format) == false) {
            throw new IllegalParameterException("Unsupported format " + format
                    + ". Currently supported:" + supportedFormats);
        } else {
        }

    }

    public void parseFromTSV(GeneProductCollection products, Reader reader, String version) throws IOException {

        CSVReader tsvReader = new CSVReader(reader, '\t', '\0');

        long start = System.currentTimeMillis();

        BLASTRowParser parser = ParserFactory.getInstance().getBLASTRowParser(version);
        String[] row;
        while ((row = tsvReader.readNext()) != null) {
            LocalAlignment alignment = parser.parse(row);
            // todo.. products.addObsevation(accession, alignment); // attaches to product of that accession
        }

        long end = System.currentTimeMillis();

        System.out.println("Completed " + (end - start) + " ms");


        tsvReader.close();

    }
}
