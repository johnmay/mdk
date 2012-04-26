package uk.ac.ebi.chemet.io.parser.xml.sbml;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SpeciesReference;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class SBMLReactionReader {

    private EntityFactory factory;
    private Model         model;
    private int           i = 0;

    public SBMLReactionReader(SBMLDocument document, EntityFactory factory, Reconstruction reconstruction){
        this(document.getModel(), factory);
    }
    
    public SBMLReactionReader(Model model, EntityFactory factory){
        this.model   = model;
        this.factory = factory;
    }

    public boolean hasNext(){
        return i + 1 < model.getNumReactions();
    }

    public MetabolicReaction getReaction(Reaction sbmlReaction){

        MetabolicReaction reaction = factory.ofClass(MetabolicReaction.class);

        sbmlReaction.getListOfReactants();

        for (int i = 0; i < sbmlReaction.getNumReactants(); i++) {
            reaction.addReactant(getParticipant(sbmlReaction.getReactant(i)));
        }

        for (int i = 0; i < sbmlReaction.getNumProducts(); i++) {
            reaction.addProduct(getParticipant(sbmlReaction.getProduct(i)));
        }

        // reaction.setDirection(sbmlReaction.isReversible() ? DirectionImpl.BIDIRECTIONAL : DirectionImplementation.FORWARD);

        return reaction;
        
    }
    
    
    public MetabolicParticipant getParticipant(SpeciesReference reference){

        Double coefficient = reference.getStoichiometry();

        MetabolicParticipant participant = factory.ofClass(MetabolicParticipant.class);

        participant.setCoefficient(coefficient);



        return participant;

    }

}
