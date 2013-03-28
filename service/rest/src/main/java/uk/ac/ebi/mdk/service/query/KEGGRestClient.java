/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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
import java.io.BufferedReader;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundEntry;
import uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundField;
import uk.ac.ebi.mdk.io.text.kegg.KEGGCompoundParser;
import uk.ac.ebi.mdk.service.query.data.MolecularFormulaService;
import uk.ac.ebi.mdk.service.query.name.NameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.name.SynonymService;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;

/**
 * A client for the KEGG REST API.
 *
 * @author John May
 */
public class KEGGRestClient
        extends AbstractRestClient<KEGGCompoundIdentifier>
        implements StructureService<KEGGCompoundIdentifier>,
        MolecularFormulaService<KEGGCompoundIdentifier>,
        PreferredNameService<KEGGCompoundIdentifier>,
        NameService<KEGGCompoundIdentifier>,
        SynonymService<KEGGCompoundIdentifier> {

    private final IChemObjectBuilder BUILDER = SilentChemObjectBuilder
            .getInstance();

    public KEGGRestClient() {
        super(new KEGGCompoundIdentifier());
    }

    @Override
    public Collection<KEGGCompoundIdentifier> searchSynonyms(String name, boolean approximate) {

        if (approximate) {
            // log warning about not being supported
            Logger.getLogger(getClass())
                  .warn("KEGG Rest Client does not support approximate matching");
        }

        String address = "http://rest.kegg.jp/find/compound/" + name;

        return getIdentifiers(address);

    }

    @Override
    public Collection<String> getSynonyms(KEGGCompoundIdentifier identifier) {

        String accession = identifier.getAccession();
        String address = "http://rest.kegg.jp/get/cpd:" + accession;

        Collection<String> names = new ArrayList<String>(10);

        InputStream in = null;

        try {
            in = getContent(address);

            KEGGCompoundEntry entry = KEGGCompoundParser
                    .load(in, KEGGCompoundField.NAME);

            for (String name : entry.get(KEGGCompoundField.NAME)) {
                names.add(name.replaceAll(";", "").trim());
            }

        } catch (IOException ex) {
            Logger.getLogger(getClass())
                  .error("unable to load entry: " + address
                                 + " reason: " + ex
                          .getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // can't do anything
            }
        }

        return names;
    }

    /**
     * Retrieve the MDL Mol file for a given KEGG Compound Identifier. If no
     * compound could be found an empty string is returned.
     *
     * @param identifier an identifier
     * @return MDL Mol file as a string
     */
    public String getMDLMol(KEGGCompoundIdentifier identifier) {

        String accession = identifier.getAccession();
        String address = "http://rest.kegg.jp/get/cpd:" + accession + "/mol";

        BufferedReader reader = null;
        try {
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(getContent(address)));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        } catch (IOException e) {
            Logger.getLogger(getClass())
                  .error("unable to load mol from: " + address + " - " + e
                          .getMessage());
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return "";
    }

    @Override
    public IAtomContainer getStructure(KEGGCompoundIdentifier identifier) {

        String accession = identifier.getAccession();
        String address = "http://rest.kegg.jp/get/cpd:" + accession + "/mol";

        MDLV2000Reader reader = null;
        try {
            reader = new MDLV2000Reader(getContent(address));
            IAtomContainer molecule = reader
                    .read(BUILDER.newInstance(IAtomContainer.class));
            reader.close();
            return molecule;
        } catch (IOException e) {
            Logger.getLogger(getClass())
                  .error("unable to load mol from: " + address + " - " + e
                          .getMessage());
        } catch (CDKException e) {
            Logger.getLogger(getClass())
                  .error("unable to load mol from: " + address + " - " + e
                          .getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        // empty atom container returned
        return BUILDER.newInstance(IAtomContainer.class, 0, 0, 0, 0);
    }

    @Override
    public Collection<KEGGCompoundIdentifier> searchPreferredName(String name, boolean approximate) {
        // a bit false bit the we can't specify the level of a name with kegg rest service
        return searchSynonyms(name, approximate);
    }

    @Override
    public String getPreferredName(KEGGCompoundIdentifier identifier) {
        // by convention the first name is the preferred name
        Collection<String> names = getNames(identifier);
        return names.isEmpty() ? "" : names.iterator().next();
    }

    @Override
    public Collection<KEGGCompoundIdentifier> searchMolecularFormula(String formula, boolean approximate) {

        if (approximate) {
            // log warning about not being supported
            Logger.getLogger(getClass())
                  .warn("KEGG Rest Client does not support approximate matching");
        }


        String address = "http://rest.kegg.jp/find/compound/" + formula + "/formula";
        return getIdentifiers(address);
    }

    /**
     * Produces a list of KEGG Compound Identifiers that participate in the
     * reactions with the given EC number.
     *
     * @param ec the desired EC number to query.
     * @return a list of KEGG Compound Identifiers that participate in those
     *         reactions.
     */
    public Collection<KEGGCompoundIdentifier> getCompoundsForECNumber(ECNumber ec) {
        String address = "http://rest.kegg.jp/link/compound/enzyme:" + ec
                .getAccession();

        return getIdentifiers(address);
    }

    @Override
    public String getMolecularFormula(KEGGCompoundIdentifier identifier) {

        String accession = identifier.getAccession();
        String address = "http://rest.kegg.jp/get/cpd:" + accession;

        InputStream in = null;

        try {
            in = getContent(address);

            KEGGCompoundEntry entry = KEGGCompoundParser
                    .load(in, KEGGCompoundField.FORMULA);

            return entry.getFirst(KEGGCompoundField.FORMULA, "");

        } catch (IOException ex) {
            Logger.getLogger(getClass())
                  .error("unable to load entry: " + address
                                 + " reason: " + ex
                          .getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // can't do anything
            }
        }

        return "";
    }

    @Override
    public Collection<KEGGCompoundIdentifier> searchMolecularFormula(IMolecularFormula formula, boolean approximate) {
        return searchMolecularFormula(MolecularFormulaManipulator
                                              .getString(formula), approximate);
    }

    @Override
    public IMolecularFormula getIMolecularFormula(KEGGCompoundIdentifier identifier) {
        String formula = getMolecularFormula(identifier);
        return formula.isEmpty()
                ? BUILDER.newInstance(IMolecularFormula.class)
                : MolecularFormulaManipulator.getMolecularFormula(formula,
                BUILDER);
    }

    @Override
    public Collection<KEGGCompoundIdentifier> searchName(String name, boolean approximate) {
        // a bit false bit the we can't specify the level of a name with kegg rest service
        return searchSynonyms(name, approximate);
    }

    @Override
    public Collection<String> getNames(KEGGCompoundIdentifier identifier) {
        return getSynonyms(identifier);
    }

    /**
     * Simple wrapper for getting an input stream from an address.
     *
     * @param address resource address
     * @return the input stream for the given resource address
     * @throws IOException
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
    private List<KEGGCompoundIdentifier> getIdentifiers(String address) {
        List<KEGGCompoundIdentifier> identifiers = new ArrayList<KEGGCompoundIdentifier>();
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
                            .add(new KEGGCompoundIdentifier(row[0].substring(4)));
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
}
