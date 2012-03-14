package uk.ac.ebi.chemet.io.entity;

import uk.ac.ebi.interfaces.entities.Entity;
import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.io.IOException;

/**
 * EntityInput - 11.03.2012 <br/>
 * <p/>
 * Provides the functionality of reading input stream
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface EntityInput {

    public <E extends Entity> E read() throws IOException, ClassNotFoundException;

    public <E extends Entity> E read(Class<E> c) throws IOException, ClassNotFoundException;

}
