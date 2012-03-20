package uk.ac.ebi.chemet.io.domain;

import uk.ac.ebi.interfaces.entities.Entity;

import java.io.IOException;

/**
 * EntityOutput - 11.03.2012 <br/>
 * <p/>
 * Write entities to an output stream
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface EntityOutput {

    public void writeClass(Class<? extends Entity> c) throws IOException;

    public void writeData(Entity entity) throws IOException;

    public void write(Entity entity) throws IOException;;

}
