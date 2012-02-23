package uk.ac.ebi.chemet.service.index.query.structure;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.chemet.service.index.query.AbstractQueryService;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.services.StructureQueryService;
import uk.ac.ebi.service.index.LuceneIndex;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;

/**
 * ${Name}.java - 20.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractStructureQueryService<I extends Identifier>
    extends AbstractQueryService
        implements StructureQueryService<I> {

    private IndexSearcher searcher;

    private Term IDENTIFIER = new Term("Identifier");

    public AbstractStructureQueryService(LuceneIndex index) throws IOException {

        if(index.isAvailable()){
            setDirectory(index.getDirectory());
            setAnalyzer(index.getAnalyzer());
        }

        searcher = new IndexSearcher(index.getDirectory());

        throw new UnsupportedOperationException("Fix my terms!");


    }

    @Override
    public IAtomContainer getStructure(I identifier) {

        try {
            String str = getMol(identifier);

            if (str.isEmpty()) {
                return null;
            }

            MDLV2000Reader reader = new MDLV2000Reader(new StringReader(str));
            IMolecule molecule = new Molecule();
            return reader.read(molecule);

        } catch (CDKException ex) {
            // throw our own exception?
            return null;
        }
    }

    @Override
    public String getMol(I identifier) {
        return getMol(identifier.getAccession());
    }

    private String getMol(String identifier) {
        try {
            Query q = new TermQuery(IDENTIFIER.createTerm(identifier));
            TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            if (hits.length > 1) {
                // LOGGER.info("more then one hit found for an id! this shouldn't happen");
            }
            for (ScoreDoc scoreDoc : hits) {
                return new String(getBinaryValue(scoreDoc, "Molecule"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }

        return "";
    }

}
