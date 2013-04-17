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
 * Parses the organism part of a UniProt XML entry. It is recommended to use
 * this jointly with {@link UniProtHostOrganismMarshal} to be able to avoid, or
 * handle, the addition of NCBI Taxonomy IDs of Host Organism dbReference
 * entries, which look the same to the Organism entries for the {@link
 * UniProtCrossreferenceMarshal}.
 *
 * @author pmoreno
 */
public final class UniProtOrganismMarshal implements UniProtXMLMarshal {

    private static final Logger LOGGER = Logger
            .getLogger(UniProtOrganismMarshal.class);

    private DefaultIdentifierFactory factory;
    private Set<String> ignored = new HashSet<String>();

    private UniProtCrossreferenceMarshal crossRefMarshal;

    /**
     * Create a new marshal for organism tag. The organism is loaded as a {@link
     * Taxonomy} id.
     *
     * @param factory entity factory
     */
    public UniProtOrganismMarshal(DefaultIdentifierFactory factory) {
        this.factory = factory;
        this.crossRefMarshal = new UniProtCrossreferenceMarshal(this.factory, Taxonomy.class);
    }

    @Override
    public String getStartTag() {
        return "organism";
    }

    @Override
    public void marshal(XMLStreamReader reader, ProteinProduct product) throws
                                                                        XMLStreamException {

        while (reader.hasNext()) {
            switch (reader.next()) {
                case START_ELEMENT:
                    String startTag = reader.getLocalName();
                    if (crossRefMarshal.getStartTag().equals(startTag)) {
                        crossRefMarshal.marshal(reader, product);
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
