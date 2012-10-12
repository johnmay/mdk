/**
 * SBMLReactionReader.java
 *
 * 2011.08.15
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
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import uk.ac.ebi.mdk.deprecated.MIRIAMLoader;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.mdk.domain.annotation.InChI;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.tool.CompartmentResolver;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * SBMLReactionReader – 2011.08.15
 * <p/>
 * Loads the reactions from an SBML document in to various types of reactions
 * <p/>
 * <pre>
 * InputStream sbmlStream                = getClass().getResourceAsStream( "streptomyces-coelicolor-6.2005.xml" );
 * SBMLReactionReader loader                 = SBMLReactionReader.getInstance();
 * List<AtomContainerReaction> reactions = loader.getReactions( sbmlStream );
 *
 * for ( AtomContainerReaction r : reactions ) {
 *     System.out.println( r );
 * }
 * </pre>
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class SBMLReactionReader {

    private static final Logger LOGGER = Logger.getLogger(SBMLReactionReader.class);

    private static final SBMLReader reader = new SBMLReader();
    // web service clients

    private final SBMLDocument document;

    private final Model model;
    // iterator location and max

    private Integer reactionIndex = -1;

    private final Integer reactionCount;

    //

    private Map<SpeciesReference, Species> speciesReferences = new HashMap<SpeciesReference, Species>();

    private Map<Species, Metabolite> speciesMap = new HashMap<Species, Metabolite>();

    private Map<String, Metabolite> speciesNameMap = new HashMap<String, Metabolite>();

    private Map<Compartment, uk.ac.ebi.mdk.domain.entity.reaction.Compartment> compartments = new HashMap<Compartment, uk.ac.ebi.mdk.domain.entity.reaction.Compartment>();

    private EntityFactory factory;


    private CompartmentResolver resolver;


    /**
     * Construct an SBML reaction reader using an input stream. This constructor
     * uses an empty {@see AcceptAllFilter} filter on reaction participants
     *
     * @param stream
     * @throws XMLStreamException
     */
    public SBMLReactionReader(InputStream stream, EntityFactory factory, CompartmentResolver resolver) throws XMLStreamException {
        this(reader.readSBMLFromStream(stream), factory, resolver);
        this.factory = factory;
        this.resolver = resolver;
    }

    public SBMLReactionReader(SBMLDocument document, EntityFactory factory, CompartmentResolver resolver) {
        this.document = document;
        this.model = document.getModel();
        this.reactionCount = model.getNumReactions();
        this.factory = factory;
        this.resolver = resolver;
    }


    public MetabolicReaction getMetabolicReaction(Reaction sbmlReaction) {

        MetabolicReaction reaction = factory.ofClass(MetabolicReaction.class,
                new BasicReactionIdentifier(sbmlReaction.getId()),
                sbmlReaction.getName(),
                sbmlReaction.getMetaId());
        LOGGER.info("Reading SBML reaction " + reaction);

        for (int i = 0; i < sbmlReaction.getNumReactants(); i++) {
            reaction.addReactant(getMetaboliteParticipant(sbmlReaction.getReactant(i)));
        }

        for (int i = 0; i < sbmlReaction.getNumProducts(); i++) {
            reaction.addProduct(getMetaboliteParticipant(sbmlReaction.getProduct(i)));
        }

        // set the reversibility
        reaction.setDirection(sbmlReaction.isReversible() ? Direction.BIDIRECTIONAL : Direction.FORWARD);

        return reaction;

    }


    public Species getSpecies(SpeciesReference speciesReference) {

        if (speciesReferences.containsKey(speciesReference)) {
            return speciesReferences.get(speciesReference);
        }

        Species species = speciesReference.getSpeciesInstance();

        if (species != null) {
            // cache
            speciesReferences.put(speciesReference, species);
            return species;
        }

        return new Species("error");


    }

    public uk.ac.ebi.mdk.domain.entity.reaction.Compartment getCompartment(Compartment compartment) {

        if (compartments.containsKey(compartment)) {
            return compartments.get(compartment);
        }

        String id = compartment.getId();
        String name = compartment.getName();

        // if no compartment name is provided we use the ID or we use the name
        uk.ac.ebi.mdk.domain.entity.reaction.Compartment c = name.isEmpty() ? resolver.getCompartment(id)
                                                                            : resolver.getCompartment(name);


        if (c != null) {
            compartments.put(compartment, c);
            return c;
        }

        c = resolver.getCompartment(name);
        if (c != null) {
            compartments.put(compartment, c);
            return c;
        }

        throw new UnsupportedOperationException("No compartment resolved");

    }

    /**
     * Constructs a reaction participant from a species reference
     *
     * @param speciesReference An instance of SBML {@see SpeciesReference}
     * @return An MetaboliteParticipant
     */
    public MetabolicParticipant getMetaboliteParticipant(SpeciesReference speciesReference) {

        Species species = getSpecies(speciesReference);

        uk.ac.ebi.mdk.domain.entity.reaction.Compartment compartment = getCompartment(species.getCompartmentInstance());

        Double coefficient = speciesReference.getStoichiometry();

        Metabolite metabolite = null;

        if (speciesMap.containsKey(species)) {
            metabolite = speciesMap.get(species);
        } else if (speciesNameMap.containsKey(species.getName())) {
            LOGGER.info("Using existing species with the same name:" + species.getName());
            metabolite = speciesNameMap.get(species.getName());
        } else {
            metabolite = factory.newInstance(Metabolite.class,
                    new BasicChemicalIdentifier(species.getId()),
                    species.getName(),
                    species.getMetaId());


            for (CVTerm term : species.getCVTerms()) {
                for (String resource : term.getResources()) {
                    //XXX bit of a hack
                    if (resource.startsWith("urn:miriam")) {
                        Identifier identifier = MIRIAMLoader.getInstance().getIdentifier(resource);
                        if (identifier != null)
                            metabolite.addAnnotation(DefaultAnnotationFactory.getInstance().getCrossReference(identifier));
                    } else if (resource.startsWith("http://rdf.openmolecules.net")) {
                        // 30 = the prefix
                        metabolite.addAnnotation(new InChI(resource.substring(30)));
                    }

                }
            }

            speciesMap.put(species, metabolite);
            speciesNameMap.put(metabolite.getName(), metabolite);
        }

        MetabolicParticipant participant = factory.newInstance(MetabolicParticipant.class);
        participant.setMolecule(metabolite);
        participant.setCoefficient(coefficient);
        participant.setCompartment(compartment);

        return participant;

    }

    public boolean hasNext() {
        return reactionIndex + 1 < reactionCount;
    }


    public MetabolicReaction next() {
        reactionIndex++;
        Reaction sbmlReaction = model.getReaction(reactionIndex);

        return getMetabolicReaction(sbmlReaction);
    }
}
