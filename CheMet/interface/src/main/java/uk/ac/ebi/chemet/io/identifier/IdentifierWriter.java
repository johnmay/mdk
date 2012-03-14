package uk.ac.ebi.chemet.io.identifier;

import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.io.IOException;

/**
 * IdentifierWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface IdentifierWriter<I extends Identifier> {
    
    public void write(I identifier) throws IOException;
    
}
