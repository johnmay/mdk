package uk.ac.ebi.core.tools.compare;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.tool.compare.EntityMatcher;
import uk.ac.ebi.mdk.tool.domain.MolecularHashFactory;
import uk.ac.ebi.mdk.tool.domain.hash.*;

import java.util.HashSet;
import java.util.Set;


/**
 *          MetaboliteHashCodeMatcher 2012.02.16 <br/>
 *          Class realises MetaboliteComparator using the hash codes of the
 *          structures to compare metabolites
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetaboliteHashCodeMatcher
        implements EntityMatcher<Metabolite> {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteHashCodeMatcher.class);

    private final MolecularHashFactory factory = MolecularHashFactory.getInstance();

    private final Set<AtomSeed> seeds;


    public MetaboliteHashCodeMatcher() {
        seeds = SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class, ConnectedAtomSeed.class, BondOrderSumSeed.class);
    }


    public MetaboliteHashCodeMatcher(Set<AtomSeed> seeds) {
        this.seeds = seeds;
    }


    /**
     * Compares the {@see ChemicalStructure} annotations of the metabolites
     * using the {@see MolecularHashFactory} to generate molecular hash codes.
     * If any of the chemical structure hashes are equal they metabolites
     * are considered equal
     * @inheritDoc
     */
    public Boolean equal(Metabolite query, Metabolite subject) {

        Set<Integer> queryHashes = new HashSet<Integer>();
        Set<Integer> subjectHashes = new HashSet<Integer>();

        for (ChemicalStructure structure : query.getStructures()) {
            IAtomContainer atomcontainer = structure.getStructure();
            queryHashes.add(factory.getHash(atomcontainer, seeds).hash);
        }

        for (ChemicalStructure structure : subject.getStructures()) {
            IAtomContainer atomcontainer = structure.getStructure();
            subjectHashes.add(factory.getHash(atomcontainer, seeds).hash);
        }

        for (Integer queryHash : queryHashes) {
            for (Integer subjectHash : subjectHashes) {
                if (queryHash.equals(subjectHash)) {
                    return true;
                }
            }
        }

        return false;

    }
}
