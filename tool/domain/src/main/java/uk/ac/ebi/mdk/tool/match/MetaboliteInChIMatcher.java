package uk.ac.ebi.mdk.tool.match;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.InChI;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.tool.inchi.InChIConnectivity;

import java.util.HashSet;
import java.util.Set;


/**
 * MetaboliteInChIMatcher 2012.02.16 <br/>
 * Class realises MetaboliteComparator using the InChI of the
 * structures to compare metabolites. The InChI's can be compared
 * straight or using only the connectivity (see.
 * {@see InChIConnectivity}). This will compare the InChI's based on
 * their atom connectivity (i.e. bond type, charge and proton's are not
 * considered).
 * -
 * Compares the {@see InChI} annotations of the metabolites. If any of the
 * InChIs are matches they metabolites are considered matches
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class MetaboliteInChIMatcher
        extends AbstractMatcher<Metabolite, Set<String>>
        implements EntityMatcher<Metabolite, Set<String>> {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteInChIMatcher.class);

    private boolean connectivity;


    @Override
    public Set<String> calculatedMetric(Metabolite entity) {

        Set<String> inchis = new HashSet<String>();

        for (InChI inchi : entity.getAnnotations(InChI.class)) {
            inchis.add(connectivity
                               ? InChIConnectivity.getInChIConnectivity(inchi.getValue())
                               : inchi.getValue());
        }

        return inchis;
    }

    @Override
    public Boolean matches(Set<String> queryMetric, Set<String> subjectMetric) {
        return matchAny(queryMetric, subjectMetric);
    }

    /**
     * Default constructor will compare InChIs using direct string comparison
     */
    public MetaboliteInChIMatcher() {
    }


    /**
     * Specify whether to only use the connectivity of InChI therefore making
     * the comparison less specific.
     *
     * @param connectivity make the comparison less specific
     */
    public MetaboliteInChIMatcher(boolean connectivity) {
        this.connectivity = connectivity;
    }

}
