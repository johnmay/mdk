/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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

package uk.ac.ebi.mdk.io.xml.uniprot.marshal;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashSet;
import java.util.Set;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Parses the organism host part of a UniProt XML entry, which is different to
 * the Organism section, but still has an NCBI Taxonomy identifier.
 *
 * @author pmoreno
 */
public final class UniProtHostOrganismMarshal implements UniProtXMLMarshal {

    private static final Logger LOGGER = Logger
            .getLogger(UniProtHostOrganismMarshal.class);

    private Set<String> ignored = new HashSet<String>();

    private Boolean skip;

    private UniProtCrossreferenceMarshal crossRefMarshal;

    /**
     * Create a new marshal for host organism tag.
     *
     * @param factory entity factory
     * @param skip    if true, it will skip adding the detected NCBI Taxonomy
     *                identifier to the Annotations of the protein.
     */
    @SuppressWarnings("unchecked")
    public UniProtHostOrganismMarshal(DefaultIdentifierFactory factory, Boolean skip) {
        this.skip = skip;
        this.crossRefMarshal = new UniProtCrossreferenceMarshal(factory, Taxonomy.class);
    }

    @Override
    public String getStartTag() {
        return "organismHost";
    }

    @Override
    public void marshal(XMLStreamReader reader, ProteinProduct product) throws
                                                                        XMLStreamException {

        while (reader.hasNext()) {
            switch (reader.next()) {
                case START_ELEMENT:
                    String startTag = reader.getLocalName();
                    if (crossRefMarshal.getStartTag().equals(startTag)) {
                        if (!skip) {
                            crossRefMarshal.marshal(reader, product);
                        }
                    }
                    break;
                case END_ELEMENT:
                    if (reader.getLocalName().equals(getStartTag())) {
                        return;
                    }
                    break;
            }
        }
    }

    public Set<String> getIgnored() {
        return ignored;
    }
}
