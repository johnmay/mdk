package uk.ac.ebi.service.query;

import org.apache.lucene.index.Term;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.interfaces.identifiers.Identifier;

/**
 * StructureService - 28.02.2012 <br/>
 * <p/>
 * Provides a CDK atom container for a given identifier
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface StructureService<I extends Identifier> extends QueryService<I>{

    public static final Term ATOM_CONTAINER = new Term("AtomContainer");

    public IAtomContainer getStructure(I identifier);

}
