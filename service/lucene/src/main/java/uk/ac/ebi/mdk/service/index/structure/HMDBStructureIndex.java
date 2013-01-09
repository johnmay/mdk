package uk.ac.ebi.mdk.service.index.structure;

import uk.ac.ebi.mdk.service.index.KeywordNIOIndex;

/**
 * HMDBStructureIndex - 21.02.2012 <br/> MetaInfo...
 * <p/>
 * Describes the {@see LuceneIndex} used in {@see HMDBStructureService} and {@see HMDBStructureLoader}.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBStructureIndex extends KeywordNIOIndex {
    public HMDBStructureIndex() {
        super("HMDB Structure", "structure/hmdb");
    }
}
