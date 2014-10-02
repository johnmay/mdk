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

/**
 * KGMLReader.java
 *
 * 2011.09.16
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.io.xml.kgml;

import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGReactionIdentifier;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.domain.observation.Observation;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * KGMLReader – 2011.09.16 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class KGMLReader {

    private static final Logger LOGGER = Logger.getLogger(KGMLReader.class);

    private InputStream in;

    private EntityFactory factory;

    private Collection<KGMLEntry> entires = new ArrayList<KGMLEntry>();

    private Collection<KGMLReaction> rxns = new ArrayList<KGMLReaction>();


    public KGMLReader(EntityFactory factory, InputStream in) throws XMLStreamException {
        this.in = in;

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif.createXMLStreamReader(in);

        int event;
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.START_ELEMENT:
                    if (xmlr.getLocalName().equals("entry")) {
                        entires.add(new KGMLEntry(factory, xmlr));
                    } else if (xmlr.getLocalName().equals("reaction")) {
                        rxns.add(new KGMLReaction(xmlr));
                    }
                    break;
            }
        }

        this.factory = factory;

    }


    public Map<Integer, KGMLEntry> getEntries() {
        Map<Integer, KGMLEntry> entryMap = new HashMap<Integer, KGMLEntry>();
        for (KGMLEntry entry : entires) {
            entryMap.put(entry.getId(), entry);
        }
        //        NodeList nodeList = doc.getElementsByTagName("entry");
        //        Map<Integer, KGMLEntry> entries =
        //                                new HashMap<Integer, KGMLEntry>(nodeList.getLength(), 1.0f);
        //        for( int i = 0 ; i < nodeList.getLength() ; i++ ) {
        //            Node n = nodeList.item(i);
        //            KGMLEntry entry = KGMLEntry.newInstance(n);
        //            entries.put(entry.id, entry);
        //        }
        //        return entries;
        return entryMap;
    }


    public Collection<KGMLReaction> getKGMLReactions() {
        //        NodeList nodeList = doc.getElementsByTagName("reaction");
        //        List<KGMLReaction> entries = new ArrayList(nodeList.getLength());
        //        for( int i = 0 ; i < nodeList.getLength() ; i++ ) {
        //            Node n = nodeList.item(i);
        //            entries.add(KGMLReaction.newInstance(n));
        //        }
        //        return entries;
        return rxns;
    }


    public Collection<MetabolicReaction> getReactions() {
        Collection<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();
        Map<Integer, Metabolite> metabolites = getMetabolites();
        Map<Integer, KGMLEntry> entries = getEntries();


        for (KGMLReaction reaction : getKGMLReactions()) {
            KGMLEntry rxnEntry = entries.get(reaction.getId());

            BasicReactionIdentifier reactionId = new BasicReactionIdentifier(Integer.toString(rxnEntry.getId()));

            String[] names = rxnEntry.getName().split(" ");
            MetabolicReaction rxn = factory.ofClass(MetabolicReaction.class,
                                                    BasicReactionIdentifier.nextIdentifier(),
                                                    names[0].substring(4),
                                                    names[0].substring(4));
            for (String name : names) {
                rxn.addAnnotation(CrossReference.create(new BasicReactionIdentifier(names[0].substring(4))));
            }
            for (int id : reaction.getSubstrateIds()) {
                MetabolicParticipant p = factory.ofClass(MetabolicParticipant.class);
                p.setMolecule(metabolites.get(id));
                rxn.addReactant(p);
            }
            for (int id : reaction.getProductIds()) {
                MetabolicParticipant p = factory.ofClass(MetabolicParticipant.class);
                p.setMolecule(metabolites.get(id));
                rxn.addProduct(p);
            }

            reactions.add(rxn);

        }

        return reactions;
    }


    public Metabolite getMetabolite(KGMLEntry entry){
        String subName = entry.getName().substring(4);
        Metabolite m = factory.newInstance(Metabolite.class, BasicChemicalIdentifier.nextIdentifier(), subName, subName);
        m.addAnnotation(new KEGGCrossReference(new KEGGCompoundIdentifier(subName)));
        return m;

    }

    public Map<Integer, Metabolite> getMetabolites() {
        Map<Integer, Metabolite> entryMap = new HashMap<Integer, Metabolite>();
        for (KGMLEntry entry : entires) {
            entryMap.put(entry.getId(), getMetabolite(entry));
        }
        return entryMap;
    }

}
