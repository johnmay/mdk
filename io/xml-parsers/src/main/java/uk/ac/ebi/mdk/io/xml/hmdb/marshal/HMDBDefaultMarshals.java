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

package uk.ac.ebi.mdk.io.xml.hmdb.marshal;

import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChemSpiderIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGDrugIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KeggGlycanIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.mdk.io.xml.hmdb.HMDBMetabolite;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Enumeration of the core XML handlers for HMDB metabolite entry. This handles
 * load attributes like accession, name, cross-references into a HMDBMetabolite
 * entry.
 *
 * <blockquote><pre>
 *     String path = ...; // file system path
 *     File   file = new File(path);
 *
 *     // only load the common name
 *     HMDBMetabolite metabolite = HMDBParser.loadAll(file,
 *                                                    HMDBDefaultMarshals.COMMON_NAME);
 *     // only load the common name, iupac and synonyms
 *     HMDBMetabolite metabolite = HMDBParser.loadAll(file,
 *                                                    HMDBDefaultMarshals.COMMON_NAME,
 *                                                    HMDBDefaultMarshals.IUPAC_NAME,
 *                                                    HMDBDefaultMarshals.SYNONYMS);
 *
 *     // if you load the accession make sure to include the secondary
 *     // accession also. these have the same start tag and so leads to
 *     // incorrect parsing if omitted
 *     HMDBMetabolite metabolite = HMDBParser.loadAll(file,
 *                                                    HMDBDefaultMarshals.ACCESSION,
 *                                                    HMDBDefaultMarshals.SECOUNDARY_ACCESSION,
 *                                                    HMDBDefaultMarshals.SECONDARY_ACCESSION);
 *
 *
 *     // load all default attributes
 *     HMDBMetabolite metabolite = HMDBParser.loadAll(file,
 *                                                    HMDBDefaultMarshals.values());
 *
 *
 * </pre></blockquote>
 *
 * @author John May
 * @see uk.ac.ebi.mdk.io.xml.hmdb.HMDBParser
 */
public enum HMDBDefaultMarshals implements HMDBXMLMarshal {

    /**
     * load the common name of a metabolite
     */
    COMMON_NAME("common_name", "setCommonName"),

    /**
     * load the accession of a metabolite. <b>Important:</b> requires {@link
     * #SECONDARY_ACCESSION} and {@link #SECOUNDARY_ACCESSION} to function
     * correctly.
     */
    ACCESSION("accession", "setAccession"),

    /**
     * load the synonyms of a metabolite
     */
    SYNONYMS("synonym", "addSynonym"),

    /**
     * load the IUPAC systematic name
     */
    IUPAC_NAME("iupac_name", "setIUPACName"),

    /**
     * load the formal charge
     */
    CHARGE("formal_charge", "setFormalCharge"),

    /**
     * load the chemical formula
     */
    FORMULA("chemical_formula", "setMolecularFormula"),

    /**
     * load the InChI line notation
     */
    INCHI("inchi", "setInChI"),

    /**
     * load the SMILES line notation
     */
    SMILES("smiles", "setSMILES"),

    /**
     * load ChEBI cross-references
     */
    CHEBI("chebi_id", new IdentifierMarshal(new ChEBIIdentifier())),

    /**
     * load PubChem-Compound cross-references
     */
    PUBCHEM_COMPOUND("pubchem_compound_id", new IdentifierMarshal(new PubChemCompoundIdentifier())),

    /**
     * load KEGG Compound cross-references
     */
    KEGG_COMPOUND("kegg_id", new IdentifierMarshal(new KEGGCompoundIdentifier())),

    /**
     * load KEGG Glycan cross-references
     */
    KEGG_GLYCAN("kegg_id", new IdentifierMarshal(new KeggGlycanIdentifier())),

    /**
     * load KEGG Drug cross-references
     */
    KEGG_DRUG("kegg_id", new IdentifierMarshal(new KEGGDrugIdentifier())),

    /**
     * load ChemSpider cross-references
     */
    CHEMSPIDER("chemspider_id", new IdentifierMarshal(new ChemSpiderIdentifier())),

    /**
     * load BioCyc cross-references, no indication is given of which pathway
     * genome database (PDGB) is used
     */
    BIOCYC("biocyc_id", new IdentifierMarshal(new BioCycChemicalIdentifier())),

    /**
     * The HMDB currently contains a typo. Both this marshal and and {@link
     * #SECONDARY_ACCESSION} should be included when parsing.
     */
    SECOUNDARY_ACCESSION("secoundary_accessions") {
        @Override
        public void marshal(XMLStreamReader2 xmlr,
                            HMDBMetabolite metabolite) throws
                                                       XMLStreamException {

            // stream until </secondary_accession>
            while (xmlr.hasNext()) {
                int event = xmlr.next();

                // matches end of element: </secondary_accession>
                if (event == XMLEvent.END_ELEMENT
                        && tag().equals(xmlr.getLocalName())) {
                    return;
                }

                // matches first start of element: <accession>...</accession>
                if (event == XMLEvent.START_ELEMENT
                        && "accession".equals(xmlr.getLocalName())) {

                    // add accession to metabolite
                    if (xmlr.next() == XMLEvent.CHARACTERS)
                        metabolite.addSecondaryAccession(xmlr.getText());

                }
            }
        }
    },


    /**
     * The HMDB currently contains a typo. Both {@link #SECOUNDARY_ACCESSION}
     * and this marshal should be included when parsing.
     */
    SECONDARY_ACCESSION("secondary_accessions") {
        @Override
        public void marshal(XMLStreamReader2 xmlr,
                            HMDBMetabolite metabolite) throws
                                                       XMLStreamException {
            // stream until </secondary_accession>
            while (xmlr.hasNext()) {
                final int event = xmlr.next();

                // matches end of element: </secondary_accession>
                if (event == XMLEvent.END_ELEMENT
                        && tag().equals(xmlr.getLocalName())) {
                    return;
                }

                // matches first start of element: <accession>...</accession>
                if (event == XMLEvent.START_ELEMENT
                        && "accession".equals(xmlr.getLocalName())) {

                    // add accession to metabolite
                    if (xmlr.next() == XMLEvent.CHARACTERS)
                        metabolite.addSecondaryAccession(xmlr.getText());

                }
            }

        }
    },;


    private final String tag;
    private final HMDBXMLMarshal delegate;

    private HMDBDefaultMarshals(String tag) {
        this.tag = tag;
        this.delegate = new HMDBXMLMarshal() {
            public String tag() {
                return "";
            }

            public void marshal(XMLStreamReader2 xmlr, HMDBMetabolite metabolite) {
            }
        };
    }

    private HMDBDefaultMarshals(String tag, String method) {
        this(tag, new SimpleReflectiveMarshal(method));
    }

    private HMDBDefaultMarshals(String tag, HMDBXMLMarshal delegate) {
        this.tag = tag;
        this.delegate = delegate;
    }

    /**
     * @inheritDoc
     */
    @Override public String tag() {
        return tag;
    }

    @Override
    public void marshal(XMLStreamReader2 xmlr, HMDBMetabolite metabolite) throws
                                                                          XMLStreamException {
        delegate.marshal(xmlr, metabolite);
    }


    private static class IdentifierMarshal implements HMDBXMLMarshal {

        private final Identifier template;

        private IdentifierMarshal(Identifier template) {
            this.template = template;
        }

        @Override public String tag() {
            throw new IllegalStateException("tag() should not be called");
        }

        @Override
        public void marshal(XMLStreamReader2 xmlr, HMDBMetabolite metabolite) throws
                                                                              XMLStreamException {
            if (xmlr.next() == XMLEvent.CHARACTERS) {

                // new id from the template
                Identifier id = template.newInstance();
                id.setAccession(xmlr.getText());

                // only add valid identifiers
                if (id.isValid())
                    metabolite.addCrossReference(id);
            }
        }
    }

    private static class SimpleReflectiveMarshal implements HMDBXMLMarshal {

        private final Method method;

        private SimpleReflectiveMarshal(String method) {
            try {
                this.method = HMDBMetabolite.class.getMethod(method, String.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
        }


        @Override public String tag() {
            throw new IllegalStateException("tag() should not be called");
        }

        @Override
        public void marshal(XMLStreamReader2 xmlr, HMDBMetabolite metabolite) throws
                                                                              XMLStreamException {

            if (xmlr.next() == XMLEvent.CHARACTERS) {
                try {
                    method.invoke(metabolite, xmlr.getText());
                } catch (IllegalAccessException e) {
                    Logger.getLogger(getClass()).error("no access (non-public) to method on HMDBMetabolite: " + e);
                } catch (InvocationTargetException e) {
                    Logger.getLogger(getClass()).error("unable to invoke method on HMDBMetabolite: " + e);
                }
            }
        }
    }

}
