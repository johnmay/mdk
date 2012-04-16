package uk.ac.ebi.chemet.service.index.crossreference;

import uk.ac.ebi.chemet.service.index.KeywordNIOIndex;

import java.io.File;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtCrossReferenceIndex extends KeywordNIOIndex {

    public UniProtCrossReferenceIndex() {
        super("UniProt CrossReferences", "xref/uniprot");
    }


    public UniProtCrossReferenceIndex(File f) {
        super("UniProt CrossReferences", f);
    }

}
