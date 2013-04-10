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

package uk.ac.ebi.mdk.service.loader.multiple;

import com.google.common.base.Joiner;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.KEGGDrugIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KeggGlycanIdentifier;
import uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundEntry;
import uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundField;
import uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundParser;
import uk.ac.ebi.mdk.service.index.crossreference.KEGGCompoundCrossReferenceIndex;
import uk.ac.ebi.mdk.service.index.data.KEGGCompoundDataIndex;
import uk.ac.ebi.mdk.service.index.name.KEGGCompoundNameIndex;
import uk.ac.ebi.mdk.service.loader.AbstractMultiIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.writer.DefaultCrossReferenceIndexWriter;
import uk.ac.ebi.mdk.service.loader.writer.DefaultDataIndexWriter;
import uk.ac.ebi.mdk.service.loader.writer.DefaultNameIndexWriter;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * KEGGCompoundLoader - 27.02.2012 <br/> <p/> Loads names and formula's from the
 * kegg/compound/compound file into lucene indexes
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundLoader
        extends AbstractMultiIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundLoader.class);

    private static final Pattern COMPOUND_IDENTIFIER = Pattern.compile("(C\\d{5})");
    private static final Pattern SAME_AS_REMARK = Pattern.compile("Same as:((?:\\s[D|G]\\d{5})+)");


    public KEGGCompoundLoader() {

        addIndex("kegg.names", new KEGGCompoundNameIndex());
        addIndex("kegg.data", new KEGGCompoundDataIndex());
        addIndex("kegg.xref", new KEGGCompoundCrossReferenceIndex());

        addRequiredResource("KEGG Compound",
                            "File with compound information (i.e. 'compound/compound') file",
                            ResourceFileLocation.class);

    }


    @Override
    public void update() throws IOException {

        ResourceFileLocation location = getLocation("KEGG Compound");
        DefaultDataIndexWriter data = new DefaultDataIndexWriter(getIndex("kegg.data"));
        DefaultNameIndexWriter name = new DefaultNameIndexWriter(getIndex("kegg.names"));
        DefaultCrossReferenceIndexWriter xref = new DefaultCrossReferenceIndexWriter(getIndex("kegg.xref"));

        try {
            KEGGCompoundParser parser = new KEGGCompoundParser(new InputStreamReader(location.open()),
                                                               KEGGCompoundField.FORMULA, KEGGCompoundField.NAME, KEGGCompoundField.ENTRY,
                                                               KEGGCompoundField.ENZYME, KEGGCompoundField.REMARK, KEGGCompoundField.DBLINKS);


            IdentifierFactory idFactory = DefaultIdentifierFactory.getInstance();

            long start = System.currentTimeMillis();
            KEGGCompoundEntry entry;
            int count = 0;
            while (!isCancelled() && (entry = parser.readNext()) != null) {

                String header = entry.get(KEGGCompoundField.ENTRY).toString();
                String[] names = Joiner.on("\n").join(entry.get(KEGGCompoundField.NAME)).split(";");
                String remark = entry.getFirst(KEGGCompoundField.REMARK, "");
                String dbLinks = Joiner.on("\n").join(entry.get(KEGGCompoundField.DBLINKS));

                String formula = entry.getFirst(KEGGCompoundField.FORMULA, null);

                if (header.contains("Obsolete")) {
                    continue;
                }

                // get identifier and write the data
                Matcher matcher = COMPOUND_IDENTIFIER.matcher(header);
                if (matcher.find()) {

                    String identifier = matcher.group(1);

                    name.write(identifier, Arrays.asList(names));
                    data.write(identifier, "", formula);

                    Matcher remarkMatcher = SAME_AS_REMARK.matcher(remark);

                    if (remarkMatcher.matches()) {
                        for (String accession : remarkMatcher.group(1).trim().split(" ")) {
                            char c = accession.charAt(0);
                            xref.write(header, c == 'G'
                                               ? new KeggGlycanIdentifier(accession)
                                               : c == 'D'
                                                 ? new KEGGDrugIdentifier(accession)
                                                 : null);
                        }
                    }

                    for (String dblink : dbLinks.split("\n")) {
                        String[] dbEntry = dblink.split(":");

                        if (idFactory.hasSynonym(dbEntry[0])) {
                            for (String accession : dbEntry[1].trim().split("\\s+")) {
                                xref.write(header,
                                           idFactory.ofSynonym(dbEntry[0], accession));
                            }

                        }

                    }


                }

                if (++count % 200 == 0)
                    fireProgressUpdate(location.progress());

            }
            long end = System.currentTimeMillis();

            System.out.println("Loaded KEGG Compound: " + (end - start) + " ms");

        } finally {

            name.close();
            data.close();
            xref.close();

            location.close();
        }

    }


    @Override
    public String getName() {
        return "KEGG Compound";
    }

}
