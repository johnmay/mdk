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
 * SBMLIOUtil.java
 *
 * 2011.09.27
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
package uk.ac.ebi.mdk.io.xml.sbml;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBO;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.annotation.InChI;
import uk.ac.ebi.mdk.domain.annotation.Note;
import uk.ac.ebi.mdk.domain.annotation.SMILES;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.rex.RExExtract;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.CompartmentalisedParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.io.xml.rex.RExHandler;

import javax.xml.bind.JAXBException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * SBMLIOUtil – 2011.09.27 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class SBMLIOUtil {

    private static final Logger LOGGER = Logger.getLogger(SBMLIOUtil.class);

    public final int level;

    public final int version;

    private long metaIdticker = 0;

    private Map<CompartmentalisedParticipant, Species> speciesReferences = new HashMap<CompartmentalisedParticipant, Species>();

    private Map<Compartment, org.sbml.jsbml.Compartment> compartmentMap = new HashMap<Compartment, org.sbml.jsbml.Compartment>();

    private EntityFactory factory;

    private SBMLSideCompoundHandler sideCompHandler;
    private Boolean                 sideCompoundHandlerAvailable;
    private RExHandler              handler;

    public SBMLIOUtil(EntityFactory factory, int level, int version) {
        this.level = level;
        this.version = version;
        this.factory = factory;
        this.sideCompoundHandlerAvailable = false;
        try {
            this.handler = new RExHandler();
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public SBMLIOUtil(EntityFactory factory, int level, int version, SBMLSideCompoundHandler sideCompHandler) {
        this.level = level;
        this.version = version;
        this.factory = factory;
        this.sideCompHandler = sideCompHandler;
        this.sideCompoundHandlerAvailable = true;
        try {
            this.handler = new RExHandler();
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public SBMLIOUtil(EntityFactory factory) {
        this(factory, 2, 4);
    }


    private String nextMetaId() {
        metaIdticker++;
        return String.format("_%09d", metaIdticker);
    }


    public SBMLDocument getDocument(Reconstruction reconstruction) {

        SBMLDocument doc = new SBMLDocument(level, version);
        Model model = new Model(level, version);
        doc.addNamespace("html", "xmlns", "http://www.w3.org/1999/xhtml");
        model.addNamespace("html");
        doc.setModel(model);
        
        for (MetabolicReaction rxn : reconstruction.reactome()) {
            addReaction(model, rxn);
        }
        if (sideCompoundHandlerAvailable)
            sideCompHandler.writeSideCompoundAnnotations(model);
        return doc;

    }


    public Reaction addReaction(Model model, MetabolicReaction rxn) {

        Reaction sbmlRxn = new Reaction(level, version);

        // set id and meta-id
        Identifier id = rxn.getIdentifier();
        sbmlRxn.setMetaId("meta_r_" + nextMetaId());
        sbmlRxn.setName(rxn.getName());
        if (id == null) {
            sbmlRxn.setId("rxn" + metaIdticker); // maybe need a try/catch to reset valid id
        }
        else {
            String accession = id.getAccession();
            accession = accession.trim();
            accession = accession
                    .replaceAll(":", "%3A"); // replace spaces and dashes with underscores
            accession = accession
                    .replaceAll("[- ]", "_"); // replace spaces and dashes with underscores
            accession = accession
                    .replaceAll("[^_A-z0-9]", ""); // replace anything not a number digit or underscore
            accession = "r_" + accession;
            sbmlRxn.setId(accession);
        }

        Direction direction = rxn.getDirection();

        if (direction != null) {

            Direction directionImplementation = (Direction) direction;

            sbmlRxn.setReversible(directionImplementation.isReversible());

            if (directionImplementation == Direction.BACKWARD) {
                rxn.transpose();
                rxn.setDirection(Direction.FORWARD);
            }

        }

        int index = 0;

        for (MetabolicParticipant p : rxn.getReactants()) {
            SpeciesReference ref = getSpeciesReference(model, p, sbmlRxn, index++);
            sbmlRxn.addReactant(ref);
            if (p.isSideCompound() && sideCompoundHandlerAvailable)
                sideCompHandler
                        .registerAsSideCompound(new Species(ref.getSpecies()));
        }
        for (MetabolicParticipant p : rxn.getProducts()) {
            SpeciesReference ref = getSpeciesReference(model, p, sbmlRxn, index++);
            sbmlRxn.addProduct(ref);
            if (p.isSideCompound() && sideCompoundHandlerAvailable)
                sideCompHandler
                        .registerAsSideCompound(new Species(ref.getSpecies()));
        }


        CVTerm term = new CVTerm(CVTerm.Qualifier.BQB_IS);
        for (CrossReference xref : rxn
                .getAnnotationsExtending(CrossReference.class)) {
            term.addResource(xref.getIdentifier().getURN());
        }

// causes large slow down in export
//        if (rxn.getAnnotations().size() > 0) {
//            String content = "<notes><html xmlns=\"http://www.w3.org/1999/xhtml\">"
//                    + notes(rxn) + "</html></notes>";
//            XMLNode notes = XMLNode.convertStringToXMLNode(content);
//            sbmlRxn.setNotes(notes);
//        }

        if (!term.getResources().isEmpty())
            sbmlRxn.addCVTerm(term);

        Collection<RExExtract> extracts = rxn.getAnnotations(RExExtract.class);
        if (!extracts.isEmpty()) {
            try {
                sbmlRxn.getAnnotation().appendNoRDFAnnotation(handler.marshal(extracts));
            } catch (JAXBException e) {
                LOGGER.error("Could not convert REx extracts");
            }
        }

        if (!model.addReaction(sbmlRxn)) {
            // could inform user that the reaction couldn't be added
        }

        return sbmlRxn;

    }


    public SpeciesReference getSpeciesReference(Model model,
                                                CompartmentalisedParticipant<Metabolite, Double, Compartment> participant,
                                                Reaction reaction,
                                                Integer index) {

        // we need a key as the coef are part of the reaction not the species...
        // however the compartment is part of the species not the reaction

        MetabolicParticipant key = factory
                .newInstance(MetabolicParticipant.class);
        key.setCoefficient(1d);
        key.setCompartment(participant.getCompartment());
        key.setMolecule(participant.getMolecule());

        // create a new entry if one doesn't exists
        if (!speciesReferences.containsKey(key)) {
            speciesReferences.put(key, addSpecies(model, participant));
        }

        Species species = speciesReferences.get(key);

        // need to set the stoichiometry on each species reference
        SpeciesReference reference = new SpeciesReference();
        reference.setId(species.getId() + "_" + reaction.getId() + "_" + index);
        reference.setSpecies(species);
        reference.setStoichiometry(participant.getCoefficient());

        return reference;

    }


    /**
     * Creates a new species in the given model adding
     *
     * @param model
     * @param participant
     * @return
     */
    public Species addSpecies(Model model,
                              CompartmentalisedParticipant<Metabolite, Double, Compartment> participant) {

        Species species = new Species(level, version);
        species.setSBOTerm(SBO.getSimpleMolecule());

        Metabolite m = participant.getMolecule();

        species.setMetaId("meta_m_" + nextMetaId());
        Identifier id = m.getIdentifier();
        if (id == null) {
            species.setId("m_" + metaIdticker); // maybe need a try/catch to reset valid id
        }
        else {
            String accession = id.getAccession();
            accession = accession.trim();
            accession = accession
                    .replaceAll("[- ]", "_"); // replace spaces and dashes with underscores
            accession = accession
                    .replaceAll("[^_A-z0-9]", ""); // replace anything not a number digit or underscore
            String compartment = ((Compartment) participant.getCompartment()).getAbbreviation();
            // suffix compartment to id
            if (!id.getAccession().endsWith("_" + compartment))
                species.setId("m_" + accession + "_" + compartment);
            else
                species.setId(accession);
        }

        species.setName(m.getName());
        species.setCompartment(getCompartment(model, participant));

        CVTerm term = new CVTerm(CVTerm.Qualifier.BQB_IS);

        if (m.getAnnotationsExtending(CrossReference.class)
             .iterator()
             .hasNext()) {

            for (CrossReference xref : m
                    .getAnnotationsExtending(CrossReference.class)) {
                String resource = xref.getIdentifier().getResolvableURL();
                term.addResource(resource);
            }

        }

        if (m.hasAnnotation(InChI.class) || m
                .hasAnnotation(AtomContainerAnnotation.class)) {
            for (ChemicalStructure structure : m
                    .getAnnotationsExtending(ChemicalStructure.class)) {
                // TODO: should abstract this mapping to an annotation handler
                String inchi = structure.toInChI();
                if (!inchi.isEmpty())
                    term.addResource("http://rdf.openmolecules.net/?" + inchi);
            }
        }

        if (m.hasAnnotation(Note.class) || m.hasStructure()) {
            Set<String> notes = notes(m, Collections.<Class>singletonList(Note.class));

            String noteContent = !notes.isEmpty() ? "<p>" + Joiner.on("</p>\n<p>")
                                                                  .join(notes) + "</p>" : "";
            
            StringBuilder sb = new StringBuilder();
            for (ChemicalStructure cs : m.getStructures()) {
                try {
                    String smi = SmilesGenerator.isomeric().create(cs.getStructure());
                    sb.append("<p>SMILES: ").append(smi).append("</p>\n");
                } catch (CDKException e) {
                    LOGGER.error("Could not generate SMILES for " + m.getAbbreviation());
                    LOGGER.error(e);
                } catch (Exception e) {
                    LOGGER.error("Could not generate SMILES for " + m.getAbbreviation());
                    LOGGER.error(e);
                }
            }
            
            noteContent += "\n" + sb.toString();

            String content = "<notes><body xmlns=\"http://www.w3.org/1999/xhtml\">" + noteContent + "</body></notes>";
            // todo the parsing is very slow
            XMLNode node = XMLNode.convertStringToXMLNode(content);
            species.setNotes(node);
        }

        // if we've added annotation
        if (!term.getResources().isEmpty())
            species.addCVTerm(term);

        model.addSpecies(species);

        return species;
    }

    @SuppressWarnings("unchecked")
    private Set<String> notes(AnnotatedEntity e, Collection<Class> cs) {
        Set<String> notes = new TreeSet<String>();
        for (Class c : cs) {
            if (c != AtomContainerAnnotation.class) {
                for (Annotation a : e.getAnnotations((Class<Annotation>) c)) {
                    notes.add(note(a));
                }
            }
        }
        return notes;
    }

    private String note(Annotation a) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>");
        sb.append(a.toString());
        sb.append("</p>");
        return sb.toString();
    }

    public org.sbml.jsbml.Compartment getCompartment(Model model, CompartmentalisedParticipant<?, ?, Compartment> participant) {

        if (compartmentMap.containsKey(participant.getCompartment())) {
            return compartmentMap.get(participant.getCompartment());
        }

        org.sbml.jsbml.Compartment sbmlCompartment = new org.sbml.jsbml.Compartment(level, version);

        Compartment compartment = (Compartment) participant.getCompartment();

        sbmlCompartment.setId(compartment.getAbbreviation());
        sbmlCompartment.setName(compartment.getDescription());

        Identifier identifier = compartment.getIdentifier();
        if (!identifier.getAccession().isEmpty()) {
            sbmlCompartment
                    .addCVTerm(new CVTerm(CVTerm.Qualifier.BQB_IS, identifier
                            .getURN()));
        }

        model.addCompartment(sbmlCompartment);

        compartmentMap.put(compartment, sbmlCompartment);

        return sbmlCompartment;

    }
}
