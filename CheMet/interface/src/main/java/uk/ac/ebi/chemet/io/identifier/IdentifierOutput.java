package uk.ac.ebi.chemet.io.identifier;

import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.io.IOException;

/**
 * IdentifierOutput - 11.03.2012 <br/>
 * <p/>
 * Write entities to an output stream
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface IdentifierOutput {

    public void writeClass(Class<? extends Identifier> c) throws IOException;

    public void writeData(Identifier identifier) throws IOException;

    public void write(Identifier identifier) throws IOException;;

}
