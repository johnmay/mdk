package uk.ac.ebi.interfaces.entities;

import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.reaction.Participant;

import java.util.Collection;


/**
 *
 *          IdentifierReaction 2012.02.07
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class provides a interface for reactions that are described using only identifiers
 *
 */
public interface IdentifierReaction<I extends Identifier> extends Reaction<Participant<I,Double>> {


}
