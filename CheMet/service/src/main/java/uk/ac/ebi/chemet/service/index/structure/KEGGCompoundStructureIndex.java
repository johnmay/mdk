package uk.ac.ebi.chemet.service.index.structure;

import uk.ac.ebi.chemet.service.index.KeywordNIOIndex;

/**
 * KEGGCompoundStructureIndex - 21.02.2012 <br/> Description...
 * <p/>
 * Describes the {@see LuceneIndex} used in {@see KEGGCompoundStructureQueryService}
 * and {@see KEGGCompoundStructureLoader}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundStructureIndex extends KeywordNIOIndex {
    public KEGGCompoundStructureIndex() {
        super("KEGG Compound Chemical Structures", "structure/kegg");
    }
}
