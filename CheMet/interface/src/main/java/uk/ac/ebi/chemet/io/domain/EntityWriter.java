package uk.ac.ebi.chemet.io.domain;

import uk.ac.ebi.interfaces.entities.Entity;

import java.io.IOException;

/**
 * EntityWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface EntityWriter<E extends Entity> {
    
    public void write(E entity) throws IOException;
    
}
