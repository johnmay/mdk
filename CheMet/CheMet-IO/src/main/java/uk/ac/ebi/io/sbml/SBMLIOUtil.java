
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
package uk.ac.ebi.io.sbml;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.chemet.entities.reaction.participant.Participant;
import uk.ac.ebi.core.Compartment;
import uk.ac.ebi.core.MetabolicReaction;
import uk.ac.ebi.core.Metabolite;
import uk.ac.ebi.core.Reconstruction;
import uk.ac.ebi.interfaces.Identifier;


/**
 *          SBMLIOUtil – 2011.09.27 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class SBMLIOUtil {

    private static final Logger LOGGER = Logger.getLogger(SBMLIOUtil.class);
    public final int level;
    public final int version;
    private long metaIdticker = 0;
    private Map<Participant, SpeciesReference> speciesReferences = new HashMap();


    public SBMLIOUtil(int level, int version) {
        this.level = level;
        this.version = version;
    }


    public SBMLIOUtil() {
        this(2, 2);
    }


    private String nextMetaId() {
        metaIdticker++;
        return Long.toString(metaIdticker);
    }


    public SBMLDocument getDocument(Reconstruction reconstruction) {

        SBMLDocument doc = new SBMLDocument(level, version);
        Model model = new Model(level, version);
        doc.setModel(model);

        for( MetabolicReaction rxn : reconstruction.getReactions() ) {
            addReaction(model, rxn);
        }

        return doc;

    }


    public Reaction addReaction(Model model, MetabolicReaction rxn) {

        Reaction sbmlRxn = new Reaction(level, version);

        // set id and meta-id
        Identifier id = rxn.getIdentifier();
        sbmlRxn.setMetaId(nextMetaId());
        if( id == null ) {
            sbmlRxn.setId("rxn" + metaIdticker); // maybe need a try/catch to reset valid id
        } else {
            sbmlRxn.setId(id.getAccession().trim());
        }


        for( Participant<Metabolite, Double, Compartment> p : rxn.getReactantParticipants() ) {
            sbmlRxn.addReactant(getSpeciesReference(model, p));
        }
        for( Participant<Metabolite, Double, Compartment> p : rxn.getProductParticipants() ) {
            sbmlRxn.addProduct(getSpeciesReference(model, p));
        }

        for( CrossReference xref : rxn.getAnnotationsExtending(CrossReference.class) ) {
            String resource = xref.getIdentifier().getURN();
            CVTerm term = new CVTerm(CVTerm.Qualifier.BQB_IS_DESCRIBED_BY, resource);
            sbmlRxn.addCVTerm(term);
        }

        model.addReaction(sbmlRxn);

        return sbmlRxn;

    }


    public SpeciesReference getSpeciesReference(Model model,
                                                Participant<Metabolite, Double, Compartment> participant) {

        // create a new entry if one doesn't exists
        if( speciesReferences.containsKey(participant) == false ) {
            speciesReferences.put(participant, addSpecies(model, participant));
        }

        return speciesReferences.get(participant);

    }


    /**
     * Creates a new species in the given model adding
     * @param model
     * @param participant
     * @return
     */
    public SpeciesReference addSpecies(Model model,
                                       Participant<Metabolite, Double, Compartment> participant) {

        Species species = new Species(level, version);

        Metabolite m = participant.getMolecule();

        species.setId(m.getIdentifier().toString() + "_" + participant.getCompartment().
          getAbbreviation());
        species.setMetaId(nextMetaId());
        species.setName(m.getName());
        species.setCompartment(participant.getCompartment().name());


        for( CrossReference xref : m.getAnnotationsExtending(CrossReference.class) ) {
            String resource = xref.getIdentifier().getURN();
            CVTerm term = new CVTerm(CVTerm.Qualifier.BQB_IS, resource);
            species.addCVTerm(term);
        }

        model.addSpecies(species);

        return new SpeciesReference(species);
    }


}

