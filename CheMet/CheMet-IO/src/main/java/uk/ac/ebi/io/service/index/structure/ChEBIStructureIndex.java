package uk.ac.ebi.io.service.index.structure;

import uk.ac.ebi.io.service.index.KeywordNIOIndex;

/**
 * ChEBIStructureIndex - 21.02.2012 <br/>
 * <p/>
 * Describes the {@see LuceneIndex} used in {@see ChEBIStructureQueryService} and {@see ChEBIStructureLoader}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBIStructureIndex extends KeywordNIOIndex {
    public ChEBIStructureIndex() {
        super("ChEBI Chemical Structures", "structure/chebi");
    }
}
