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

import org.apache.log4j.Logger;
import org.sbml.jsbml.*;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.annotation.InChI;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.*;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.HashMap;
import java.util.Map;


/**
 * SBMLIOUtil – 2011.09.27 <br>
 * Class description
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

    public SBMLIOUtil(EntityFactory factory, int level, int version) {
        this.level = level;
        this.version = version;
        this.factory = factory;
    }


    public SBMLIOUtil(EntityFactory factory) {
        this(factory, 2, 2);
    }


    private String nextMetaId() {
        metaIdticker++;
        return Long.toString(metaIdticker);
    }


    public SBMLDocument getDocument(Reconstruction reconstruction) {

        SBMLDocument doc = new SBMLDocument(level, version);
        Model model = new Model(level, version);
        doc.setModel(model);

        for (MetabolicReaction rxn : reconstruction.getReactome()) {
            addReaction(model, rxn);
        }

        return doc;

    }


    public Reaction addReaction(Model model, MetabolicReaction rxn) {

        Reaction sbmlRxn = new Reaction(level, version);

        // set id and meta-id
        Identifier id = rxn.getIdentifier();
        sbmlRxn.setMetaId(nextMetaId());
        sbmlRxn.setName(rxn.getName());
        if (id == null) {
            sbmlRxn.setId("rxn" + metaIdticker); // maybe need a try/catch to reset valid id
        } else {
            String accession = id.getAccession();
            accession = accession.trim();
            accession = accession.replaceAll(":", "%3A"); // replace spaces and dashes with underscores
            accession = accession.replaceAll("[- ]", "_"); // replace spaces and dashes with underscores
            accession = accession.replaceAll("[^_A-z0-9]", ""); // replace anything not a number digit or underscore
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
            sbmlRxn.addReactant(getSpeciesReference(model, p, sbmlRxn, index++));
        }
        for (MetabolicParticipant p : rxn.getProducts()) {
            sbmlRxn.addProduct(getSpeciesReference(model, p, sbmlRxn, index++));
        }


        CVTerm term = new CVTerm(CVTerm.Qualifier.BQB_IS);
        for (CrossReference xref : rxn.getAnnotationsExtending(CrossReference.class)) {
            term.addResource(xref.getIdentifier().getURN());
        }

        if(!term.getResources().isEmpty())
            sbmlRxn.addCVTerm(term);

        if(!model.addReaction(sbmlRxn)){
            // could inform user that the reaction couldn't be added
        }

        return sbmlRxn;

    }


    public SpeciesReference getSpeciesReference(Model model,
                                                CompartmentalisedParticipant<Metabolite, Double,  Compartment> participant,
                                                Reaction reaction,
                                                Integer index) {

        // we need a key as the coef are part of the reaction not the species...
        // however the compartment is part of the species not the reaction

        MetabolicParticipant key = factory.newInstance(MetabolicParticipant.class);
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
     *
     * @return
     */
    public Species addSpecies(Model model,
                              CompartmentalisedParticipant<Metabolite, Double, Compartment> participant) {

        Species species = new Species(level, version);

        Metabolite m = participant.getMolecule();


        species.setMetaId(nextMetaId());
        Identifier id = m.getIdentifier();
        if (id == null) {
            species.setId("met" + metaIdticker); // maybe need a try/catch to reset valid id
        } else {
            String accession = id.getAccession();
            accession = accession.trim();
            accession = accession.replaceAll("[- ]", "_"); // replace spaces and dashes with underscores
            accession = accession.replaceAll("[^_A-z0-9]", ""); // replace anything not a number digit or underscore
            species.setId(accession + "_" + ((Compartment) participant.getCompartment()).getAbbreviation());
        }

        species.setName(m.getName());
        species.setCompartment(getCompartment(model, participant));

        CVTerm term = new CVTerm(CVTerm.Qualifier.BQB_IS);

        if (m.getAnnotationsExtending(CrossReference.class).iterator().hasNext()) {

            for (CrossReference xref : m.getAnnotationsExtending(CrossReference.class)) {
                String resource = xref.getIdentifier().getResolvableURL();
                term.addResource(resource);
            }

        }

        if(m.hasAnnotation(InChI.class) || m.hasAnnotation(AtomContainerAnnotation.class)){
            for (ChemicalStructure structure : m.getAnnotationsExtending(ChemicalStructure.class)) {
                // TODO: should abstract this mapping to an annotation handler
                String inchi = structure.toInChI();
                if(!inchi.isEmpty())
                    term.addResource("http://rdf.openmolecules.net/?" + inchi);
            }
        }

        // if we've added annotation
        if(!term.getResources().isEmpty())
            species.addCVTerm(term);

        model.addSpecies(species);

        return species;
    }


    public org.sbml.jsbml.Compartment getCompartment(Model model, CompartmentalisedParticipant<?, ?,Compartment> participant) {

        if (compartmentMap.containsKey(participant.getCompartment())) {
            return compartmentMap.get(participant.getCompartment());
        }

        org.sbml.jsbml.Compartment sbmlCompartment = new org.sbml.jsbml.Compartment(level, version);

        Compartment compartment = (Compartment) participant.getCompartment();

        sbmlCompartment.setId(compartment.getAbbreviation());
        sbmlCompartment.setName(compartment.getDescription());

        Identifier identifier = compartment.getIdentifier();
        if(!identifier.getAccession().isEmpty()){
            sbmlCompartment.addCVTerm(new CVTerm(CVTerm.Qualifier.BQB_IS, identifier.getURN()));
        }

        model.addCompartment(sbmlCompartment);

        compartmentMap.put(compartment, sbmlCompartment);

        return sbmlCompartment;

    }
}
