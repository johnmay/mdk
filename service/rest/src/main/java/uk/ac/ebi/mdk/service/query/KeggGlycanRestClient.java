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
package uk.ac.ebi.mdk.service.query;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.base.Joiner;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import uk.ac.ebi.mdk.domain.identifier.KeggGlycanIdentifier;
import uk.ac.ebi.mdk.io.text.attribute.AttributedEntry;
import uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundField;
import uk.ac.ebi.mdk.io.text.kegg.KeggFlatfile;
import uk.ac.ebi.mdk.service.query.name.NameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.name.SynonymService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A client for the KEGG REST API.
 *
 * @author John May
 */
public class KeggGlycanRestClient
        extends AbstractRestClient<KeggGlycanIdentifier>
        implements PreferredNameService<KeggGlycanIdentifier>,
                   NameService<KeggGlycanIdentifier>,
                   SynonymService<KeggGlycanIdentifier> {

    private final IChemObjectBuilder BUILDER = SilentChemObjectBuilder
            .getInstance();

    public KeggGlycanRestClient() {
        super(new KeggGlycanIdentifier());
    }

    @Override
    public Collection<KeggGlycanIdentifier> searchSynonyms(String name, boolean approximate) {

        if (approximate) {
            // log warning about not being supported
            Logger.getLogger(getClass())
                  .warn("KEGG Rest Client does not support approximate matching");
        }

        String address = "http://rest.kegg.jp/find/glycan/" + name;

        return getIdentifiers(address);

    }

    @Override
    public Collection<String> getSynonyms(KeggGlycanIdentifier identifier) {

        String accession = identifier.getAccession();
        String address = "http://rest.kegg.jp/get/gl:" + accession;

        Collection<String> names = new ArrayList<String>(10);

        try {
            AttributedEntry<KEGGCompoundField, String> entry = KeggFlatfile
                    .compound(address);

            for (String name : entry.get(KEGGCompoundField.NAME)) {
                names.add(name.replaceAll(";", "").trim());
            }

        } catch (IOException ex) {
            Logger.getLogger(getClass())
                  .error("unable to load entry: " + address
                                 + " reason: " + ex
                          .getMessage());
        }

        return names;
    }


    @Override
    public Collection<KeggGlycanIdentifier> searchPreferredName(String name, boolean approximate) {
        // a bit false bit the we can't specify the level of a name with kegg rest service
        return searchSynonyms(name, approximate);
    }

    @Override
    public String getPreferredName(KeggGlycanIdentifier identifier) {
        // by convention the first name is the preferred name
        Collection<String> names = getNames(identifier);
        String name = names.isEmpty() ? "" : names.iterator().next();
        return names.isEmpty() ? getComposition(identifier) : name;
    }

    public String getComposition(KeggGlycanIdentifier identifier) {
        String accession = identifier.getAccession();
        String address = "http://rest.kegg.jp/get/gl:" + accession;

        try {
            AttributedEntry<KEGGCompoundField, String> entry = KeggFlatfile
                    .compound(address);

            return Joiner.on(", ").join(entry.get(KEGGCompoundField.COMPOSITION));

        } catch (IOException ex) {
            Logger.getLogger(getClass())
                  .error("unable to load entry: " + address
                                 + " reason: " + ex
                          .getMessage());
        }

        return "";
    }


    @Override
    public Collection<KeggGlycanIdentifier> searchName(String name, boolean approximate) {
        // a bit false bit the we can't specify the level of a name with kegg rest service
        return searchSynonyms(name, approximate);
    }

    @Override
    public Collection<String> getNames(KeggGlycanIdentifier identifier) {
        return getSynonyms(identifier);
    }

    /**
     * Simple wrapper for getting an input stream from an address.
     *
     * @param address resource address
     * @return the input stream for the given resource address
     * @throws java.io.IOException
     */
    private InputStream getContent(String address) throws IOException {
        URLConnection connection = new URL(address).openConnection();
        InputStream in = connection.getInputStream();
        return in;
    }

    /**
     * Helper which loads tsv results into compound identifiers
     *
     * @param address resource address of search
     * @return list of identifiers (empty if error)
     */
    private List<KeggGlycanIdentifier> getIdentifiers(String address) {
        List<KeggGlycanIdentifier> identifiers = new ArrayList<KeggGlycanIdentifier>();
        InputStream in = null;

        try {
            in = getContent(address);
            CSVReader reader = new CSVReader(new InputStreamReader(getContent(address)),
                                             '\t', '\0');
            String[] row;

            while ((row = reader.readNext()) != null) {
                // watch out for invalid entries
                if (row[0].length() > 4) {
                    identifiers
                            .add(new KeggGlycanIdentifier(row[0].substring(4)));
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(getClass())
                  .error("unable to load entry: " + address);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // can't do anything
            }
        }
        return identifiers;
    }


    @Override public boolean startup() {
        return super.startup() && reachable("http://www.kegg.jp/");
    }
}
