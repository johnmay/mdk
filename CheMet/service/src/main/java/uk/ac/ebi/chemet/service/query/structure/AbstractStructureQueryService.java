package uk.ac.ebi.chemet.service.query.structure;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.chemet.service.query.AbstractQueryService;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.services.StructureQueryService;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.StructureService;

import java.io.*;

/**
 * ${Name}.java - 20.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractStructureQueryService<I extends Identifier>
        extends AbstractQueryService<I>
        implements StructureService<I> {

    private IndexSearcher searcher;

    private Term IDENTIFIER = new Term("Identifier");

    public AbstractStructureQueryService(LuceneIndex index) {

        super(index);

    }

    @Override
    public IAtomContainer getStructure(I identifier) {

        byte[] bytes = getFirstBinaryValue(identifier, ATOM_CONTAINER);

        try{
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (IAtomContainer) in.readObject();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
        return null;

    }


}
