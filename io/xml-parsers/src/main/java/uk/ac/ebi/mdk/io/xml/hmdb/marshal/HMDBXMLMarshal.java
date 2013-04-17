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

package uk.ac.ebi.mdk.io.xml.hmdb.marshal;

import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.io.xml.hmdb.HMDBMetabolite;

import javax.xml.stream.XMLStreamException;

/**
 * Describes a marshal for HMDB XML event. The class marshals data in an XML
 * stream into the correct field of a {@link HMDBMetabolite}.
 *
 * @author John May
 * @see HMDBDefaultMarshals
 * @see uk.ac.ebi.mdk.io.xml.hmdb.HMDBParser
 * @see HMDBMetabolite
 */
public interface HMDBXMLMarshal {

    /**
     * The tag of this marshal. When the tag is encountered, {@link
     * #marshal(XMLStreamReader2, HMDBMetabolite)} is invoked.
     *
     * @return tag
     */
    public String tag();

    /**
     * Handles the XML Stream for the provided {@link #tag()}
     */
    public void marshal(XMLStreamReader2 xmlr, HMDBMetabolite metabolite) throws
                                                                          XMLStreamException;

}
