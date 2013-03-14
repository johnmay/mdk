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
package uk.ac.ebi.mdk.io.xml.hmdb.marshal;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.io.xml.hmdb.HMDBMetabolite;

/**
 * @name HMDBBiofluidsMarshal
 * @date 2013.03.13
 * @version $Rev$ : Last Changed $Date$
 * @author Pablo Moreno <pablacious at users.sf.net>
 * @author $Author$ (this version)
 * @brief ...class description...
 *
 */
public class HMDBTissueMarshal implements HMDBXMLMarshal {

    @Override
    public String tag() {
        return "tissue_locations";
    }

    @Override
    public void marshal(XMLStreamReader2 xmlr, HMDBMetabolite metabolite) throws XMLStreamException {
        // stream until </tissue_locations>
        while (xmlr.hasNext()) {
            int event = xmlr.next();

            // matches end of element: </tissue_locations>
            if (event == XMLEvent.END_ELEMENT
                    && tag().equals(xmlr.getLocalName())) {
                return;
            }

            // matches first start of element: <tissue>...</tissue>
            if (event == XMLEvent.START_ELEMENT
                    && "tissue".equals(xmlr.getLocalName())) {

                // add accession to metabolite
                if (xmlr.next() == XMLEvent.CHARACTERS) {
                    metabolite.addBodyFluid(xmlr.getText());
                }

            }
        }
    }
}
