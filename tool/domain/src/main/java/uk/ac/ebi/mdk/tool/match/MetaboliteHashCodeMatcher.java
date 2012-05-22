package uk.ac.ebi.mdk.tool.match;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.domain.annotation.ChemicalStructure;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.tool.domain.MolecularHashFactory;
import uk.ac.ebi.mdk.tool.domain.hash.AtomSeed;
import uk.ac.ebi.mdk.tool.domain.hash.AtomicNumberSeed;
import uk.ac.ebi.mdk.tool.domain.hash.ConnectedAtomSeed;
import uk.ac.ebi.mdk.tool.domain.hash.SeedFactory;

import java.util.HashSet;
import java.util.Set;


/**
 * MetaboliteHashCodeMatcher 2012.02.16 <br/>
 * Class realises MetaboliteComparator using the hash codes of the
 * structures to compare metabolites
 * <p/>
 * <p/>
 * Compares the {@see ChemicalStructure} annotations of the metabolites
 * using the {@see MolecularHashFactory} to generate molecular hash codes.
 * If any of the chemical structure hashes are matches they metabolites
 * are considered matches
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class MetaboliteHashCodeMatcher
        extends AbstractMatcher<Metabolite, Set<Integer>>
        implements EntityMatcher<Metabolite, Set<Integer>> {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteHashCodeMatcher.class);

    private final MolecularHashFactory factory = MolecularHashFactory.getInstance();

    private final Set<AtomSeed> seeds;

    private static final Integer DEFAULT_ATOM_COUNT_THRESHOLD = 150;
    private              int     atomCountThreshold           = DEFAULT_ATOM_COUNT_THRESHOLD;


    public MetaboliteHashCodeMatcher() {
        this(DEFAULT_ATOM_COUNT_THRESHOLD);
    }

    public MetaboliteHashCodeMatcher(Integer threshold) {
        this(SeedFactory.getInstance().getSeeds(AtomicNumberSeed.class, ConnectedAtomSeed.class),
             threshold);
    }

    public MetaboliteHashCodeMatcher(Set<AtomSeed> seeds) {
        this(seeds, DEFAULT_ATOM_COUNT_THRESHOLD);
    }

    /**
     * Main constructor
     *
     * @param seeds
     * @param threshold
     */
    public MetaboliteHashCodeMatcher(Set<AtomSeed> seeds, Integer threshold) {
        this.seeds = seeds;
    }

    @Override
    public Boolean matchMetric(Set<Integer> queryMetric, Set<Integer> subjectMetric) {
        return matchAny(queryMetric, subjectMetric);
    }

    @Override
    public Set<Integer> calculatedMetric(Metabolite entity) {

        Set<Integer> hashes = new HashSet<Integer>();

        for (ChemicalStructure annotation : entity.getStructures()) {
            IAtomContainer structure = annotation.getStructure();

            if (!(structure.getAtomCount() == 1
                    && structure.getAtom(0).getSymbol().equals("H"))) {
                structure = AtomContainerManipulator.removeHydrogens(structure);
            }


            if (structure.getAtomCount() < atomCountThreshold) {
                hashes.add(factory.getHash(structure, seeds).hash);
            }
        }

        return hashes;

    }


}
