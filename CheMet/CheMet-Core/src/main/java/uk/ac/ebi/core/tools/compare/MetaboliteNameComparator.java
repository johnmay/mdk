package uk.ac.ebi.core.tools.compare;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.Synonym;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 *          MetaboliteNameComparator 2012.02.16 <br/>
 *          Class realises MetaboliteComparator using the names and synonyms of
 *          the metabolites
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetaboliteNameComparator
        implements MetaboliteComparator {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteNameComparator.class);


    /**
     * Compares the names and synonyms of two metabolites. The names are
     * normalised to lower case (Locale.ENGLISH) and trimmed of excess space. If
     * any name/synonym matches then the metabolites are considered equal
     * @inheritDoc
     */
    public boolean equal(Metabolite query, Metabolite subject) {

        Set<String> queryNames = new HashSet<String>();
        Set<String> subjectNames = new HashSet<String>();

        queryNames.add(query.getName().trim().toLowerCase(Locale.ENGLISH));
        subjectNames.add(subject.getName().trim().toLowerCase(Locale.ENGLISH));

        /**
         * Include the synonyms
         */
        for (Synonym synonym : query.getAnnotations(Synonym.class)) {
            queryNames.add(synonym.getValue().trim().toLowerCase(Locale.ENGLISH));
        }
        for (Synonym synonym : subject.getAnnotations(Synonym.class)) {
            subjectNames.add(synonym.getValue().trim().toLowerCase(Locale.ENGLISH));
        }

        for (String subjectName : subjectNames) {
            if (queryNames.contains(subjectName)) {
                return true;
            }
        }

        return false;

    }
}
