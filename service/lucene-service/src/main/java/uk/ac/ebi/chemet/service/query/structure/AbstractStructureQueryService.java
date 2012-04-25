package uk.ac.ebi.chemet.service.query.structure;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.chemet.service.analyzer.ChemicalSimilarity;
import uk.ac.ebi.chemet.service.query.AbstractQueryService;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.StructureService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.BitSet;
import java.util.Collection;

/**
 * ${Name}.java - 20.02.2012 <br/> MetaInfo...
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
        getSearcher().setSimilarity(new ChemicalSimilarity());
    }


    public Collection<I> searchStructure(BitSet fpQuery) throws IOException {

        BooleanQuery query = new BooleanQuery();
        for (int i = fpQuery.nextSetBit(0); i != -1; i = fpQuery.nextSetBit(i + 1)) {
            query.add(construct(Integer.toString(i), FINGERPRINT_BIT), BooleanClause.Occur.SHOULD);
        }

        return getIdentifiers(query);

    }

    @Override
    public IAtomContainer getStructure(I identifier) {

        byte[] bytes = firstBinaryValue(construct(identifier.getAccession(), IDENTIFIER), ATOM_CONTAINER);

        try{
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (IAtomContainer) in.readObject();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return null;

    }


}
