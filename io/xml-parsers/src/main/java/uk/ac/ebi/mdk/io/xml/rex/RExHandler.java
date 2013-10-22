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

import org.xml.sax.InputSource;
import uk.ac.bbk.rex.*;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.rex.RExAnnotation;
import uk.ac.ebi.mdk.domain.annotation.rex.RExCompound;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.annotation.rex.RExTag;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/** @author John May */
public class RExHandler {

    JAXBContext  context;
    Unmarshaller unmarshaller;
    Marshaller   marshaller;

    IdentifierFactory identifiers;

    public RExHandler() throws JAXBException {
        this.context = JAXBContext.newInstance(Rex.class);
        this.unmarshaller = context.createUnmarshaller();
        this.marshaller = context.createMarshaller();
        this.identifiers = DefaultIdentifierFactory.getInstance();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    }

    public String marshal(final Collection<RExExtract> extracts, final Collection<RExCompound> compounds)
            throws JAXBException {
        Rex rex = new Rex();
        rex.setExtracts(new Extracts());

        for (RExExtract extract : extracts) {
            final Extract xmlExtract = new Extract();
            xmlExtract.setSentence(extract.sentence());
            xmlExtract.setSource(extract.source().getResolvableURL());
            xmlExtract.setIsInCorrectOrganism(extract.isInCorrectOrganism());
            for (final RExTag tag : extract.tags()) {
                Tag xmlTag = new Tag();
                xmlTag.setId(tag.id());
                xmlTag.setStart(tag.start());
                xmlTag.setLength(tag.length());
                xmlTag.setType(tag.type().toString());
                xmlExtract.getTag().add(xmlTag);
            }
            rex.getExtracts().getExtract().add(xmlExtract);
        }

        rex.setComponents(new Components());
        rex.getComponents().setReactants(new Compounds());
        rex.getComponents().setProducts(new Compounds());

        for(RExCompound rexCompound : compounds)
        {
            Compound c = rexCompoundToCompound(rexCompound);

            if(rexCompound.getType() == RExCompound.Type.SUBSTRATE)
            {
                rex.getComponents().getReactants().getCompound().add(c);
            }
            else if(rexCompound.getType() == RExCompound.Type.PRODUCT)
            {
                rex.getComponents().getProducts().getCompound().add(c);
            }
        }

        StringWriter sw = new StringWriter();
        marshaller.marshal(rex, sw);
        return sw.toString();
    }

    public RExAnnotation unmarshal(final String str) throws JAXBException {
        //Rex rex = (Rex) unmarshaller.unmarshal(new StringReader(str));
        JAXBElement<Rex> root = unmarshaller.unmarshal(new StreamSource(new StringReader(str)), Rex.class);
        Rex rex = root.getValue();
        List<RExExtract> extracts = new ArrayList<RExExtract>(1);
        for (Extract e : rex.getExtracts().getExtract()) {
            Identifier identifier = identifiers.ofURL(e.getSource());
            String sentence = e.getSentence().replaceAll("\n", "").replaceAll(
                "\\s+", " ");
            List<RExTag> tags = new ArrayList<RExTag>(4);
            for (final Tag tag : e.getTag()) {
                tags.add(new RExTag(tag.getId(), tag.getStart(), tag.getLength(),
                                    tag.getType()));
            }
            extracts.add(new RExExtract(identifier, sentence, tags, e.isIsInCorrectOrganism()));
        }

        List<RExCompound> compounds = new ArrayList<RExCompound>();
        for(Compound c : rex.getComponents().getReactants().getCompound())
        {
            compounds.add(new RExCompound(c.getId(),
                                          RExCompound.Type.SUBSTRATE,
                                          c.isIsInBRENDA(),
                                          c.isIsInSeed(),
                                          c.isIsInBranch(),
                                          pathwaysToStrings(c.getAlternativePathways().getPathway()),
                                          pathwaysToStrings(c.getOtherPathways().getPathway()),
                                          c.getExtraction(),
                                          c.getRelevance()));
        }

        for(Compound c : rex.getComponents().getProducts().getCompound())
        {
            compounds.add(new RExCompound(c.getId(),
                                          RExCompound.Type.PRODUCT,
                                          c.isIsInBRENDA(),
                                          c.isIsInSeed(),
                                          c.isIsInBranch(),
                                          pathwaysToStrings(c.getAlternativePathways().getPathway()),
                                          pathwaysToStrings(c.getOtherPathways().getPathway()),
                                          c.getExtraction(),
                                          c.getRelevance()));
        }

        return new RExAnnotation(extracts, compounds);
    }

    private List<String> pathwaysToStrings(List<Pathway> pathways)
    {
        List<String> ids = new ArrayList<String>();
        for(Pathway pathway : pathways)
        {
            ids.add(pathway.getId());
        }

        return ids;
    }

    private List<Pathway> stringsToPathways(List<String> ids)
    {
        List<Pathway> pathways = new ArrayList<Pathway>();
        for(String id : ids)
        {
            Pathway pathway = new Pathway();
            pathway.setId(id);
            pathways.add(pathway);
        }

        return pathways;
    }

    private Compound rexCompoundToCompound(RExCompound rexCompound)
    {
        Compound compound = new Compound();
        compound.setId(rexCompound.getID());
        compound.setIsInBRENDA(rexCompound.isInBRENDA());
        compound.setIsInSeed(rexCompound.isInSeed());
        compound.setIsInBranch(rexCompound.isInBranch());

        compound.setAlternativePathways(new Pathways());
        compound.getAlternativePathways().getPathway().addAll(stringsToPathways(rexCompound.getAlternativePathways()));

        compound.setOtherPathways(new Pathways());
        compound.getOtherPathways().getPathway().addAll(stringsToPathways(rexCompound.getOtherPathways()));

        compound.setExtraction(rexCompound.getExtraction());
        compound.setRelevance(rexCompound.getRelevance());

        return compound;
    }
}
