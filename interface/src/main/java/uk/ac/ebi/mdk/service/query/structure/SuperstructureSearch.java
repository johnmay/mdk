package uk.ac.ebi.mdk.service.query.structure;

import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * @author John May
 */
public interface SuperstructureSearch<I extends Identifier> extends QueryService<I> {

    public Collection<I> superstructureSearch(IAtomContainer molecule);

}
