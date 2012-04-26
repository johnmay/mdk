package uk.ac.ebi.mdk.service.query;

import org.apache.lucene.index.Term;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.util.Collection;

/**
 * StructureService - 28.02.2012 <br/>
 * <p/>
 * Provides a CDK atom container for a given identifier
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface StructureService<I extends Identifier> extends QueryService<I> {

    public static final Term ATOM_CONTAINER = new Term("AtomContainer");
    public static final Term FINGERPRINT_BIT = new Term("Fingerprint.Bit");

    /**
     * Access the CDK IAtomContainer for the given identifier
     *
     * @param identifier
     *
     * @return
     */
    public IAtomContainer getStructure(I identifier);

    /**
     * Search for similar structures
     * <p/>
     * <b>NOTE: API may change in future</b>
     *
     * @param molecule the structure to search
     *
     * @return identifiers in this service which have similar structure
     */
    public Collection<I> searchStructure(IAtomContainer molecule);

}
