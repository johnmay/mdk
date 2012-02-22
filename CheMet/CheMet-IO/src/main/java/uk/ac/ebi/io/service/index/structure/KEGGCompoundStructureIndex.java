package uk.ac.ebi.io.service.index.structure;

import uk.ac.ebi.io.service.index.KeywordNIOIndex;

/**
 * KEGGCompoundStructureIndex.java - 21.02.2012 <br/> Description...
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
