package uk.ac.ebi.core.tools.compare;

import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *          MetaboliteComparator 2012.02.16 <br/>
 *          Interface defines a method to comparing two metabolites
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public interface MetaboliteComparator {

    /**
     * 
     * @param  query   the query metabolite
     * @param  subject the subject metabolite
     * @return         whether the metabolite names are equal
     */
    public boolean areEqual(Metabolite query, Metabolite subject);
}
