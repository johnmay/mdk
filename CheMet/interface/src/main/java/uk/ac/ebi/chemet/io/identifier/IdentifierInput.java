package uk.ac.ebi.chemet.io.identifier;

import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.io.IOException;

/**
 * IdentifierInput - 11.03.2012 <br/>
 * <p/>
 * Provides the functionality of reading input stream
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface IdentifierInput {

    public <I extends Identifier> I read() throws IOException, ClassNotFoundException;

    public <I extends Identifier> I read(Class<I> c) throws IOException, ClassNotFoundException;

}
