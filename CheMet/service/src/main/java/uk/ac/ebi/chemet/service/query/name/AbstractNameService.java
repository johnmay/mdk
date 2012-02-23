package uk.ac.ebi.chemet.service.query.name;

import uk.ac.ebi.chemet.service.query.AbstractQueryService;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.service.index.LuceneIndex;

import java.io.IOException;

/**
 * ${Name}.java - 21.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractNameService<I extends Identifier>
        extends AbstractQueryService {

    public AbstractNameService(LuceneIndex index) throws IOException {
        if(index.isAvailable()){
            setAnalyzer(index.getAnalyzer());
            setDirectory(index.getDirectory());
        }
    }



}
