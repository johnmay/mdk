package uk.ac.ebi.mdk.domain.entity.reaction;

import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.entity.Reaction;


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
