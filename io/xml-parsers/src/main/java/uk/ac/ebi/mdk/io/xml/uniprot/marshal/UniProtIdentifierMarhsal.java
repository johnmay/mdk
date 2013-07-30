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

package uk.ac.ebi.mdk.io.xml.uniprot.marshal;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.identifier.SwissProtIdentifier;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.identifier.UniProtIdentifier;
import uk.ac.ebi.mdk.domain.observation.Observation;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/**
 * Adds an identifier to the protein product
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtIdentifierMarhsal implements UniProtXMLMarshal {

    private static final Logger LOGGER = Logger.getLogger(UniProtIdentifierMarhsal.class);

    @Override
    public String getStartTag() {
        return "accession";
    }

    @Override
    public void marshal(XMLStreamReader reader, ProteinProduct product) throws XMLStreamException {
        if( reader.next() == XMLEvent.CHARACTERS){
            if(product.getIdentifier()==null)
                product.setIdentifier(new SwissProtIdentifier(reader.getText()));
            else {
                /**
                 * this handles cases where the entry has multiple accessions. This also avoids rewriting the main
                 * accession by the following ones, which are normally secondary or obsolete.
                 */
                product.addAnnotation(
                        new CrossReference<UniProtIdentifier,Observation>(new SwissProtIdentifier(reader.getText())));
            }
        }
    }
}
