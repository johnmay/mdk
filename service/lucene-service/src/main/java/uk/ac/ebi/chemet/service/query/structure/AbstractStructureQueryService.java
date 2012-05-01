package uk.ac.ebi.chemet.service.query.structure;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.fingerprint.IFingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.chemet.service.analyzer.FingerprintSimilarity;
import uk.ac.ebi.chemet.service.query.AbstractLuceneService;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.structure.StructureSearch;
import uk.ac.ebi.mdk.service.query.structure.StructureService;
import uk.ac.ebi.mdk.service.query.structure.SubstructureSearch;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
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
        extends AbstractLuceneService<I>
        implements StructureService<I>,
                   StructureSearch<I>,
                   SubstructureSearch<I> {

    private IndexSearcher searcher;
    private IFingerprinter fingerprinter = new Fingerprinter();

    private static final Logger LOGGER = Logger.getLogger(AbstractStructureQueryService.class);

    private Term IDENTIFIER = new Term("Identifier");

    public AbstractStructureQueryService(LuceneIndex index) {
        super(index);
    }

    @Override
    public boolean startup() {
        if( super.startup() ){
            getSearcher().setSimilarity(new FingerprintSimilarity());
            return true;
        }
        return false;
    }

    @Override
    public IAtomContainer getStructure(I identifier) {

        byte[] bytes = firstBinaryValue(construct(identifier.getAccession(), IDENTIFIER), ATOM_CONTAINER);

        try {
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (IAtomContainer) in.readObject();
        } catch (Exception ex) {
            LOGGER.warn("Error reading atom container");
        }

        return new AtomContainer();

    }

    /**
     * Set the fingerprint method. This method is needed if the fingerprint method
     * used to write the index was different from the default. Note: changing the
     * fingerprint method here and not when creating the index will not work
     *
     * @param fingerprinter
     *
     * @see IFingerprinter
     * @see uk.ac.ebi.chemet.service.loader.writer.DefaultStructureIndexWriter#DefaultStructureIndexWriter(uk.ac.ebi.mdk.service.index.LuceneIndex,
     *      org.openscience.cdk.fingerprint.IFingerprinter)
     */
    public void setFingerprinter(IFingerprinter fingerprinter) {
        this.fingerprinter = fingerprinter;
    }

    /**
     * Simple method use the set fingerprinter method to create a bitset
     * which is then converted into a boolean query and searched in the index
     *
     * @param molecule the structure to search
     *
     * @return identifiers with similar structure
     *
     * @see #setFingerprinter(org.openscience.cdk.fingerprint.IFingerprinter)
     * @see #searchStructure(java.util.BitSet, boolean, boolean)
     */
    @Override
    public Collection<I> structureSearch(IAtomContainer molecule, boolean approximate) {
        LOGGER.error("Approximate variable not used!");
        try {
            return searchStructure(fingerprinter.getFingerprint(molecule), approximate, false);
        } catch (CDKException ex) {
            return new ArrayList<I>();
        }
    }

    @Override
    public Collection<I> substructureSearch(IAtomContainer molecule) {
        try {
            return searchStructure(fingerprinter.getFingerprint(molecule), true, true);
        } catch (CDKException ex) {
            return new ArrayList<I>();
        }
    }

    public Collection<I> searchStructure(BitSet fp, boolean approximate, boolean substructure) {

        // construct the query
        BooleanQuery query = new BooleanQuery();
        for (int i = fp.nextSetBit(0); i != -1; i = fp.nextSetBit(i + 1)) {
            query.add(construct(Integer.toString(i), FINGERPRINT_BIT),
                      substructure ? BooleanClause.Occur.MUST : BooleanClause.Occur.SHOULD);
        }

        return getIdentifiers(query);

    }
}
