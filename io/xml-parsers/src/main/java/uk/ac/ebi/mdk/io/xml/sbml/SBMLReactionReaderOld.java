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
package uk.ac.ebi.mdk.io.xml.sbml;

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
public class SBMLReactionReaderOld {

//    private static final Logger LOGGER = Logger.getLogger(SBMLReactionReaderOld.class);
//
//    private static final SBMLReader reader = new SBMLReader();
//    // web service clients
//
//    private final CachedChemicalWS chebiWS = new CachedChemicalWS(new ChEBIWebServiceConnection());
//
//    private final CachedChemicalWS keggWS = new CachedChemicalWS(new KeggCompoundWebServiceConnection());
//    // sbml storage
//
//    private final SBMLDocument document;
//
//    private final Model model;
//    // iterator location and max
//
//    private Integer reactionIndex = -1;
//
//    private final Integer reactionCount;
//
//    private AbstractParticipantFilter filter;
//    //
//
//    private Map<SpeciesReference, Species> speciesReferences = new HashMap<SpeciesReference, Species>();
//
//    private Map<Species, Metabolite> speciesMap = new HashMap<Species, Metabolite>();
//
//    private Map<String, Metabolite> speciesNameMap = new HashMap<String, Metabolite>();
//
//    private Map<Compartment, uk.ac.ebi.mdk.domain.entity.reaction.Compartment> compartments = new HashMap<Compartment, uk.ac.ebi.mdk.domain.entity.reaction.Compartment>();
//
//    private EntityFactory factory;
//
//
//    private CompartmentResolver resolver;
//
//
//    /**
//     * Construct an SBML reaction reader using an input stream. This constructor
//     * uses an empty {@see AcceptAllFilter} filter on reaction participants
//     *
//     * @param stream
//     *
//     * @throws javax.xml.stream.XMLStreamException
//     */
//    public SBMLReactionReaderOld(InputStream stream, EntityFactory factory, CompartmentResolver resolver) throws XMLStreamException {
//        this(reader.readSBMLFromStream(stream));
//        this.factory = factory;
//        this.resolver = resolver;
//    }
//
//
//    /**
//     * Construct an SBML reaction reader using an input stream using a specified participant
//     * filter
//     *
//     * @param stream
//     * @param filter
//     *
//     * @throws javax.xml.stream.XMLStreamException
//     */
//    public SBMLReactionReaderOld(InputStream stream, AbstractParticipantFilter filter) throws
//            XMLStreamException {
//        this(reader.readSBMLFromStream(stream), filter, DefaultEntityFactory.getInstance(), new AutomaticCompartmentResolver());
//    }
//
//
//    /**
//     * This constructor uses an empty {@see AcceptAllFilter} (i.e. accept all participants)
//     *
//     * @param document
//     */
//    public SBMLReactionReaderOld(SBMLDocument document) {
//        // default filter is an empty instantiation of BasicFilter (accepts all)
//        this(document, new AcceptAllFilter(), DefaultEntityFactory.getInstance(), new AutomaticCompartmentResolver());
//    }
//
//
//    public SBMLReactionReaderOld(SBMLDocument document, AbstractParticipantFilter filter, EntityFactory factory, CompartmentResolver resolver) {
//        this.document = document;
//        this.model = document.getModel();
//        this.reactionCount = model.getNumReactions();
//        this.filter = filter;
//        this.factory = factory;
//        this.resolver = resolver;
//    }
//
//
//    /**
//     * Loads the specified Reactions as an AtomContainer reaction (Note: RDF annotations) for
//     * ChEBI (KEGG also in future) is required.
//     *
//     * @param Model SBML Model
//     *
//     * @return A collection of fully structured reactions
//     */
//    public List<AtomContainerReaction> getReactions() {
//
//        List<AtomContainerReaction> loadedReactions = new ArrayList<AtomContainerReaction>(
//                reactionCount);
//
//        while (hasNext()) {
//            try {
//                AtomContainerReaction reaction = next();
//                loadedReactions.add(reaction);
//            } catch (UnknownCompartmentException ex) {
//                LOGGER.warn(ex.getMessage());
//            } catch (AbsentAnnotationException ex) {
//                LOGGER.warn(ex.getMessage());
//            } catch (MissingStructureException ex) {
//                LOGGER.warn(ex.getMessage());
//            }
//        }
//
//        return loadedReactions;
//
//    }
//
//
//    public MetabolicReactionImpl getMetabolicReaction(Reaction sbmlReaction)
//            throws UnknownCompartmentException {
//
//        MetabolicReactionImpl reaction = new MetabolicReactionImpl(new BasicReactionIdentifier(sbmlReaction.getId()),
//                                                                                       sbmlReaction.getMetaId(),
//                                                                                       sbmlReaction.getName());
//        LOGGER.info("Reading SBML reaction " + reaction);
//
//        for (int i = 0; i < sbmlReaction.getNumReactants(); i++) {
//            reaction.addReactant(getMetaboliteParticipant(sbmlReaction.getReactant(i)));
//        }
//
//        for (int i = 0; i < sbmlReaction.getNumProducts(); i++) {
//            reaction.addProduct(getMetaboliteParticipant(sbmlReaction.getProduct(i)));
//        }
//
//        // set the reversibility
//        reaction.setDirection(sbmlReaction.isReversible() ? Direction.BIDIRECTIONAL : Direction.FORWARD);
//
//        return reaction;
//
//    }
//
//
//    public Species getSpecies(SpeciesReference speciesReference) {
//
//        if (speciesReferences.containsKey(speciesReference)) {
//            return speciesReferences.get(speciesReference);
//        }
//
//        Species species = speciesReference.getSpeciesInstance();
//
//        if (species != null) {
//            return species;
//        }
//
//        species = model.getSpecies(speciesReference.getSpecies());
//
//        if (species != null) {
//            return species;
//        }
//
//        return new Species("error");
//
//
//    }
//
//    public uk.ac.ebi.mdk.domain.entity.reaction.Compartment getCompartment(Compartment compartment) {
//
//        if(compartments.containsKey(compartment)){
//            return compartments.get(compartment);
//        }
//
//        String id = compartment.getId();
//        String name = compartment.getName();
//
//        uk.ac.ebi.mdk.domain.entity.reaction.Compartment c = resolver.getCompartment(id);
//
//        if(c != null){
//            compartments.put(compartment, c);
//            return c;
//        }
//
//        c = resolver.getCompartment(name);
//        if(c != null){
//            compartments.put(compartment, c);
//            return c;
//        }
//
//        compartments.put(compartment, CompartmentImplementation.UNKNOWN);
//        return CompartmentImplementation.UNKNOWN;
//
//    }
//
//    /**
//     * Constructs a reaction participant from a species reference
//     *
//     * @param speciesReference An instance of SBML {@see SpeciesReference}
//     *
//     * @return An MetaboliteParticipant
//     *
//     * @throws UnknownCompartmentException Thrown if compartment identifier is not recognised (valid names are found
//     *                                     in the {@see Compartment} class)
//     * @throws MissingAnnotationException  Thrown
//     * @throws MissingStructureException
//     */
//    public MetabolicParticipantImplementation getMetaboliteParticipant(SpeciesReference speciesReference)
//            throws UnknownCompartmentException {
//
//        Species species = getSpecies(speciesReference);
//
//        uk.ac.ebi.mdk.domain.entity.reaction.Compartment compartment = getCompartment(species.getCompartmentInstance());
//
//        Double coefficient = speciesReference.getStoichiometry();
//
//        Metabolite metabolite = null;
//
//        if (speciesMap.containsKey(species)) {
//            metabolite = speciesMap.get(species);
//        } else if (speciesNameMap.containsKey(species.getName())) {
//            LOGGER.info("Using existing species with the same name:" + species.getName());
//            metabolite = speciesNameMap.get(species.getName());
//        } else {
//            metabolite = factory.newInstance(Metabolite.class,
//                                             new BasicChemicalIdentifier(species.getId()),
//                                             species.getName(),
//                                             species.getMetaId());
//
//            metabolite.setCharge(((Integer) species.getCharge()).doubleValue());
//
//            for (CVTerm term : species.getCVTerms()) {
//                for (String resource : term.getResources()) {
//                    Identifier identifier = MIRIAMLoader.getInstance().getIdentifier(resource);
//                    if (identifier != null)
//                        metabolite.addAnnotation(DefaultAnnotationFactory.getInstance().getCrossReference(identifier));
//                }
//            }
//
//            speciesMap.put(species, metabolite);
//            speciesNameMap.put(metabolite.getName(), metabolite);
//        }
//
//        return new MetabolicParticipantImplementation(metabolite,
//                                                      coefficient,
//                                                      compartment);
//    }
//
//
//    /**
//     * Loads the SBML {@see Reaction} object as a custom {@see AtomContainerReaction} instance.
//     *
//     * @param sbmlReaction Instance of a loaded SBML reaction
//     *
//     * @return Instance of {@see AtomContainerReaction}
//     *
//     * @throws UnknownCompartmentException Thrown if a compartment name cannot be matched
//     * @throws AbsentAnnotationException   Thrown if a reaction is missing RDF CV terms
//     * @throws MissingStructureException   Thrown if a structure for the molecule could not be loaded
//     */
//    public AtomContainerReaction getReaction(Reaction sbmlReaction) throws
//            UnknownCompartmentException,
//            AbsentAnnotationException,
//            MissingStructureException {
//        AtomContainerReaction reaction = new AtomContainerReaction(filter);
//
//        for (int i = 0; i < sbmlReaction.getNumReactants(); i++) {
//
//            reaction.addReactant(getParticipant(sbmlReaction.getReactant(i)));
//        }
//
//        for (int i = 0; i < sbmlReaction.getNumProducts(); i++) {
//
//            reaction.addProduct(getParticipant(sbmlReaction.getProduct(i)));
//        }
//
//        // set the reversibility
//        reaction.setDirection(sbmlReaction.isReversible() ? Direction.BIDIRECTIONAL : Direction.FORWARD);
//
//        // TODO(johnmay): Add Enzyme annotations and modifiers
//
//        return reaction;
//    }
//
//
//    /**
//     * Constructs a reaction participant (i.e. AtomContainerParticipant or GenericParticipant if the molecule contains
//     * and R/Alkyl group)
//     *
//     * @param speciesReference An instance of SBML {@see SpeciesReference}
//     *
//     * @return An {@see AtomContainerParticipant} or {@see GenericParticipant}.
//     *         The class {@see GenericParticipant} extends {@see AtomContainerParticipant} and so can
//     *         be handled identically.
//     *
//     * @throws UnknownCompartmentException Thrown if compartment identifier is not recognised (valid names are found
//     *                                     in the {@see Compartment} class)
//     * @throws MissingAnnotationException  Thrown
//     * @throws MissingStructureException
//     */
//    public AtomContainerParticipant getParticipant(SpeciesReference speciesReference)
//            throws
//            UnknownCompartmentException,
//            AbsentAnnotationException,
//            MissingStructureException {
//
//        Species species = speciesReference.getSpeciesInstance();
//        Compartment sbmlCompartment = species.getCompartmentInstance();
//
//
//        CompartmentImplementation compartment = CompartmentImplementation.getCompartment(sbmlCompartment == null
//                                                                                                 ? ""
//                                                                                                 : sbmlCompartment.getId());
//
//        if (compartment == CompartmentImplementation.UNKNOWN) {
//            throw new UnknownCompartmentException("Compartment " + species.getCompartmentInstance().
//                    getId()
//                                                          + " was not identifiable");
//        }
//
//        if (species.getNumCVTerms() == 0) {
//            throw new AbsentAnnotationException(
//                    "Species " + species.getId()
//                            + " did not have any associated Controlled Vocabulary terms");
//        }
//
//        IAtomContainer molecule = getAtomContainer(species);
//        Double coefficient = speciesReference.getStoichiometry();
//
//        return CDKUtils.isMoleculeGeneric(molecule)
//                ? new GenericParticipant(molecule, coefficient, compartment)
//                : new AtomContainerParticipant(molecule, coefficient, compartment);
//
//    }
//
//
//    /**
//     * Constructs and IAtomContainer from the given SBML chemical {@see Species} using RDF MIRIAM annotations
//     *
//     * @param species An instance of an SBML Species using the attached Controlled Vocabulary (CV) terms. The CV
//     *                terms must contain a CHEBI identifier (MIRIAM Registry) to allow fetching of a structure using
//     *                Web
//     *                Services
//     *
//     * @return A instance of a CDK {@see IAtomContainer}
//     *
//     * @throws MissingStructureException Thrown if an {@see IAtomContainer} can not be built
//     */
//    public IAtomContainer getAtomContainer(Species species) throws MissingStructureException {
//
//        for (int i = 0; i < species.getNumCVTerms(); i++) {
//            for (String resource : species.getCVTerm(i).getResources()) {
//
//                Identifier id = MIRIAMLoader.getInstance().getIdentifier(resource);
//
//                if (id == null) {
//                    continue;
//                }
//
//                if (id instanceof ChEBIIdentifier) {
//                    LOGGER.debug("Fetching molecule for " + id);
//                    try {
//                        return chebiWS.getAtomContainer(id.getAccession());
//                    } catch (UnfetchableEntry ex) {
//                        LOGGER.debug("There was a problem loading: " + id + " : " + ex.getMessage());
//                    }
//
//                } else if (id instanceof KEGGIdentifier) {
//                    LOGGER.debug("Fetching molecule for " + id);
//                    try {
//                        return keggWS.getAtomContainer(id.getAccession());
//                    } catch (UnfetchableEntry ex) {
//                        LOGGER.debug("There was a problem loading: " + id + " : " + ex.getMessage());
//                    }
//
//                }
//            }
//        }
//
//        throw new MissingStructureException(species.getId());
//
//    }
//
//
//    public boolean hasNext() {
//        return reactionIndex + 1 < reactionCount;
//    }
//
//
//    public AtomContainerReaction next() throws UnknownCompartmentException,
//            AbsentAnnotationException,
//            MissingStructureException {
//
//        reactionIndex++;
//        Reaction sbmlReaction = model.getReaction(reactionIndex);
//
//        AtomContainerReaction reaction = getReaction(sbmlReaction);
//
//        return reaction;
//
//    }
//
//
//    public MetabolicReactionImpl nextMetabolicReaction() throws UnknownCompartmentException {
//        reactionIndex++;
//        Reaction sbmlReaction = model.getReaction(reactionIndex);
//
//        return getMetabolicReaction(sbmlReaction);
//    }
}
