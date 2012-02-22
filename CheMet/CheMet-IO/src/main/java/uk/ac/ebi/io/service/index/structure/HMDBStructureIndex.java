package uk.ac.ebi.io.service.index.structure;

import uk.ac.ebi.io.service.index.KeywordNIOIndex;

/**
 * ${Name}.java - 21.02.2012 <br/> Description...
 * <p/>
 * Describes the {@see LuceneIndex} used in {@see HMDBStructureQueryService} and {@see HMDBStructureLoader}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBStructureIndex extends KeywordNIOIndex {
    public HMDBStructureIndex() {
        super("HMDB Chemical Structures", "structure/hmdb");
    }
}
