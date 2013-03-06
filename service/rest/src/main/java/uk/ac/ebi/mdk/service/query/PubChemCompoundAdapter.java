package uk.ac.ebi.mdk.service.query;

import com.google.common.collect.Sets;
import com.google.common.io.LineReader;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.mdk.service.query.data.MolecularFormulaAccess;
import uk.ac.ebi.mdk.service.query.name.IUPACNameAccess;
import uk.ac.ebi.mdk.service.query.name.NameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameAccess;
import uk.ac.ebi.mdk.service.query.name.SynonymAccess;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * RESTful adapter for the PubChem-Compound REST web-service. Most search
 * functionality is not available and so these methods have been marked as
 * deprecated.
 *
 * @author John May
 */
public class PubChemCompoundAdapter
        extends AbstractRestClient<PubChemCompoundIdentifier>
        implements SynonymAccess<PubChemCompoundIdentifier>,
                   IUPACNameAccess<PubChemCompoundIdentifier>,
                   StructureService<PubChemCompoundIdentifier>,
                   PreferredNameAccess<PubChemCompoundIdentifier>,
                   NameService<PubChemCompoundIdentifier>,
                   MolecularFormulaAccess<PubChemCompoundIdentifier> {

    private static final Logger LOGGER = Logger
            .getLogger(PubChemCompoundAdapter.class);
    private static final Pattern numeric = Pattern.compile("\\d+");

    private static final String prefix = "http://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/cid/";

    /**
     * Create a new PubChem-Compound adapter for their rest service.
     */
    public PubChemCompoundAdapter() {
        super(new PubChemCompoundIdentifier());
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<PubChemCompoundIdentifier> searchName(String name, boolean approximate) {

        List<PubChemCompoundIdentifier> cids = new ArrayList<PubChemCompoundIdentifier>(5);
        InputStream in = null;
        try {
            String address = new URI("http",
                                     "pubchem.ncbi.nlm.nih.gov",
                                     "/rest/pug/compound/name/" + name + "/cids/TXT/",
                                     null).toASCIIString();
            in = new URL(address).openStream();
            LineReader lines = new LineReader(new InputStreamReader(in));
            String line;
            while ((line = lines.readLine()) != null) {
                if (numeric.matcher(line).matches()) {
                    cids.add(new PubChemCompoundIdentifier(line));
                }
            }
        } catch (IOException e) {
            LOGGER.error("could not complete search for " + name, e);
        } catch (URISyntaxException e) {
            System.out.println(e);
            LOGGER.error("could not encode URI for " + name, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
        return cids;
    }

    /**
     * Returns the synonyms of the specified id.
     *
     * @param identifier identifier to get the names for
     * @return
     */
    @Override
    public Collection<String> getSynonyms(PubChemCompoundIdentifier identifier) {
        List<String> names = new ArrayList<String>(getNames(identifier));
        return names.size() < 2 ? Collections.<String>emptyList()
                                : names.subList(1, names.size());
    }


    /**
     * Returns the IUPAC name which is used as the preferred name by PubChem.
     *
     * @param identifier a service specific identifier to retrieve the preferred
     *                   name for
     * @return
     */
    @Override
    public String getPreferredName(PubChemCompoundIdentifier identifier) {
        Collection<String> names = getNames(identifier);
        return names.isEmpty() ? "" : names.iterator().next();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getNames(PubChemCompoundIdentifier identifier) {

        List<String> synonyms = new ArrayList<String>();

        String address = prefix + identifier.getAccession() + "/synonyms/TXT/";
        InputStream in = null;

        try {
            in = new URL(address).openStream();
            LineReader reader = new LineReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                synonyms.add(line);
            }
        } catch (IOException e) {
            LOGGER.error("could not open stream for " + identifier, e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                // ignore
            }
        }
        return synonyms;
    }

    private PubchemRecord record(String cid, String... labels) {
        String address = prefix + cid + "/XML/";
        try {
            PubchemRecord record = new PubchemRecord(new URL(address), labels);
            return record;
        } catch (MalformedURLException e) {
            LOGGER.error(address + " was malformed");
        }
        return new PubchemRecord();
    }

    @Override public String getIUPACName(PubChemCompoundIdentifier identifier) {
        Set<String> iupacs = record(identifier.getAccession(), "IUPAC Name")
                .values("IUPAC Name");
        return iupacs.isEmpty() ? "" : iupacs.iterator().next();
    }


    @Override
    public IAtomContainer getStructure(PubChemCompoundIdentifier identifier) {
        String address = prefix + identifier.getAccession() + "/SDF/";

        InputStream in = null;
        try {
            in = new URL(address).openStream();
            MDLV2000Reader mdl = new MDLV2000Reader(new InputStreamReader(in));
            IAtomContainer mol = mdl.read(new AtomContainer(0, 0, 0, 0));
            return mol;
        } catch (MalformedURLException e) {
            LOGGER.error(address, e);
        } catch (IOException e) {
            LOGGER.error(address, e);
        } catch (CDKException e) {
            LOGGER.error(address, e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }

        return new AtomContainer(0, 0, 0, 0);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getMolecularFormula(PubChemCompoundIdentifier identifier) {
        Set<String> formulas = record(identifier
                                              .getAccession(), "Molecular Formula")
                .values("Molecular Formula");
        return formulas.isEmpty() ? "" : formulas.iterator().next();
    }

    /**
     * @inheritDoc
     */
    @Override
    public IMolecularFormula getIMolecularFormula(PubChemCompoundIdentifier identifier) {
        String formula = getMolecularFormula(identifier);
        if (formula.isEmpty())
            return SilentChemObjectBuilder.getInstance()
                                          .newInstance(IMolecularFormula.class);
        return MolecularFormulaManipulator.getMolecularFormula(formula,
                                                               SilentChemObjectBuilder
                                                                       .getInstance());
    }

    /**
     * Quick and dirty parser for handling PubChem xml.
     */
    static class PubchemRecord {

        private final Map<String, Set<String>> values = new HashMap<String, Set<String>>();
        private final Set<String> labels;

        PubchemRecord() {
            this.labels = Collections.emptySet();
        }

        PubchemRecord(URL url, String... labels) {
            this(urlToDoc(url), labels);
        }

        PubchemRecord(Document document, String... labels) {

            this.labels = Sets.newHashSet(labels);

            for (String label : labels) {
                values.put(label, new TreeSet<String>());
            }

            NodeList infoData = document.getElementsByTagName("PC-InfoData");
            for (int i = 0; i < infoData.getLength(); i++) {
                Node node = infoData.item(i);
                String label = dataLabel(node);
                if (this.labels.contains(label)) {
                    values.get(label).add(dataValue(node));
                }
            }

        }

        private Set<String> values(String label) {
            return labels.contains(label) ? values.get(label)
                                          : Collections.<String>emptySet();
        }

        static String dataLabel(Node node) {
            // it's turtles all the way down...
            return node.getChildNodes().item(1).getChildNodes().item(1)
                       .getChildNodes().item(1).getTextContent();
        }

        static String dataValue(Node node) {
            return node.getChildNodes().item(3).getChildNodes().item(1)
                       .getTextContent();
        }


        static Document urlToDoc(URL url) {
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                                                                .newDocumentBuilder();
                InputStream in = null;
                // checked exceptions...F*******
                try {
                    in = url.openStream();
                    return builder.parse(in);
                } catch (IOException e) {
                    LOGGER.error("unable to load pubchem xml for " + url, e);
                } catch (SAXException e) {
                    LOGGER.error("unable to load pubchem xml for " + url, e);
                } finally {
                    try {
                        if (in != null)
                            in.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            } catch (ParserConfigurationException e) {
                System.err.println(e.getMessage());
            }
            return null;
        }

    }

}
