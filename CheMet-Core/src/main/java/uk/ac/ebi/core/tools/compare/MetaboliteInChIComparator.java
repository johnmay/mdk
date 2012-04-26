package uk.ac.ebi.core.tools.compare;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.chemical.InChI;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.tool.compare.EntityComparator;
import uk.ac.ebi.metabolomes.util.inchi.InChIConnectivity;

import java.util.HashSet;
import java.util.Set;


/**
 *          MetaboliteInChIComparator 2012.02.16 <br/>
 *          Class realises MetaboliteComparator using the InChI of the
 *          structures to compare metabolites. The InChI's can be compared
 *          straight or using only the connectivity (see. 
 *          {@see InChIConnectivity}). This will compare the InChI's based on
 *          their atom connectivity (i.e. bond type, charge and proton's are not
 *          considered).
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetaboliteInChIComparator
        implements EntityComparator<Metabolite> {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteInChIComparator.class);

    private boolean connectivity;


    /**
     * Default constructor will compare InChIs using direct string comparison
     */
    public MetaboliteInChIComparator() {
    }


    /**
     * Specify whether to only use the connectivity of InChI therefore making
     * the comparison less specific.
     * @param connectivity make the comparison less specific
     */
    public MetaboliteInChIComparator(boolean connectivity) {
        this.connectivity = connectivity;
    }


    /**
     * Compares the {@see InChI} annotations of the metabolites. If any of the
     * InChIs are equal they metabolites are considered equal
     * @inheritDoc
     */
    public Boolean equal(Metabolite query, Metabolite subject) {

        Set<String> queryInChIs = new HashSet<String>();
        Set<String> subjectInChIs = new HashSet<String>();

        for (InChI inchi : query.getAnnotations(InChI.class)) {
            queryInChIs.add(connectivity
                            ? InChIConnectivity.getInChIConnectivity(inchi.getValue())
                            : inchi.getValue());
        }

        for (InChI inchi : subject.getAnnotations(InChI.class)) {
            subjectInChIs.add(connectivity
                              ? InChIConnectivity.getInChIConnectivity(inchi.getValue())
                              : inchi.getValue());
        }

        for (String queryInChI : queryInChIs) {
            for (String subjectInChI : subjectInChIs) {
                if (queryInChI.equals(subjectInChI)) {
                    return true;
                }
            }
        }

        return false;

    }
}
