package uk.ac.ebi.mdk.service.query.structure;

import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.util.Collection;

/**
 * @author John May
 */
public interface StructureSearch<I extends Identifier> extends QueryService<I> {

    /**
     * Provides structure searching of a CDK IAtomContainer.
     * @param molecule
     * @param approximate whether to consider only similar matches or direct matches
     * @return
     */
    public Collection<I> structureSearch(IAtomContainer molecule, boolean approximate);

}
