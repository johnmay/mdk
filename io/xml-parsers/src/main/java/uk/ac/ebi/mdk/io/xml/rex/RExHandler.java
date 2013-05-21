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

package uk.ac.ebi.mdk.io.xml.rex;

import uk.ac.bbk.rex.Extract;
import uk.ac.bbk.rex.Extracts;
import uk.ac.bbk.rex.Tag;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.rex.RExTag;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/** @author John May */
public class RExHandler {

    JAXBContext  context;
    Unmarshaller unmarshaller;
    Marshaller   marshaller;

    public RExHandler() throws JAXBException {
        this.context = JAXBContext.newInstance(Extracts.class);
        this.unmarshaller = context.createUnmarshaller();
        this.marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    }

    public String marshal(final RExExtract extract) throws JAXBException {
        final Extract xmlExtract = new Extract();
        xmlExtract.setSentence(extract.sentence());
        xmlExtract.setSource(extract.source().getResolvableURL());
        for (final RExTag tag : extract.tags()) {
            Tag xmlTag = new Tag();
            xmlTag.setStart(tag.start());
            xmlTag.setLength(tag.length());
            xmlTag.setType(tag.type().toString());
            xmlExtract.getTag().add(xmlTag);
        }
        StringWriter sw = new StringWriter();
        Extracts xmlExtracts = new Extracts();
        xmlExtracts.getExtract().add(xmlExtract);
        marshaller.marshal(xmlExtracts, sw);
        return sw.toString();
    }

    public List<RExExtract> unmarshal(final String str) throws JAXBException {
        Extracts xmlExtracts = (Extracts) unmarshaller.unmarshal(new StringReader(str));
        List<RExExtract> extracts = new ArrayList<RExExtract>(1);
        for (Extract e : xmlExtracts.getExtract()) {
            Identifier identifier = null;
            String sentence = e.getSentence();
            List<RExTag> tags = new ArrayList<RExTag>(4);
            for (final Tag tag : e.getTag()) {
                tags.add(new RExTag(tag.getStart(), tag.getLength(), tag.getType()));
            }
            extracts.add(new RExExtract(identifier, sentence, tags));
        }
        return extracts;
    }
}
